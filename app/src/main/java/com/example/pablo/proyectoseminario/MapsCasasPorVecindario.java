package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsCasasPorVecindario extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Intent intent;
    private String idV;
    private ArrayList<LatLng> positions;
    private Polygon vecindario;
    private LatLng pos;
    private TextView vecindarionom;
    private String nombrevec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParamsConnection.LISTDATA = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_casas_por_vecindario);
        intent = getIntent();
        positions = new ArrayList<>();
        idV = intent.getExtras().getString("idVecindario");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.vecindario_detalle);
        mapFragment.getMapAsync(this);
        vecindarionom = this.findViewById(R.id.vecindarionom);
        loadVecindario();
    }

    private void loadVecindario() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(ParamsConnection.HOST_VECINDARIO+idV, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String nombre = response.getString("nombrevecindario");
                    Integer zoom = response.getInt("zoom");
                    double lat = response.getDouble("lat");
                    double lng = response.getDouble("lng");
                    String id = response.getString("_id");

                    JSONArray points = response.getJSONArray("coordenadas");
                    positions = getArrayListFromJsonArray(points);

                    vecindarionom.setText(nombre);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),zoom));

                    vecindario.setPoints(positions);

                    //Toast.makeText(MapsCasasPorVecindario.this, "id del vecindario : "+id, Toast.LENGTH_LONG).show();

                    nombrevec = nombre;


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
                                    String nombrev = itemJson.getString("nombrevecindario");
                                    JSONArray listGallery = itemJson.getJSONArray("gallery");
                                    ArrayList<String> urllist =  new ArrayList<String>();
                                    for (int j = 0; j < listGallery.length(); j ++) {
                                        urllist.add(ParamsConnection.HOST2 + listGallery.getString(j));
                                    }
                                    if (nombrevec.equals(nombrev)) {
                                        ItemList item = new ItemList(id, precio, direccion, lat, lon, urllist);
                                        ParamsConnection.LISTDATA.add(item);
                                    }
                                }
                                if (ParamsConnection.LISTDATA != null && ParamsConnection.LISTDATA.size() > 0) {
                                    for (int i = 0; i < ParamsConnection.LISTDATA.size(); i++) {
                                        LatLng position = new LatLng(ParamsConnection.LISTDATA.get(i).getLat(), ParamsConnection.LISTDATA.get(i).getLon());

                                        //mMap.addMarker(new MarkerOptions().position(position).title(LISTINFO.get(i).getDireccion()));
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
        UiSettings uiSettings;
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        UiSettings uiSetting = mMap.getUiSettings();
        uiSetting.setMyLocationButtonEnabled(true);
        uiSetting.setMyLocationButtonEnabled(true);

        LatLng potosi = new LatLng(-19.5722805, -65.7550063);
       //mMap.addMarker(new MarkerOptions().position(potosi).title("Marker in Potosi"));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(potosi)
                .zoom(15)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        /*LatLng sydney = new LatLng(-34, 151);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        //ArrayList<LatLng> points = intent.getParcelableArrayListExtra("points");


        PolygonOptions rectOptions = new PolygonOptions()
                .add(potosi)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(50,50,50,50));


        vecindario = mMap.addPolygon(rectOptions);
        vecindario.setClickable(true);
        //polygon.setPoints(list);
        //mMap.setOnMapClickListener(this);
        mMap.setOnPolygonClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(this, "click polygon" ,Toast.LENGTH_SHORT).show();
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
