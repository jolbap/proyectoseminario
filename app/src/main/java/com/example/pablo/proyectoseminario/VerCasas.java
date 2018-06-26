package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pablo.proyectoseminario.DataDetaild.Detaild;
import com.example.pablo.proyectoseminario.ListDataSource.CustomAdapter;
import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VerCasas extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView LIST;
    private Context root;
    private CustomAdapter ADAPTER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        ParamsConnection.LISTDATA = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_casas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        loadComponents();
    }

    private void loadComponents() {
        LIST = (ListView) this.findViewById(R.id.listviewlayout);
        loadInitialRestData();
        LIST.setOnItemClickListener(this);
    }

    private void loadInitialRestData() {
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
                            urllist.add("http://192.168.43.109:7777" + listGallery.getString(j));
                        }
                        ItemList item = new ItemList(id, precio, direccion, lat, lon, urllist);

                        ParamsConnection.LISTDATA.add(item);
                    }
                    ADAPTER = new CustomAdapter(root, ParamsConnection.LISTDATA);
                    LIST.setAdapter(ADAPTER);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent mDetaild = new Intent(this,Detalle.class);
        mDetaild.putExtra("id", position);
        this.startActivity(mDetaild);
    }
}
