package com.example.cabonerfbe.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOEventHandler {

    @Autowired
    private SocketIOServer server;

    @OnConnect
    public void onConnect(com.corundumstudio.socketio.SocketIOClient client) {
        System.out.println("Client connected: " + client.getSessionId());
    }

    // Xử lý khi client ngắt kết nối
    @OnDisconnect
    public void onDisconnect(com.corundumstudio.socketio.SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    // Lắng nghe sự kiện "message" từ client
    @OnEvent("message")
    public void onMessage(com.corundumstudio.socketio.SocketIOClient client, String data) {
        System.out.println("Received message: " + data);

        // Phát ngược lại sự kiện tới tất cả các client
        server.getBroadcastOperations().sendEvent("message", "Server received: " + data);
    }

}

