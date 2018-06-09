package com.example.pablo.proyectoseminario.DataDetaild;

public class Detaild {
    private String idC;
    private String canthabit;
    private String cantbaños;
    private String superficie;
    private int precio;
    private String año;
    private String descripcion;

    public Detaild (String idC, String canthabit, String cantbaños, String superficie, int precio, String año, String descripcion) {
        this.idC = idC;
        this.canthabit = canthabit;
        this.cantbaños = cantbaños;
        this.superficie = superficie;
        this.precio = precio;
        this.año = año;
        this.descripcion = descripcion;

    }
    public String getIdC(){
        return this.idC;
    }
    public String getCanthabit(){
        return this.canthabit;
    }
    public String getCantbaños(){
        return this.cantbaños;
    }
    public String getSuperficie(){
        return this.superficie;
    }
    public int getPrecio(){
        return this.precio;
    }
    public String getAño(){
        return this.año;
    }
    public String getDescripcion(){
        return this.descripcion;
    }
}
