package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.example.pablo.proyectoseminario.Utils.UserData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LatLonMaps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private MarkerOptions marker;
    private Context root;
    Button btnubicacioncasa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lat_lon_maps);
        MapView map = this.findViewById(R.id.ubicacioncasa);
        if (map != null) {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();
        Double lat = latLng.latitude;
        Double lon = latLng.longitude;
        load(lat, lon);
        mMap.clear();
        marker = new MarkerOptions();
        marker.position(latLng);
        marker.title("Casa");
        marker.draggable(true);
        mMap.addMarker(marker);
    }

    private void load(final Double lat, final Double lon) {
        btnubicacioncasa = (Button) findViewById(R.id.btnubicacioncasa);
        btnubicacioncasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        LatLng potosi = new LatLng(-19.578297, -65.758633);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(potosi, 14));
        mMap.setOnMapClickListener(this);
    }
}
