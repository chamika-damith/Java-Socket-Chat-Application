package org.example.client;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.controller.LoginFormController;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public Text txtNameField;
    public TextField txtMessage;
    public ScrollPane scrollPane;
    public VBox vBox;
    public JFXButton button_send;
    private Client client;

    private static String name;


    public void initialize(URL url, ResourceBundle resourceBundle) {

        vBox.setStyle("-fx-background-color: linear-gradient(to bottom, #b1c7f2, #dbbdf2);");



        txtNameField.setText(LoginFormController.username);

        try {
            client = new Client(new Socket("localhost", 3004));
            System.out.println("Connected to Server");
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
}
