package com.example.pablo.proyectoseminario;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<ItemList> LISTINFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LISTINFO = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        String url = "http://192.168.1.2:7777/api/v1.0/homes";
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    LISTINFO.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject itemJson = response.getJSONObject(i);
                        String id = itemJson.getString("_id");
                        int precio = itemJson.getInt("precio");
                        String direccion = itemJson.getString("direccion");
                        double lat = itemJson.getDouble("lat");
                        double lon = itemJson.getDouble("lon");

                        ItemList item = new ItemList(id, precio, direccion, lat, lon);

                        LISTINFO.add(item);
                    }
                    if (LISTINFO != null && LISTINFO.size() > 0) {
                        for (int i = 0; i < LISTINFO.size(); i++) {
                            LatLng position = new LatLng(LISTINFO.get(i).getLat(), LISTINFO.get(i).getLon());

                            //mMap.addMarker(new MarkerOptions().position(position).title(LISTINFO.get(i).getDireccion()));
                            mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.casa))
                                    .position(position).title(LISTINFO.get(i).getDireccion()));

                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng potosi = new LatLng(-19.578297, -65.758633);
        //mMap.addMarker(new MarkerOptions().position(potosi).title("Marker in Potosi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(potosi,14));
    }
}
