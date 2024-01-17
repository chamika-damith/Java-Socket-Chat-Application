package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<Clients> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3004)) {
            System.out.println("Server started!");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected!");

                    Clients clients = new Clients(socket, Server.clients);
                    Server.clients.add(clients);
                    clients.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
