package com.example.pablo.proyectoseminario.ListDataSource;

import android.graphics.Bitmap;

public class ItemList {
    private String idC;
    private int precio;
    private String direccion;
    private double lat;
    private double lon;
    private String url;
    private Bitmap img;

    public ItemList(String idC, int precio, String direccion, double lat, double lon, String urlimg) {
        this.idC = idC;
        this.precio = precio;
        this.direccion = direccion;
        this.lat = lat;
        this.lon = lon;
        this.url = urlimg;
    }
    public void setImg(Bitmap img){
        this.img = img;
    }

    public String getIdC() {
        return idC;
    }

    public int getPrecio() {
        return precio;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getUrlimg() {
        return url;
    }

    public Bitmap getImg() {
        return this.img;
    }
    public Bitmap getBitmap(){
        return this.img;
    }
}
