package com.example.topyk.ukmdigital.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Konfirmasi_Beli_Kelas;

import java.util.ArrayList;

/**
 * Created by topyk on 9/26/2017.
 */

public class KonfirmasiBeliAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater = null;
    private ArrayList<Konfirmasi_Beli_Kelas> data_konfirmasi = new ArrayList<Konfirmasi_Beli_Kelas>();

//    public KonfirmasiBeliAdapter(Context context,ArrayList data_nama, ArrayList data_jumlah, ArrayList data_total){
//        this.context = context;
//        this.data_nama = data_nama;
//        this.data_jumlah = data_jumlah;
//        this.data_total = data_total;
//
//        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
public KonfirmasiBeliAdapter(Context context,ArrayList<Konfirmasi_Beli_Kelas> kbk){
    this.context = context;
    this.data_konfirmasi = kbk;
    inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

}
    @Override
    public int getCount()
    {
        return data_konfirmasi.size();
    }
    @Override
    public Object getItem(int position)
    {
        return data_konfirmasi.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;

            vi = inflater.inflate(R.layout.list_konfirmasi_beli, null);
            TextView no = (TextView)vi.findViewById(R.id.no);
            TextView nama = (TextView)vi.findViewById(R.id.nama);
            TextView jumlah = (TextView)vi.findViewById(R.id.jumlah);
            TextView total = (TextView)vi.findViewById(R.id.total);

            Konfirmasi_Beli_Kelas kbk = data_konfirmasi.get(position);



            no.setText(String.valueOf(position+1));
//        nama.setText(String.valueOf(data_nama.get(position)));
//        jumlah.setText(String.valueOf(data_jumlah.get(position)));
//        total.setText(String.valueOf(data_total.get(position)));

            nama.setText(kbk.getNama());
            jumlah.setText(kbk.getJumlah());
            total.setText(kbk.getTotal());
            Log.d("nox",no.getText().toString() + " " + nama.getText().toString() + " - " + String.valueOf(position));


        return vi;
    }
}
