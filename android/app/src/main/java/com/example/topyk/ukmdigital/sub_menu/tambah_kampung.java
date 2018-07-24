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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.topyk.ukmdigital.variabel.url_create_kampung;

/**
 * Created by topyk on 8/16/2017.
 */

public class tambah_kampung extends Fragment {
    EditText nama, alamat, namaGambar;
    ImageView gambar;
    Button pilih, tambah, batal;
    int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String pnama, palamat, pnamaGambar;
    Bitmap bitmap;
    Uri filePath;
    JSONParser jParser = new JSONParser();
    String pet;
    CustomMapView mapView;
    private GoogleMap googleMap;
    String alamatMap;
    LatLng paramlatLng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.tambah_kampung, container, false);
        new reqStoragePermission(getActivity().getApplicationContext(),getActivity(),STORAGE_PERMISSION_CODE);

        mapView = (CustomMapView) root.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }

        nama = (EditText)root.findViewById(R.id.nama_kampung);
        alamat = (EditText)root.findViewById(R.id.alamat_kampung);
        namaGambar = (EditText)root.findViewById(R.id.namaGambar);
        gambar = (ImageView)root.findViewById(R.id.gambar);
        pilih = (Button)root.findViewById(R.id.butChoose);
        tambah = (Button)root.findViewById(R.id.but_tambah);
        batal = (Button)root.findViewById(R.id.butBatal);

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

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnama = nama.getText().toString();
                palamat = alamat.getText().toString();
                pnamaGambar = namaGambar.getText().toString();

                if (pnamaGambar.matches("")){
                    pnamaGambar = UUID.randomUUID().toString().substring(0,4);
                }
                if (bitmap == null){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "silahkan masukkan gambar", Toast.LENGTH_LONG).show();
                }
                else if (pnama.matches("") || palamat.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ada field yang masih kosong", Toast.LENGTH_LONG).show();
                }
                else {
                    new CreateKampung().execute();
                }
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

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
                LatLng latLngKamera = new LatLng(-0.494828, 117.143559); //posisi kamera ke smd
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLngKamera).zoom(15).build();
                try {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }catch (Exception e){
                    e.printStackTrace();
                    cameraPosition = new CameraPosition.Builder().target(latLngKamera).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
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
                                        paramlatLng = latLng;
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        googleMap.clear();
                                        paramlatLng = null;
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tambah Kampung");
    }

    private void showFileChooser(){
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(i, "Pilih Gambar"), PICK_IMAGE_REQUEST);
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
//            String ext = getPath(filePath);
//            ext = ext.substring(ext.lastIndexOf(".") + 1);
            compress c = new compress(getActivity().getApplicationContext());
            String ext = c.getPath(getActivity().getApplicationContext(),filePath);
            ext = ext.substring(ext.lastIndexOf(".") + 1);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                gambar.setImageBitmap(bitmap);
//                pet = c.rz(getRealPath(filePath),ext);
                pet = c.rz(c.getPath(getActivity().getApplicationContext(),filePath), ext);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri)
    {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null
        );
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
    public void onRequestPermissionsResult(int resultCode, @NonNull String[] permission, @NonNull int[] grantResults)
    {
        if (resultCode == STORAGE_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Permission granted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(),"Oops! dont deny please :)", Toast.LENGTH_LONG).show();
            }
        }
    }

    class CreateKampung extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;


        String uploadId = UUID.randomUUID().toString();
//        String path = getPath(filePath);
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            try {
                new MultipartUploadRequest(getActivity().getApplicationContext(),uploadId, url_create_kampung)
                        .addFileToUpload(pet,"image")
                        .addParameter("name",pnamaGambar)
                        .addParameter("nama_kampung", pnama)
                        .addParameter("alamat_kampung",palamat)
                        .addParameter("lat", Double.toString(paramlatLng.latitude))
                        .addParameter("lng", Double.toString(paramlatLng.longitude))
                        .setMaxRetries(2)
                        .startUpload();
                return "ok";
            }
            catch (Exception e){
                e.printStackTrace();
                return "exception";
            }
        }


        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(),"Terjadi masalah. Silahkan periksa koneksi anda!",
                        Toast.LENGTH_LONG).show();
            }
            else {

                ((MainActivity)getActivity()).reset();
//                kelola_kampung kk = new kelola_kampung();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame,kk,"kampung");
//                ft.commit();
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
