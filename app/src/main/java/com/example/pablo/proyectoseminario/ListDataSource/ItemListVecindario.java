package com.example.pablo.proyectoseminario.ListDataSource;

import com.example.pablo.proyectoseminario.LatLonMaps;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;

public class ItemListVecindario {
    private String nombrevecindario;
    private Integer zoom;
    private double latv;
    private double lngv;
    private String idV;
    private Polygon poligono;

    public ItemListVecindario(String nombrevecindario, Integer zoom, double latv, double lngv, String idV, Polygon poligono) {
        this.nombrevecindario = nombrevecindario;
        this.zoom = zoom;
        this.latv = latv;
        this.lngv = lngv;
        this.idV = idV;
        this.poligono = poligono;
    }

    public String getNombrevecindario() {
        return nombrevecindario;
    }

    public Integer getZoom() {
        return zoom;
    }

    public double getLatv() {
        return latv;
    }

    public double getLngv() {
        return lngv;
    }

    public String getIdV() {
        return idV;
    }

    public Polygon getPoligono() {
        return poligono;
    }

    public void setPoligono(Polygon poligono) {
        this.poligono = poligono;
    }
}
