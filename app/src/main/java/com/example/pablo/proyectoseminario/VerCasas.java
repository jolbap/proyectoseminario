package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.pablo.proyectoseminario.ListDataSource.CustomAdapter;
import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VerCasas extends AppCompatActivity {

    private ListView LIST;
    private ArrayList<ItemList> LISTINFO;
    private Context root;
    private CustomAdapter ADAPTER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        LISTINFO = new ArrayList<ItemList>();
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
    }

    private void loadInitialRestData() {
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
                        String precio = itemJson.getString("precio");

                        ItemList item = new ItemList(id, precio);

                        LISTINFO.add(item);
                    }
                    ADAPTER = new CustomAdapter(root, LISTINFO);
                    LIST.setAdapter(ADAPTER);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

}