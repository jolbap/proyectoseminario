package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListVecindario;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.example.pablo.proyectoseminario.Utils.UserData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import static android.media.CamcorderProfile.get;

public class DentroVecindario extends FragmentActivity implements OnMapReadyCallback, /*GoogleMap.OnMapClickListener,*/ GoogleMap.OnPolygonClickListener {

    private GoogleMap mMap;
    private GoogleMap mapita;
    private MarkerOptions marker;
    private Context root;
    Button btnubicacioncasa;
    private Polygon vecindario;
    Double minlat, maxlat, minlon, maxlon, num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        ParamsConnection.LISTVECINDARIO = new ArrayList<ItemListVecindario>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_vecindario);
        MapView map = this.findViewById(R.id.ubicacioncasa);
        if (map != null) {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }
        //loadVecindario();
    }

    private void loadVecindario() {
        /*AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://192.168.1.15:7777/api/v1.0/vecindario/", new JsonHttpResponseHandler() {
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
                        PolygonOptions rectOptions = new PolygonOptions()
                                .add(new LatLng(0,0))
                                .strokeColor(Color.RED)
                                .fillColor(Color.argb(50,50,50,50));

                        Polygon polygon = mMap.addPolygon(rectOptions);
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
        });*/
    }
    private ArrayList<LatLng> getArrayListFromJsonArray(JSONArray points) throws JSONException {
        ArrayList<LatLng> posiciones = new ArrayList<>();
        for(int i = 0; i<points.length();i+=2){
            posiciones.add(new LatLng(Double.parseDouble(points.getString(i)) ,Double.parseDouble(points.getString(i+1)) ));
        }
        return posiciones;
    }
    /*@Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();
        Double lat = latLng.latitude;
        Double lon = latLng.longitude;
        load(lat, lon);
        mapita.clear();
        marker = new MarkerOptions();
        marker.position(latLng);
        marker.title("Casa");
        marker.draggable(true);
        mapita.addMarker(marker);
    }*/

    private void load(final Double lat, final Double lon) {
        btnubicacioncasa = (Button) findViewById(R.id.btnubicacioncasa);
        btnubicacioncasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client2 = new AsyncHttpClient();

                client2.get(ParamsConnection.HOST_VECINDARIO, new JsonHttpResponseHandler() {
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

                                //Double minlat, maxlat, num;
                                JSONArray points = itemJson.getJSONArray("coordenadas");
                                for (int j=0; j < points.length(); j+=2){
                                    if (j != 0){
                                        num = Double.parseDouble(points.getString(j));
                                        if (minlat > num){
                                            minlat = num;
                                        }
                                    }
                                    else {
                                        minlat = Double.parseDouble(points.getString(j));
                                    }
                                }
                                for (int j=0; j < points.length(); j+=2){
                                    if (j != 0){
                                        num = Double.parseDouble(points.getString(j));
                                        if (maxlat < num){
                                            maxlat = num;
                                        }
                                    }
                                    else {
                                        maxlat = Double.parseDouble(points.getString(j));
                                    }
                                }
                                for (int j=1; j < points.length(); j+=2){
                                    if (j != 1){
                                        num = Double.parseDouble(points.getString(j));
                                        if (minlon > num){
                                            minlon = num;
                                        }
                                    }
                                    else {
                                        minlon = Double.parseDouble(points.getString(j));
                                    }
                                }
                                for (int j=1; j < points.length(); j+=2){
                                    if (j != 1){
                                        num = Double.parseDouble(points.getString(j));
                                        if (maxlon < num){
                                            maxlon = num;
                                        }
                                    }
                                    else {
                                        maxlon = Double.parseDouble(points.getString(j));
                                    }
                                }
                                if((lat>=minlat)&&(lat<=maxlat)&&(lon>=minlon)&&(lon<=maxlon)){
                                    AsyncHttpClient client3 = new AsyncHttpClient();
                                    RequestParams params = new RequestParams();
                                    params.put("nombrevecindario", nombre);
                                    client3.patch(ParamsConnection.REST_HOME_PATCH + UserData.ID, params, new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            //Intent camera = new Intent(root, Camara.class);
                                            //root.startActivity(camera);
                                        }
                                    });
                                }

                                ArrayList<LatLng> position;
                                position = new ArrayList<>();
                                position = getArrayListFromJsonArray(points);
                                PolygonOptions rectOptions = new PolygonOptions()
                                        .add(new LatLng(0,0))
                                        .strokeColor(Color.RED)
                                        .fillColor(Color.argb(50,50,50,50));

                                Polygon polygon = mMap.addPolygon(rectOptions);
                                //polygon.setClickable(true);
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
                if (marker != null){
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("lat", lat.doubleValue());
                    params.put("lon", lon.doubleValue());
                    client.patch(ParamsConnection.REST_HOME_PATCH + UserData.ID, params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Intent camera = new Intent(root, Camara.class);
                            root.startActivity(camera);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings;
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        UiSettings uiSetting = mMap.getUiSettings();
        uiSetting.setMyLocationButtonEnabled(true);
        uiSetting.setMyLocationButtonEnabled(true);

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
                        Double n = Double.parseDouble(points.getString(0));
                        ArrayList<LatLng> position;
                        position = new ArrayList<>();
                        position = getArrayListFromJsonArray(points);
                        PolygonOptions rectOptions = new PolygonOptions()
                                .add(new LatLng(0,0))
                                .strokeColor(Color.RED)
                                .fillColor(Color.argb(50,50,50,50));

                        Polygon polygon = mMap.addPolygon(rectOptions);
                        //polygon.setClickable(true);
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();
                Double lat = latLng.latitude;
                Double lon = latLng.longitude;
                load(lat, lon);
                //mMap.clear();
                marker = new MarkerOptions();
                marker.position(latLng);
                marker.title("Casa");
                marker.draggable(true);
                mMap.addMarker(marker);
            }
        });

        LatLng potosi = new LatLng(-19.578297, -65.758633);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(potosi, 14));
        //mMap.setOnMapClickListener(this);

        PolygonOptions rectOptions = new PolygonOptions()
                .add(potosi)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50,50,50,50));

        vecindario = mMap.addPolygon(rectOptions);
        //vecindario.setClickable(true);
        mMap.setOnPolygonClickListener(this);


    }
    @Override
    public void onPolygonClick(Polygon polygon) {
        //Toast.makeText(this, polygon.getId(), Toast.LENGTH_SHORT).show();
        if (ParamsConnection.LISTVECINDARIO != null && ParamsConnection.LISTVECINDARIO.size() > 0) {
            for (int i = 0; i < ParamsConnection.LISTVECINDARIO.size(); i++) {
                Polygon n = ParamsConnection.LISTVECINDARIO.get(i).getPoligono();
                if (polygon.equals(n)) {
                    Toast.makeText(this, ParamsConnection.LISTVECINDARIO.get(i).getNombrevecindario(), Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
}
