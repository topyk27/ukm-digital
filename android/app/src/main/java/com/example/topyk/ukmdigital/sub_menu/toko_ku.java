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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.BarangAdapter;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.app.CustomMapView;
import com.example.topyk.ukmdigital.kelas.Barang;
import com.example.topyk.ukmdigital.kelas.Toko;
import com.example.topyk.ukmdigital.menu.toko;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_delete_toko;
import static com.example.topyk.ukmdigital.variabel.url_read_barang;
import static com.example.topyk.ukmdigital.variabel.url_read_toko;

/**
 * Created by topyk on 7/24/2017.
 */

public class toko_ku extends Fragment {
    TextView id_toko, nama_toko, alamat_toko, telp, deskripsi_toko, id_kampung, id_anggota, tidak_ada_barang;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ListView lv;
    ScrollView sc;
    FloatingActionButton fab;
    Button butUbah, butHapus, lihat_dagangan;
    ArrayList<Barang> daftar_barang = new ArrayList<Barang>();
    JSONArray tokoKu = null;
    JSONParser jParser = new JSONParser();
    JSONArray daftarBar = null;
//    String url_read_barang = "http://192.168.43.192/ukm_digital/barang/read_barang.php";
//    String url_read_toko = "http://192.168.43.192/ukm_digital/toko/read_toko.php";
//    String url_delete_toko = "http://192.168.43.192/ukm_digital/toko/delete_toko.php";
    String param_id_toko, resultTokoku;

    String sid_toko, snama_toko, salamat_toko, stelp, sdeskripsi_toko, sid_kampung, sid_anggota, sgambar;
    SessionManager session;
    CustomMapView mapView;
    private GoogleMap googleMap;
    LatLng latLng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.toko_ku, container, false);
        mapView = (CustomMapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar_toko);
        id_toko = (TextView)rootView.findViewById(R.id.id_toko);
        nama_toko = (TextView)rootView.findViewById(R.id.nama_toko);
        alamat_toko = (TextView)rootView.findViewById(R.id.alamat_toko);
        telp = (TextView)rootView.findViewById(R.id.telp);
        deskripsi_toko = (TextView)rootView.findViewById(R.id.deskripsi_toko);
        id_kampung = (TextView)rootView.findViewById(R.id.id_kampung);
        id_anggota = (TextView)rootView.findViewById(R.id.id_anggota);
//        lv = (ListView)rootView.findViewById(R.id.list_barang);
        sc = (ScrollView)rootView.findViewById(R.id.scroll_toko);
//        tidak_ada_barang = (TextView)rootView.findViewById(R.id.tidak_ada_barang);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        butUbah = (Button)rootView.findViewById(R.id.butUbahToko);
        butHapus = (Button)rootView.findViewById(R.id.butHapusToko);
        lihat_dagangan = (Button)rootView.findViewById(R.id.lihat_dagangan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambah_barang tb = new tambah_barang();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,tb,"tambah_barang");
                ft.addToBackStack("tokoku");
                ft.commit();
            }
        });

