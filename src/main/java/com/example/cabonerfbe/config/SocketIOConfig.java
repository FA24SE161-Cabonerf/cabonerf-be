package com.example.cabonerfbe.config;

import com.corundumstudio.socketio.Configuration; // Socket.IO Configuration
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration // Trực tiếp chỉ định annotation của Spring
public class SocketIOConfig {

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092); // Cổng socket server

        config.setOrigin("*");

        SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(client -> {
            HandshakeData handshakeData = client.getHandshakeData();

            // Lấy userId từ header
            String userId = handshakeData.getHttpHeaders().get("x-user-id");
            if (userId != null && !userId.isEmpty()) {
                client.joinRoom(userId);
                System.out.println("User connected with userId: " + userId);
            } else {
                System.out.println("Connection rejected: userId is missing");
                client.disconnect(); // Từ chối kết nối nếu thiếu userId
            }
        });

        server.addDisconnectListener(client -> {
            HandshakeData handshakeData = client.getHandshakeData();

            String userId = handshakeData.getHttpHeaders().get("x-user-id");
            if (userId != null && !userId.isEmpty()) {
                client.leaveRoom(userId); // Rời khỏi Room khi ngắt kết nối
                System.out.println("User disconnected: " + userId);
            }else {
                System.out.println("Disconnect rejected: userId is missing");
            }
        });
        return server;
    }
}

