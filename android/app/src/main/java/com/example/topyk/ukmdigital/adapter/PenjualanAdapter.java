package com.example.topyk.ukmdigital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Penjualan;

import java.util.ArrayList;

/**
 * Created by topyk on 7/17/2017.
 */

public class PenjualanAdapter extends BaseAdapter {
    private ArrayList<Penjualan> data_penjualan = new ArrayList<Penjualan>();
    Context context;
    private static LayoutInflater inflater = null;

    public PenjualanAdapter(Context context, ArrayList<Penjualan> data_penjualan){
        this.context = context;
        this.data_penjualan = data_penjualan;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        return data_penjualan.size();
    }

    @Override
    public Object getItem(int position) {
        return data_penjualan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_transaksi, null);
            TextView id_transaksi = (TextView)vi.findViewById(R.id.id_transaksi);
//            TextView id_barang = (TextView)vi.findViewById(R.id.id_barang);
//            TextView jumlah_barang = (TextView)vi.findViewById(R.id.jumlah_barang);
//            TextView total = (TextView)vi.findViewById(R.id.total);
            TextView waktu = (TextView)vi.findViewById(R.id.waktu);
//            TextView id_anggota = (TextView)vi.findViewById(R.id.id_anggota);
            TextView status = (TextView)vi.findViewById(R.id.status);
//            TextView alamat = (TextView)vi.findViewById(R.id.alamat);
            TextView nama_barang = (TextView)vi.findViewById(R.id.nama_barang); //jadi id transaksi
            TextView nomor = (TextView)vi.findViewById(R.id.no_transaksi);
            TextView nama_pembeli = (TextView)vi.findViewById(R.id.nama_pembeli); //nama_pembeli diganti waktu


            Penjualan daftar_transaksi = data_penjualan.get(position);
            id_transaksi.setText(daftar_transaksi.getId_transaksi());
//            id_barang.setText(daftar_transaksi.getId_barang());
//            jumlah_barang.setText(String.valueOf(daftar_transaksi.getJumlah_barang()));
//            total.setText(String.valueOf(daftar_transaksi.getTotal()));
            waktu.setText(daftar_transaksi.getWaktu());
//            id_anggota.setText(daftar_transaksi.getId_anggota());
            status.setText(daftar_transaksi.getStatus());
//            alamat.setText(daftar_transaksi.getAlamat());
            nama_barang.setText(daftar_transaksi.getId_transaksi());
            nomor.setText(String.valueOf(position+1));
            nama_pembeli.setText(daftar_transaksi.getWaktu());


        return vi;
    }
}
