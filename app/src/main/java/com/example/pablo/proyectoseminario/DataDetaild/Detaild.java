package com.example.pablo.proyectoseminario.DataDetaild;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Detaild {
    private String idC;
    private String canthabit;
    private String cantbaños;
    private String superficie;
    private int precio;
    private String año;
    private String descripcion;
    private String direccion;

    private ArrayList<String> url;
    private ArrayList<Bitmap> img;

    public Detaild (String idC, String canthabit, String cantbaños, String superficie, int precio, String año, String descripcion, ArrayList<String> urlimg, String direccion) {
        this.idC = idC;
        this.canthabit = canthabit;
        this.cantbaños = cantbaños;
        this.superficie = superficie;
        this.precio = precio;
        this.año = año;
        this.descripcion = descripcion;
        this.url = urlimg;
        this.direccion = direccion;

    }
    public void setImg(ArrayList<Bitmap> img) {
        this.img = img;
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
    public String getDireccion(){
        return this.direccion;
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
