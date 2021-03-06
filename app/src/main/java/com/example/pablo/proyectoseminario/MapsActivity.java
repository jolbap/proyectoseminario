package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListSchool;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ParamsConnection.LISTDATA = new ArrayList<ItemList>();
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
        UiSettings uiSettings;
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        UiSettings uiSetting = mMap.getUiSettings();
        uiSetting.setMyLocationButtonEnabled(true);
        //uiSetting.setMyLocationButtonEnabled(true);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = ParamsConnection.HOST;
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    ParamsConnection.LISTDATA.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject itemJson = response.getJSONObject(i);
                        String id = itemJson.getString("_id");
                        int precio = itemJson.getInt("precio");
                        String direccion = itemJson.getString("direccion");
                        double lat = itemJson.getDouble("lat");
                        double lon = itemJson.getDouble("lon");
                        JSONArray listGallery = itemJson.getJSONArray("gallery");
                        ArrayList<String> urllist =  new ArrayList<String>();
                        for (int j = 0; j < listGallery.length(); j ++) {
                            urllist.add(ParamsConnection.HOST2 + listGallery.getString(j));
                        }
                        ItemList item = new ItemList(id, precio, direccion, lat, lon, urllist);

                        ParamsConnection.LISTDATA.add(item);
                    }
                    if (ParamsConnection.LISTDATA != null && ParamsConnection.LISTDATA.size() > 0) {
                        for (int i = 0; i < ParamsConnection.LISTDATA.size(); i++) {
                            LatLng position = new LatLng(ParamsConnection.LISTDATA.get(i).getLat(), ParamsConnection.LISTDATA.get(i).getLon());
                            mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.casa))
                                    .position(position).title(ParamsConnection.LISTDATA.get(i).getDireccion()));
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
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //marker.equals(ParamsConnection.LISTDATA);
        LatLng n = marker.getPosition();
        for (int i=0; i<ParamsConnection.LISTDATA.size(); i++) {
            LatLng m = new LatLng(ParamsConnection.LISTDATA.get(i).getLat(), ParamsConnection.LISTDATA.get(i).getLon());
            if (n.equals(m)) {
                //String idC = ParamsConnection.LISTDATA.get(i).getIdC();
                Intent mDetaild = new Intent(this, Detalle.class);
                mDetaild.putExtra("id", i);
                this.startActivity(mDetaild);
            }
        }
        return true;
    }
}
