package org.example.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginRoot;
    public JFXTextField txtUsername;

    public static String username;


    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        setUsername();
        Parent parent=FXMLLoader.load(getClass().getResource("/view/ClientForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Client Form");
        stage.show();
        txtUsername.clear();
    }

    public void setUsername(){
        username=txtUsername.getText();
    }
}
