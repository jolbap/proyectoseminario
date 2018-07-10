package com.example.pablo.proyectoseminario;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VerVecindariosMaps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener {

    private GoogleMap mMap;
    private Intent intent;
    private String idV;
    private ArrayList<LatLng> positions;
    private Polygon vecindario;
    private LatLng pos;
    private TextView vecindarionombre;
    private String nombrevec;
    private String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParamsConnection.LISTDATA = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_vecindarios_maps);
        intent = getIntent();
        positions = new ArrayList<>();
        idV = intent.getExtras().getString("idVecindario");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.vec_detalle);
        mapFragment.getMapAsync(this);
        vecindarionombre = this.findViewById(R.id.vecindarionombre);
        loadVecindario();
    }
    public void salirMapsVecindario(View v){
        Intent inte = new Intent(this, FuncionesAgente.class);
        startActivity(inte);
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

                    vecindarionombre.setText(nombre);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),zoom));

                    vecindario.setPoints(positions);

                    Toast.makeText(VerVecindariosMaps.this, "id del vecindario : "+id, Toast.LENGTH_LONG).show();


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


        PolygonOptions rectOptions = new PolygonOptions()
                .add(potosi)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(50,50,50,50));


        vecindario = mMap.addPolygon(rectOptions);
        vecindario.setClickable(true);
        //polygon.setPoints(list);
        //mMap.setOnMapClickListener(this);
        mMap.setOnPolygonClickListener(this);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(this, "click polygon" ,Toast.LENGTH_SHORT).show();
    }
}
