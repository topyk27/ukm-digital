package com.example.topyk.ukmdigital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Jenis_Barang;

import java.util.ArrayList;

/**
 * Created by topyk on 8/22/2017.
 */

public class JenisBarangAdapter extends BaseAdapter {
    private ArrayList<Jenis_Barang> data_jenis = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;

    public JenisBarangAdapter(Context context, ArrayList<Jenis_Barang> data_jenis){
        this.context = context;
        this.data_jenis = data_jenis;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount(){
        return data_jenis.size();
    }
    @Override
    public Object getItem(int position){
        return data_jenis.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_jenis, null);
        TextView id = (TextView)vi.findViewById(R.id.id_jenis_barang);
        TextView jenis_barang = (TextView)vi.findViewById(R.id.jenis_barang);

        Jenis_Barang daftar_jenis = data_jenis.get(position);
        id.setText(daftar_jenis.getId_jenis_barang());
        jenis_barang.setText(daftar_jenis.getJenis_barang());

        return vi;
    }
}
