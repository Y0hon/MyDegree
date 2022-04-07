package com.example.projet_formation;

import com.example.projet_formation.Modele.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author franklin
 */
public class EtudiantsController implements Initializable {

    //Variables générales
    private String repertoireCourant = System.getProperty("user.dir")+"/Donnees";//Recupère le répertoire pour les csv
    private Controller utilisateur; //Pour utiliser les méthodes d'utilisateur
    private String annee = "2021-2022"; //Pour stocker l'année en cours de consultation
    private int idAnnee; //On stock l'index de l'annee en cours de consultation
    private ArrayList<String> listAnneeU = new ArrayList<>(); //On stock toute les années
    private String semestre = "impair"; //Pour stocker le semestre en cours de consultation
    private String etat = "en cours"; //Pour stocker l'état actuel des UE en cours de consultation
    private String idUeActuel = ""; //Pour stocker l'id de l
    private static String repertoireImages = System.getProperty("user.dir")+"/projet_formation/src/main/resources/images/appicon.png";//Recupère le répertoire pour les csv
    // 'UE séléctionné

    @FXML
    private Label lbl_annee;
    @FXML
    private BorderPane bp;

    //Éléménts tableau étudiants
    @FXML
    private TableView<Etudiant> tableEtudiants;

    @FXML
    private TableColumn<Etudiant, String> colNom;

    @FXML
    private TableColumn<Etudiant, String> colNum;

    @FXML
    private TableColumn<Etudiant, String> colPrenom;

    @FXML
    private TextField tbx_filtre;

    private static Map<String, Etudiant> etudiants;
    private ArrayList<Etudiant> listEtud;
    private FilteredList<Etudiant> filterListEtud;

    //Éléménts infos générales
    @FXML
    private TextField tbx_dateNaiss;

    @FXML
    private TextField tbx_ects;

    @FXML
    private Label lbl_ects;

    @FXML
    private ComboBox<String> cbx_parcours;

    @FXML
    private ComboBox<String> cbx_mention;

    @FXML
    private TextField tbx_nom;

    @FXML
    private TextField tbx_prenom;

    @FXML
    private Button btn_ajouter_modifier;

    @FXML
    private Button btn_supprimer;

    private String idActuel;

    @FXML
    private Button btn_vers_ajouter;

    @FXML
    private ImageView btn_refreshEtudiant;

    @FXML
    private Circle cercle1;

    //Partie tableau UE
    @FXML
    private TableView<UE> tableUE;

    @FXML
    private TableColumn<UE, String> colECT;

    @FXML
    private TableColumn<UE, String> colId;

    @FXML
    private TableColumn<UE, String> colLib;

    @FXML
    private Button btn_anneeInf;

    @FXML
    private Button btn_anneeSup;

    @FXML
    private Button btn_semestre1;

    @FXML
    private Button btn_semestre2;

    @FXML
    private Button btn_actuel;

    @FXML
    private Button btn_valide;

    @FXML
    private Button btn_echoue;

    @FXML
    private Button btn_prerequis;

    @FXML
    private Button btn_valider;

    @FXML
    private Button btn_echouer;

    @FXML
    private Button btn_inscrire;

    @FXML
    private Button btn_enregistrer;

    @FXML
    private ImageView btn_refreshUE;

    @FXML
    private Circle cercle2;

    //MODIF
    @FXML
    private Button btn_suivi;

    //Au lancement de la fenêtre
    public void initialize(URL url, ResourceBundle rb) {
        //On initialise un début de session
        //utilisateur = new Utilisateur();
        Controller.debuterSession();

        btn_supprimer.setVisible(false);
        lbl_ects.setVisible(false);
        tbx_ects.setVisible(false);
        btn_inscrire.setVisible(false);
        btn_vers_ajouter.setVisible(false);
        btn_refreshEtudiant.setVisible(false);
        btn_refreshUE.setVisible(false);
        cercle1.setVisible(false);
        cercle2.setVisible(false);
        //MODIF
        btn_suivi.setVisible(false);
        chargerCsvEtudiant();
    }

