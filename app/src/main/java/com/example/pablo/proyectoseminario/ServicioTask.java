package com.example.pablo.proyectoseminario;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;


public class ServicioTask extends AsyncTask<Void, Void, String> {
    private Context httpContext;
    ProgressDialog progressDialog;
    public String resultadoapi="";
    public String linkrequestAPI="";
    public String canthabit = "";
    public String cantbaños = "";
    public String superficie = "";
    public int precio = 0;
    public String año = "";
    public String descripcion = "";
    public String direccion = "";
    public double lat = 0;
    public double lon = 0;

    public ServicioTask(Context ctx, String linkAPI, String canthabit, String cantbaños, String superficie, int precio, String año, String descripcion, String direccion, double lat, double lon ){
        this.httpContext = ctx;
        this.linkrequestAPI = linkAPI;
        this.canthabit = canthabit;
        this.cantbaños = cantbaños;
        this.superficie = superficie;
        this.precio = precio;
        this.año = año;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.lat = lat;
        this.lon = lon;
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Procesando Solicitud", "por favor, espere");
    }

    @Override
    protected String doInBackground(Void... params){
        String result = null;
        String wsURL = linkrequestAPI;
        URL url = null;
        try {

            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            JSONObject parametrosPost = new JSONObject();
            parametrosPost.put("canthabit",canthabit);
            parametrosPost.put("cantbaños",cantbaños);
            parametrosPost.put("superficie",superficie);
            parametrosPost.put("precio",precio);
            parametrosPost.put("año",año);
            parametrosPost.put("descripcion",descripcion);
            parametrosPost.put("direccion",direccion);
            parametrosPost.put("lat",lat);
            parametrosPost.put("lon",lon);

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(parametrosPost));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer ab = new StringBuffer("");
                String linea = "";
                while ((linea=in.readLine()) != null){
                    ab.append(linea);
                    break;
                }
                in.close();
                result = ab.toString();
            }
            else {
                result = new String("Error" + responseCode);
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        progressDialog.dismiss();
        resultadoapi=s;
        Toast.makeText(httpContext,resultadoapi,Toast.LENGTH_LONG).show();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while (itr.hasNext()){
            String key = itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}

