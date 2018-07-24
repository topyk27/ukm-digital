package com.example.topyk.ukmdigital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.kelas.Kampung;

import java.util.ArrayList;

/**
 * Created by topyk on 8/15/2017.
 */

public class KampungAdapter extends BaseAdapter {
    private ArrayList<Kampung> data_kampung = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public KampungAdapter(Context context, ArrayList<Kampung> data_kampung){
        this.context = context;
        this.data_kampung = data_kampung;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount(){
        return data_kampung.size();
    }
    @Override
    public Object getItem(int position){
        return data_kampung.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_kampung, null);
        TextView id = (TextView)vi.findViewById(R.id.id_kampung);
        TextView nama = (TextView)vi.findViewById(R.id.nama_kampung);
        TextView alamat = (TextView)vi.findViewById(R.id.alamat_kampung);
        NetworkImageView gambar = (NetworkImageView)vi.findViewById(R.id.gambar);
        TextView lat = (TextView)vi.findViewById(R.id.lat);
        TextView lng = (TextView)vi.findViewById(R.id.lng);

        Kampung daftar_kampung = data_kampung.get(position);
        id.setText(daftar_kampung.getId_kampung());
        nama.setText(daftar_kampung.getNama_kampung());
        alamat.setText(daftar_kampung.getAlamat_kampung());
        gambar.setImageUrl(daftar_kampung.getGambar_kampung(),imageLoader);
        lat.setText(String.valueOf(daftar_kampung.getLatLng().latitude));
        lng.setText(String.valueOf(daftar_kampung.getLatLng().longitude));
        return vi;
    }
}
