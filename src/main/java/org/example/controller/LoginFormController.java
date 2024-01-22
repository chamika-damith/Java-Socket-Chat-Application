package org.example.controller;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.server.Server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    public static String clientName = "";
    public static ArrayList<String> clientsNames = new ArrayList<>();
    public JFXTextField t1;
    public JFXButton b1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Server.startServer();

    }

    public void loginOnAction(ActionEvent actionEvent) {
        clientName = t1.getText();
        clientsNames.add(clientName);
        t1.clear();
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error while loading the client UI : " + e.getLocalizedMessage()).show();
        }
        primaryStage.getIcons().add(new Image("/image/chat.png"));
        primaryStage.setTitle(clientName+" chat");
        primaryStage.show();
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Confirm Exit!");
            alert.setContentText("Are you sure you want to exit the chatroom?");

            // Handling the user's response
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                Server.handleExitedClient(clientsNames.indexOf(primaryStage.getTitle()));
                primaryStage.close();


            } else {
                // Cancelling the close request.
                event.consume();
            }
        });


    }
}
