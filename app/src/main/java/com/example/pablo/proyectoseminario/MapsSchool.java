package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListSchool;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsSchool extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap, mMap2;
    public String idC;
    Button buscarescuelas, btnescuelacercana;
    double latitudcasa, longitudcasa, latmenor, lonmenor, distanciamenor, cuantadistancia;
    String escuela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParamsConnection.LISTDATA = new ArrayList<ItemList>();
        ParamsConnection.LISTDATASCHOOL = new ArrayList<ItemListSchool>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_school);
        idC = this.getIntent().getExtras().getString("id");
        MapView map = this.findViewById(R.id.mapescuelas);
        if (map != null) {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }
    }

    public void salirMapaEscuelas(View v){
        Intent inte = new Intent(this, MainActivity.class);
        startActivity(inte);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings;
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        UiSettings uiSetting = mMap.getUiSettings();
        uiSetting.setMyLocationButtonEnabled(true);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = ParamsConnection.HOST + this.idC;
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    ParamsConnection.LISTDATA.clear();
                    String id = response.getString("_id");
                    int precio = response.getInt("precio");
                    String direccion = response.getString("direccion");
                    double lat = response.getDouble("lat");
                    double lon = response.getDouble("lon");
                    JSONArray listGallery = response.getJSONArray("gallery");
                    ArrayList<String> urllist = new ArrayList<String>();
                    for (int j = 0; j < listGallery.length(); j++) {
                        urllist.add(ParamsConnection.HOST2 + listGallery.getString(j));
                    }
                    ItemList item = new ItemList(id, precio, direccion, lat, lon, urllist);
                    ParamsConnection.LISTDATA.add(item);
                    if (ParamsConnection.LISTDATA != null && ParamsConnection.LISTDATA.size() > 0) {
                        LatLng position = new LatLng(lat, lon);
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.casa))
                                .position(position).title(ParamsConnection.LISTDATA.get(0).getDireccion()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
                    }
                    latitudcasa = lat;
                    longitudcasa = lon;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        buscarescuelas = (Button) findViewById(R.id.btnbuscarescuelas);
        buscarescuelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client2 = new AsyncHttpClient();
                String url2 = ParamsConnection.HOSTSCHOOL;
                client2.get(url2, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        try {
                            ParamsConnection.LISTDATASCHOOL.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject itemJson = response.getJSONObject(i);
                                String ide = itemJson.getString("_id");
                                String escuelanombre = itemJson.getString("escuelanombre");
                                double late = itemJson.getDouble("late");
                                double lone = itemJson.getDouble("lone");


                                double num1 = (double)Math.pow((late - latitudcasa),2);
                                double num2 = (double)Math.pow((lone - longitudcasa),2);
                                double suma = num1 + num2;
                                double distancia = (double)Math.sqrt(suma);

                                if(i!=0){
                                    if (distancia <= distanciamenor){
                                        distanciamenor = distancia;
                                        cuantadistancia = distanciamenor * 160000;
                                        cuantadistancia = Math.round(cuantadistancia*100.0)/100.0;

                                        latmenor = late;
                                        lonmenor = lone;
                                        escuela = escuelanombre;
                                    }
                                }else{
                                    distanciamenor = distancia;
                                    latmenor = late;
                                    lonmenor = lone;
                                    escuela = escuelanombre;
                                }


                                ItemListSchool itemschool = new ItemListSchool(ide, escuelanombre, late, lone);
                                ParamsConnection.LISTDATASCHOOL.add(itemschool);
                            }
                            if (ParamsConnection.LISTDATASCHOOL != null && ParamsConnection.LISTDATASCHOOL.size() > 0) {
                                for (int i = 0; i < ParamsConnection.LISTDATASCHOOL.size(); i++) {
                                    LatLng position = new LatLng(ParamsConnection.LISTDATASCHOOL.get(i).getLate(), ParamsConnection.LISTDATASCHOOL.get(i).getLone());
                                    mMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.escuela))
                                            .position(position).title(ParamsConnection.LISTDATASCHOOL.get(i).getEscuelanombre()));

                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        btnescuelacercana = (Button)findViewById(R.id.btnescuelacercana);
        btnescuelacercana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLngcasa = new LatLng(latitudcasa,longitudcasa);
                LatLng latLngescuela = new LatLng(latmenor,lonmenor);
                Polyline polyline = mMap.addPolyline(new PolylineOptions()
                        .add(latLngcasa,latLngescuela)
                        .width(10)
                        .color(Color.RED)
                        .geodesic(true));
                Toast.makeText(getApplicationContext(),""+ escuela +" a "+cuantadistancia+ " metros",Toast.LENGTH_LONG).show();
            }
        });
    }
}
