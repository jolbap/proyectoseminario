package com.example.pablo.proyectoseminario.ListDataSource;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.proyectoseminario.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements OnLoadCompleImg{
    private Context CONTEXT;
    private ArrayList<ItemList> LIST;

    public CustomAdapter (Context contex, ArrayList<ItemList> list) {
        this.CONTEXT = contex;
        this.LIST = list;
    }

    @Override
    public int getCount() {
        return this.LIST.size();
    }

    @Override
    public Object getItem(int position) {
        return this.LIST.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflate = (LayoutInflater) this.CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.item_layout, null);
        }
        TextView direccion = (TextView)convertView.findViewById(R.id.direcciontxt);
        TextView precio = (TextView)convertView.findViewById(R.id.preciotxt);
        ImageView img = convertView.findViewById(R.id.imageView);

        direccion.setText(this.LIST.get(position).getDireccion());
        precio.setText(this.LIST.get(position).getPrecio()+"");

        if (this.LIST.get(position).getImg() == null) {
            //Load IMG
            LoaderImg loader = new LoaderImg();
            loader.setOnloadCompleteImg(img , position,this);
            loader.execute(this.LIST.get(position).getUrlimg().get(0));
        } else {
            img.setImageBitmap(this.LIST.get(position).getImg().get(0));
        }
        return convertView;
    }
    @Override
    public void OnloadCompleteImgResult(ImageView img, int position, Bitmap imgsourceimg) {
        ArrayList<Bitmap> source = new ArrayList<>();
        source.add(imgsourceimg);
        this.LIST.get(position).setImg(source);
        img.setImageBitmap(imgsourceimg);
    }
}
