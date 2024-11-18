package com.example.cabonerfbe.config;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerShutdown {

    @Autowired
    private SocketIOServer server;

    @PreDestroy
    public void onDestroy() {
        server.stop();
        System.out.println("Socket.IO server stopped");
    }
}
