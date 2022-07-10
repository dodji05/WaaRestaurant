package com.dga.services.waa.models;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurants {
    @Expose
    private int id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("adresse")
    @Expose
    private String adresse;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("plat")
    @Expose
    private List<Object> plat = null;
    @SerializedName("avis")
    @Expose
    private List<Object> avis = null;
    @SerializedName("tables")
    @Expose
    private List<Object> tables = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Object> getPlat() {
        return plat;
    }

    public void setPlat(List<Object> plat) {
        this.plat = plat;
    }

    public List<Object> getAvis() {
        return avis;
    }

    public void setAvis(List<Object> avis) {
        this.avis = avis;
    }

    public List<Object> getTables() {
        return tables;
    }

    public void setTables(List<Object> tables) {
        this.tables = tables;
    }
}
