package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginRoot;

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        loginRoot.getChildren().clear();
        loginRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/welcomeForm.fxml")));
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {

    }
}
