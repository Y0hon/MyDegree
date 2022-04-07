package com.example.projet_formation;

import com.example.projet_formation.Modele.UE;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UEsController implements Initializable {

    private String repertoireCourant = System.getProperty("user.dir");

    private static Map<String, UE> ues;
    private ArrayList<UE> listeUEs;
    private FilteredList<UE> filterListeUEs;

    private Controller controller;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private TableColumn<UE, String> colECTS_UEs;

    @FXML
    private TableColumn<UE, String> colECTS_prerequisUE;

    @FXML
    private TableColumn<UE, String> colID_UEs;

    @FXML
    private TableColumn<UE, String> colID_prerequisUE;

    @FXML
    private TableColumn<UE, String> colLib_UEs;

    @FXML
    private TableColumn<UE, String> colLib_prerequisUE;

    @FXML
    private ImageView img_back;

    @FXML
    private Label lbl_parcours_ue;

    @FXML
    private Label lbl_ues;

    @FXML
    private TableView<UE> tablePrerequisUE;

    @FXML
    private TableView<UE> tableUEs;

    @FXML
    private TextField tbx_filtre_ues;

    /**
     * Bascule sur le menu principal
     * @param event
     * @throws IOException
     */
    @FXML
    void switchToMenu(MouseEvent event) throws IOException {
        App.setRoot("menu", 600, 400);
    }

    /**
     * Initialise la fenêtre
     * @param url
     * @param rb
     */
    //Au lancement de la fenêtre
    public void initialize(URL url, ResourceBundle rb) {
        Controller.debuterSession();

        chargerCsvUEs();
    }

    /**
     * Charge le csv des ues
     */
    public void chargerCsvUEs() {
        ues = new HashMap<String,UE>();
        try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/Donnees/ues.csv"))) {
            List<String[]> uesCSV = reader.readAll();
            listeUEs = Controller.obtenirListeUEs();

            chargerTableauUEs();

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge le tableau des UEs
     */
    public void chargerTableauUEs() {
        colID_UEs.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib_UEs.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTS_UEs.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listObs = FXCollections.observableArrayList(listeUEs);

        //Filtre
        filterListeUEs = new FilteredList<UE>(listObs, b->true);

        tbx_filtre_ues.textProperty().addListener((observable, oldValue, newValue)->{
            filterListeUEs.setPredicate(UE -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCasFilter = newValue.toLowerCase();
                if(UE.getId().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else if(UE.getLibelle().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else if(String.valueOf(UE.getValeurCredit()).toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else{
                    return false;
                }
            });
        });

        SortedList<UE> sortedListUEs = new SortedList<UE>(filterListeUEs);
        sortedListUEs.comparatorProperty().bind(tableUEs.comparatorProperty());
        tableUEs.setItems(sortedListUEs);
    }

    /**
     * Affiche les ues prérequises dans le tableau de droite suite au clic
     * @param event
     */
    @FXML
    public void clickItemUEs(MouseEvent event){
        String idUEsSelect = tableUEs.getSelectionModel().getSelectedItem().getId();
        UE UESelect = tableUEs.getSelectionModel().getSelectedItem();
        Map<String, String> listeSTRPrerequisUEs = UESelect.getPrerequis();

        ArrayList<UE> listePrerequisUEs = new ArrayList<UE>();

        for (String idUE : listeSTRPrerequisUEs.keySet()) {
            if (Controller.ues.containsKey(idUE)){
                listePrerequisUEs.add(Controller.ues.get(idUE));
            }
        }

        colID_prerequisUE.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib_prerequisUE.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTS_prerequisUE.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listeUEObs = FXCollections.observableArrayList(listePrerequisUEs);
        tablePrerequisUE.setItems(listeUEObs);
    }

    /**
     * Récupération des données de prérequis UE
     * @param recup
     */
    public void recupDonnees(String recup) {
        tbx_filtre_ues.setText(recup);

        String idUEsSelect = recup;
        UE UESelect = Controller.ues.get(recup);
        Map<String, String> listeSTRPrerequisUEs = UESelect.getPrerequis();

        ArrayList<UE> listePrerequisUEs = new ArrayList<UE>();

        for (String idUE : listeSTRPrerequisUEs.keySet()) {
            if (Controller.ues.containsKey(idUE)){
                listePrerequisUEs.add(Controller.ues.get(idUE));
            }
        }

        colID_prerequisUE.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib_prerequisUE.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTS_prerequisUE.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listeUEObs = FXCollections.observableArrayList(listePrerequisUEs);
        tablePrerequisUE.setItems(listeUEObs);
    }
}
