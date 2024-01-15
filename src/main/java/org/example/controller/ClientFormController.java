package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class ClientFormController {
    public Text txtNameField;
    public AnchorPane ClientRoot;

    public void initialize(){
        txtNameField.setText( LoginFormController.username);
    }

    public void btnSendOnAction(ActionEvent actionEvent) {


    }
}
