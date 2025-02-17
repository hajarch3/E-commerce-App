package com.example.e_commerce_app;

import java.io.Serializable;

public class Produits  implements Serializable {
    private String description;
    private String titre;
    private String type;
    private String url;
    private String Id;
    private  String prix;

    // Constructeur par défaut nécessaire pour Firestore
    public Produits()  {}

    public Produits(String description, String titre, String type, String url, String prix) {
        this.description = description;
        this.titre = titre;
        this.type = type;
        this.url = url;
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}