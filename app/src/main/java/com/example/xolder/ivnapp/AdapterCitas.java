package com.example.xolder.ivnapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by XOLDER on 04/06/2016.
 */
public class AdapterCitas extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Citas> items;

    public AdapterCitas (Activity activity, ArrayList<Citas> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Citas> citases) {
        for (int i = 0; i < citases.size(); i++) {
            items.add(citases.get(i));
        }
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.listview_json, parent, false);
        }

        Citas cita = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.alumno);
        title.setText(cita.getAlumno());

        TextView description = (TextView) v.findViewById(R.id.hora);
        description.setText(cita.getHora());

        ImageView imagen = (ImageView) v.findViewById(R.id.imgUser);
        imagen.setImageDrawable(cita.getImagen());

        return v;
    }
}
