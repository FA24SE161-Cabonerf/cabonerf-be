package com.example.cabonerfbe.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerRunner implements CommandLineRunner {

    @Autowired
    private SocketIOServer server;

    @Override
    public void run(String... args) throws Exception {
        server.start();
        System.out.println("Socket.IO server started on port 9092");
    }

}
