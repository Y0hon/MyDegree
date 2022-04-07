/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.projet_formation.Modele;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yohan
 */
public class Mention {
    
   private String libelle;
   private String id;
   private HashMap<String,Parcours> listeParcours;
   private HashMap<String, String> ueAssociees;
   private static ArrayList<String> UEOuvertures = new ArrayList<String>();;
   
   public Mention(String id, String libelle){
       this.libelle=libelle;
       this.id=id;
       listeParcours = new HashMap<String, Parcours>();
       ueAssociees = new HashMap<String, String>();
   }

    public String getLibelle() {
        return libelle;
    }


    public static ArrayList<String> getUEOuvertures() {
        return UEOuvertures;
    }

    /**
     * Ajoute les parcours correspondants à la mention à partir des CSV
     * @param id
     * @param repertoireCourant
     * @param ues
     */
    public void ajouterParcours(String id, String repertoireCourant, Map<String, UE> ues){
        try (CSVReader readerParcours = new CSVReader(new FileReader(repertoireCourant+"/parcours.csv"))) {
            List<String[]> parcoursCsv = readerParcours.readAll();
            for (String[] parcours : parcoursCsv){
                if(parcours[0].equals(id)){
                    listeParcours.put(parcours[0],new Parcours(parcours[0],parcours[2]));
                    for (int i = 3; i <parcours.length ; i++) {
                        listeParcours.get(parcours[0]).ajouterUeObligatoire(parcours[i],ues.get(parcours[i]).getLibelle());
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public void ajouterUEAssociees(String idUe, String libelleUe){
       ueAssociees.put(idUe, libelleUe);
    }

    public Map<String, Parcours> getListeParcours() {
        return listeParcours;
    }

    public Map<String, String> getUeAssociees() {
        return ueAssociees;
    }

    /**
     * Ajoute une UE d'ouverture au tableau statique des UEs
     * @param ue
     */
    public static void ajouterUeOuverture(String ue){
       UEOuvertures.add(ue);
    }

    public String toStringEtudiant(){
       return libelle;
   }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Mention{" +
                "libelle='" + libelle + '\'' +
                ", id='" + id + '\'' +
                ", listeParcours=" + listeParcours +
                ", ueAssociees=" + ueAssociees +
                '}';
    }

    // Ne pas oublier les méthodes d'ajouts et lectures de fichier CSV
}