    /**
     * Permet de charger le fichier csv étudiants pour en faire des objets étudiants
     */
    public void chargerCsvEtudiant(){
        etudiants= new HashMap<String,Etudiant>();
        try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/etudiants.csv"))) {
            List<String[]> etudiantscsv = reader.readAll();
            listEtud = new ArrayList<Etudiant>();
            for (String[] etudiant : etudiantscsv){
                Etudiant e = new Etudiant(etudiant[0],etudiant[1],etudiant[2],etudiant[3],Integer.parseInt(etudiant[4]),Integer.parseInt(etudiant[5]),etudiant[6],etudiant[7],etudiant[8]);
                etudiants.put(etudiant[0],e);
                listEtud.add(e);
            }

            chargerTableauEtudiant();

            //Partie Infos générales avec cbx
            CSVReader reader2 = new CSVReader(new FileReader(repertoireCourant+"/mentions.csv"));
            List<String[]> mentionsscsv = reader2.readAll();
            ObservableList<String> listMention = cbx_mention.getItems();
            for (String[] mention : mentionsscsv){
                listMention.add(mention[1]);
            }

            //Récupération des annees et de l'id annee actuel
            CSVReader reader3 = new CSVReader(new FileReader(repertoireCourant+"/anneesUniversitaires.csv"));
            List<String[]> RAnnee = reader3.readAll();
            int i = 0;
            //On recupere l'annee et le mois en cours pour vérifier dans quel annee Univ on se trouve
            int anneeActuel = Calendar.getInstance().get(Calendar.YEAR);
            int moisActuel = Calendar.getInstance().get(Calendar.MONTH)+1;
            boolean test = false;
            for (String[] annee : RAnnee){
                if((Integer.parseInt(annee[1]) == anneeActuel) && !test){//Ex: 2021  == 2022 donc non
                    if (moisActuel>8 && moisActuel<13){//Si on est en première partie de l'annee scolaire
                        idAnnee = i;
                        test = true;
                    }
                }
                else if((Integer.parseInt(annee[2]) == anneeActuel) && !test){//Ex: 2022  == 2022 donc oui
                    if (moisActuel>0 && moisActuel<9){ //Ex: 3(mars) donc oui
                        idAnnee = i;
                        test = true;
                    }
                }
                listAnneeU.add(annee[0]);
                i++;
            }
            annee = listAnneeU.get(idAnnee);
            lbl_annee.setText(listAnneeU.get(idAnnee));

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }


