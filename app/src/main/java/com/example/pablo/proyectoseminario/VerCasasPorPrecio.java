package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

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

public class VerCasasPorPrecio extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView LIST;
    private ArrayList<ItemList> LISTINFO;
    private Context root;
    private CustomAdapter ADAPTER;

    Spinner desde, hasta;
    ArrayAdapter<String> des, has;
    String [] arreglo_d = new String[]{"20000 $","50000 $","100000 $","200000 $","500000 $","1000000 $"};
    String [] arreglo_h = new String[]{"20000 $","50000 $","100000 $","200000 $","500000 $","1000000 $"};
    int d= 0, h=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        LISTINFO = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_casas_por_precio);

        loadComponents();
    }
    private void loadComponents() {
        LIST = (ListView) this.findViewById(R.id.listviewlayout);
        desde = (Spinner)findViewById(R.id.spinnerdesde);
        hasta = (Spinner)findViewById(R.id.spinnerhasta);

        desde.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    d = 20000;
                }else if (position == 1){
                    d = 50000;
                }else if (position == 2){
                    d = 100000;
                }else if (position == 3){
                    d = 200000;
                }else if (position == 4){
                    d = 500000;
                }else if (position == 5){
                    d = 1000000;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        hasta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    h = 20000;
                }else if (position == 1){
                    h = 50000;
                }else if (position == 2){
                    h = 100000;
                }else if (position == 3){
                    h = 200000;
                }else if (position == 4){
                    h = 500000;
                }else if (position == 5){
                    h = 1000000;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        des = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arreglo_d);
        has = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arreglo_h);

        desde.setAdapter(des);
        hasta.setAdapter(has);

        LIST.setOnItemClickListener(this);

        Button btnprecio = findViewById(R.id.btnbuscarprecio);

        btnprecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInitialRestData(d, h);
            }
        });
    }

    private void loadInitialRestData(final int d, final int h) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = ParamsConnection.HOST;
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
                        String url = "http://192.168.1.15:7777" + (String)itemJson.getJSONArray("gallery").get(0);

                        if (precio >= d && precio <= h) {
                            ItemList item = new ItemList(id, precio, direccion, lat, lon, url);

                            LISTINFO.add(item);
                        }
                    }
                    ADAPTER = new CustomAdapter(root, LISTINFO);
                    LIST.setAdapter(ADAPTER);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idC = this.LISTINFO.get(position).getIdC();
        Intent mDetaild = new Intent(this, com.example.pablo.proyectoseminario.Detaild.class);
        mDetaild.putExtra("id", idC);
        this.startActivity(mDetaild);
    }
}
