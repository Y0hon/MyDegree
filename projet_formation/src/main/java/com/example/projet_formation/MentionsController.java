package com.example.projet_formation;

import com.example.projet_formation.Modele.Mention;
import com.example.projet_formation.Modele.Parcours;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MentionsController implements Initializable {

    private String repertoireCourant = System.getProperty("user.dir");

    private static Map<String, Mention> mentions;
    private ArrayList<Mention> listMention;
    private FilteredList<Mention> filterListMention;

    //Éléménts tableau étudiants
    @FXML
    private TableView<Mention> tableMentions;

    @FXML
    private TableView<Parcours> tableParcours;

    @FXML
    private TableColumn<Mention, String> colID;

    @FXML
    private TableColumn<Parcours, String> colID_parcours;

    @FXML
    private TableColumn<Mention, String> colLib;

    @FXML
    private TableColumn<Parcours, String> colLib_parcours;

    @FXML
    private TextField tbx_filtre;

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu", 600, 400);
    }

    private Controller controller;

    //Au lancement de la fenêtre
    public void initialize(URL url, ResourceBundle rb) {
        Controller.debuterSession();

        chargerCsvMentions();
    }

    /**
     * Charge le csv des mentions
     */
    public void chargerCsvMentions(){
        mentions= new HashMap<String,Mention>();
        try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/Donnees/mentions.csv"))) {
            List<String[]> mentionsCSV = reader.readAll();
            listMention = Controller.obtenirListeMentions();

            chargerTableauMentions();

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge le tableau des mentions
     */
    public void chargerTableauMentions(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        ObservableList<Mention> listObs = FXCollections.observableArrayList(listMention);

        //Filtre
        filterListMention = new FilteredList<Mention>(listObs, b->true);

        tbx_filtre.textProperty().addListener((observable, oldValue, newValue)->{
            filterListMention.setPredicate(Mentions -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCasFilter = newValue.toLowerCase();
                if(Mentions.getId().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else if(Mentions.getLibelle().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else{
                    return false;
                }
            });
        });

        SortedList<Mention> sortedListMention = new SortedList<Mention>(filterListMention);
        sortedListMention.comparatorProperty().bind(tableMentions.comparatorProperty());
        tableMentions.setItems(sortedListMention);
    }

    @FXML
    public void clickItemMention(MouseEvent event){
        String idMentionSelect = tableMentions.getSelectionModel().getSelectedItem().getId();
        ArrayList<Parcours> listeParcours = Controller.obtenirListeParcours(idMentionSelect);
        colID_parcours.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib_parcours.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        ObservableList<Parcours> listeParcoursObs = FXCollections.observableArrayList(listeParcours);
        tableParcours.setItems(listeParcoursObs);
    }

    /**
     * Permet de basculer sur le menu parcours
     * @param event
     * @throws IOException
     */
    @FXML
    public void switchToParcours(MouseEvent event) throws IOException {
        App.setRoot2("parcours", 800, 450, tableParcours.getSelectionModel().getSelectedItem().getId());
    }
}
