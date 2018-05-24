package com.example.pablo.proyectoseminario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Agente extends AppCompatActivity {

    EditText editTextid, editTextcanthabit, editTextcantbaños, editTextsuperficie, editTextprecio, editTextaño, editTextdescripcion;
    Button buttonagregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente);
        editTextid = findViewById(R.id.editTextid);
        editTextcanthabit = findViewById(R.id.editTextcanthabit);
        editTextcantbaños = findViewById(R.id.editTextcantbaños);
        editTextsuperficie = findViewById(R.id.editTextsuperficie);
        editTextprecio = findViewById(R.id.editTextprecio);
        editTextaño = findViewById(R.id.editTextaño);
        editTextdescripcion = findViewById(R.id.editTextdescripcion);
        buttonagregar = findViewById(R.id.buttonagregar);

        buttonagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicio();
            }
        });
    }
    public void consumirServicio(){

        String id = editTextid.getText().toString();
        String canthabit = editTextcanthabit.getText().toString();
        String cantbaños = editTextcantbaños.getText().toString();
        String superficie = editTextsuperficie.getText().toString();
        String precio = editTextprecio.getText().toString();
        String año = editTextaño.getText().toString();
        String descripcion = editTextdescripcion.getText().toString();

        ServicioTask servicioTask = new ServicioTask(this,"http://192.168.1.3:7777/api/v1.0/homes", id, canthabit, cantbaños, superficie, precio, año, descripcion);
        servicioTask.execute();
    }
}
