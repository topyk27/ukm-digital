package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.menu.dashboard;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_cek_anggota;
import static com.example.topyk.ukmdigital.variabel.url_cek_pemilik;
import static com.example.topyk.ukmdigital.variabel.url_create_transaksi;
import static com.example.topyk.ukmdigital.variabel.url_read_toko;

/**
 * Created by topyk on 7/6/2017.
 */

public class detail_barang extends Fragment {
    TextView id_barang, id_jenis_barang, id_toko, nama_barang, harga, stok, jumlah_barang, deskirpsi, ukuran, merk, khusus;
    Button but_kurang, but_tambah, but_beli, but_lihat_toko;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    int jumlah = 1;
    String id, id_pemilik;
    String barang, jumlah_beli, id_anggota, tot, jumlah_stok;
    int total, harga_barang;
    SessionManager session;
    JSONParser jParser = new JSONParser();
//    String url_cek_pemilik = "http://192.168.43.192/ukm_digital/barang/cek_pemilik.php";
//    String url_create_transaksi = "http://192.168.43.192/ukm_digital/transaksi/create_transaksi.php";
//    String url_read_toko = "http://192.168.43.192/ukm_digital/toko/read_toko.php";
    JSONArray pemilik = null;
    JSONArray toko = null;
    Boolean boleh_beli;
    ArrayList<String> id_barangList = new ArrayList<>();
    ArrayList<String> nama_barangList = new ArrayList<>();
    ArrayList<Integer> jumlah_barangList = new ArrayList<>();
    ArrayList<Integer> totalList = new ArrayList<>();
    ArrayList<Integer> stokList = new ArrayList<>();
    String nama;
    int stokx;
    FloatingActionButton endBelanja;
    //variabel untuk ke detail toko
    String dnama_pemilik_toko, did_toko, dnama_toko, daalamat_toko, dtelp, ddeskripsi_toko, dgambar, did_kampung, did_anggota;
    LatLng latLng;
    //end

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.detail_barang, container, false);
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar);
        id_barang = (TextView)rootView.findViewById(R.id.id_barang);
        id_jenis_barang = (TextView)rootView.findViewById(R.id.id_jenis_barang);
        id_toko = (TextView)rootView.findViewById(R.id.id_toko);
        nama_barang = (TextView)rootView.findViewById(R.id.txtNama_Barang);
        harga = (TextView)rootView.findViewById(R.id.txtHarga);
        stok = (TextView)rootView.findViewById(R.id.txtStok);
        jumlah_barang = (TextView)rootView.findViewById(R.id.jumlah);
        deskirpsi = (TextView)rootView.findViewById(R.id.txtDeskripsi_Barang);
        but_kurang = (Button)rootView.findViewById(R.id.but_kurang);
        but_tambah = (Button)rootView.findViewById(R.id.but_tambah);
        but_beli = (Button)rootView.findViewById(R.id.but_beli);
        but_lihat_toko = (Button)rootView.findViewById(R.id.but_lihat_toko);
        endBelanja = (FloatingActionButton)rootView.findViewById(R.id.endBelanja);
        ukuran = (TextView)rootView.findViewById(R.id.ukuran);
        merk = (TextView)rootView.findViewById(R.id.merk);
        khusus = (TextView)rootView.findViewById(R.id.khusus);

        if (((MainActivity)getActivity()).getBelanja()){
            endBelanja.setVisibility(View.VISIBLE);
        }
        else {
            endBelanja.setVisibility(View.GONE);
        }

        session = new SessionManager(getActivity().getApplicationContext());
        id_anggota = session.getUserDetails().get("id_anggota");
//        String id_user = session.getUserDetails().get("id_anggota");
        String asalFrag = getArguments().getString("asalFrag");
        if (asalFrag != null && asalFrag.equalsIgnoreCase("list_barang_toko")){
            id_barangList = getArguments().getStringArrayList("id_barangList");
            nama_barangList = getArguments().getStringArrayList("nama_barangList");
            jumlah_barangList = getArguments().getIntegerArrayList("jumlah_barangList");
            totalList = getArguments().getIntegerArrayList("totalList");
            stokList = getArguments().getIntegerArrayList("stokList");
        }

        endBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Apakah sudah tidak ada yang ingin anda pesan?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle b = new Bundle();
                                b.putStringArrayList("id_barangList",id_barangList);
                                b.putStringArrayList("nama_barangList",nama_barangList);
                                b.putIntegerArrayList("jumlah_barangList",jumlah_barangList);
                                b.putIntegerArrayList("totalList",totalList);
                                b.putIntegerArrayList("stokList",stokList);
                                konfirmasi_beli kb = new konfirmasi_beli();
                                kb.setArguments(b);
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame,kb,"konfirmasi");
                                ft.addToBackStack("list_barang_toko");
                                ft.commit();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


        String isi_id_barang = getArguments().getString("id_barang");
        String isi_id_jenis_barang = getArguments().getString("id_jenis_barang");
        String isi_id_toko = getArguments().getString("id_toko");
        final String isi_nama_barang = getArguments().getString("nama_barang");
        String isi_harga = getArguments().getString("harga");
        final String isi_stok = getArguments().getString("stok");
        String isi_deskripsi = getArguments().getString("deskripsi_barang");
        String isi_gambar = getArguments().getString("gambar");
        String isi_ukuran = getArguments().getString("ukuran");
        String isi_merk = getArguments().getString("merk");
        String isi_khusus = getArguments().getString("khusus");

        id = isi_id_barang;
        id_barang.setText(isi_id_barang);
        id_jenis_barang.setText(isi_id_jenis_barang);
        id_toko.setText(isi_id_toko);
        nama_barang.setText(isi_nama_barang);
        harga.setText(isi_harga);
        stok.setText(isi_stok);
        deskirpsi.setText(isi_deskripsi);
        gambar.setImageUrl(isi_gambar, imageLoader);
        jumlah_barang.setText(String.valueOf(jumlah));
        ukuran.setText(isi_ukuran);
        merk.setText(isi_merk);
        khusus.setText(isi_khusus);
