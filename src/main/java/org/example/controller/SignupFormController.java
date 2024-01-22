package org.example.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SignupFormController {

    public JFXTextField userName;
    public JFXTextField txtEmail;
    public AnchorPane signupRoot;

    public void userNameOnAction(ActionEvent actionEvent) {

    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
    }

    public void SignUpOnAction(ActionEvent actionEvent) throws IOException {
        signupRoot.getChildren().clear();
        signupRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
    }

    public void LoginOnAction(MouseEvent mouseEvent) throws IOException {
        signupRoot.getChildren().clear();
        signupRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
    }
}
