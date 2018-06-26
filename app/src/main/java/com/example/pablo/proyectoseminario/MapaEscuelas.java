package com.example.pablo.proyectoseminario;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListSchool;
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

public class MapaEscuelas extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap, mMap2;
    public String idC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParamsConnection.LISTDATA = new ArrayList<ItemList>();
        ParamsConnection.LISTDATASCHOOL = new ArrayList<ItemListSchool>();
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
                    ArrayList<String> urllist = new ArrayList<String>();
                    for (int j = 0; j < listGallery.length(); j++) {
                        urllist.add("http://192.168.43.109:7777" + listGallery.getString(j));
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
}
