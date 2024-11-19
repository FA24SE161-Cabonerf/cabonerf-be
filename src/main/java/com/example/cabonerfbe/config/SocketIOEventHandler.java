package com.example.cabonerfbe.config;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.cabonerfbe.converter.UserOrganizationConverter;
import com.example.cabonerfbe.models.UserOrganization;
import com.example.cabonerfbe.repositories.UserOrganizationRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SocketIOEventHandler {

    @Autowired
    private SocketIOServer server;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserOrganizationRepository uoRepository;
    @Autowired
    private UserOrganizationConverter uoConverter;

    @OnConnect
    public void onConnect(com.corundumstudio.socketio.SocketIOClient client) {
        System.out.println("Client connected: " + client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(com.corundumstudio.socketio.SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    @OnEvent("message")
    public void onMessage(com.corundumstudio.socketio.SocketIOClient client, String data) {
        System.out.println("Received message: " + data);

        server.getBroadcastOperations().sendEvent("message", "Server received: " + data);
    }

    @PostConstruct
    public void init() {
        registerEvents();
    }

    private void registerEvents() {
        server.addEventListener("historyInvite", String.class, (client, data, ackRequest) -> {
            // Lấy userId từ header
            HandshakeData handshakeData = client.getHandshakeData();
            String userId = handshakeData.getHttpHeaders().get("x-user-id");

            if (userId == null || userId.isEmpty()) {
                ackRequest.sendAckData("x-user-id not found in headers");
                return;
            }

            try {
                List<UserOrganization> history = uoRepository.getByUser(UUID.fromString(userId));
                ackRequest.sendAckData(history.stream().map(uoConverter::enityToDto).collect(Collectors.toList()));
            } catch (Exception e) {
                ackRequest.sendAckData("Error fetching history: " + e.getMessage());
            }
        });

        server.addEventListener("joinRoom", String.class, (client, userId, ackSender) -> {
            // Thêm client vào Room
            client.joinRoom(userId);
            System.out.println("Client " + client.getSessionId() + " joined room: " + userId);

            // (Tùy chọn) Gửi xác nhận cho client
            client.sendEvent("joinRoomSuccess", "You have joined room: " + userId);
        });
    }

}

