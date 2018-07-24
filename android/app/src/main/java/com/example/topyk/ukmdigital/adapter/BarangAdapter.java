package com.example.topyk.ukmdigital.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.kelas.Barang;
import com.example.topyk.ukmdigital.util.BMP;
import com.example.topyk.ukmdigital.util.CustomNetworkImageView;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by topyk on 6/6/2017.
 */

public class BarangAdapter extends BaseAdapter {
    private ArrayList<Barang> data_barang = new ArrayList<Barang>();
    Context context;
    LayoutInflater inflater = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public BarangAdapter(){

    }

    public BarangAdapter(Context context, ArrayList<Barang> data_barang){
        this.context = context;
        this.data_barang = data_barang;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return data_barang.size();
    }
    @Override
    public Object getItem(int position)
    {
        return data_barang.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View vi = convertView;
        if (convertView == null)

            vi = inflater.inflate(R.layout.list_barang, null);

            TextView id_barang = (TextView) vi.findViewById(R.id.id_barang);
            //TextView kode_barang = (TextView)vi.findViewById(R.id.kode_barang);
            TextView nama_barang = (TextView) vi.findViewById(R.id.nama_barang);
            TextView harga = (TextView)vi.findViewById(R.id.txtHarga);
            TextView stok = (TextView) vi.findViewById(R.id.stok);
            TextView deskripsi_barang = (TextView) vi.findViewById(R.id.deskripsi_barang);
            NetworkImageView gambar_barang = (NetworkImageView) vi.findViewById(R.id.gambar_barang);
            TextView id_jenis_barang = (TextView) vi.findViewById(R.id.id_jenis_barang);
            TextView id_toko = (TextView) vi.findViewById(R.id.id_toko);
            TextView ukuran = (TextView)vi.findViewById(R.id.ukuran);
            TextView merk = (TextView)vi.findViewById(R.id.merk);
            TextView khusus = (TextView)vi.findViewById(R.id.khusus);



            Barang daftar_barang = data_barang.get(position);
        Log.d("wkwk",String.valueOf(position));
            id_barang.setText(daftar_barang.getId_barang());
//        kode_barang.setText(daftar_barang.getKode_barang());
            nama_barang.setText(daftar_barang.getNama_barang());
            harga.setText(String.valueOf(daftar_barang.getHarga()));
//        stok.setText(daftar_barang.getStok());
            stok.setText(String.valueOf(daftar_barang.getStok()));
            deskripsi_barang.setText(daftar_barang.getDeskripsi_barang());
            gambar_barang.setImageUrl(daftar_barang.getGambar_barang(), imageLoader);

            id_jenis_barang.setText(daftar_barang.getId_jenis_barang());
            id_toko.setText(daftar_barang.getId_toko());
            ukuran.setText(daftar_barang.getUkuran());
            merk.setText(daftar_barang.getMerk());
            khusus.setText(daftar_barang.getKhusus());
        
            return vi;

    }

    public void updateAdapter(ArrayList<Barang> data_barang){
        this.data_barang = data_barang;
        notifyDataSetChanged();
    }

    public void clear(){
        data_barang.clear();
        this.notifyDataSetChanged();
    }


}
