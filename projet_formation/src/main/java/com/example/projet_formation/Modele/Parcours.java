package com.example.projet_formation.Modele;

import java.util.HashMap;

public class Parcours {
    private String id;
    private String libelle;
    private HashMap<String, String> uesObligatoires; // La clé est l'ID, la valeur est le libellé

    public Parcours(String id, String libelle){
        this.id=id;
        this.libelle=libelle;
        this.uesObligatoires= new HashMap<String, String>();
    }

    /**
     * Ajoute une UE obligatoire au parcours
     * @param idUe
     * @param libelleUE
     */
    public void ajouterUeObligatoire(String idUe, String libelleUE){
        uesObligatoires.put(idUe, libelleUE);
    }

    public String getLibelle() {
        return libelle;
    }

    public HashMap<String, String> getUesObligatoires() {
        return uesObligatoires;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Parcours{" +
                "id='" + id + '\'' +
                ", libelle='" + libelle + '\'' +
                ", uesObligatoires=" + uesObligatoires +
                '}';
    }
}
