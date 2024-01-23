package org.example.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

    public void userNameOnAction(ActionEvent actionEvent) {
        name=userName.getText();
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
            new Alert(Alert.AlertType.INFORMATION, "Saved client").show();
        }
        name=null;
    }
}
