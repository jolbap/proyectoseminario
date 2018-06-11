package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginAgente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_agente);

        Button boton = (Button) findViewById(R.id.ingresar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = ((EditText)findViewById(R.id.txtagente)).getText().toString();
                String password = ((EditText)findViewById(R.id.txtpassword)).getText().toString();
                if (usuario.equals("pablo") && password.equals("1234")){
                    Intent nuevoform = new Intent(LoginAgente.this, FuncionesAgente.class);
                    startActivity(nuevoform);
                }else {
                    Toast.makeText(getApplicationContext(), "Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}