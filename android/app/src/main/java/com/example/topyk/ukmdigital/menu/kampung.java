package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
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
import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.BarangAdapter;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.app.CustomMapView;
import com.example.topyk.ukmdigital.kelas.Barang;
import com.example.topyk.ukmdigital.sub_menu.detail_barang;
import com.example.topyk.ukmdigital.util.JSONParser;
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

import static com.example.topyk.ukmdigital.variabel.url_read_barang;

/**
 * Created by topyk on 8/1/2017.
 */

public class kampung extends Fragment {
    TextView id_kampung, nama_kampung, alamat_kampung;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ListView lv;
    ScrollView sv;
    Button lihat_toko, lihat_barang;
    ArrayList<Barang> daftar_barang = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarBar = null;
//    String url_read_barang = "http://192.168.43.192/ukm_digital/barang/read_barang.php";
    String param_id_kampung, param_nama_kampung, title;
    LatLng ilat;
    CustomMapView mapView;
    private GoogleMap googleMap;
    CameraPosition cameraPosition;
    String inama_kampung;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.menu_kampung, container, false);
        mapView = (CustomMapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }
//        sv = (ScrollView)rootView.findViewById(R.id.scroll_kampung);
//        lv = (ListView)rootView.findViewById(R.id.list_barang);
        id_kampung = (TextView)rootView.findViewById(R.id.id_kampung);
        nama_kampung = (TextView)rootView.findViewById(R.id.nama_kampung);

        alamat_kampung = (TextView)rootView.findViewById(R.id.alamat_kampung);
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar_kampung);
        lihat_toko = (Button)rootView.findViewById(R.id.but_list_toko);
        lihat_barang = (Button)rootView.findViewById(R.id.but_list_barang);



        String iid_kampung = getArguments().getString("id_kampung");
        inama_kampung = getArguments().getString("nama_kampung");
        param_nama_kampung = inama_kampung;
        inama_kampung = ((MainActivity)getActivity()).kapital(inama_kampung);
        title = inama_kampung;
        String ialamat_kampung = getArguments().getString("alamat_kampung");
        String igambar = getArguments().getString("gambar");
        Log.d("isigambar", igambar);
        igambar = igambar.replaceAll(" ","%20");
        ilat = getArguments().getParcelable("latlng");
        param_id_kampung = iid_kampung;
        id_kampung.setText(iid_kampung);
        nama_kampung.setText(inama_kampung);
        alamat_kampung.setText(ialamat_kampung);
        gambar.setImageUrl(igambar,imageLoader);

        updateMap(ilat);

        lihat_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id_kampung",id_kampung.getText().toString());
                toko t = new toko();
                t.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, t,"daftar_toko");
                ft.addToBackStack(param_nama_kampung);
                ft.commit();
            }
        });

        lihat_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id_kampung", id_kampung.getText().toString());
                dashboard d = new dashboard();
                d.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,d,"daftar_barang");
                ft.addToBackStack(param_nama_kampung);
                ft.commit();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(title);
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

                googleMap.addMarker(new MarkerOptions().position(latLng).title(inama_kampung));
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


}
