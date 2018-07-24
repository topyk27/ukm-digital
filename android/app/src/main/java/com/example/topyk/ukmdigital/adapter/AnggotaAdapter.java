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
import com.example.topyk.ukmdigital.kelas.Anggota;

import java.util.ArrayList;

/**
 * Created by topyk on 8/10/2017.
 */

public class AnggotaAdapter extends BaseAdapter {
    private ArrayList<Anggota> data_anggota = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AnggotaAdapter(Context context, ArrayList<Anggota> data_anggota){
        this.context = context;
        this.data_anggota = data_anggota;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return data_anggota.size();
    }
    @Override
    public Object getItem(int position){
        return data_anggota.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_anggota, null);

        TextView id_anggota = (TextView)vi.findViewById(R.id.id_anggota);
        TextView nama = (TextView)vi.findViewById(R.id.nama_anggota);
        TextView alamat = (TextView)vi.findViewById(R.id.alamat_anggota);
        TextView no_hp = (TextView)vi.findViewById(R.id.no_hp);
        TextView email = (TextView)vi.findViewById(R.id.email);
        NetworkImageView gambar = (NetworkImageView)vi.findViewById(R.id.gambar_anggota);
        TextView username = (TextView)vi.findViewById(R.id.username);
        TextView password = (TextView)vi.findViewById(R.id.password);

        Anggota daftar_anggota = data_anggota.get(position);
        id_anggota.setText(daftar_anggota.getId());
        nama.setText(daftar_anggota.getNama());
        alamat.setText(daftar_anggota.getAlamat());
        no_hp.setText(daftar_anggota.getNo_hp());
        email.setText(daftar_anggota.getEmail());
        gambar.setImageUrl(daftar_anggota.getGambar(),imageLoader);
        username.setText(daftar_anggota.getUsername());
        password.setText(daftar_anggota.getPassword());

        return vi;
    }
}
