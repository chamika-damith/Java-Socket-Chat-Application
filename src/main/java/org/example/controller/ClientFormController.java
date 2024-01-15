package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientFormController {
    public Text txtNameField;
    public AnchorPane ClientRoot;
    public TextField txtMessage;

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Socket socket;
    String message = "";
    String reply = "";

    public void initialize(){
        txtNameField.setText( LoginFormController.username);

        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                while (!message.equals("finish")) {
                    message = dataInputStream.readUTF();
                    System.out.println("Server: " + message);
                }
            } catch (IOException e) {
                System.out.println("Error in the client: " + e.getMessage());
            }
        }).start();
    }

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        reply = txtMessage.getText();
        dataOutputStream.writeUTF(reply);
        dataOutputStream.flush();
    }
}
