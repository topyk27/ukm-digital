package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.app.CustomMapView;
import com.example.topyk.ukmdigital.menu.kelola_kampung;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.compress;
import com.example.topyk.ukmdigital.util.reqStoragePermission;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.topyk.ukmdigital.variabel.url_delete_kampung;
import static com.example.topyk.ukmdigital.variabel.url_update_anggota;
import static com.example.topyk.ukmdigital.variabel.url_update_kampung;

/**
 * Created by topyk on 8/15/2017.
 */

public class edit_kampung extends Fragment {
    EditText nama, alamat,namaGambar;
    Button pilih,ubah,hapus,batal;
    TextView id;
    NetworkImageView gambar;
    String pid, pnama, palamat, pnamaGambar;
    JSONParser jParser = new JSONParser();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Uri filepath;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String pet;
    CustomMapView mapView;
    private GoogleMap googleMap;
    LatLng ilatlng, paramlatlng;
    CameraPosition cameraPosition;
    String inama;
    String alamatMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.edit_kampung, container, false);
        new reqStoragePermission(getActivity().getApplicationContext(),getActivity(),STORAGE_PERMISSION_CODE);

        mapView = (CustomMapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }

        id = (TextView)rootView.findViewById(R.id.id_kampung);
        nama = (EditText)rootView.findViewById(R.id.nama_kampung);
        alamat = (EditText)rootView.findViewById(R.id.alamat_kampung);
        namaGambar = (EditText)rootView.findViewById(R.id.namaGambar);
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar);
        pilih = (Button)rootView.findViewById(R.id.butChoose);
        ubah = (Button)rootView.findViewById(R.id.butUbah);
        hapus = (Button)rootView.findViewById(R.id.butHapus);
        batal = (Button)rootView.findViewById(R.id.butBatal);

        String iid = getArguments().getString("id_kampung");
        inama = getArguments().getString("nama");
        String ialamat = getArguments().getString("alamat");
        String igambar = getArguments().getString("gambar");
        ilatlng = getArguments().getParcelable("latlng");

        id.setText(iid);
        nama.setText(inama);
        alamat.setText(ialamat);
        gambar.setImageUrl(igambar,imageLoader);
        updateMap(ilatlng);

        nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nama.getText().toString().startsWith(" ")){
                    nama.setText(nama.getText().toString().replaceFirst(" ",""));
                }
            }
        });
        alamat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (alamat.getText().toString().startsWith(" ")){
                    alamat.setText(alamat.getText().toString().replaceFirst(" ",""));
                }
            }
        });

        pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid = id.getText().toString();
                pnama = nama.getText().toString();
                palamat = alamat.getText().toString();
                pnamaGambar = namaGambar.getText().toString();

                if (pnamaGambar.matches("")){
                    pnamaGambar = UUID.randomUUID().toString().substring(0,4);
                }

                if (pnama.matches("") || palamat.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ada field yang masih kosong",Toast.LENGTH_LONG).show();
                }
                else if (paramlatlng == null){
                    paramlatlng = ilatlng;
                }
                else {
                    new UbahKampung().execute();
                }
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid = id.getText().toString();
                pnama = nama.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Anda yakin akan menghapus kampung " + pnama)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new HapusKampung().execute();
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

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });



        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Ubah Kampung");
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
                cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
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

                googleMap.addMarker(new MarkerOptions().position(latLng).title(inama));
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
//                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        pDialog.dismiss();
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(final LatLng latLng) {
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng));
                        try {
                            Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if (addresses.isEmpty()){
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Mohon tunggu . .", Toast.LENGTH_LONG).show();;
                            }
                            else {
                                if (addresses.size() > 0){
                                    alamatMap = addresses.get(0).getAddressLine(0);
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Apakah ini lokasi kampung UKM?")
                                .setMessage(alamatMap)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        paramlatlng = latLng;
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        googleMap.clear();
                                        paramlatlng = ilatlng;
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                });
            }
        });

    }

    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
//            String ext = getPath(filepath);
//            ext = ext.substring(ext.lastIndexOf(".") + 1);
            compress c = new compress(getActivity().getApplicationContext());
            String ext = c.getPath(getActivity().getApplicationContext(),filepath);
            ext = ext.substring(ext.lastIndexOf(".") + 1);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
//                imageView.setImageBitmap(bitmap);
                gambar.setImageBitmap(bitmap);
//                pet = c.rz(getRealPath(filepath),ext);
                pet = c.rz(c.getPath(getActivity().getApplicationContext(),filepath), ext);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public String getRealPath(Uri contentUri){
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity().getApplicationContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity().getApplicationContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    class UbahKampung extends AsyncTask<String, Void, String>{
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

            if (bitmap == null){
                List<NameValuePair> p = new ArrayList<>();
                p.add(new BasicNameValuePair("id_kampung",pid));
                p.add(new BasicNameValuePair("nama_kampung", pnama));
                p.add(new BasicNameValuePair("alamat_kampung", palamat));
                p.add(new BasicNameValuePair("lat", Double.toString(paramlatlng.latitude)));
                p.add(new BasicNameValuePair("lng", Double.toString(paramlatlng.longitude)));

                try {
                    JSONObject json = jParser.makeHttpRequest(url_update_kampung,"POST", p);
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
            else {
//                String path = getPath(filepath);
                String uploadId = UUID.randomUUID().toString();
                try {
                    new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_update_kampung )
                            .addFileToUpload(pet,"image")
                            .addParameter("name",pnamaGambar)
                            .addParameter("id_kampung",pid)
                            .addParameter("nama_kampung",pnama)
                            .addParameter("alamat_kampung",palamat)
                            .addParameter("lat", Double.toString(paramlatlng.latitude))
                            .addParameter("lng", Double.toString(paramlatlng.longitude))
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
                    return "okGambar";
                }
                catch (Exception e){
                    e.printStackTrace();
                    return "exception";
                }
            }
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to server,please check your internet connection!",
                        Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Data kosong", Toast.LENGTH_LONG).show();
            }

            else {
                ((MainActivity)getActivity()).reset();
                Intent i = getActivity().getIntent();
                Bundle b = new Bundle();
                b.putInt("menudarifrag", R.id.kampung);
                i.putExtras(b);
                getActivity().finish();
                startActivity(i);
            }
        }
    }

    class HapusKampung extends AsyncTask<String,Void,String>{
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
            p.add(new BasicNameValuePair("id_kampung",pid));
            try {
                JSONObject json = jParser.makeHttpRequest(url_delete_kampung,"POST",p);
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
                        "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Data kosong", Toast.LENGTH_LONG).show();
            }
            else {
                ((MainActivity)getActivity()).reset();
                Intent i = getActivity().getIntent();
                Bundle b = new Bundle();
                b.putInt("menudarifrag", R.id.kampung);
                i.putExtras(b);
                getActivity().finish();
                startActivity(i);
            }
        }
    }
}
