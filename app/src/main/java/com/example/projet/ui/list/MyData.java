package com.example.projet.ui.list;

public class MyData {

    private String titre;
    private String description;
    private double latitude;
    private double longitude;
    private String date;
    private byte[] image;

    public MyData(String titre,String description, String date, double latitude, double longitude, byte[] image) {
        this.titre= titre;
        this.description= description;
        this.latitude= latitude;
        this.longitude= longitude;
        this.date= date;
        this.image= image;
    }

    public byte[] getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
    public String getTitre() {
        return titre;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getDate() {
        return date;
    }
}