    /**
     * Permet de générer le tableau d'étudiant
     */
    public void chargerTableauEtudiant(){
        colNum.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        ObservableList<Etudiant> listObs = FXCollections.observableArrayList(listEtud);
        //tableEtudiants.setItems(listObs);

        //Filtre
        filterListEtud = new FilteredList<Etudiant>(listObs, b->true);

        tbx_filtre.textProperty().addListener((observable, oldValue, newValue)->{
            filterListEtud.setPredicate(Etudiant -> {
                if(newValue == null || newValue.isEmpty()){
                   return true;
                }
                String lowerCasFilter = newValue.toLowerCase();
                if(Etudiant.getId().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else if(Etudiant.getNom().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else if(Etudiant.getPrenom().toLowerCase().contains(lowerCasFilter)){
                    return true;
                }
                else{
                    return false;
                }
            });
        });

        SortedList<Etudiant> sortedListEtud = new SortedList<Etudiant>(filterListEtud);
        sortedListEtud.comparatorProperty().bind(tableEtudiants.comparatorProperty());
        tableEtudiants.setItems(sortedListEtud);
    }

    /**
     * Permet de définir les parcours possible à partir d'une mention séléctionnée
     * @param event
     * @throws FileNotFoundException
     */
    @FXML
    void chargerParcours(ActionEvent event) throws FileNotFoundException {
        try(CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/parcours.csv"))){
            List<String[]> parcoursscsv = reader.readAll();
            cbx_parcours.getItems().clear();
            ObservableList<String> listParcours = cbx_parcours.getItems();
            for (String[] parcours : parcoursscsv){
                if(parcours[1].equals(cbx_mention.getSelectionModel().getSelectedItem())){
                    listParcours.add(parcours[2]);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'afficher les informations générales du dernier étudiant séléctionné
     */
    public void majInfosG(){
        tbxAVide();
        Map<String,Mention> mentions = Controller.mentions;
        String idMention = tableEtudiants.getSelectionModel().getSelectedItem().getMention();
        String idParcours = tableEtudiants.getSelectionModel().getSelectedItem().getParcours();

        //affiche les infos
        tbx_nom.setText(tableEtudiants.getSelectionModel().getSelectedItem().getNom());
        tbx_prenom.setText(tableEtudiants.getSelectionModel().getSelectedItem().getPrenom());
        tbx_dateNaiss.setText(tableEtudiants.getSelectionModel().getSelectedItem().getDateNaissance());
        tbx_ects.setText(String.valueOf(tableEtudiants.getSelectionModel().getSelectedItem().getCredits()));
        //gérer mention et parcours
        cbx_mention.setValue(mentions.get(idMention).getLibelle());
        cbx_parcours.setValue(mentions.get(idMention).getListeParcours().get(idParcours).getLibelle());
    }

    /**
     * Permet d'afficher les infos générales de l'étudiant séléctionné
     * @param event
     */
    @FXML
    public void clickItem(MouseEvent event) {
        if(tableEtudiants.getSelectionModel().getSelectedItem() != null){
            //On sauvegarde l'id de létudiant en cours
            idActuel = tableEtudiants.getSelectionModel().getSelectedItem().getId();
            majInfosG();

            //Modification de l'affichage
            btn_ajouter_modifier.setText("Modifier");
            btn_vers_ajouter.setVisible(true);
            btn_supprimer.setVisible(true);
            lbl_ects.setVisible(true);
            tbx_ects.setVisible(true);
            btn_enregistrer.setDisable(false);
            btn_valider.setDisable(false);
            btn_echouer.setDisable(false);
            btn_valide.setDisable(false);
            btn_echoue.setDisable(false);
            btn_prerequis.setDisable(false);
            btn_semestre2.setDisable(false);
            btn_anneeSup.setDisable(false);
            btn_anneeInf.setDisable(false);
            if(idAnnee == listAnneeU.size()-1){
                btn_anneeSup.setDisable(true);
            }
            if(idAnnee == 0){
                btn_anneeInf.setDisable(true);
            }
            btn_refreshEtudiant.setVisible(true);
            btn_refreshUE.setVisible(true);
            cercle1.setVisible(true);
            cercle2.setVisible(true);
            //Gestion du tableau des UE
            remplirTableauUE(utilisateur.obtenirUesEnCours(idActuel,annee,semestre));
            //MODIF
            btn_suivi.setVisible(true);
            btn_suivi.setDisable(false);
        }
    }


    /**
     * Permet d'ajouter un étudiant dans le csv et dans le tableau
     * @param event
     */
    @FXML
    void ajouterModifier(ActionEvent event) {


        if(tbx_nom.getText() != "" && tbx_prenom.getText() != "" && tbx_dateNaiss.getText() != "" && cbx_mention.getValue() != "" && cbx_parcours.getValue() != ""){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {

                String nomMention=cbx_mention.getValue();
                String nomParcours=cbx_parcours.getValue();
                String idMention="";
                String idParcours="";
                for(String keyM: Controller.mentions.keySet()){
                    if(Controller.mentions.get(keyM).getLibelle().equals(nomMention)){
                        idMention=keyM;
                        for(String keyP : Controller.mentions.get(keyM).getListeParcours().keySet()){
                            if(Controller.mentions.get(keyM).getListeParcours().get(keyP).getLibelle().equals(nomParcours)){
                                idParcours=keyP;
                            }
                        }
                    }
                }
                date = dateFormat.parse(tbx_dateNaiss.getText());
                if(btn_ajouter_modifier.getText().equals("Ajouter")){
                    //Ajout
                    try{
                        //On écrit sur le fichier csv (sans le supprimer donc ajout)
                        BufferedWriter sortie = new BufferedWriter(new FileWriter(repertoireCourant+"/etudiants.csv", true));
                        //A terme enlever l'id du constructeur pour qu'il soit automatique et différent
                        //Création de l'objet étudiant
                        Etudiant etudiant = new Etudiant(tbx_prenom.getText(),tbx_nom.getText(),tbx_dateNaiss.getText(),0,30,"en cours",idMention,idParcours);
                        //On ajoute l'étudiant au csv
                        sortie.write(etudiant.getId());
                        sortie.write(",");
                        sortie.write("\""+etudiant.getPrenom()+"\"");
                        sortie.write(",");
                        sortie.write("\""+etudiant.getNom()+"\"");
                        sortie.write(",");
                        sortie.write(etudiant.getDateNaissance());
                        sortie.write(",");
                        sortie.write(String.valueOf(etudiant.getCredits()));
                        sortie.write(",");
                        sortie.write("30");
                        sortie.write(",");
                        sortie.write("\"en cours\"");
                        sortie.write(",");
                        sortie.write("\""+idMention+"\"");
                        sortie.write(",");
                        sortie.write("\""+idParcours+"\"");
                        sortie.write("\n");
                        sortie.flush();
                        sortie.close();
                        Controller.chargerCsvs();
                        //On gère l'affichage
                        tbxAVide();
                        btn_supprimer.setVisible(false);
                        lbl_ects.setVisible(false);
                        tbx_ects.setVisible(false);
                        etudiants.put(etudiant.getId(),etudiant);
                        listEtud.add(etudiant);
                        chargerTableauEtudiant();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //modification
                    Etudiant etudiant = etudiants.get(idActuel);
                    etudiant.setPrenom(tbx_prenom.getText());
                    etudiant.setNom(tbx_nom.getText());
                    etudiant.setDateNaissance(tbx_dateNaiss.getText());
                    etudiant.setParcours(idParcours);
                    etudiant.setMention(idMention);

                    try{
                        //réécriture du csv avec les modifications
                        csvWriter();
                        //On gère l'affichage
                        chargerCsvEtudiant();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message d'information");
                alert.setContentText("La date de naissance est invalide");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        //Rien ne ce passe
                    }
                });
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message d'information");
            alert.setContentText("Vous avez des champs vides, c'est donc impossible d'ajouter ou de modifier l'étudiant!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    //Rien ne ce passe
                }
            });
        }
    }

    /**
     * Permet de supprimer un étudiant
     * @param event
     */
    @FXML
    void supprimer(ActionEvent event) throws IOException {
        listEtud.remove(etudiants.get(idActuel));
        etudiants.remove(idActuel);

        try{
            //Réécriture du csv avec suppression
            csvWriter();
            csvWriterSuivi();
            //On gère l'affichage
            tbxAVide();
            btn_supprimer.setVisible(false);
            lbl_ects.setVisible(false);
            tbx_ects.setVisible(false);
            btn_ajouter_modifier.setText("Ajouter");
            chargerTableauEtudiant();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de réécrire le fichier csv étudiants (il y a suppression des éléments puis réécriture donc remplacement)
     * @throws IOException
     */
    public void csvWriter() throws IOException {
        try{
            FileWriter sortie = new FileWriter(repertoireCourant+"/etudiants.csv");

            for(Etudiant etudiant: listEtud){
                sortie.append(etudiant.getId());
                sortie.append(",");
                sortie.append("\""+etudiant.getPrenom()+"\"");
                sortie.append(",");
                sortie.append("\""+etudiant.getNom()+"\"");
                sortie.append(",");
                sortie.append(etudiant.getDateNaissance());
                sortie.append(",");
                sortie.append(String.valueOf(etudiant.getCredits()));
                sortie.append(",");
                sortie.append("30");
                sortie.append(",");
                sortie.append("\"en cours\"");
                sortie.append(",");
                sortie.append("\""+etudiant.getMention()+"\"");
                sortie.append(",");
                sortie.append("\""+etudiant.getParcours()+"\"");
                sortie.append("\n");
            }
            sortie.flush();
            sortie.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Réécrit les suivis dans les CSV
     * @throws IOException
     */
    public void csvWriterSuivi() throws IOException {
        try{
            FileWriter sortie = new FileWriter(repertoireCourant+"/suivis.csv");

            for(Etudiant etudiant: listEtud){
                for(String keyUE : etudiant.getUeSuivies().keySet())
                {
                    for(String keyDate : etudiant.getUeSuivies().get(keyUE).keySet()){
                        sortie.append(etudiant.getId());
                        sortie.append(",");
                        sortie.append("\""+keyUE+"\"");
                        sortie.append(",");
                        sortie.append("\""+etudiant.getUeSuivies().get(keyUE).get(keyDate).getAnnee()+"\"");
                        sortie.append(",");
                        sortie.append(etudiant.getUeSuivies().get(keyUE).get(keyDate).getSemestre());
                        sortie.append(",");
                        sortie.append(etudiant.getUeSuivies().get(keyUE).get(keyDate).getStatut());
                        sortie.append("\n");
                    }
                }

            }
            sortie.flush();
            sortie.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Permet de vider tous les champs (informations générales)
     */
    public void tbxAVide(){
        tbx_prenom.setText("");
        tbx_nom.setText("");
        tbx_dateNaiss.setText("");
        cbx_parcours.setValue(null);
        cbx_mention.setValue(null);
        tbx_ects.setText("");
    }

    /**
     * Permet de remplir le tableau Ue selon l'etat en cours de consultation
     */
    public void choixEtat(){
        if(etat.equals("en cours")){
            remplirTableauUE(utilisateur.obtenirUesEnCours(idActuel,annee,semestre));
        }
        else if(etat.equals("validee")){
            remplirTableauUE(utilisateur.obtenirUesValidees(idActuel,annee,semestre));
        }
        else if(etat.equals("echouee")){
            remplirTableauUE(utilisateur.obtenirUesEchouees(idActuel,annee,semestre));
        }
        else if(etat.equals("prerequis")){
            remplirTableauUE(utilisateur.obtenirUesEligibles(idActuel));
        }
    }

    /**
     * Permet de faciliter l'affichage du tableau d'UE à partir de csv
     * @param listUe C'est la liste des UE de l'étudiant dans un état en 'En cour', 'validee', 'echouee' ou avec les prérequis
     */
    public void remplirTableauUE(ArrayList<UE> listUe){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLib.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colECT.setCellValueFactory(new PropertyValueFactory<>("valeurCredit"));

        ObservableList<UE> listObs2 = FXCollections.observableArrayList(listUe);
        tableUE.setItems(listObs2);
    }

    /**
     * Permet d'afficher les UE actuel de l'étudiant séléctionné'
     * @param event
     */
    @FXML
    void actuel(ActionEvent event) throws IOException {
        etat = "en cours";
        btn_actuel.setDisable(true);
        btn_valide.setDisable(false);
        btn_echoue.setDisable(false);
        btn_prerequis.setDisable(false);

        btn_valider.setVisible(true);
        btn_echouer.setVisible(true);
        btn_inscrire.setVisible(false);
        choixEtat();
    }

    /**
     * Permet d'afficher les UE que l'étudiant séléctionné à validé'
     * @param event
     */
    @FXML
    void valide(ActionEvent event) throws IOException {
        etat = "validee";
        btn_actuel.setDisable(false);
        btn_valide.setDisable(true);
        btn_echoue.setDisable(false);
        btn_prerequis.setDisable(false);

        btn_valider.setVisible(false);
        btn_echouer.setVisible(false);
        btn_inscrire.setVisible(false);
        choixEtat();
    }

    /**
     * Permet d'afficher les UE que l'étudiant séléctionné à échoué'
     * @param event
     */
    @FXML
    void echoue(ActionEvent event) throws IOException {
        etat = "echouee";
        btn_actuel.setDisable(false);
        btn_valide.setDisable(false);
        btn_echoue.setDisable(true);
        btn_prerequis.setDisable(false);

        btn_valider.setVisible(false);
        btn_echouer.setVisible(false);
        btn_inscrire.setVisible(false);
        choixEtat();
    }

    /**
     * Permet d'afficher les UE dont l'étudiant séléctionné a les prérequis'
     * @param event
     */
    @FXML
    void prerequis(ActionEvent event) throws IOException {
        etat = "prerequis";
        btn_actuel.setDisable(false);
        btn_valide.setDisable(false);
        btn_echoue.setDisable(false);
        btn_prerequis.setDisable(true);

        btn_valider.setVisible(false);
        btn_echouer.setVisible(false);
        btn_inscrire.setVisible(true);
        choixEtat();
    }

    /**
     * Permet de changer l'année vers l'arrière
     * @param event
     */
    @FXML
    void passerAnneeInf(ActionEvent event) {
        idAnnee = idAnnee - 1;
        annee = listAnneeU.get(idAnnee);
        lbl_annee.setText(annee);
        if(idAnnee == 0){
            btn_anneeInf.setDisable(true);
        }
        else {
            btn_anneeSup.setDisable(false);
        }
        choixEtat();
    }

    /**
     * Permet de changer l'année vers l'avant
     * @param event
     */
    @FXML
    void passerAnneeSup(ActionEvent event) {
        idAnnee = idAnnee + 1;
        annee = listAnneeU.get(idAnnee);
        lbl_annee.setText(annee);
        if(idAnnee == listAnneeU.size()-1){
            btn_anneeSup.setDisable(true);
        }
        else {
            btn_anneeInf.setDisable(false);
        }
        choixEtat();
    }

    /**
     * Permet de passer au semestre 1 (impair)
     * @param event
     */
    @FXML
    void passerSemestre1(ActionEvent event) {
        semestre = "impair";
        btn_semestre1.setDisable(true);
        btn_semestre2.setDisable(false);
        choixEtat();
    }

    /**
     * Permet de passer au semestre 2 (pair)
     * @param event
     */
    @FXML
    void passerSemestre2(ActionEvent event) {
        semestre = "pair";
        btn_semestre1.setDisable(false);
        btn_semestre2.setDisable(true);
        choixEtat();
    }

    /**
     * Permet d'enregistrer toutes les modification apportées au tableau d'UE
     * On effectue bien les passages d'UE à validé, échouer ou ce inscrit
     * Cela consiste à réécrire le csv de suivi avec les nouveaux
     * @param event
     */
    @FXML
    void enregistrerUE(ActionEvent event) throws IOException {

        try{
            FileWriter sortie = new FileWriter(repertoireCourant+"/suivis.csv");

            Map<String,Map<String, HashMap<String, Suivi>>> listeSuivi = utilisateur.obtenirSuivi();
            String idEtudiant = "";
            String idUe = "";
            Suivi s ;

            for(String keyEtudiant : listeSuivi.keySet()){
                idEtudiant = keyEtudiant;
                for(String keyUE : listeSuivi.get(keyEtudiant).keySet()){
                    idUe = keyUE;
                    for(String keySuivi : listeSuivi.get(keyEtudiant).get(keyUE).keySet()){
                        s = listeSuivi.get(keyEtudiant).get(keyUE).get(keySuivi);
                        sortie.append("\""+idEtudiant+"\"");//id de l'etudiant
                        sortie.append(",");
                        sortie.append("\""+idUe+"\"");//id de l'ue
                        sortie.append(",");
                        sortie.append("\""+s.getAnnee()+"\"");//annee
                        sortie.append(",");
                        sortie.append("\""+s.getSemestre()+"\"");//semestre
                        sortie.append(",");
                        sortie.append("\""+s.getStatut()+"\"");//statut
                        sortie.append("\n");
                    }
                }
            }
            sortie.flush();
            sortie.close();

            listEtud = utilisateur.obtenirListeEtudiants();
            csvWriter();
            tbxAVide();
            Map<String,Mention> mentions = utilisateur.mentions;
            String idMention = utilisateur.etudiants.get(idActuel).getMention();
            String idParcours = utilisateur.etudiants.get(idActuel).getParcours();

            //affiche les infos
            tbx_nom.setText(utilisateur.etudiants.get(idActuel).getNom());
            tbx_prenom.setText(utilisateur.etudiants.get(idActuel).getPrenom());
            tbx_dateNaiss.setText(utilisateur.etudiants.get(idActuel).getDateNaissance());
            tbx_ects.setText(String.valueOf(utilisateur.etudiants.get(idActuel).getCredits()));
            //gérer mention et parcours
            cbx_mention.setValue(mentions.get(idMention).getLibelle());
            cbx_parcours.setValue(mentions.get(idMention).getListeParcours().get(idParcours).getLibelle());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Permet de passer une UE de actuel à validée (sans le faire dans le csv)
     * @param event
     */
    @FXML
    void validerUE(ActionEvent event) {
        if(!idUeActuel.equals("")){
            Controller.modifierUnSuivi(idActuel,idUeActuel,annee+" "+semestre,"validee");
            System.out.println(Controller.etudiants.get(idActuel).getCredits());
            Controller.etudiants.get(idActuel).ajouterCredits(Controller.ues.get(idUeActuel).getValeurCredit());
            System.out.println(Controller.etudiants.get(idActuel).getCredits());
            choixEtat();
        }
    }

    /**
     * Permet de passer une UE de actuel à échouée (sans le faire dans le csv)
     * @param event
     */
    @FXML
    void echouerUE(ActionEvent event) {
        if(!idUeActuel.equals("")){
            Controller.modifierUnSuivi(idActuel,idUeActuel,annee+" "+semestre,"echouee");
            choixEtat();
        }
    }

    /**
     * Permet de passer un UE de prérequis à actuel (sans le faire dans le csv)
     * @param event
     */
    @FXML
    void inscrireUE(ActionEvent event) {
        if(!idUeActuel.equals("")){
            Controller.ajouterUnSuivi(idActuel,idUeActuel,annee,semestre,"en cours");
            choixEtat();
        }
    }

    /**
     * Permet d'afficher les infos générales de l'étudiant séléctionné
     * @param event
     */
    @FXML
    public void clickItemUE(MouseEvent event) throws IOException {

        if(tableUE.getSelectionModel().getSelectedItem() != null){
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if(event.getClickCount() == 2){
                    App.setRoot2("ues", 850, 450, tableUE.getSelectionModel().getSelectedItem().getId());
                }
            }
            idUeActuel = tableUE.getSelectionModel().getSelectedItem().getId();
        }
    }

    /**
     * Permet de revenir en mode ajout étudiant (btn ajout visible et formulaire vide)
     * @param event
     */
    @FXML
    void swichToAjouter(ActionEvent event) {
        idActuel = "";
        //On gère l'affichage
        tbxAVide();
        btn_vers_ajouter.setVisible(false);
        btn_supprimer.setVisible(false);
        lbl_ects.setVisible(false);
        tbx_ects.setVisible(false);
        btn_ajouter_modifier.setText("Ajouter");
        btn_enregistrer.setDisable(true);
        btn_valider.setDisable(true);
        btn_echouer.setDisable(true);
        btn_valide.setDisable(true);
        btn_echoue.setDisable(true);
        btn_prerequis.setDisable(true);
        btn_semestre2.setDisable(true);
        btn_anneeSup.setDisable(true);
        btn_anneeInf.setDisable(true);
        btn_refreshEtudiant.setVisible(false);
        btn_refreshUE.setVisible(false);
        cercle1.setVisible(false);
        cercle2.setVisible(false);
        tableUE.getItems().clear();
        //MODIF
        btn_suivi.setDisable(true);
        btn_suivi.setVisible(true);
    }

    /**
     * Permet refresh le tableau des UE avec les informations actuelles de l'étudiant (au appuie sur enregistrement)
     * @param event
     */
    @FXML
    void refreshUE(MouseEvent event) {
        //On recharge tous les objets à partir du csv
        Controller.chargerCsvs();
        //On réaffiche le tableau
        choixEtat();
    }

    /**
     * Permet de refresh les informations de l'étudiant dans le cas où l'utilisateur souhaite les retrouver (au appuie sur modification)
     * @param event
     */
    @FXML
    void refreshEtudiant(MouseEvent event) {
        //On recharge toutes les infos de l'étudiant
        majInfosG();
    }

    /**
     * Permet de ce déplacer vers le menu
     * @throws IOException
     */
    @FXML
    private void switchToMenu() throws IOException {
        //App.setRoot("menu");
        App.setRoot("menu", 640, 480);
        //App.setRoot2("menu", 640, 480);
    }

    @FXML
    public void switchToUEs(MouseEvent event) throws IOException {
        App.setRoot2("ues", 850, 450, tableUE.getSelectionModel().getSelectedItem().getId());
    }

    /**
     * Permet de ce déplacer vers le menu
     * @throws IOException
     */
    @FXML
    private void switchToSuivi() throws IOException {
        //MODIF
        Stage newWindow = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("suivi.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        newWindow.setScene(scene);
        newWindow.centerOnScreen();
        Image icon = new Image((repertoireImages));
        newWindow.getIcons().add(icon);

        SuiviController controllerSuivi = fxmlLoader.getController();
        //Utilise la méthode test() présent dans le MenuControler avec un string en param
        controllerSuivi.recupDonnees(idActuel, utilisateur);

        newWindow.show();

    }
}