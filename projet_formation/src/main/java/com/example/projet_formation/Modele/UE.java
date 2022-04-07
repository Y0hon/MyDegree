/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.projet_formation.Modele;


import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author yohan
 */
public class UE {
    private String id;
    private String libelle;   
    private int valeurCredit;
    private int ordreUE;
    private HashMap<String,String> prerequis;

    public UE(String id, String libelle,int valeurCredit) {
        this.id=id;
        this.libelle=libelle;
        this.valeurCredit =valeurCredit;
        this.prerequis= new HashMap<String,String>();
    }

    /**
     * Ajoute un prérequis à l'UE
     * @param id
     * @param libellePrerequis
     */
    public void ajouterPrerequis(String id, String libellePrerequis){
            this.prerequis.put(id,libellePrerequis);
    }

    public int getValeurCredit() {
        return valeurCredit;
    }

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public HashMap<String, String> getPrerequis() {
        return prerequis;
    }

    public int getOrdreUE() {
        return ordreUE;
    }

    public void setOrdreUE(int ordreUE) {
        this.ordreUE = ordreUE;
    }

    @Override
    public String toString() {
        return "UE{" +
                "id='" + id + '\'' +
                ", libelle='" + libelle + '\'' +
                ", valeurCredit=" + valeurCredit +
                ", prerequis=" + prerequis +
                '}';
    }
}
