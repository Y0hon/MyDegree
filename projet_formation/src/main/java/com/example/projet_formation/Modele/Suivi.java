/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.projet_formation.Modele;

/**
 *
 * @author yohan
 */
public class Suivi {

    private enum resultat {
        valide, echouee;
    }
    private String id;
    private String annee;
    private String semestre;
    private String statut;

    public Suivi (String annee, String semestre, String statut) {
        this.annee = annee;
        this.semestre = semestre;
        this.id = annee+" "+semestre;
        this.statut=statut;
    }

    public String getAnnee() {
        return annee;
    }

    public String getSemestre() {
        return semestre;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Suivi{" +
                "annee='" + annee + '\'' +
                ", semestre='" + semestre + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }

}
