package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pablo.proyectoseminario.ListDataSource.CustomAdapter;
import com.example.pablo.proyectoseminario.ListDataSource.CustomAdapterVecindario;
import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListVecindario;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VerVecindario extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView LIST;
    private Context root;
    private Polygon polygon;
    private CustomAdapterVecindario ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        ParamsConnection.LISTVECINDARIO = new ArrayList<ItemListVecindario>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_vecindario);

        loadComponents();
    }

    private void loadComponents() {
        LIST = (ListView) this.findViewById(R.id.vervecindarios);
        loadInitialRestData();
        LIST.setOnItemClickListener(this);
    }

    private void loadInitialRestData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = ParamsConnection.HOST_VECINDARIO;
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    ParamsConnection.LISTVECINDARIO.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject itemJson = response.getJSONObject(i);
                        String nombrevecindario = itemJson.getString("nombrevecindario");
                        Integer zoom = itemJson.getInt("zoom");
                        double lat = itemJson.getDouble("lat");
                        double lng = itemJson.getDouble("lng");
                        String id = itemJson.getString("_id");

                        JSONArray points = itemJson.getJSONArray("coordenadas");

                        ItemListVecindario itemListVecindario = new ItemListVecindario(nombrevecindario, zoom, lat, lng, id, polygon);
                        ParamsConnection.LISTVECINDARIO.add(itemListVecindario);
                        //Toast.makeText(VerTodosLosVecindarios.this, "id del vecindario : " + id, Toast.LENGTH_LONG).show();
                    }
                    ADAPTER = new CustomAdapterVecindario(root, ParamsConnection.LISTVECINDARIO);
                    LIST.setAdapter(ADAPTER);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idV = ParamsConnection.LISTVECINDARIO.get(position).getIdV();
        Intent mDetaild = new Intent(this, MapsCasasPorVecindario.class);
        mDetaild.putExtra("idVecindario", idV);
        this.startActivity(mDetaild);
    }

}


