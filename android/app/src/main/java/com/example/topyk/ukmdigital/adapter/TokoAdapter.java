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
import com.example.topyk.ukmdigital.kelas.Toko;

import java.util.ArrayList;

/**
 * Created by topyk on 7/15/2017.
 */

public class TokoAdapter extends BaseAdapter {
    private ArrayList<Toko> data_toko = new ArrayList<Toko>();
    Context context;
    LayoutInflater inflater = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public TokoAdapter(Context context, ArrayList<Toko> data_toko){
        this.context = context;
        this.data_toko = data_toko;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return data_toko.size();
    }
    @Override
    public Object getItem(int position){
        return data_toko.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_toko, null);
            TextView nama_pemilik_toko = (TextView)vi.findViewById(R.id.nama_pemilik_toko);
            TextView id_toko = (TextView)vi.findViewById(R.id.id_toko);
            TextView nama_toko = (TextView)vi.findViewById(R.id.nama_toko);
            TextView alamat_toko = (TextView)vi.findViewById(R.id.alamat_toko);
            TextView telp = (TextView)vi.findViewById(R.id.telp);
            TextView deskripsi_toko = (TextView)vi.findViewById(R.id.deskripsi_toko);
            NetworkImageView gambar = (NetworkImageView)vi.findViewById(R.id.gambar_toko);
            TextView id_kampung = (TextView)vi.findViewById(R.id.id_kampung);
            TextView id_anggota = (TextView)vi.findViewById(R.id.id_anggota);
            TextView lat = (TextView)vi.findViewById(R.id.lat);
            TextView lng = (TextView)vi.findViewById(R.id.lng);

            Toko daftar_toko = data_toko.get(position);
            nama_pemilik_toko.setText(daftar_toko.getNama());
            id_toko.setText(daftar_toko.getId_toko());
            nama_toko.setText(daftar_toko.getNama_toko());
            alamat_toko.setText(daftar_toko.getAlamat_toko());
            telp.setText(daftar_toko.getTelp());
            deskripsi_toko.setText(daftar_toko.getDeskripsi_toko());
            gambar.setImageUrl(daftar_toko.getGambar(), imageLoader);
            id_kampung.setText(daftar_toko.getId_kampung());
            id_anggota.setText(daftar_toko.getId_anggota());
            lat.setText(String.valueOf(daftar_toko.getLatLng().latitude));
            lng.setText(String.valueOf(daftar_toko.getLatLng().longitude));

        return vi;
    }
}
