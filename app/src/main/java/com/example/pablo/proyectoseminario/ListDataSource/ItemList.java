package com.example.pablo.proyectoseminario.ListDataSource;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ItemList {
    private String idC;
    private int precio;
    private String direccion;
    private double lat;
    private double lon;
    private ArrayList<String> url;
    private ArrayList<Bitmap> img;

    public ItemList(String idC, int precio, String direccion, double lat, double lon, ArrayList<String> urlimg) {
        this.idC = idC;
        this.precio = precio;
        this.direccion = direccion;
        this.lat = lat;
        this.lon = lon;
        this.url = urlimg;
    }
    public void setImg(ArrayList<Bitmap> img) {
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
    public ArrayList<String> getUrlimg() {
        return this.url;
    }
    public ArrayList<Bitmap> getImg() {
        return this.img;
    }
    public ArrayList<Bitmap> getBitmap(){
        return this.img;
    }
}
