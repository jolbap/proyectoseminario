package com.example.pablo.proyectoseminario.ListDataSource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.pablo.proyectoseminario.R;

import java.util.ArrayList;

public class CustomAdapterBorrarVecindario extends BaseAdapter{
    private Context CONTEXT;
    private ArrayList<ItemListVecindario> LIST;
    public CustomAdapterBorrarVecindario (Context contex, ArrayList<ItemListVecindario> list) {
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
            convertView = inflate.inflate(R.layout.item_borrar_vecindario, null);
        }
        TextView nombrevecindario = (TextView)convertView.findViewById(R.id.txtborrarvecindario);
        nombrevecindario.setText(this.LIST.get(position).getNombrevecindario());
        return convertView;
    }


}
