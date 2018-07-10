package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BorrarCasasVecindarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_casas_vecindarios);
    }
    public void borrarVecindario(View v){
        Intent inte = new Intent(this, BorrarVecindario.class);
        startActivity(inte);
    }
    public void borrarCasa(View v){
        Intent inte2 = new Intent(this, BorrarCasa.class);
        startActivity(inte2);
    }
}