//        new cekPemilikBarang().execute();
        if (session.isLoggedIn() && !session.isAdmin()){
            new cekPemilikBarang().execute();
            new CekAnggota().execute();
        }
        for (int i = 0; i < id_barangList.size(); i++){
            if (id_barang.getText().toString().equalsIgnoreCase(id_barangList.get(i))){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Item ini sudah ada di keranjang anda, silahkan pilih yang lain")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFragmentManager().popBackStack();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }


        but_kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isi_stok != null && isi_stok.equalsIgnoreCase("0")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Stok Habis",Toast.LENGTH_LONG).show();
                }
                else {
                    jumlah--;
                    if (jumlah <= 0){
                        jumlah = 1;
                    }
                    jumlah_barang.setText(String.valueOf(jumlah));
                }

            }
        });

        but_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isi_stok != null && isi_stok.equalsIgnoreCase("0")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Stok Habis",Toast.LENGTH_LONG).show();
                }

                try {
                    stokx = Integer.parseInt(stok.getText().toString());

                    if (jumlah >= stokx){
                        jumlah_barang.setText(String.valueOf(jumlah));
                    }
                    else {
                        jumlah++;
                        jumlah_barang.setText(String.valueOf(jumlah));
                    }
                }
                catch (NumberFormatException nfe){
                    Log.d("parseInt","gagal");
                }

