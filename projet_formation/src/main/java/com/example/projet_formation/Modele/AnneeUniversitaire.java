/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.projet_formation.Modele;

import java.util.Date;
/**
 *
 * @author yohan
 */
public class AnneeUniversitaire {

    private String semestreImpair;
    private String semestrePair;

    public AnneeUniversitaire(String semestreImpair, String semestrePair){
        this.semestreImpair=semestreImpair;
        this.semestrePair=semestrePair;
    }

    public String getSemestreImpair() {
        return semestreImpair;
    }

    public String getSemestrePair() {
        return semestrePair;
    }

    /**
     * Retourne l'ID de l'année universitaire constitué de l'année du semestre impair et du semestre pair
     * @return
     */
    public String getPeriode() {
        return semestreImpair+"-"+semestrePair;
    }

    @Override
    public String toString() {
        return "AnneeUniversitaire{" +
                "semestreImpair='" + semestreImpair + '\'' +
                ", semestrePair='" + semestrePair + '\'' +
                '}';
    }
}
