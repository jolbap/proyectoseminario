package com.example.pablo.proyectoseminario.ListDataSource;

public class ItemList {
    private String idC;
    private int precio;
    private String direccion;
    private double lat;
    private double lon;

    public ItemList(String idC, int precio, String direccion, double lat, double lon) {
        this.idC = idC;
        this.precio = precio;
        this.direccion = direccion;
        this.lat = lat;
        this.lon = lon;
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
}
