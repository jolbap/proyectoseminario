package com.example.pablo.proyectoseminario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LlamarAgente extends AppCompatActivity {

    Button btnllamar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar_agente);
        load();
    }

    private void load() {
        btnllamar = (Button) findViewById(R.id.btnllamar);
        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:76171376"));
                if (ActivityCompat.checkSelfPermission(LlamarAgente.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                  return;
                }
                startActivity(intent);
            }
        });
    }
}
