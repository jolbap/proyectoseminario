package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FuncionesAgente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funciones_agente);
    }
    public void registrarCasa(View v){
        Intent inte6 = new Intent(this, RegistrarCasa.class);
        startActivity(inte6);
    }
    public void verCasaPorId(View v){
        Intent inte7 = new Intent(this, VerPorId.class);
        startActivity(inte7);
    }
}
