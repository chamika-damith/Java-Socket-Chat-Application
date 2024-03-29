package org.example.controller;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.model.ClientModel;
import org.example.server.Server;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    public static String clientName = "";
    public static ArrayList<String> clientsNames = new ArrayList<>();
    public JFXTextField t1;
    public JFXButton b1;
    public AnchorPane loginRoot;

    private ClientModel clientModel=new ClientModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Server.startServer();

    }

    public void loginOnAction(ActionEvent actionEvent) throws SQLException {
        clientName = t1.getText();

        boolean b = clientModel.searchClient(clientName);
        if (b){

            clientsNames.add(clientName);
            t1.clear();
            Stage primaryStage = new Stage();

            try {
                primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error while loading the client UI : " + e.getLocalizedMessage());
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
                alert.showAndWait();

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
                    int clientIndex = clientsNames.indexOf(primaryStage.getTitle());
                    if (clientIndex != -1) {
                        Server.handleExitedClient(clientIndex);
                    }
                    primaryStage.close();
                } else {
                    // Cancelling the close request.
                    event.consume();
                }
            });
        }else {
            t1.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username not found", ButtonType.OK);
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
            alert.showAndWait();
        }


    }

    public void signupOnAction(ActionEvent mouseEvent) throws IOException {
        loginRoot.getChildren().clear();
        loginRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/SignupForm.fxml")));
    }
}
