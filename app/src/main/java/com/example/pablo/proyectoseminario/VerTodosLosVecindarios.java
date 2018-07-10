package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListVecindario;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.example.pablo.proyectoseminario.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VerTodosLosVecindarios extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener {

    private GoogleMap mMap;
    private Intent intent;
    private String idV;
    private ArrayList<LatLng> positions;
    private Polygon vecindario;
    private ArrayList<Polygon> vecindario2;
    private LatLng pos;
    private TextView vecindarionombre;
    private Polygon polygon;
    private String nnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParamsConnection.LISTVECINDARIO = new ArrayList<ItemListVecindario>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_todos_los_vecindarios);
        intent = getIntent();

        //idV = "5b3afe8dcda9c2487fbab035";

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadVecindario();
    }
    private void loadVecindario() {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(ParamsConnection.HOST_VECINDARIO, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    ParamsConnection.LISTVECINDARIO.clear();
                    for (int i = 0; i < response.length(); i++ ) {
                        JSONObject itemJson = response.getJSONObject(i);
                        String nombre = itemJson.getString("nombrevecindario");
                        Integer zoom = itemJson.getInt("zoom");
                        double lat = itemJson.getDouble("lat");
                        double lng = itemJson.getDouble("lng");
                        String id = itemJson.getString("_id");

                        JSONArray points = itemJson.getJSONArray("coordenadas");
                        ArrayList<LatLng> position;
                        position = new ArrayList<>();
                        position = getArrayListFromJsonArray(points);

                        //vecindarionombre.setText(nombre);

                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));

                        //vecindario.setPoints(positions);
                        PolygonOptions rectOptions = new PolygonOptions()
                                .add(new LatLng(0,0))
                                .strokeColor(Color.RED)
                                .fillColor(Color.argb(50,50,50,50));

                        polygon = mMap.addPolygon(rectOptions);
                        polygon.setClickable(true);
                        polygon.setPoints(position);

                        ItemListVecindario itemListVecindario = new ItemListVecindario(nombre, zoom, lat, lng, id, polygon);
                        ParamsConnection.LISTVECINDARIO.add(itemListVecindario);
                        //Toast.makeText(VerTodosLosVecindarios.this, "id del vecindario : " + id, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private ArrayList<LatLng> getArrayListFromJsonArray(JSONArray points) throws JSONException {
        ArrayList<LatLng> posiciones = new ArrayList<>();
        for(int i = 0; i<points.length();i+=2){
            posiciones.add(new LatLng(Double.parseDouble(points.getString(i)) ,Double.parseDouble(points.getString(i+1)) ));
        }
        return posiciones;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng potosi = new LatLng(-19.5722805, -65.7550063);
        mMap.addMarker(new MarkerOptions().position(potosi).title("Marker in Potosi"));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(potosi)
                .zoom(15)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        /*LatLng sydney = new LatLng(-34, 151);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        //ArrayList<LatLng> points = intent.getParcelableArrayListExtra("points");
/*
        PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(-19.57796211, -65.773425321),
                        new LatLng(-19.57896211, -65.773425321),
                        new LatLng(-19.57996211, -65.77425321),
                        new LatLng(-19.57896211, -65.77425321),
                        new LatLng(-19.57796211, -65.77525321),
                        new LatLng(-19.57696211, -65.77525321))
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50,50,50,50));
        Polygon miPoligono = mMap.addPolygon(polygonOptions);
        miPoligono.setClickable(true);*/


        PolygonOptions rectOptions = new PolygonOptions()
                .add(potosi)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50,50,50,50));

        vecindario = mMap.addPolygon(rectOptions);
        vecindario.setClickable(true);
        //polygon.setPoints(list);
        //mMap.setOnMapClickListener(this);

        mMap.setOnPolygonClickListener(this);


    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        //Toast.makeText(this, "click polygon" ,Toast.LENGTH_SHORT).show();

        //ParamsConnection.LISTVECINDARIO;

        Toast.makeText(this, polygon.getId(), Toast.LENGTH_SHORT).show();
    }
}