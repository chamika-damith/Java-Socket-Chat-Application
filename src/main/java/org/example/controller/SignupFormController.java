package org.example.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.example.model.ClientModel;

import java.io.IOException;
import java.sql.SQLException;

public class SignupFormController {

    public JFXTextField userName;
    public JFXTextField txtEmail;
    public AnchorPane signupRoot;

    private ClientModel clientModel=new ClientModel();

    private String name;
    private String email;

    private LoginFormController loginFormController;

    public void userNameOnAction(ActionEvent actionEvent) {
        name=userName.getText();
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        email=txtEmail.getText();
    }

    public void SignUpOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        name=userName.getText();
        if (userName!=null){
            clientModel.saveClient(name);

        }
    }

    public void LoginOnAction(ActionEvent mouseEvent) throws IOException {
        signupRoot.getChildren().clear();
        signupRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
    }

    public void btnSignupOnAction(ActionEvent actionEvent) throws SQLException {
        name=userName.getText();
        if (name!=null){
            clientModel.saveClient(name);
            System.out.println("saved client");
        }
        name=null;
    }
}
