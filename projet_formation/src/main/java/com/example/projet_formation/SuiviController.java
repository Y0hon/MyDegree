package com.example.projet_formation;

import com.example.projet_formation.Modele.UE;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class SuiviController {

    private String idActuel;

    @FXML
    private TableColumn<UE, String> colECTEffectuees;

    @FXML
    private TableColumn<UE, String> colECTEnCours;

    @FXML
    private TableColumn<UE, String> colIDEffectuees;

    @FXML
    private TableColumn<UE, String> colIDEnCours;


    @FXML
    private TableColumn<UE, String> colLibAVenir;

    @FXML
    private TableColumn<UE, String> colIDAVenir;

    @FXML
    private TableColumn<UE, String> colECTAVenir;

    @FXML
    private TableColumn<UE, String> colLibEffectuees;

    @FXML
    private TableColumn<UE, String> colLibEnCours;

    @FXML
    private TableColumn<UE, String> colLibMinimum;

    @FXML
    private TableView<UE> tableUEAVenir;

    @FXML
    private TableView<UE> tableUEEffectuees;

    @FXML
    private TableView<UE> tableUEEnCours;

    @FXML
    private TextField txt_mimimum;

    @FXML
    private Label Titre;

    public void recupDonnees(String recup, Controller utilisateur){
        this.idActuel = recup;

        colIDEnCours.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLibEnCours.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTEnCours.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listObs1 = FXCollections.observableArrayList(utilisateur.obtenirUesEnCoursSansDates(this.idActuel));
        tableUEEnCours.setItems(listObs1);

        colIDEffectuees.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLibEffectuees.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTEffectuees.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listObs2 = FXCollections.observableArrayList(utilisateur.obtenirUesValideesSansDates(this.idActuel));
        tableUEEffectuees.setItems(listObs2);

        colIDAVenir.setCellValueFactory(new PropertyValueFactory<>("ordreUE"));
        colLibAVenir.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTAVenir.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listObs3 = FXCollections.observableArrayList(utilisateur.obtenirUesAVenir(this.idActuel));
        tableUEAVenir.setItems(listObs3);

        txt_mimimum.setText(Controller.nombreDeSemestresMinimum(this.idActuel));
        Titre.setText("Suivi de "+Controller.etudiants.get(this.idActuel).getNom()+" "+Controller.etudiants.get(this.idActuel).getPrenom());
    }


}
