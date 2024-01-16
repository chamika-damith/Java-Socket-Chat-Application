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
    public AnchorPane ClientRoot;
    public TextField txtMessage;
    public ScrollPane scrollPane;
    public VBox vBox;
    public JFXButton button_send;

    private Client client;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtNameField.setText(LoginFormController.username);

        try {
            client = new Client(new Socket("localhost", 3003));
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


    public static void addLabel(String messageFromServer, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        String messageToSend = txtMessage.getText();
        if (!(messageToSend.isEmpty())) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);

            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle(
                    "-fx-color: rgb(239, 242, 255);" +
                            "-fx-background-color: rgb(15, 125, 242);" +
                            "-fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.925, 0.996));

            hBox.getChildren().add(textFlow);
            vBox.getChildren().add(hBox);

            if (client != null) {
                client.sendMessageToServer(messageToSend);
            } else {
                System.out.println("Client is null");
            }

            txtMessage.clear();
        }
    }
}