//        sc.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.findViewById(R.id.list_barang).getParent()
//                        .requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//        });
//        lv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        session = new SessionManager(getActivity().getApplicationContext());

        new TokoKu().execute();
        lihat_dagangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id_toko",id_toko.getText().toString());
                b.putString("nama_toko", nama_toko.getText().toString());
                b.putString("asalFrag","tokoku");
                list_barang_toko lbt = new list_barang_toko();
                lbt.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,lbt,"list_barang_toko");
                ft.addToBackStack("tokoku");
                ft.commit();
            }
        });

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String isi_id_barang = ((TextView)view.findViewById(R.id.id_barang)).getText().toString();
//                String isi_id_jenis_barang = ((TextView)view.findViewById(R.id.id_jenis_barang)).getText().toString();
//                String isi_id_toko = ((TextView)view.findViewById(R.id.id_toko)).getText().toString();
//                String isi_nama_barang = ((TextView)view.findViewById(R.id.nama_barang)).getText().toString();
//                String isi_harga = ((TextView)view.findViewById(R.id.txtHarga)).getText().toString();
//                String isi_stok = ((TextView)view.findViewById(R.id.stok)).getText().toString();
//                String isi_deskripsi = ((TextView)view.findViewById(R.id.deskripsi_barang)).getText().toString();
//                String isi_gambar = daftar_barang.get(position).getGambar_barang();
//
//                Bundle b = new Bundle();
//                b.putString("id_barang", isi_id_barang);
//                b.putString("id_jenis_barang", isi_id_jenis_barang);
//                b.putString("id_toko", isi_id_toko);
//                b.putString("nama_barang", isi_nama_barang);
//                b.putString("harga", isi_harga);
//                b.putString("stok", isi_stok);
//                b.putString("deskripsi_barang", isi_deskripsi);
//                b.putString("gambar", isi_gambar);
//                b.putString("asalFrag", "tokoku");
//
//                daftar_barang.clear();
////                detail_barang db = new detail_barang();
//                edit_barang db = new edit_barang();
//                db.setArguments(b);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, db, "edit_barang");
//                ft.addToBackStack("tokoku");
//                ft.commit();
//            }
//        });

        butUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String igambar = sgambar;
                Log.d("isigambar", igambar);
                String iid_toko = id_toko.getText().toString();
                String inama_toko = nama_toko.getText().toString();
                String ialamat_toko = alamat_toko.getText().toString();
                String itelp = telp.getText().toString();
                String ideskripsi_toko = deskripsi_toko.getText().toString();
                String iid_kampung = id_kampung.getText().toString();
                String iid_anggota = id_anggota.getText().toString();

                Bundle b = new Bundle();
                b.putString("id_toko", iid_toko);
                b.putString("nama_toko", inama_toko);
                b.putString("alamat_toko", ialamat_toko);
                b.putString("telp", itelp);
                b.putString("deskripsi_toko", ideskripsi_toko);
                b.putString("id_kampung", iid_kampung);
                b.putString("id_anggota", iid_anggota);
                b.putString("gambar", igambar);
                b.putString("menudarifrag","tokoku");
                b.putParcelable("latlng",latLng);
                daftar_barang.clear();
                edit_toko et = new edit_toko();
                et.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,et,"edit_toko");
                ft.addToBackStack("tokoku");
                ft.commit();
            }
        });
        butHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param_id_toko = id_toko.getText().toString();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Anda yakin akan menghapus UKM anda")
                        .setCancelable(false)
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                                b.setMessage("Semua barang yang anda jual akan terhapus")
                                        .setCancelable(false)
                                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new HapusTokoKu().execute();
                                            }
                                        })
                                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = b.create();
                                alert.show();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert2 = builder.create();
                alert2.show();


            }
        });


        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("UKM Ku");
    }




    class TokoKu extends AsyncTask<String, Void, String>{
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
            Toko t = new Toko();
            String id_user = session.getUserDetails().get("id_anggota");
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("id_anggota", id_user));
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_toko, "POST", parameter);
                int success = json.getInt("success");
                if (success == 1){
                    tokoKu = json.getJSONArray("toko");
                    for (int i = 0; i < tokoKu.length(); i++){
                        JSONObject c = tokoKu.getJSONObject(i);
                        sid_toko = c.getString("id_toko");
                        snama_toko = c.getString("nama_toko");
                        salamat_toko = c.getString("alamat_toko");
                        stelp = c.getString("telp");
                        sdeskripsi_toko = c.getString("deskripsi_toko");
                        sgambar = c.getString("gambar");
                        sid_kampung = c.getString("id_kampung");
                        sid_anggota = c.getString("id_anggota");
                        latLng = new LatLng(c.getDouble("lat"), c.getDouble("lng"));
                    }
                    return "ok";
                }
                else {
                    return "gagal";
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
            Log.d("resultnya", result);
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("gagal")){
//                Toast.makeText(getActivity().getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
//                resultTokoku = "kosong";

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Anda belum memiliki UKM. Daftar sekarang?")
                            .setCancelable(false)
                            .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String tag;
//                                    tambah_toko tk = new tambah_toko();
//                                    tag = "tambah_toko";
                                    konfirmasi_bikin_toko kbt = new konfirmasi_bikin_toko();
                                    tag = "konfirmasi_bikin_toko";
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.content_frame,kbt,tag);
                                    ft.addToBackStack("tokoku");
                                    ft.commit();
                                }
                            })
                            .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getFragmentManager().popBackStack();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


            }
            else {
                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setMessage("Mohon Tunggu..");
                pd.setIndeterminate(true);
                pd.setCancelable(true);
                pd.show();
                gambar.setImageUrl(sgambar,imageLoader);
                id_toko.setText(sid_toko);
                nama_toko.setText(snama_toko);
                alamat_toko.setText(salamat_toko);
                telp.setText(stelp);
                deskripsi_toko.setText(sdeskripsi_toko);
                id_kampung.setText(sid_kampung);
                id_anggota.setText(sid_anggota);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap gMap) {
                        googleMap = gMap;
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(snama_toko));
                        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                pd.dismiss();
                            }
                        });
                    }
                });

//                new BacaBarangKu().execute();
            }

        }
    }



    class HapusTokoKu extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... sText){
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("id_toko", param_id_toko));
            try {
                JSONObject json = jParser.makeHttpRequest(url_delete_toko,"POST", parameter);
                int success = json.getInt("success");
                if (success == 1){
                    return "ok";
                }
                else {
                    return "gagal";
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
                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("gagal")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "gagal.. silahkan coba kembali", Toast.LENGTH_LONG).show();
            }
            else {
                toko t = new toko();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, t, "toko");
                fragmentTransaction.commit();
            }
        }
    }
}
