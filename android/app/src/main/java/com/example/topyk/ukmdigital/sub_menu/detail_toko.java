package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
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
import com.example.topyk.ukmdigital.menu.profil;
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

/**
 * Created by topyk on 7/16/2017.
 */

public class detail_toko extends Fragment {
    TextView nama_pemilik_toko, id_toko, nama_toko, alamat_toko, telp, deskripsi_toko, id_kampung, id_anggota, tidak_ada_barang, tvKosong;
    Button butUbah, butHapus, lihat_dagangan;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ListView lv;
    ScrollView sv;
    ArrayList<Barang> daftar_barang = new ArrayList<Barang>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarBar = null;
//    String url_read_barang = "http://192.168.43.192/ukm_digital/barang/read_barang.php";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_BAR = "barang";
    public static final String TAG_ID_BAR = "id_barang";

    public static final String TAG_NAMA_BAR = "nama_barang";
    public static final String TAG_HARGA = "harga";
    public static final String TAG_STOK = "stok";
    public static final String TAG_DESKRIPSI = "deskripsi_barang";
    public static final String TAG_GAMBAR_BAR = "gambar";
    public static final String TAG_ID_JENIS = "id_jenis_barang";
    public static final String TAG_ID_TOKO = "id_toko";
    String param_id_toko, param_gambar;
    SessionManager session;
    CustomMapView mapView;
    private GoogleMap googleMap;

    String inama_toko;
    LatLng latLng;
    CameraPosition cameraPosition;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.detail_toko, container, false);
        mapView = (CustomMapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        setUpMap();
        cameraPosition = null;
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        sv = (ScrollView)rootView.findViewById(R.id.scroll_detail);
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar_toko);
        nama_pemilik_toko = (TextView)rootView.findViewById(R.id.nama_pemilik_toko);
        id_toko = (TextView)rootView.findViewById(R.id.id_toko);
        nama_toko = (TextView)rootView.findViewById(R.id.nama_toko);
        alamat_toko = (TextView)rootView.findViewById(R.id.alamat_toko);
        telp = (TextView)rootView.findViewById(R.id.telp);
        deskripsi_toko = (TextView)rootView.findViewById(R.id.deskripsi_toko);
        id_kampung = (TextView)rootView.findViewById(R.id.id_kampung);
        id_anggota = (TextView)rootView.findViewById(R.id.id_anggota);
//        lv = (ListView)rootView.findViewById(R.id.list_barang);
//
//        tidak_ada_barang = (TextView)rootView.findViewById(R.id.tidak_ada_barang);
        tvKosong = (TextView)rootView.findViewById(R.id.tvKosong);
        butUbah = (Button)rootView.findViewById(R.id.butUbahToko);
        butHapus = (Button)rootView.findViewById(R.id.butHapusToko);
        lihat_dagangan = (Button)rootView.findViewById(R.id.lihat_dagangan);
        session = new SessionManager(getActivity().getApplicationContext());
        if (session.isAdmin()){
            tvKosong.setVisibility(View.VISIBLE);
            butUbah.setVisibility(View.VISIBLE);
            butHapus.setVisibility(View.VISIBLE);
        }



        String inama_pemilik_toko = getArguments().getString("nama_pemilik_toko");
        String iid_toko = getArguments().getString("id_toko");
        inama_toko = getArguments().getString("nama_toko");
        String ialamat_toko = getArguments().getString("alamat_toko");
        String itelp = getArguments().getString("telp");
        String ideskripsi_toko = getArguments().getString("deskripsi_toko");
        String iid_kampung = getArguments().getString("id_kampung");
        String iid_anggota = getArguments().getString("id_anggota");
        String igambar = getArguments().getString("gambar");
        latLng = getArguments().getParcelable("latlng");
        param_gambar = igambar;
        Log.d("alamat", ialamat_toko);
        param_id_toko = iid_toko;
        nama_pemilik_toko.setText(inama_pemilik_toko);
        id_toko.setText(iid_toko);
        nama_toko.setText(inama_toko);
        alamat_toko.setText(ialamat_toko);
        telp.setText(itelp);
        deskripsi_toko.setText(ideskripsi_toko);
        id_kampung.setText(iid_kampung);
        id_anggota.setText(iid_anggota);
        gambar.setImageUrl(igambar, imageLoader);
