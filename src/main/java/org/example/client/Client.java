package org.example.client;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){
            System.out.println("Error creating Client!");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(String senderName, String messageToServer) {
        try {
            // Format the message as "senderName: messageContent"
            String formattedMessage = senderName + ": " + messageToServer;
            bufferedWriter.write(formattedMessage);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to the Server!");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void receiveMessageFromServer(VBox vbox_messages) {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    String messageFromServer = bufferedReader.readLine();
                    if (messageFromServer == null) {
                        // Handle disconnection gracefully
                        break;
                    }

                    // Split the message into senderName and messageContent
                    String[] parts = messageFromServer.split(": ", 2);

                    if (parts.length == 2) {
                        String senderName = parts[0];
                        String messageContent = parts[1];

                        // Pass senderName and messageContent to addLabel method
                        ClientController.addLabel(senderName, messageContent, vbox_messages);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error receiving message from the Server!");
            } finally {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }).start();
    }
}
