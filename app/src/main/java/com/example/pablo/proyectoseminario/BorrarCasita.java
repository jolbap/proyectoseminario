package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class BorrarCasita extends AppCompatActivity implements View.OnClickListener{

    public String idC;
    public static int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_casita);
        ID = this.getIntent().getExtras().getInt("id");
        idC = ParamsConnection.LISTDATA.get(ID).getIdC();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.borrarcasita);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = ParamsConnection.HOST + idC;
        client.delete(url, new JsonHttpResponseHandler(){

        });
        Intent intent = new Intent(this, FuncionesAgente.class);
        this.startActivity(intent);
        Toast.makeText(getApplicationContext(),"Casa Borrada",Toast.LENGTH_LONG).show();
    }
}
