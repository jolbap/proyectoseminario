package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.example.pablo.proyectoseminario.Utils.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegistrarCasa extends AppCompatActivity implements View.OnClickListener{

    TextView editTextid, editTextcanthabit, editTextcantbaños, editTextsuperficie, editTextprecio, editTextaño, editTextdescripcion, editTextdireccion, editTextlat, editTextlon;
    Button buttonagregar;
    private Context root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_casa);

        loadComponents();
    }

    private void loadComponents() {
        buttonagregar = (Button)this.findViewById(R.id.buttonagregar);
        buttonagregar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        editTextcanthabit = (TextView)this.findViewById(R.id.editTextcanthabit);
        editTextcantbaños = (TextView)this.findViewById(R.id.editTextcantbaños);
        editTextsuperficie = (TextView)this.findViewById(R.id.editTextsuperficie);
        editTextprecio = (TextView)this.findViewById(R.id.editTextprecio);
        editTextaño = (TextView)this.findViewById(R.id.editTextaño);
        editTextdescripcion = (TextView)this.findViewById(R.id.editTextdescripcion);
        editTextdireccion = (TextView)this.findViewById(R.id.editTextdireccion);
        editTextlat = (TextView)this.findViewById(R.id.editTextlat);
        editTextlon = (TextView)this.findViewById(R.id.editTextlon);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("canthabit", editTextcanthabit.getText());
        params.put("cantbaños", editTextcantbaños.getText());
        params.put("superficie", editTextsuperficie.getText());
        params.put("precio", editTextprecio.getText());
        params.put("año", editTextaño.getText());
        params.put("descripcion", editTextdescripcion.getText());
        params.put("direccion", editTextdireccion.getText());
        params.put("lat", editTextlat.getText());
        params.put("lon", editTextlon.getText());

        client.post(ParamsConnection.HOST, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msn = response.getString("msn");
                    String id = response.getString("id");
                    UserData.ID = id;
                    if (msn != null) {

                        Intent camera = new Intent(root, Camara.class);
                        root.startActivity(camera);
                    } else {
                        Toast.makeText(root, "ERROR AL enviar los datos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