//                jumlah++;
//                jumlah_barang.setText(String.valueOf(jumlah));
            }
        });

        but_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isi_stok != null && isi_stok.equalsIgnoreCase("0")) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Stok Habis", Toast.LENGTH_LONG).show();
                } else {


                if (session.isLoggedIn() && !session.isAdmin()) {
//                    new cekPemilikBarang().execute();

                    did_toko = id_toko.getText().toString();
                    barang = id_barang.getText().toString();
                    try {
                        harga_barang = Integer.parseInt(harga.getText().toString());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                    total = jumlah * harga_barang;
                    jumlah_beli = jumlah_barang.getText().toString();
                    tot = String.valueOf(total);
                    jumlah_stok = stok.getText().toString();
                    nama = nama_barang.getText().toString();
                    stokx = Integer.parseInt(stok.getText().toString());

                    if (!boleh_beli) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Anda tidak bisa membeli barang sebagai Anggota UKM", Toast.LENGTH_LONG).show();
                    } else {
//                        new createTransaksi().execute();
                        new bacaPemilikToko().doInBackground();
                        final Bundle b = new Bundle();
                        b.putString("id_toko",did_toko);
                        b.putString("nama_toko",dnama_toko);
                        b.putString("asalFrag","detail_barang");

                        id_barangList.add(barang);
                        b.putStringArrayList("id_barangList",id_barangList);

                        nama_barangList.add(nama);
                        b.putStringArrayList("nama_barangList",nama_barangList);;

                        jumlah_barangList.add(jumlah);
                        b.putIntegerArrayList("jumlah_barangList",jumlah_barangList);

                        totalList.add(total);
                        b.putIntegerArrayList("totalList",totalList);

                        stokList.add(stokx);
                        b.putIntegerArrayList("stokList",stokList);
                        Log.d("stokdetail",String.valueOf(stokx));
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Apakah anda ingin membeli yang lain?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((MainActivity)getActivity()).belanja(true);
                                        ((MainActivity)getActivity()).id_barangList(id_barangList);
                                        ((MainActivity)getActivity()).nama_barangList(nama_barangList);
                                        ((MainActivity)getActivity()).jumlahBarangList(jumlah_barangList);
                                        ((MainActivity)getActivity()).totalList(totalList);
                                        ((MainActivity)getActivity()).stokList(stokList);

                                        list_barang_toko lbt = new list_barang_toko();
                                        lbt.setArguments(b);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.content_frame, lbt,"list_barang_toko");
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                        ((MainActivity)getActivity()).belanja(true);
                                        ((MainActivity)getActivity()).id_barangList(id_barangList);
                                        ((MainActivity)getActivity()).nama_barangList(nama_barangList);
                                        ((MainActivity)getActivity()).jumlahBarangList(jumlah_barangList);
                                        ((MainActivity)getActivity()).totalList(totalList);
                                        ((MainActivity)getActivity()).stokList(stokList);

                                        konfirmasi_beli kb = new konfirmasi_beli();
                                        kb.setArguments(b);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.content_frame,kb,"konfirmasi");
                                        ft.addToBackStack("detail_barang");
                                        ft.commit();


                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                } else if (session.isAdmin()) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Tidak bisa membeli menggunakan ID admin", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Anda belum login, silah login terlebih dahulu", Toast.LENGTH_LONG).show();
                }
            }
        }
        });

        but_lihat_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                did_toko = id_toko.getText().toString();
                new bacaPemilikToko().execute();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detail Barang");
    }

    class cekPemilikBarang extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText){
            List<NameValuePair> p = new ArrayList<>();

            p.add(new BasicNameValuePair("id_barang", id));
            try {
                JSONObject json = jParser.makeHttpRequest(url_cek_pemilik, "POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    pemilik = json.getJSONArray("barang");
                    for (int i = 0; i < pemilik.length(); i++){
                        JSONObject c = pemilik.getJSONObject(i);
                        id_pemilik = c.getString("id_anggota");

                    }
                    return "ok";
                }
                else {
                    return "no result";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return "exception caught";
            }

        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("no results")){
                Toast.makeText(getActivity().getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
            }
//            else {
//                session = new SessionManager(getActivity().getApplicationContext());
//                String id_user = session.getUserDetails().get("id_anggota");
////                Log.d("userHP", id_user);
////                Log.d("userBarang", id_pemilik);
//                if (id_user.equalsIgnoreCase(id_pemilik)){
//                    boleh_beli = false;
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "gak boleh beli",Toast.LENGTH_LONG).show();
//                }
//                else {
//                    boleh_beli = true;
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "beli beli beli",Toast.LENGTH_LONG).show();
//                }
//            }
        }
    }

    //gak dipake, soalnya dipindah ke konfirmasi beli


    class bacaPemilikToko extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText){
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("id_toko", did_toko));
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_toko,"POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    toko = json.getJSONArray("toko");
                    for (int i = 0; i < toko.length(); i++){
                        JSONObject c = toko.getJSONObject(i);
                        dnama_pemilik_toko = c.getString("nama");
                        did_toko = c.getString("id_toko");
                        dnama_toko = c.getString("nama_toko");
                        daalamat_toko = c.getString("alamat_toko");
                        dtelp = c.getString("telp");
                        ddeskripsi_toko = c.getString("deskripsi_toko");
                        dgambar = c.getString("gambar");
                        did_kampung = c.getString("id_kampung");
                        did_anggota = c.getString("id_anggota");
                        latLng = new LatLng(c.getDouble("lat"), c.getDouble("lng"));
                    }
                    return "ok";
                }
                else {
                    return "gagal";
                }
            }
            catch (Exception e){
                return "exception";
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("gagal")){
                Toast.makeText(getActivity().getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
            }
            else {

                session = new SessionManager(getActivity().getApplicationContext());
                String id_user = session.getUserDetails().get("id_anggota");
                if (id_user != null && id_user.equalsIgnoreCase(id_pemilik)){
                    toko_ku tk = new toko_ku();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, tk, "tokoku");
                    ft.commit();
                }
                else {
                    Bundle b = new Bundle();
                    b.putString("nama_pemilik_toko", dnama_pemilik_toko);
                    b.putString("id_toko", did_toko);
                    b.putString("nama_toko", dnama_toko);
                    b.putString("alamat_toko", daalamat_toko);
                    b.putString("telp", dtelp);
                    b.putString("deskripsi_toko", ddeskripsi_toko);
                    b.putString("id_kampung", did_kampung);
                    b.putString("id_anggota", did_anggota);
                    b.putString("gambar", dgambar);
                    b.putParcelable("latlng",latLng);
                    detail_toko dt = new detail_toko();
                    dt.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, dt, "detail_toko");
                    ft.addToBackStack("detail_barang");
                    ft.commit();
                }


            }
        }
    }

    class CekAnggota extends AsyncTask<String,Void,String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText){
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("id_anggota",id_anggota));
            try {
                JSONObject json = jParser.makeHttpRequest(url_cek_anggota,"POST",p);
                int success = json.getInt("success");
                if (success == 1){
                    return "ok";
                }
                else {
                    return "kosong";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return "exception";
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sambungan terputus, mohon periksa jaringan internet anda",Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                boleh_beli = true;
            }
            else {
                boleh_beli = false;
            }
        }
    }

}
