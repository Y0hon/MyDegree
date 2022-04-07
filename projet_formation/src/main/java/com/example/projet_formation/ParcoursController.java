package com.example.projet_formation;

import com.example.projet_formation.Modele.Parcours;
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

public class ParcoursController implements Initializable {

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private TableColumn<UE, String> colECTS_UE;

    @FXML
    private TableColumn<Parcours, String> colID_Parcours;

    @FXML
    private TableColumn<UE, String> colID_UE;

    @FXML
    private TableColumn<Parcours, String> colLib_Parcours;

    @FXML
    private TableColumn<UE, String> colLib_UE;

    @FXML
    private ImageView img_back;

    @FXML
    private Label lbl_parcours;

    @FXML
    private Label lbl_parcours_ue;

    @FXML
    private TableView<Parcours> tableParcours;

    @FXML
    private TableView<UE> tableUE;

    @FXML
    private TextField tbx_filtre_parcours;

    @FXML
    void switchToMenu(MouseEvent event) throws IOException {
        App.setRoot("menu", 600, 400);
    }

    private String repertoireCourant = System.getProperty("user.dir");

    private static Map<String, Parcours> parcours;
    private ArrayList<Parcours> listeParcours;
    private FilteredList<Parcours> filterListeParcours;

    private Controller controller;

    /**
     * Initialisation du contrôleur
     * @param url
     * @param rb
     */
    //Au lancement de la fenêtre
    public void initialize(URL url, ResourceBundle rb) {
        Controller.debuterSession();

        chargerCsvParcours();
    }

    /**
     * Charge le csv de parcours
     */
    public void chargerCsvParcours() {
        parcours= new HashMap<String,Parcours>();
        try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/Donnees/parcours.csv"))) {
            List<String[]> parcoursCSV = reader.readAll();
            listeParcours = Controller.obtenirListeParcours();

            chargerTableauParcours();

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge le tableau de parcours
     */
    public void chargerTableauParcours() {
        colID_Parcours.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib_Parcours.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        ObservableList<Parcours> listObs = FXCollections.observableArrayList(listeParcours);

        //Filtre
        filterListeParcours = new FilteredList<Parcours>(listObs, b->true);

        tbx_filtre_parcours.textProperty().addListener((observable, oldValue, newValue)->{
            filterListeParcours.setPredicate(Parcours -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCasFilter = newValue.toLowerCase();
                if(Parcours.getId().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else if(Parcours.getLibelle().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else{
                    return false;
                }
            });
        });

        SortedList<Parcours> sortedListParcours = new SortedList<Parcours>(filterListeParcours);
        sortedListParcours.comparatorProperty().bind(tableParcours.comparatorProperty());
        tableParcours.setItems(sortedListParcours);
    }

    /**
     * Affichage après l'évènement clic sur un parcours
     * @param event
     */
    @FXML
    public void clickItemParcours(MouseEvent event){
        String idParcoursSelect = tableParcours.getSelectionModel().getSelectedItem().getId();
        ArrayList<UE> listeUE = Controller.obtenirListeUEsParcours(idParcoursSelect);

        colID_UE.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib_UE.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTS_UE.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listeUEObs = FXCollections.observableArrayList(listeUE);
        tableUE.setItems(listeUEObs);
    }

    /**
     * Récupère les données observables
     * @param recup
     */
    public void recupDonnees(String recup){
        tbx_filtre_parcours.setText(recup);

        String idParcoursSelect = recup;
        ArrayList<UE> listeUE = Controller.obtenirListeUEsParcours(idParcoursSelect);

        colID_UE.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib_UE.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECTS_UE.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listeUEObs = FXCollections.observableArrayList(listeUE);
        tableUE.setItems(listeUEObs);
    }

    @FXML
    public void switchToUEs(MouseEvent event) throws IOException {
        App.setRoot2("ues", 850, 450, tableUE.getSelectionModel().getSelectedItem().getId());
    }

}
