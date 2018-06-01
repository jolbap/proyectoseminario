package com.example.pablo.proyectoseminario.ListDataSource;

public class ItemList {
    private String precio;
    private String idC;

    public ItemList (String idC, String precio) {
        this.idC = idC;
        this.precio = precio;
    }
    public String getPrecio(){
        return this.precio;
    }
    public String getIdC(){
        return this.idC;
    }
}
