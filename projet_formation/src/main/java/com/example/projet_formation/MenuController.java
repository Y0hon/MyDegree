package com.example.projet_formation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuController {
    @FXML
    private void switchToEtudiants() throws IOException {
        App.setRoot("etudiants", 900, 600);
    }

    @FXML
    private void switchToMentions() throws IOException {
        App.setRoot("mentions", 660, 450);
    }

    @FXML
    private void switchToParcours() throws IOException {
        App.setRoot("parcours", 800, 450);
    }

    @FXML
    private void switchToUEs() throws IOException {
        App.setRoot("ues", 850, 450);
    }

    @FXML
    private AnchorPane AnchorPane;
}
