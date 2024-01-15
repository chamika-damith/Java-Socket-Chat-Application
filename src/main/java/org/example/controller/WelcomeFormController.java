package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class WelcomeFormController {
    public AnchorPane WelcomeRoot;

    public void btnGetStartOnAction(ActionEvent actionEvent) throws IOException {
        WelcomeRoot.getChildren().clear();
        WelcomeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
    }
}
