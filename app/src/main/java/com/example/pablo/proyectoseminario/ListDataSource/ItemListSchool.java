package com.example.pablo.proyectoseminario.ListDataSource;



public class ItemListSchool {
    private String idE;
    private String escuelanombre;
    private double late;
    private double lone;

    public ItemListSchool(String idE, String escuelanombre, double late, double lone) {
        this.idE = idE;
        this.escuelanombre = escuelanombre;
        this.late = late;
        this.lone = lone;
    }
    public String getIdE() {
        return idE;
    }
    public String getEscuelanombre() {
        return escuelanombre;
    }
    public double getLate() {
        return late;
    }
    public double getLone() {
        return lone;
    }
}
