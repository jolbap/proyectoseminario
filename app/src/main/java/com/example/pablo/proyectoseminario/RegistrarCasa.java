package com.example.pablo.proyectoseminario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pablo.proyectoseminario.Utils.ParamsConnection;

public class RegistrarCasa extends AppCompatActivity {

    EditText editTextid, editTextcanthabit, editTextcantbaños, editTextsuperficie, editTextprecio, editTextaño, editTextdescripcion, editTextdireccion, editTextlat, editTextlon;
    Button buttonagregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_casa);
        editTextcanthabit = findViewById(R.id.editTextcanthabit);
        editTextcantbaños = findViewById(R.id.editTextcantbaños);
        editTextsuperficie = findViewById(R.id.editTextsuperficie);
        editTextprecio = findViewById(R.id.editTextprecio);
        editTextaño = findViewById(R.id.editTextaño);
        editTextdescripcion = findViewById(R.id.editTextdescripcion);
        editTextdireccion = findViewById(R.id.editTextdireccion);
        editTextlat = findViewById(R.id.editTextlat);
        editTextlon = findViewById(R.id.editTextlon);
        buttonagregar = findViewById(R.id.buttonagregar);

        buttonagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicio();
            }
        });
    }
    public void consumirServicio(){

        String canthabit = editTextcanthabit.getText().toString();
        String cantbaños = editTextcantbaños.getText().toString();
        String superficie = editTextsuperficie.getText().toString();
        int precio = Integer.parseInt(editTextprecio.getText().toString());
        String año = editTextaño.getText().toString();
        String descripcion = editTextdescripcion.getText().toString();
        String direccion = editTextdireccion.getText().toString();
        double lat = Double.parseDouble(editTextlat.getText().toString());
        double lon = Double.parseDouble(editTextlon.getText().toString());

        ServicioTask servicioTask = new ServicioTask(this, ParamsConnection.HOST, canthabit, cantbaños, superficie, precio, año, descripcion, direccion, lat, lon);
        servicioTask.execute();
    }
}
