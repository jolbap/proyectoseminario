package com.example.pablo.proyectoseminario;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.proyectoseminario.ListDataSource.LoaderImg;
import com.example.pablo.proyectoseminario.ListDataSource.OnLoadCompleImg;
import com.example.pablo.proyectoseminario.Utils.ParamsConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Detalle extends AppCompatActivity implements OnLoadCompleImg,View.OnClickListener  {

    public Button btnmapaescuelas;
    public String idC;
    protected TextView direccion, canthabit, cantbaños, superficie, precio, año, descripcion ,nombrevecindario;
    protected Detalle root;
    protected com.example.pablo.proyectoseminario.DataDetaild.Detaild DATA;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static int ID;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        ID = this.getIntent().getExtras().getInt("id");
        idC = ParamsConnection.LISTDATA.get(ID).getIdC();
        loadComponents();
        loadAsyncData();
        load();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void load() {
        btnmapaescuelas = (Button) this.findViewById(R.id.btnmapaescuelas);
        btnmapaescuelas.setOnClickListener(this);
    }

    public void contactar(View v){
        Intent inte = new Intent(this, LoginCliente.class);
        startActivity(inte);
    }

    public void loadAsyncData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ParamsConnection.HOST + this.idC,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String idH = response.getString("_id");
                            String canthabit = response.getString("canthabit");
                            String cantbaños = response.getString("cantbaños");
                            String superficie = response.getString("superficie");
                            int precio = response.getInt("precio");
                            String año = response.getString("año");
                            String descripcion = response.getString("descripcion");
                            String direccion = response.getString("direccion");
                            String nombrevecindario = response.getString("nombrevecindario");
                            JSONArray listGallery = response.getJSONArray("gallery");
                            ArrayList<String> urllist =  new ArrayList<String>();
                            for (int j = 0; j < listGallery.length(); j ++) {
                                urllist.add(ParamsConnection.HOST2 + listGallery.getString(j));
                            }

                            DATA = new com.example.pablo.proyectoseminario.DataDetaild.Detaild(idH, canthabit, cantbaños, superficie, precio, año, descripcion, urllist, direccion, nombrevecindario);
                            root.setInformation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void setInformation() {
        this.direccion.setText(DATA.getDireccion());
        this.canthabit.setText(DATA.getCanthabit());
        this.cantbaños.setText(DATA.getCantbaños());
        this.superficie.setText(DATA.getSuperficie());
        this.precio.setText(DATA.getPrecio()+"");
        this.año.setText(DATA.getAño());
        this.descripcion.setText(DATA.getDescripcion());
        this.nombrevecindario.setText(DATA.getNombrevecindario());
    }

    private void loadComponents() {
        this.direccion = (TextView)this.findViewById(R.id.direccionC);
        this.canthabit = (TextView) this.findViewById(R.id.canthabitC);
        this.cantbaños = (TextView)this.findViewById(R.id.cantbañosC);
        this.superficie = (TextView)this.findViewById(R.id.superficieC);
        this.precio = (TextView)this.findViewById(R.id.precioC);
        this.año = (TextView)this.findViewById(R.id.añoC);
        this.descripcion = (TextView)this.findViewById(R.id.descripcionC);
        this.nombrevecindario = (TextView)this.findViewById(R.id.nombrevecindarioC);
        //this.fotito = (ImageView)this.findViewById(R.id.fotito);
    }
    @Override
    public void OnloadCompleteImgResult(ImageView img, int position, Bitmap imgsourceimg) {
        ArrayList<Bitmap> source = new ArrayList<>();
        source.add(imgsourceimg);
        DATA.setImg(source);
        img.setImageBitmap(imgsourceimg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent mapa = new Intent(this,MapsSchool.class);
        mapa.putExtra("id", idC);
        this.startActivity(mapa);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnLoadCompleImg{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            ArrayList<String> info = ParamsConnection.LISTDATA.get(ID).getUrlimg();
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("url", info.get(sectionNumber));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detalle, container, false);

            ImageView img = (ImageView)rootView.findViewById(R.id.gallery);
            String url = this.getArguments().getString("url");
            LoaderImg loader = new LoaderImg();
            loader.setOnloadCompleteImg(img,0,this);
            loader.execute(url);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
        @Override
        public void OnloadCompleteImgResult(ImageView img, int position, Bitmap imgsourceimg) {
            img.setImageBitmap(imgsourceimg);
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return ParamsConnection.LISTDATA.get(ID).getUrlimg().size();
        }
    }
}
