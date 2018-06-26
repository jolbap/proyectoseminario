package com.example.pablo.proyectoseminario;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapaEscuelas extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public String idC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_escuelas);

        idC = this.getIntent().getExtras().getString("id");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                    ArrayList<String> urllist =  new ArrayList<String>();
                    for (int j = 0; j < listGallery.length(); j ++) {
                        urllist.add("http://192.168.1.15:7777" + listGallery.getString(j));
                    }
                    ItemList item = new ItemList(id, precio, direccion, lat, lon, urllist);
                    ParamsConnection.LISTDATA.add(item);
                    if (ParamsConnection.LISTDATA != null && ParamsConnection.LISTDATA.size() > 0) {
                        LatLng position = new LatLng(lat, lon);
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.casa))
                                .position(position).title(ParamsConnection.LISTDATA.get(0).getDireccion()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,16));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
