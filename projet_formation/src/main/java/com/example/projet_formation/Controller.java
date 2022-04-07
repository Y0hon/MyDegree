/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.projet_formation;

import com.example.projet_formation.Modele.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author yohan
 */
public class Controller {

    protected static HashMap<String, Etudiant> etudiants; //Hashmap statique de tous les étudiants clé : =idEtudiant valeur=objet Etudiant
    protected static HashMap<String, Mention> mentions; //Hashmap statique de toutes les mentions :  clé=idEtudiant valeur=objet Etudiant
    protected static HashMap<String, AnneeUniversitaire> annees; //Toutes les années universitaires disponibles pour les données
    protected static HashMap<String, UE> ues; //Toutes les UEs existantes
    protected static String repertoireCourant;

    /**
     * Initialisation des structures de données
     */
    protected static void debuterSession(){
         repertoireCourant = System.getProperty("user.dir")+"/Donnees";
         etudiants= new HashMap<String,Etudiant>();
         mentions= new HashMap<String,Mention>();
         annees = new HashMap<String,AnneeUniversitaire>();
         ues = new HashMap<String,UE>();
         //Chargement des fichiers CSV
         chargerCsvs();
     }

    /**
     * Initialise toutes les UEs à partir des CSV
     */
    private static void chargerUEs(){
         //Charge les UEs depuis le csv
         ues.clear();
         try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/ues.csv"))) {
             List<String[]> uesCsv = reader.readAll(); //List générée par le reader
             for (String[] ue : uesCsv){ //Itération sur chaque ligne (une ligne est représentée par un tableau ue indexé pour chaque élément)
                 ues.put(ue[0],new UE(ue[0],ue[1],Integer.parseInt(ue[2]))); //Ajoute l'UE à l'arraylist ues
                     for(int i=3;i<ue.length;i++) {
                         ues.get(ue[0]).ajouterPrerequis(ue[i],ues.get(ue[i]).getLibelle()); //Ajoute les UE prérequis à cette UE,
                         // Le second paramètre va aller chercher le libellé correspondant à l'ID du prérequis
                     }
             }
         } catch (IOException | CsvException e) {
             e.printStackTrace();
         }

     }

    /**
     * Initialise toutes les années des CSV
     */
    private static void chargerAnnees(){
        //Initialisation années
        annees.clear();
        try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/anneesUniversitaires.csv"))) {
            List<String[]> anneesCsv = reader.readAll();
            for (String[] annee : anneesCsv){
                annees.put(annee[0],new AnneeUniversitaire(annee[1],annee[2]));//Ajoute l'année à la HashMap annees
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise tous les objets étudiants des CSV
     */
    private static void chargerEtudiants(){
        //Initialisation années
        etudiants.clear();
        try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/etudiants.csv"))) {
            List<String[]> etudiantsCsv = reader.readAll();
            for (String[] etudiant : etudiantsCsv){ //Itération sur chaque ligne (une ligne est représentée par un tableau ue indexé pour chaque élément)
                etudiants.put(etudiant[0],new Etudiant(etudiant[0],etudiant[1],etudiant[2],etudiant[3],Integer.parseInt(etudiant[4])
                        ,Integer.parseInt(etudiant[5]),etudiant[6],etudiant[7],etudiant[8])); //Ajoute l'étudiant à l'arraylist annees
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise tous les suivis des étudiants
     */
    private static void chargerSuivis(){
        //Initialisation suivis
        for(String key : etudiants.keySet()){
            etudiants.get(key).getUeSuivies().clear();
        }
        try (CSVReader reader = new CSVReader(new FileReader(repertoireCourant+"/suivis.csv"))) {
            List<String[]> suivisCsv = reader.readAll();
            for (String[] suivi : suivisCsv){ //Itération sur chaque ligne (une ligne est représentée par un tableau ue indexé pour chaque élément)
                etudiants.get(suivi[0]).ajouterUeSuivis(suivi[1],suivi[2],suivi[3],suivi[4]); //Ajoute les matières suivies à l'étudiant avec l'id en suivi[0]
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise toutes les mentions et parcours des CSV
     */
    private static void chargerMentionsParcours(){
        //Initialisation des mentions et des parcours associées + leurs UEs obligatoires
        mentions.clear();
        Mention.getUEOuvertures().clear();
        try (CSVReader readerMentions = new CSVReader(new FileReader(repertoireCourant+"/mentions.csv"))) {
            List<String[]> mentionsCsv = readerMentions.readAll();
            for (String[] mention : mentionsCsv){ //Itération sur chaque ligne (une ligne est représentée par un tableau ue indexé pour chaque élément)
                mentions.put(mention[0],new Mention(mention[0],mention[1])); //Ajoute la mention à la Hashmap Mention
                for(int i=2;i<mention.length;i++){
                    mentions.get(mention[0]).ajouterParcours(mention[i],repertoireCourant, ues); //Ajoute les parcours correspondants à cette mention
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        // Chargement des UEs associées
        try (CSVReader readerUEAssociees = new CSVReader(new FileReader(repertoireCourant+"/uesAssociees.csv"))) {
            List<String[]> uesAssocieesCsv = readerUEAssociees.readAll();
            for (String[] ue : uesAssocieesCsv){
                for (int i=1; i < ue.length; i++) {
                    mentions.get(ue[0]).ajouterUEAssociees(ue[i],ues.get(ue[i]).getLibelle());
                }
            }
        } catch (IOException  | CsvException e) {
            e.printStackTrace();
        }
        //Chargement des UEs d'ouverture
        try (CSVReader readerUEsOuvertures = new CSVReader(new FileReader(repertoireCourant+"/ues.csv"))) {
            List<String[]> uesOuverturesCsv = readerUEsOuvertures.readAll();
            for (String[] ueOuverture : uesOuverturesCsv){
                if(ueOuverture[0].substring(0,3).equals("OUV")){
                    Mention.ajouterUeOuverture(ueOuverture[0]);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge l'ensemble des fichiers CSV
     */
    public static void chargerCsvs(){
        chargerUEs();
        chargerAnnees();
        chargerMentionsParcours();
        chargerEtudiants();
        chargerSuivis();
    }

    /**
     * Permet de changer le statut d'un Suivi
     * @param idEtudiant Étudiant choisi
     * @param idUe id de l'UE choisi
     * @param idSuivi   Id du suivi de l'étudiant
     * @param nouveauStatut Le nouveau statut ('en cours','validee' ou 'echouee')
     * @author franklin
     */
    public static void modifierUnSuivi(String idEtudiant,String idUe, String idSuivi, String nouveauStatut){
        etudiants.get(idEtudiant).modifierStatutSuivi(idUe,idSuivi,nouveauStatut);
    }

    public static void ajouterUnSuivi(String idEtudiant, String idUE, String annee, String semestre, String statut){
        etudiants.get(idEtudiant).ajouterUeSuivis(idUE,annee,semestre,statut);
    }

    /**
     * Permet de récuperer la liste de tous les suivis
     * @return La liste de tous les UE
     * @author franklin
     */
    public static Map<String,Map<String, HashMap<String, Suivi>>> obtenirSuivi(){
        Map<String,Map<String, HashMap<String, Suivi>>> listeSuivi = new HashMap<String,Map<String, HashMap<String, Suivi>>>();
        for(String key : etudiants.keySet()){
            listeSuivi.put(etudiants.get(key).getId(),etudiants.get(key).getUeSuivies());
        }
        return  listeSuivi;
    }

    /**
     * Retourne toutes les UEs en cours d'un étudiant au cours d'un semestre et d'une année
     * @param idEtudiant
     * @param annee
     * @param semestre
     * @return
     */
    public static ArrayList<UE> obtenirUesEnCours(String idEtudiant, String annee, String semestre){
        String idDate = annee+" "+semestre;
        ArrayList<UE> uesValidees = new ArrayList<UE>();
        Map <String, HashMap<String, Suivi>> listeSuivis = etudiants.get(idEtudiant).getUeSuivies();
        for(String keyUeSuivis : listeSuivis.keySet()){ //Itération sur toutes les UEs suivies
            if(listeSuivis.get(keyUeSuivis).containsKey(idDate)){ //Vérifie si cette UE a été suivi à la période demandée, exemple : "2021-2022 impair"
                if(listeSuivis.get(keyUeSuivis).get(idDate).getStatut().equals("en cours")){ //Vérifie si l'UE a été validé
                    uesValidees.add(ues.get(keyUeSuivis)); //Ajoute l'UE au retour
                }
            }
        }
        return  uesValidees;
    }

    /**
     * Retourne toutes les UEs en cours d'un étudiant
     * @param idEtudiant
     * @return
     */
    public static ArrayList<UE> obtenirUesEnCoursSansDates(String idEtudiant){
        ArrayList<UE> uesActuelles = new ArrayList<UE>();
        Map <String, HashMap<String, Suivi>> listeSuivis = etudiants.get(idEtudiant).getUeSuivies();
        for(String keyUeSuivis : listeSuivis.keySet()){ //Itération sur toutes les UEs suivies
            for(String keyIdDate : listeSuivis.get(keyUeSuivis).keySet()){ //Itération sur toutes les dates suivies pour l'UE courante (qui a pour ID keyUeSuivis)
                if(listeSuivis.get(keyUeSuivis).get(keyIdDate).getStatut().equals("en cours")){
                    uesActuelles.add(ues.get(keyUeSuivis));
                }
            }
        }
        return  uesActuelles;
    }

    /**
     * Retourne toutes les UEs validées par un étudiant selon son id
     * @param idEtudiant
     * @return
     */
    public static ArrayList<String> obtenirUesValidees(String idEtudiant){
        ArrayList<String> uesValidees = new ArrayList<String>();
        Map <String, HashMap<String, Suivi>> listeSuivis = etudiants.get(idEtudiant).getUeSuivies();
        for(String keyUeSuivis : listeSuivis.keySet()){ //Itération sur toutes les UEs suivies
            for(String keyDate : listeSuivis.get(keyUeSuivis).keySet())
                if(listeSuivis.get(keyUeSuivis).get(keyDate).getStatut().equals("validee")){ //Vérifie si l'UE a été validé
                    uesValidees.add(keyUeSuivis); //Ajoute l'UE au retour
                }
        }
        return  uesValidees;
    }

    /**
     * Retourne toutes les UEs validées par l'étudiant
     * @param idEtudiant
     * @return
     */
    public static ArrayList<UE> obtenirUesValideesSansDates(String idEtudiant){
        ArrayList<UE> uesValidees = new ArrayList<UE>();
        Map <String, HashMap<String, Suivi>> listeSuivis = etudiants.get(idEtudiant).getUeSuivies();
        for(String keyUeSuivis : listeSuivis.keySet()){ //Itération sur toutes les UEs suivies
            for(String keyIdDate : listeSuivis.get(keyUeSuivis).keySet()){ //Itération sur toutes les dates suivies pour l'UE courante (qui a pour ID keyUeSuivis)
                if(listeSuivis.get(keyUeSuivis).get(keyIdDate).getStatut().equals("validee")){
                    uesValidees.add(ues.get(keyUeSuivis));
                }
            }
        }
        return  uesValidees;
    }


    /**
     * Retourne l'ArrayList de toutes les UEs validées par un étudiant au cours d'un semestre et d'une année
     * @param idEtudiant
     * @param annee
     * @param semestre
     * @return
     */
    public static ArrayList<UE> obtenirUesValidees(String idEtudiant, String annee, String semestre){
         String idDate = annee+" "+semestre;
        ArrayList<UE> uesValidees = new ArrayList<UE>();
        Map <String, HashMap<String, Suivi>> listeSuivis = etudiants.get(idEtudiant).getUeSuivies();
        for(String keyUeSuivis : listeSuivis.keySet()){ //Itération sur toutes les UEs suivies
            if(listeSuivis.get(keyUeSuivis).containsKey(idDate)){ //Vérifie si cette UE a été suivi à la période demandée, exemple : "2021-2022 impair"
                if(listeSuivis.get(keyUeSuivis).get(idDate).getStatut().equals("validee")){ //Vérifie si l'UE a été validé
                    uesValidees.add(ues.get(keyUeSuivis)); //Ajoute l'UE au retour
                }
            }
        }
        return  uesValidees;
    }

    /**
     * Retourne les UEs d'une année et d'un semestre auxquelles l'étudiant a échoué
     * @param idEtudiant
     * @param annee
     * @param semestre
     * @return
     */
    public static ArrayList<UE> obtenirUesEchouees(String idEtudiant, String annee, String semestre){
        String idDate = annee+" "+semestre;
        ArrayList<UE> uesValidees = new ArrayList<UE>();
        Map <String, HashMap<String, Suivi>> listeSuivis = etudiants.get(idEtudiant).getUeSuivies();
        for(String keyUeSuivis : listeSuivis.keySet()){ //Itération sur toutes les UEs suivies
            if(listeSuivis.get(keyUeSuivis).containsKey(idDate)){ //Vérifie si cette UE a été suivi à la période demandée, exemple : "2021-2022 impair"
                if(listeSuivis.get(keyUeSuivis).get(idDate).getStatut().equals("echouee")){ //Vérifie si l'UE a été validé
                    uesValidees.add(ues.get(keyUeSuivis)); //Ajoute l'UE au retour
                }
            }
        }
        return  uesValidees;
    }

    /**
     * Retourne l'ArrayList de toutes les ues auxquelles l'étudiant est éligible selon l'objet étudiant
     * @param e
     * @return
     */
    public static ArrayList<String> obtenirUesEligibles(Etudiant e){ //Eligibles correspond à toutes les UEs auxquelles l'étudiant peut s'inscrire
        Map <String, HashMap<String, Suivi>> listeSuivis = e.getUeSuivies();
        Map <String, String> listeUesAssociees = mentions.get(e.getMention()).getUeAssociees();
        Map <String, String> listeUesPrerequis;
        ArrayList<String> listeUeSOuvertures = Mention.getUEOuvertures();
        ArrayList<String> uesEligibles = new ArrayList<String>();
        String keyUeCourante;
        boolean ueDejaValidee;
        boolean ueAssocieeDejaValidee;
        boolean aLesPrerequis=true;
        boolean uePrerequiseValideeUneFois;
        //ajout des UE d'ouvertures qui n'ont pas déjà été validé
        for (int i = 0; i < listeUeSOuvertures.size(); i++) {
            ueDejaValidee=false;
            keyUeCourante=listeUeSOuvertures.get(i); //Clé/ID courant du parcours de la liste des UEs d'ouvertures
            if(listeSuivis.containsKey(keyUeCourante)){ //On vérifie si l'étudiant a déjà suivi cette UE
                for(String keyIdDate : listeSuivis.get(keyUeCourante).keySet()){ //Itération sur toutes les dates à laquelle l'étudiant a suivi cette UE
                    if(listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("validee") || listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("en cours")){ // On vérifie si l'étudiant a validé l'UE
                        ueDejaValidee=true;
                    }
                }
            }
            if(!ueDejaValidee){ // Si l'UE n'a jamais été validé, on l'ajoute à la liste 'UEs éligibles à renvoyer
                uesEligibles.add(keyUeCourante);
            }
        }
        //ajout des UE associées dont l'étudiant a les prérequis
        for(String keyUeAssociee : listeUesAssociees.keySet()){
            aLesPrerequis=true;
            ueAssocieeDejaValidee=false;
            keyUeCourante=keyUeAssociee; //Clé/ID courant du parcours de la liste des UEs associées
            if(listeSuivis.containsKey(keyUeCourante)){ //On vérifie si l'étudiant a déjà suivi cette UE
                for(String keyIdDate : listeSuivis.get(keyUeCourante).keySet()){ //Itération sur toutes les dates à laquelle l'étudiant a suivi cette UE
                    if(listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("validee") || listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("en cours")){ // On vérifie si l'étudiant a validé l'UE
                        ueAssocieeDejaValidee=true;
                    }
                }
            }
            if(!ues.get(keyUeCourante).getPrerequis().isEmpty()){
                listeUesPrerequis= ues.get(keyUeCourante).getPrerequis(); //On récupère la liste des UEs prérequis
                for(String keyUePrerequis : listeUesPrerequis.keySet()){ //Itération sur les UEs prérequises de l'UE associée
                    uePrerequiseValideeUneFois=false;
                    if(listeSuivis.containsKey(keyUePrerequis)){ //On vérifie si l'étudiant a déjà suivi cette UE
                        for(String keyIdDate : listeSuivis.get(keyUePrerequis).keySet()){ //Itération sur toutes les dates à laquelle l'étudiant a suivi cette UE
                            if(!uePrerequiseValideeUneFois && listeSuivis.get(keyUePrerequis).get(keyIdDate).getStatut().equals("validee")){ // On vérifie si l'étudiant a validé l'UE
                                uePrerequiseValideeUneFois=true;
                            }
                        }
                        if(!uePrerequiseValideeUneFois){ //Si l'UE a pas été validé une seule fois, c'est que l'étudiant ne possède pas les prérequis pour cette UE
                            aLesPrerequis=false;
                        }
                    }
                    else{
                        aLesPrerequis=false;
                    }
                }
            }
            if(aLesPrerequis && !ueAssocieeDejaValidee){ //L'étudiant est éligible à l'UE s'il ne l'a pas déjà validé ET s'il en a les prérequis
                uesEligibles.add(keyUeCourante);
            }
        }
        return  uesEligibles;
    }

    /**
     * Retourne l'ArrayList de toutes les UEs auxquelles l'étudiant est éligible à partir d'un ID
     * @param idEtudiant
     * @return
     */
    public static ArrayList<UE> obtenirUesEligibles(String idEtudiant){ //Eligibles correspond à toutes les UEs auxquelles l'étudiant peut s'inscrire
        Map <String, HashMap<String, Suivi>> listeSuivis = etudiants.get(idEtudiant).getUeSuivies();
        Map <String, String> listeUesAssociees = mentions.get(etudiants.get(idEtudiant).getMention()).getUeAssociees();
        Map <String, String> listeUesPrerequis;
        ArrayList<String> listeUeSOuvertures = Mention.getUEOuvertures();
        ArrayList<UE> uesEligibles = new ArrayList<UE>();
        String keyUeCourante;
        boolean ueDejaValidee;
        boolean ueAssocieeDejaValidee;
        boolean aLesPrerequis=true;
        boolean uePrerequiseValideeUneFois;
        //ajout des UE d'ouvertures qui n'ont pas déjà été validé
        for (int i = 0; i < listeUeSOuvertures.size(); i++) {
            ueDejaValidee=false;
            keyUeCourante=listeUeSOuvertures.get(i); //Clé/ID courant du parcours de la liste des UEs d'ouvertures
            if(listeSuivis.containsKey(keyUeCourante)){ //On vérifie si l'étudiant a déjà suivi cette UE
                for(String keyIdDate : listeSuivis.get(keyUeCourante).keySet()){ //Itération sur toutes les dates à laquelle l'étudiant a suivi cette UE
                    if(listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("validee") || listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("en cours")){ // On vérifie si l'étudiant a validé l'UE
                        ueDejaValidee=true;
                    }
                }
            }
            if(!ueDejaValidee){ // Si l'UE n'a jamais été validé, on l'ajoute à la liste 'UEs éligibles à renvoyer
                uesEligibles.add(ues.get(keyUeCourante));
            }
        }
        //ajout des UE associées dont l'étudiant a les prérequis
        for(String keyUeAssociee : listeUesAssociees.keySet()){
            aLesPrerequis=true;
            ueAssocieeDejaValidee=false;
            keyUeCourante=keyUeAssociee; //Clé/ID courant du parcours de la liste des UEs associées
            if(listeSuivis.containsKey(keyUeCourante)){ //On vérifie si l'étudiant a déjà suivi cette UE
                for(String keyIdDate : listeSuivis.get(keyUeCourante).keySet()){ //Itération sur toutes les dates à laquelle l'étudiant a suivi cette UE
                    if(listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("validee") || listeSuivis.get(keyUeCourante).get(keyIdDate).getStatut().equals("en cours")){ // On vérifie si l'étudiant a validé l'UE
                        ueAssocieeDejaValidee=true;
                    }
                }
            }
            if(!ues.get(keyUeCourante).getPrerequis().isEmpty()){
                listeUesPrerequis= ues.get(keyUeCourante).getPrerequis(); //On récupère la liste des UEs prérequis
                for(String keyUePrerequis : listeUesPrerequis.keySet()){ //Itération sur les UEs prérequises de l'UE associée
                    uePrerequiseValideeUneFois=false;
                    if(listeSuivis.containsKey(keyUePrerequis)){ //On vérifie si l'étudiant a déjà suivi cette UE
                        for(String keyIdDate : listeSuivis.get(keyUePrerequis).keySet()){ //Itération sur toutes les dates à laquelle l'étudiant a suivi cette UE
                            if(!uePrerequiseValideeUneFois && listeSuivis.get(keyUePrerequis).get(keyIdDate).getStatut().equals("validee")){ // On vérifie si l'étudiant a validé l'UE
                                uePrerequiseValideeUneFois=true;
                            }
                        }
                        if(!uePrerequiseValideeUneFois){ //Si l'UE a pas été validé une seule fois, c'est que l'étudiant ne possède pas les prérequis pour cette UE
                            aLesPrerequis=false;
                        }
                    }
                    else{
                        aLesPrerequis=false;
                    }
                }
            }
            if(aLesPrerequis && !ueAssocieeDejaValidee){ //L'étudiant est éligible à l'UE s'il ne l'a pas déjà validé ET s'il en a les prérequis
                uesEligibles.add(ues.get(keyUeCourante));
            }
        }
        return  uesEligibles;
    }

    /**
     * Retourne l'ArrayList de toutes les mentions
     * @return
     */
    public static ArrayList<Mention> obtenirListeMentions() {
        ArrayList<Mention> listeMentions= new ArrayList<Mention>();
        for(String keyM : mentions.keySet()){ //Itération sur la liste de toutes les mentions
            listeMentions.add(mentions.get(keyM)); //On ajoute l'objet Mention à la liste de retour
        }
        return listeMentions;
    }

    /**
     * Retourne l'ArrayList de tous les UE
     * @return
     */
    public static ArrayList<UE> obtenirListeUEs() {
        ArrayList<UE> listeUEs= new ArrayList<UE>();
        for(String keyU : ues.keySet()){ //Itération sur la liste de toutes les UEs
            listeUEs.add(ues.get(keyU));
        }
        return listeUEs;
    }

    /**
     * Retourne l'ArrayList de tous les parcours
     * @return
     */
    public static ArrayList<Parcours> obtenirListeParcours() {
        ArrayList<Parcours> listeParcours= new ArrayList<Parcours>();
        for(String keyM : mentions.keySet()){ //Itération sur la liste de toutes les mentions
            for(String keyP : mentions.get(keyM).getListeParcours().keySet()){ //Itération sur la liste des parcours de toutes les mentions
                listeParcours.add(mentions.get(keyM).getListeParcours().get(keyP)); //On ajoute le parcours
            }
        }
        return listeParcours;
    }

    /** Retourne l'ArrayList de tous les parcours d'une mention
     *
     * @param idMention
     * @return
     */
    public static ArrayList<Parcours> obtenirListeParcours(String idMention) {
        ArrayList<Parcours> listeParcours= new ArrayList<Parcours>();
        for(String keyP : mentions.get(idMention).getListeParcours().keySet()){ //Itération sur la liste des parcours de idMention
            listeParcours.add(mentions.get(idMention).getListeParcours().get(keyP)); //On ajoute le parcours
        }

        return listeParcours;
    }

    /**
     * Retourne l'ArrayList de toutes les  UEs obligatoires du parcours
     * @param idParcours
     * @return
     */
    public static ArrayList<UE> obtenirListeUEsParcours(String idParcours) {
        ArrayList<UE> listeUEsParcours= new ArrayList<UE>();
        HashMap<String, UE>listeTmpUEsParcours = new HashMap<String, UE>(); //Liste temporaire qui va permettre de différencier les UEs obligatoires des UEs associées
        String idMention = idParcours.substring(0,idParcours.length()-2); //On obtient l'ID de la mention en enlevant les 2 derniers caractères
        for(String keyUEObligatoire : mentions.get(idMention).getListeParcours().get(idParcours).getUesObligatoires().keySet()){ //Itération sur les UEs obligatoires du parcours pour les déterminer avant
            UE ueObligatoire = ues.get(keyUEObligatoire);
            listeTmpUEsParcours.put(keyUEObligatoire,ueObligatoire); //On l'ajoute à la liste
        }
        for(String keyUEAssociee : mentions.get(idMention).getUeAssociees().keySet()){
            if(!listeTmpUEsParcours.containsKey(keyUEAssociee)){ //On vérifie que la liste ne contient pas déjà cette UE (qui aurait été ajouté en tant qu'UE obligatoire du parcours
                UE ueAssociee = ues.get(keyUEAssociee);
                listeTmpUEsParcours.put(keyUEAssociee,ueAssociee); //On l'ajoute à la liste
            }
        }
        for(String keyTmp : listeTmpUEsParcours.keySet()){ //On transfère tout le contenu de la HashMap temporaire vers l'ArrayList de retour qui est une structure de données manipulable en JavaFX
            listeUEsParcours.add(listeTmpUEsParcours.get(keyTmp));
        }
        return listeUEsParcours;
    }

    /**
     * Retourne l'ArrayList de tous les étudiants
     * @return
     */
    public static ArrayList<Etudiant> obtenirListeEtudiants(){
        ArrayList<Etudiant> listeEtudiants = new ArrayList<Etudiant>();
        for(String keyEtudiant : etudiants.keySet()){
            listeEtudiants.add(etudiants.get(keyEtudiant));
        }
        return listeEtudiants;
    }

    /**
     * Vérifie si l'étudiant a déjà validé une UE
     * @param ueValidees
     * @param idUEAVerifier
     * @return
     */
    public static boolean verifierUEDejaValidee(ArrayList<String> ueValidees, String idUEAVerifier){
        boolean dejaValidee=false;
        int i = 0;
        while(i<ueValidees.size() && !dejaValidee) {
            if(ueValidees.get(i).equals(idUEAVerifier)){
                dejaValidee=true;
            }
            i++;
        }
        return dejaValidee;
    }

    /**
     * Constitue et retourne les UEs obligatoires à la validation de
     * @param idEtudiant
     * @param e
     * @return
     */
    public static ArrayList<ArrayList<UE>>  obtenirCheminObligatoire(String idEtudiant, Etudiant e){
        ArrayList<ArrayList<UE>> cheminObligatoire = new ArrayList<ArrayList<UE>> ();
        ArrayList<String> uesValidees = obtenirUesValidees(idEtudiant); //ArrayList contenant toutes les UEs déjà validées par l'étudiant
        boolean dejaDansLeChemin;
        boolean aDesPrerequis=true; //Condition d'arrêt de la construction de chemin obligatoire
        int indexUEObligatoire=0; //Ce second index va permettre de naviguer dans la construction du chemin obligatoire comme compteur de complexité
        UE ueanalysee; // UE en cours de lecture dans les UEs correspondantes
        Parcours parcoursEtudiant = mentions.get(e.getMention()).getListeParcours().get(e.getParcours()); //On récupère l'objet de parcours de l'étudiant
        cheminObligatoire.add(new ArrayList<UE>());
        for(String idUEObligatoire : parcoursEtudiant.getUesObligatoires().keySet()){ //On ajoute toutes les UEs obligatoires en complexité 0
            if(!verifierUEDejaValidee(uesValidees,idUEObligatoire)){ //On vérifie tout de même si cette UE a déjà été validée par l'étudiant
                cheminObligatoire.get(0).add(ues.get(idUEObligatoire));
            }
        }
        while(aDesPrerequis){ //La boucle persiste tant qu'une seule UE a des prérequis, l'objectif ici est de construire le chemin obligatoire de complexité n+1 à partir des UES de complexité n
            aDesPrerequis=false;
            for (int i = 0; i < cheminObligatoire.get(indexUEObligatoire).size(); i++) { //On se positionne sur la complexité des UEs à ajouter
                cheminObligatoire.add(new ArrayList<UE>()); // On initialise donc un nouvel Arraylist d'ID d'UE
                ueanalysee=ues.get(cheminObligatoire.get(indexUEObligatoire).get(i).getId()); //On récupère l'objet d'UE à analyser au cours de ce parcours
                if(!aDesPrerequis && ueanalysee.getPrerequis().keySet().size()>0){ //Verifie si ce niveau de complexité a déjà des prérequis et si l'UE courante en a
                    aDesPrerequis=true; //Si oui, aDesPrerequis passe à true, ce qui permettra de passer au niveau de complexité suivant (car il faut aller chercher les prérequis de ces nouvelles UEs)
                }
                for (String cleUEPrerequis :  ueanalysee.getPrerequis().keySet()) {
                    if(!verifierUEDejaValidee(uesValidees,cleUEPrerequis)){
                        dejaDansLeChemin = false;
                        for (int j = 0; j <cheminObligatoire.size() ; j++) {
                            for (int k = 0; k <cheminObligatoire.get(j).size(); k++) {
                                if(cheminObligatoire.get(j).get(k).getId().equals(cleUEPrerequis)){
                                    dejaDansLeChemin=true;
                                }
                            }
                        }
                        if(!dejaDansLeChemin){
                            cheminObligatoire.get(indexUEObligatoire+1).add(ues.get(cleUEPrerequis));
                        }
                    }
                }
            }
            indexUEObligatoire++;
        }
        return cheminObligatoire;
    }

    /**
     * Retourne le chemin optimal d'UE pour que l'étudiant valide son parcours, grâce à l'attribut ordreUE de l'étudiant
     * @param idEtudiant
     * @return
     */
    public static ArrayList<UE> obtenirUesAVenir(String idEtudiant){
        ArrayList<ArrayList<UE>> cheminObligatoire = new ArrayList<ArrayList<UE>> (); // L'ArrayList contient des ArrayList de String, ces Strings sont les clés des UE
        // L'index correspond à la "complexité" de l'UE : plus une UE se trouve dans un index élevé, plus elle est loin de l'UE obligatoire du parcours, il faut donc s'y inscrire en premier
        Etudiant e = new Etudiant(etudiants.get(idEtudiant)); //On copie l'étudiant pour la simulation
        ArrayList<UE> uesAVenir = new ArrayList<UE>();
        int compteurSemestre=0; //Valeur de retour pour la simulation
        int indexPrioriteUEObligatoire; //Première indice d'itération de l'ArrayList
        int indexUEObligatoire=0; //Ce second index va permettre de naviguer dans la construction du chemin obligatoire comme compteur de complexité
        int creditsTotauxSemestre;
        int indexUEsEligibles;
        boolean ueEligibleTrouvee;
        ArrayList<String> uesEligiblesParSemestre;
        cheminObligatoire=obtenirCheminObligatoire(idEtudiant, e);
        // Le chemin obligatoire étant construit, on va effectuer une simulation
        indexPrioriteUEObligatoire=cheminObligatoire.size()-1; //On commence à la complexité maximale, vu qu'il s'agit des premières UEs à valider
        while(cheminObligatoire.size()>0) { //Chaque itération de cette boucle correspond à un semestre, on s'arrête lorsque l'ensemble des UEs du chemin obligatoire ont été suivi (car on les enlève à l'inscription)
            compteurSemestre++; //On simule un semestre de +
            creditsTotauxSemestre = 0; //On remet les crédits à 0
            uesEligiblesParSemestre = obtenirUesEligibles(e); //On réactualise les UEs auxquelles l'étudiant est éligible
            while(indexPrioriteUEObligatoire>=0){ //On commence à itérer par le dernier index car il s'agit de la priorité la plus haute actuelle
                indexUEObligatoire = 0;
                while (indexUEObligatoire < cheminObligatoire.get(indexPrioriteUEObligatoire).size() && creditsTotauxSemestre < 30) { //On inscrit l'étudiant à un maximum d'UE obligatoire auxquelles il est éligible, tout en respectant le maximum de 30 crédits par semestre
                    indexUEsEligibles = 0;
                    ueEligibleTrouvee = false; //booléan afin d'arrêter la boucle suivante :
                    while (indexUEsEligibles < uesEligiblesParSemestre.size() && !ueEligibleTrouvee) { //On cherche l'UE obligatoire dans la liste des UEs auxquelles l'étudiant est éligible
                        if (uesEligiblesParSemestre.get(indexUEsEligibles).equals(cheminObligatoire.get(indexPrioriteUEObligatoire).get(indexUEObligatoire).getId())) { //On vérifie si elle s'y trouve, si oui on l'ajoute et on arrête la boucle de recherche
                            ueEligibleTrouvee = true; //On arrête la boucle
                            creditsTotauxSemestre += ues.get(uesEligiblesParSemestre.get(indexUEsEligibles)).getValeurCredit(); //On ajoute la valeur en crédit de cette UE au maximum possible
                            e.ajouterUeSuivis(uesEligiblesParSemestre.get(indexUEsEligibles), "2049", "impair", "validee"); //On simule un suivi
                            cheminObligatoire.get(indexPrioriteUEObligatoire).remove(indexUEObligatoire); // On supprime l'UE du chemin obligatoire, comme une to-do list
                            indexUEObligatoire--; // On enlève 1 à l'index car la suppression de l'élément décale les index de 1
                            uesAVenir.add(ues.get(uesEligiblesParSemestre.get(indexUEsEligibles)));
                            uesAVenir.get(uesAVenir.size()-1).setOrdreUE(compteurSemestre);
                        }
                        indexUEsEligibles++;
                    }
                    indexUEObligatoire++;
                }
                if (cheminObligatoire.get(indexPrioriteUEObligatoire).size() == 0) {
                    cheminObligatoire.remove(indexPrioriteUEObligatoire); //Si la priorité courante ne dispose plus d'UE, on la supprime, ce qui met à jour toutes les priorités
                }
                indexPrioriteUEObligatoire--;
            }
            indexPrioriteUEObligatoire=cheminObligatoire.size()-1; //On se replace sur la priorité la plus haute du chemin
        }
        return uesAVenir;
    }

    /**
     * Calcule le nombre de semestres minimum nécesssaires à l'étudiant pour qu'il valide son parcours
     * @param idEtudiant
     * @return
     */
    public static String nombreDeSemestresMinimum(String idEtudiant){
        ArrayList<ArrayList<UE>> cheminObligatoire = new ArrayList<ArrayList<UE>> (); // L'ArrayList contient des ArrayList de String, ces Strings sont les clés des UE
        // L'index correspond à la "complexité" de l'UE : plus une UE se trouve dans un index élevé, plus elle est loin de l'UE obligatoire du parcours, il faut donc s'y inscrire en premier
        Etudiant e = new Etudiant(etudiants.get(idEtudiant)); //On copie l'étudiant pour la simulation
        int nbSemestresMinimum=0; //Valeur de retour pour la simulation
        int indexPrioriteUEObligatoire; //Première indice d'itération de l'ArrayList
        int indexUEObligatoire=0; //Ce second index va permettre de naviguer dans la construction du chemin obligatoire comme compteur de complexité
        int creditsTotauxSemestre;
        int indexUEsEligibles;
        boolean ueEligibleTrouvee;
        ArrayList<String> uesEligiblesParSemestre;
        cheminObligatoire=obtenirCheminObligatoire(idEtudiant, e);
        // Le chemin obligatoire étant construit, on va effectuer une simulation
        indexPrioriteUEObligatoire=cheminObligatoire.size()-1; //On commence à la complexité maximale, vu qu'il s'agit des premières UEs à valider
        while(cheminObligatoire.size()>0) { //Chaque itération de cette boucle correspond à un semestre, on s'arrête lorsque l'ensemble des UEs du chemin obligatoire ont été suivi (car on les enlève à l'inscription)
            nbSemestresMinimum++; //On simule un semestre de +
            creditsTotauxSemestre = 0; //On remet les crédits à 0
            uesEligiblesParSemestre = obtenirUesEligibles(e); //On réactualise les UEs auxquelles l'étudiant est éligible
            while(indexPrioriteUEObligatoire>=0){ //On commence à itérer par le dernier index car il s'agit de la priorité la plus haute actuelle
            indexUEObligatoire = 0;
            while (indexUEObligatoire < cheminObligatoire.get(indexPrioriteUEObligatoire).size() && creditsTotauxSemestre < 30) { //On inscrit l'étudiant à un maximum d'UE obligatoire auxquelles il est éligible, tout en respectant le maximum de 30 crédits par semestre
                indexUEsEligibles = 0;
                ueEligibleTrouvee = false; //booléan afin d'arrêter la boucle suivante :
                while (indexUEsEligibles < uesEligiblesParSemestre.size() && !ueEligibleTrouvee) { //On cherche l'UE obligatoire dans la liste des UEs auxquelles l'étudiant est éligible
                    if (uesEligiblesParSemestre.get(indexUEsEligibles).equals(cheminObligatoire.get(indexPrioriteUEObligatoire).get(indexUEObligatoire).getId())) { //On vérifie si elle s'y trouve, si oui on l'ajoute et on arrête la boucle de recherche
                        ueEligibleTrouvee = true; //On arrête la boucle
                        creditsTotauxSemestre += ues.get(uesEligiblesParSemestre.get(indexUEsEligibles)).getValeurCredit(); //On ajoute la valeur en crédit de cette UE au maximum possible
                        e.ajouterUeSuivis(uesEligiblesParSemestre.get(indexUEsEligibles), "2049", "impair", "validee"); //On simule un suivi
                        cheminObligatoire.get(indexPrioriteUEObligatoire).remove(indexUEObligatoire); // On supprime l'UE du chemin obligatoire, comme une to-do list
                        indexUEObligatoire--; // On enlève 1 à l'index car la suppression de l'élément décale les index de 1
                    }
                    indexUEsEligibles++;
                }
                indexUEObligatoire++;
            }
            if (cheminObligatoire.get(indexPrioriteUEObligatoire).size() == 0) {
                cheminObligatoire.remove(indexPrioriteUEObligatoire); //Si la priorité courante ne dispose plus d'UE, on la supprime, ce qui met à jour toutes les priorités
            }
            indexPrioriteUEObligatoire--;
        }
                indexPrioriteUEObligatoire=cheminObligatoire.size()-1; //On se replace sur la priorité la plus haute du chemin
        }
        return Integer.toString(nbSemestresMinimum);
    }
    public static void main(String[] args) {
        Controller.debuterSession();
        ArrayList<ArrayList<UE>> test = obtenirCheminObligatoire("b46a2556", etudiants.get("b46a2556"));
        for (int i = 0; i < test.size(); i++) {
            for (int j = 0; j < test.get(i).size(); j++) {
                System.out.println("i : "+i+" "+test.get(i).get(j));
            }
        }
    }

}

