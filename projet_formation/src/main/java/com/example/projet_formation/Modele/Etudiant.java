/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.projet_formation.Modele;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author yohan
 */
public class Etudiant {
    
    private String id;
    private String prenom;
    private String nom;
    private int credits;   //Lien vers la photo
    private String dateNaissance;
    private int maxCreditSemestre;
    private String mention;
    private String parcours;
    private HashMap<String, HashMap<String, Suivi>> ueSuivies; // La clé est le libellé de l'UE, la seconde hashmap a pour clé la date

    public Etudiant(String prenom, String nom, String dateNaissance, int credits, int maxCreditSemestre,
                    String validationParcours, String mention,
                    String parcours) {
        this.id=UUID.randomUUID().toString().substring(0,8);
        this.prenom=prenom;
        this.nom=nom;
        this.credits=credits;
        this.dateNaissance=dateNaissance;
        this.maxCreditSemestre=30;
        this.ueSuivies =new HashMap<String, HashMap<String, Suivi>>();
        this.mention=mention;
        this.parcours=parcours;
    }

    public Etudiant(Etudiant e) { //Constructeur pour copie profonde d'un étudiant, il ne sert qu'à la simulation du nombre de semestres minimum
        this.id=UUID.randomUUID().toString().substring(0,8);
        this.prenom=e.getPrenom();
        this.nom=e.getNom();
        this.credits=e.getCredits();
        this.dateNaissance=e.getDateNaissance();
        this.maxCreditSemestre=30;
        this.mention=e.getMention();
        this.parcours=e.getParcours();
        this.ueSuivies =new HashMap<String, HashMap<String, Suivi>>();
        for(String keyUE : e.getUeSuivies().keySet()){ //Copie profonde la hashmap d'UE suivis afin de ne pas altérer l'originale
            this.ueSuivies.put(keyUE,new HashMap<String,Suivi>());
            for (String keyDate : e.getUeSuivies().get(keyUE).keySet()){
                this.ueSuivies.get(keyUE).put(keyDate,e.getUeSuivies().get(keyUE).get(keyDate));
            }
        }
    }

    public Etudiant(String id,String prenom, String nom, String dateNaissance, int credits, int maxCreditSemestre,
                    String validationParcours, String mention,
                    String parcours) {
        this.id= id;
        this.prenom=prenom;
        this.nom=nom;
        this.credits=credits;
        this.dateNaissance=dateNaissance;
        this.maxCreditSemestre=30;
        this.ueSuivies =new HashMap<String, HashMap<String, Suivi>>();
        this.mention=mention;
        this.parcours=parcours;
    }

    public void setMaxCreditSemestre(int maxCreditSemestre) {
        this.maxCreditSemestre = maxCreditSemestre;
        return;
    }

    public Map<String, HashMap<String, Suivi>> getUeSuivies() {
        return ueSuivies;
    }

    /**
     * Ajoute un suivi d'une UE à un étudiant en fonction d'une année, d'un semestre et d'un statut
     * @param idUE
     * @param annee
     * @param semestre
     * @param statut
     */
    public void ajouterUeSuivis(String idUE, String annee, String semestre, String statut){
        if(!ueSuivies.containsKey(idUE)){
            ueSuivies.put(idUE,new HashMap<String,Suivi>());
        }
        ueSuivies.get(idUE).put(annee+" "+semestre,new Suivi(annee,semestre,statut));
    }

    public String getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public int getCredits() { return credits; }

    public String getMention() {
        return mention;
    }

    public String getParcours() {
        return parcours;
    }

    public void setMention(String mention) {
        this.mention=mention;
    }

    public void setParcours(String parcours) {
        this.parcours = parcours;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }



    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Modifie le statut de suivi d'une UE selon le double identifiant : id de l'UE et id du semestre-année
     * @param idUe
     * @param idSuivi
     * @param nouveauStatut
     */
    public void modifierStatutSuivi(String idUe,String idSuivi,String nouveauStatut){
        this.ueSuivies.get(idUe).get(idSuivi).setStatut(nouveauStatut);
    }

    public void ajouterCredits(int credits){
        this.credits+=credits;
    }

    @Override
    public String toString() {
        return id+","+prenom+","+nom+","+dateNaissance+","+credits+","+maxCreditSemestre+","
                +mention+","+parcours+","+ ueSuivies.keySet();
    }
}
