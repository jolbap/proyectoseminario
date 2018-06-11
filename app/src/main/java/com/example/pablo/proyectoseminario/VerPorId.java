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

import com.example.pablo.proyectoseminario.ListDataSource.CustomAdapter;
import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VerPorId extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView LIST;
    private ArrayList<ItemList> LISTINFO;
    private Context root;
    private CustomAdapter ADAPTER;
    EditText editTextbuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        LISTINFO = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_por_id);

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
    }

    private void loadInitialRestData(String keystr) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://192.168.1.15:7777/api/v1.0/homes/" + keystr;
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    LISTINFO.clear();
                    int precio = response.getInt("precio");
                    String id = response.getString("_id");
                    String direccion = response.getString("direccion");
                    double lat = response.getDouble("lat");
                    double lon = response.getDouble("lon");
                    ItemList item = new ItemList(id, precio, direccion, lat, lon);
                    LISTINFO.add(item);
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
