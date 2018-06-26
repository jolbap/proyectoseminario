package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class BorrarBuscarCasaPorId extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView LIST;
    private Context root;
    private CustomAdapter ADAPTER;
    EditText editTextbuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        ParamsConnection.LISTDATA = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_buscar_casa_por_id);

        loadComponents();
    }
    private void loadComponents() {
        LIST = (ListView) this.findViewById(R.id.listviewlayout);
        editTextbuscar = (EditText)this.findViewById(R.id.buscarcasa);
        LIST.setOnItemClickListener(this);
        Button btnbuscar = findViewById(R.id.btnbuscar);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buscar = editTextbuscar.getText().toString();
                loadInitialRestData(buscar);
            }
        });

        Button btnborrar = findViewById(R.id.btnborrar);
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buscar = editTextbuscar.getText().toString();
                borrar(buscar);
                Toast.makeText(getApplicationContext(),"Casa Borrada",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void borrar(String b){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = ParamsConnection.HOST + b;
        client.delete(url, new JsonHttpResponseHandler(){

        });
        Intent intent = new Intent(root, BorrarBuscarCasaPorId.class);
        this.startActivity(intent);

    }

    private void loadInitialRestData(String b) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = ParamsConnection.HOST + b;
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    ParamsConnection.LISTDATA.clear();
                    int precio = response.getInt("precio");
                    String id = response.getString("_id");
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
