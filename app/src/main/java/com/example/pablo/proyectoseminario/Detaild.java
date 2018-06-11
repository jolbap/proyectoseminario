package com.example.pablo.proyectoseminario;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Detaild extends AppCompatActivity {

    public String idC;
    protected TextView idH, canthabit, cantbaños, superficie, precio, año, descripcion;
    protected Detaild root;
    protected com.example.pablo.proyectoseminario.DataDetaild.Detaild DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaild);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idC = this.getIntent().getExtras().getString("id");
        loadComponents();
        loadAsyncData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadAsyncData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ParamsConnection.HOST + this.idC,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String idH = response.getString("_id");
                            String canthabit = response.getString("canthabit");
                            String cantbaños = response.getString("cantbaños");
                            String superficie = response.getString("superficie");
                            int precio = response.getInt("precio");
                            String año = response.getString("año");
                            String descripcion = response.getString("descripcion");

                            DATA = new com.example.pablo.proyectoseminario.DataDetaild.Detaild(idH, canthabit, cantbaños, superficie, precio, año, descripcion);
                            root.setInformation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setInformation() {
        this.idH.setText(DATA.getIdC());
        this.canthabit.setText(DATA.getCanthabit());
        this.cantbaños.setText(DATA.getCantbaños());
        this.superficie.setText(DATA.getSuperficie());
        this.precio.setText(DATA.getPrecio()+"");
        this.año.setText(DATA.getAño());
        this.descripcion.setText(DATA.getDescripcion());

    }

    private void loadComponents() {
        this.idH = (TextView)this.findViewById(R.id.idC);
        this.canthabit = (TextView) this.findViewById(R.id.canthabitC);
        this.cantbaños = (TextView)this.findViewById(R.id.cantbañosC);
        this.superficie = (TextView)this.findViewById(R.id.superficieC);
        this.precio = (TextView)this.findViewById(R.id.precioC);
        this.año = (TextView)this.findViewById(R.id.añoC);
        this.descripcion = (TextView)this.findViewById(R.id.descripcionC);
    }

}
