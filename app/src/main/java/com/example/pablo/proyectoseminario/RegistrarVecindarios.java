package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.ListDataSource.ItemListVecindario;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RegistrarVecindarios extends AppCompatActivity implements View.OnClickListener{

    private GoogleMap mMap;
    Button btnguardarvecindario;
    Intent intent;
    EditText nombrevecindario,lat,lng;
    ArrayList<LatLng> points;
    Context root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_vecindarios);
        root = this;

        intent = getIntent();
        points = new ArrayList<>();
        points = intent.getExtras().getParcelableArrayList("points");

        loadComponents();
    }

    private void loadComponents() {
        btnguardarvecindario = (Button)this.findViewById(R.id.btnguardarvecindario);
        btnguardarvecindario.setOnClickListener(this);
        nombrevecindario = this.findViewById(R.id.vecindarionombre);

    }
    /*Metodo para obtener el centro de un poligono*/
    private LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }
    @Override
    public void onClick(View v) {
        intent = getIntent();
        if (intent.getExtras() == null){
            Toast.makeText(this, "No se recibio las coordenadas", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        Double positions[] = getStringArrayFromLatLngArray(points);
        if (nombrevecindario.getText().length() != 0){
            params.put("nombrevecindario", nombrevecindario.getText());
        }else{
            Toast.makeText(this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        params.put("zoom", 15);
        params.put("lat", getPolygonCenterPoint(points).latitude);
        params.put("lng", getPolygonCenterPoint(points).longitude);

        params.put("coordenadas", positions);
        /*if (positions.length < 1 ){
            Toast.makeText(this, getPolygonCenterPoint(points).toString(), Toast.LENGTH_LONG).show();
            //Log.i("latlng", "onClick: "+ getPolygonCenterPoint(points).toString());
            finish();
            return;
        }*/

        client.post(ParamsConnection.HOST_VECINDARIO, params, new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msn = response.getString("msn");
                    String doc = response.getString("doc");


                    if (msn != null) {

                        Toast.makeText(RegistrarVecindarios.this, msn, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(root,VerVecindariosMaps.class);
                        intent.putExtra("idVecindario",doc);
                        startActivity(intent);

                    } else {
                        Toast.makeText(RegistrarVecindarios.this, "Error al insertar el vecindario", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(RegistrarVecindarios.this, "Error : " +throwable.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                Toast.makeText(RegistrarVecindarios.this, "Intento de reenvio nro : " + retryNo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Double[] getStringArrayFromLatLngArray(ArrayList<LatLng> latLngArrayList) {
        int size = latLngArrayList.size();
        Double latLngs[] = new Double[size*2];
        latLngs[0] = latLngArrayList.get(0).latitude;
        latLngs[1] = latLngArrayList.get(0).longitude;

        for (int i = 2; i<size*2; i+=2){

            Double lat = latLngArrayList.get(i/2).latitude;
            Double lng = latLngArrayList.get(i/2).longitude;

            latLngs[i] = lat;
            latLngs[i+1] = lng;

        }
        return latLngs;
    }

}