//        new BacaBarang().execute();
//        final ProgressDialog pDialog;
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Mohon tunggu...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(false);
//        pDialog.show();
//        mapView.getMapAsync(new OnMapReadyCallback() {
//
//            @Override
//            public void onMapReady(GoogleMap gMap) {
//                googleMap = gMap;
//                cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                googleMap.addMarker(new MarkerOptions().position(latLng).title(inama_toko));
//                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//                    @Override
//                    public void onMapLoaded() {
//                        pDialog.dismiss();
//                    }
//                });
//            }
//        });
        cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
        Log.d("kamera",String.valueOf(cameraPosition));
        updateMap(latLng);
        lihat_dagangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id_toko",param_id_toko);
                b.putString("nama_toko", nama_toko.getText().toString());
                list_barang_toko lbt = new list_barang_toko();
                lbt.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,lbt,"list_barang_toko");
                ft.addToBackStack("detail_toko");
                ft.commit();
            }
        });
        nama_pemilik_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                profil p = new profil();
                b.putString("id_anggota",id_anggota.getText().toString());
                p.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,p,"detail_anggota");
                ft.addToBackStack("detail_toko");
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
//                b.putString("asalFrag", "detail_toko");
//
//                daftar_barang.clear();
//                if (session.isAdmin()){
//                    edit_barang eb = new edit_barang();
//                    eb.setArguments(b);
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame,eb,"edit_barang");
//                    ft.addToBackStack("detail_toko");
//                    ft.commit();
//                }
//                else {
//                    detail_barang db = new detail_barang();
////                edit_barang db = new edit_barang();
//
//                    db.setArguments(b);
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, db, "detail_barang");
//                    ft.addToBackStack("detail_toko");
//                    ft.commit();
//                }
//
//            }
//        });
        butUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iid_toko = id_toko.getText().toString();
                String inama_toko = nama_toko.getText().toString();
                String ialamat_toko = alamat_toko.getText().toString();
                String itelp = telp.getText().toString();
                String ideskripsi_toko = deskripsi_toko.getText().toString();
                String iid_kampung = id_kampung.getText().toString();
                String iid_anggota = id_anggota.getText().toString();
                String igambar = param_gambar;

                Bundle b = new Bundle();
                b.putString("id_toko", iid_toko);
                b.putString("nama_toko", inama_toko);
                b.putString("alamat_toko", ialamat_toko);
                b.putString("telp", itelp);
                b.putString("deskripsi_toko", ideskripsi_toko);
                b.putString("id_kampung", iid_kampung);
                b.putString("id_anggota", iid_anggota);
                b.putString("gambar", igambar);
                b.putString("asalFrag","detail_toko");
                b.putParcelable("latlng",latLng);

                edit_toko et = new edit_toko();
                et.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, et,"edit_toko");
                ft.addToBackStack("toko");


                ft.commit();
            }
        });
        butHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Anda yakin akan menghapus UKM " + nama_toko.getText().toString())
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteToko().execute();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detail UKM");
    }

//    @Override
//    public void onDestroyView(){
//        try {
//            Fragment dt = getFragmentManager().findFragmentByTag("detail_toko");
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.remove(dt);
//            ft.commit();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        super.onDestroyView();
//    }

//    @Override
//    public void onPause(){
////        mapView.onPause();
//        super.onPause();
////        cameraPosition = googleMap.getCameraPosition();
//        googleMap = null;
//    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        if (googleMap != null){
//            googleMap.clear();
//        }
////        setUpMap();
////
////        updateMap(latLng);
////        mapView.onResume();
//
////        if (cameraPosition != null){
//////            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////            cameraPosition = null;
////        }
//    }
    private void setUpMap(){
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void updateMap(final LatLng latLng){
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mohon tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
//                cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
//                Log.d("kamera",String.valueOf(cameraPosition));
                try {

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                catch (Exception e){
                    e.printStackTrace();
                    cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
                    Log.d("kamera",String.valueOf(cameraPosition));
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                googleMap.addMarker(new MarkerOptions().position(latLng).title(inama_toko));
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
//                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        pDialog.dismiss();
                    }
                });
            }
        });

    }

    class DeleteToko extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
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
                    return "fail";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();

            if(result.equalsIgnoreCase("Exception Caught"))
            {

                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            }

            else if(result.equalsIgnoreCase("FAIL"))
            {
                Toast.makeText(getActivity().getApplicationContext(), "Fail.. Try Again", Toast.LENGTH_LONG).show();
            }

            else {
                toko t = new toko();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction()
//                        .remove(getFragmentManager().findFragmentByTag("dash"));

                fragmentTransaction.replace(R.id.content_frame, t, "toko");
                fragmentTransaction.commit();
//                getFragmentManager().popBackStack();
//                Toast.makeText(getActivity().getApplicationContext(),"ini delete",Toast.LENGTH_LONG);
            }
        }
    }


}
