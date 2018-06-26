package com.example.pablo.proyectoseminario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LlamarAgente extends AppCompatActivity {

    private Button btnllamar, btnenviarmensaje;
    private EditText etxmensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar_agente);
        validaPermisosdeLlamadas();
        validaPermisosdeMensajes();
        llamadas();
        mensajes();
    }
    private boolean validaPermisosdeLlamadas() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
        return false;
    }
    private boolean validaPermisosdeMensajes() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},100);
        return false;
    }

    private void llamadas() {
        btnllamar = (Button) findViewById(R.id.btnllamar);
        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:76160349"));
                if (ActivityCompat.checkSelfPermission(LlamarAgente.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                  return;
                }
                startActivity(intent);
            }
        });
    }
    private void mensajes() {
        etxmensaje = (EditText) findViewById(R.id.etxmensaje);
        btnenviarmensaje = (Button) findViewById(R.id.btnenviarmensaje);
        btnenviarmensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = "tel:76160349";
                String mensaje = etxmensaje.getText().toString();
                SmsManager mensSMS = SmsManager.getDefault();
                mensSMS.sendTextMessage(numero,null,mensaje,null,null);
                Toast.makeText(getApplicationContext(),"Mensaje Enviado",Toast.LENGTH_LONG).show();
            }
        });
    }
}
