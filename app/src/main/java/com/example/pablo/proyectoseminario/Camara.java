package com.example.pablo.proyectoseminario;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.example.pablo.proyectoseminario.Utils.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Camara extends AppCompatActivity implements View.OnClickListener{

    ImageView IMG_CONTAINER;
    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    private int MEDIA_CODE = 123;
    private int CAMERA_CODE = 124;
    private Context root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        validaPermisos();
        loadComponents();
    }
    private boolean validaPermisos(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        return false;
    }
    private void loadComponents() {
        Button media = (Button)this.findViewById(R.id.btnmedia);
        Button foto = (Button)this.findViewById(R.id.btnfoto);
        IMG_CONTAINER = (ImageView)this.findViewById(R.id.fototomada);
        media.setOnClickListener(this);
        foto.setOnClickListener(this);

        Button enviar = (Button) this.findViewById(R.id.btnenviarfoto);
        enviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnmedia) {
            loadMedia();
        }
        if(v.getId() == R.id.btnfoto) {
            tomarFotos();
        }
        if (v.getId() == R.id.btnenviarfoto) {
            try {
                enviarFotos();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void enviarFotos() throws FileNotFoundException {
        if ( IMG_CONTAINER.getDrawable() != null) {
            if (imageFilePath != null) {
                File file = new File(imageFilePath);
                RequestParams params = new RequestParams();
                params.put("img", file);
                AsyncHttpClient client = new AsyncHttpClient();
                if (UserData.ID != null) {
                    client.post(ParamsConnection.REST_USERIMG_POST + "/" + UserData.ID, params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {

                                String path = response.getString("path");
                                if (path != null) {
                                    Intent profile = new Intent(root, Camara.class);
                                    root.startActivity(profile);
                                    Toast.makeText(getApplicationContext(),"Foto Enviada",Toast.LENGTH_LONG).show();
                                }
                            }catch(JSONException json){
                                Log.i("ERROR", json.getMessage());
                            }

                        }

                    });

                }
            }
        } else {
            Toast.makeText(this, "No se ha sacado una foto", Toast.LENGTH_LONG).show();
        }
    }
    private void loadMedia() {
        Intent media = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        media.setType("image/");
        startActivityForResult(media.createChooser(media, "Escoja la Aplicacion"), MEDIA_CODE);
    }
    private String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);

        imageFilePath = image.getAbsolutePath();
        return image;
    }
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private void tomarFotos() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileuri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(camera, CAMERA_CODE);
    }
    private File createFile() {
        //Logica de creado
        File file = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        if (!file.exists()) {
            file.mkdirs();
        }
        //generar el nombre
        String name = "";
        if (file.exists()) {
            name = "IMG_" +System.currentTimeMillis()/1000+ ".jpg";
        }
        imageFilePath = file.getAbsolutePath() +File.separator + name;
        File fileimg = new File(imageFilePath);
        return fileimg;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MEDIA_CODE) {
            IMG_CONTAINER.setImageURI(data.getData());
        }
        if(requestCode == CAMERA_CODE) {

            loadImageCamera();
        }
    }
    private void loadImageCamera() {
        Bitmap img = BitmapFactory.decodeFile(imageFilePath);
        if(img != null) {
            IMG_CONTAINER.setImageBitmap(img);

        }
    }
    public void salirCamara(View v){
        Intent inte = new Intent(this, MainActivity.class);
        startActivity(inte);
        Toast.makeText(getApplicationContext(),"Casa Registrada",Toast.LENGTH_LONG).show();
    }
}
