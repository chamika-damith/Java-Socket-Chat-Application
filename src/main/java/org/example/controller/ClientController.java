package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public Text txtNameField;
    public TextField txtMessage;
    public ScrollPane scrollPane;
    public VBox vBox;
    public JFXButton button_send;
    public AnchorPane emojiPane;
    public AnchorPane imagePane;
    public ImageView imageView;
    private Client client;

    private static String name;

    private File file;


    public void initialize(URL url, ResourceBundle resourceBundle) {

        vBox.setStyle("-fx-background-color: linear-gradient(to bottom, #b1c7f2, #dbbdf2);");

        emojiPane.setVisible(false);

        txtNameField.setText(LoginFormController.username);

        try {
            client = new Client(new Socket("localhost", 3004));
            System.out.println("Connected to Server");

            if (LoginFormController.username != null) {
                //adding name of client which join the chat
                String joinMessage = "You have joined the chat";

                Label text = new Label(joinMessage);
                text.getStyleClass().add("join-text");
                HBox hBox = new HBox();
                hBox.getChildren().add(text);
                hBox.setAlignment(Pos.CENTER);

                Platform.runLater(() -> {
                    vBox.getChildren().add(hBox);

                    HBox hBox1 = new HBox();
                    hBox1.setPadding(new Insets(5, 5, 5, 10));
                    vBox.getChildren().add(hBox1);
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating Client ... ");
        }

        vBox.heightProperty().addListener((observable, oldValue, newValue) ->
                scrollPane.setVvalue(newValue.doubleValue()));

        if (client != null) {
            client.receiveMessageFromServer(vBox);
        } else {
            System.out.println("Client is null");
        }
    }


    public static void addLabel(String sender,String messageFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        name=sender;


        Text senderText = new Text( sender+ " :  ");
        senderText.setFill(Color.BLACK); // Set the color for the sender's name

        Text messageText = new Text(messageFromServer);
        messageText.setFill(Color.WHITE);
        TextFlow textFlow = new TextFlow(senderText, messageText);

        textFlow.setStyle(
                "-fx-background-color: linear-gradient(to right, #a8adad, #858c8c);" +
                        "-fx-font-size: 14px;"+
                        "-fx-background-radius: 8px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        String messageToSend = txtMessage.getText();
        if (!(messageToSend.isEmpty())) {

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text senderText = new Text("me :  ");
            senderText.setFill(Color.BLACK); // Set the color for the sender's name

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(senderText,text);
            textFlow.setStyle(
                    "-fx-color: rgb(239, 242, 255);" +
                            "-fx-background-color: linear-gradient(to right, #83e7eb, #3948ed);" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-radius: 8px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.925, 0.996));


            hBox.getChildren().addAll(textFlow);
            vBox.getChildren().add(hBox);

            if (client != null) {
                client.sendMessageToServer(txtNameField.getText(),messageToSend);
            } else {
                System.out.println("Client is null");
            }

            txtMessage.clear();
        }
    }

    public void btnEmojiOnAction(ActionEvent actionEvent) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    public void angrymoodOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE08");
        emojiPane.setVisible(false);
    }

    public void smilemoodOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }

    public void glassmoodOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE0E");
        emojiPane.setVisible(false);
    }

    public void heartmoodOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE0D");
        emojiPane.setVisible(false);
    }

    public void likeOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDC4D");
        emojiPane.setVisible(false);
    }

    public void txtMessageOnAction(ActionEvent actionEvent) {
        btnSendOnAction(actionEvent);
    }

    public void btnFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtMessage.getScene().getWindow());

    }

    private class Client{
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

                        // Process text message
                        String[] parts = messageFromServer.split(": ", 2);

                        if (parts.length == 2) {
                            String senderName = parts[0];
                            String messageContent = parts[1];

                            // Use Platform.runLater to update UI from a non-JavaFX thread
                            Platform.runLater(() -> addLabel(senderName, messageContent, vbox_messages));
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
}
