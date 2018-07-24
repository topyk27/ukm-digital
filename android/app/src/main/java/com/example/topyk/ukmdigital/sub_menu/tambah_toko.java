package com.example.topyk.ukmdigital.sub_menu;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.CustomMapView;
import com.example.topyk.ukmdigital.kelas.Anggota;
import com.example.topyk.ukmdigital.kelas.Kampung;
import com.example.topyk.ukmdigital.menu.dashboard;
import com.example.topyk.ukmdigital.menu.toko;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.example.topyk.ukmdigital.util.compress;
import com.example.topyk.ukmdigital.util.reqStoragePermission;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.topyk.ukmdigital.variabel.url_create_toko;
import static com.example.topyk.ukmdigital.variabel.url_read_anggota_gak_ada_toko;
import static com.example.topyk.ukmdigital.variabel.url_read_kampung;

/**
 * Created by topyk on 7/15/2017.
 */

public class tambah_toko extends Fragment {
    public static final String TAG_SUCCESS = "success";

    EditText edit_nama, edit_alamat, edit_telp, edit_deskripsi, editGambar;
    TextView textPemilikToko;
    Spinner spinnerAnggota;
    Button addBtn, chooseBtn, cancelBtn;
    ImageView gambar;
    String nama, alamat, telp, deskripsi, namaGambar, kampung, anggota;
    int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    Bitmap bitmap;
    Uri filePath;
    ArrayList<Kampung> kampungArrayList = new ArrayList<Kampung>();
    ArrayList<Anggota> anggotaArrayList = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarKampung = null;
    JSONArray daftarAnggota = null;
    SessionManager session;
    String pet;
    Spinner dropdown;

    CustomMapView mapView;
    private GoogleMap googleMap;
    List<String> nama_kampung = new ArrayList<>();
    List<Double> lat_kampung = new ArrayList<>();
    List<Double> lng_kampung = new ArrayList<>();
    LatLng latLng, latLngToko;
    Double lat,lng;
    boolean bikinmap = false;
    String alamatMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.tambah_toko, container, false);
        new reqStoragePermission(getActivity().getApplicationContext(), getActivity(),PICK_IMAGE_REQUEST);
        mapView = (CustomMapView) root.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }

        getKampungList();
        dropdown = (Spinner)root.findViewById(R.id.spinner);
        ArrayAdapter<Kampung> adapter = new ArrayAdapter<Kampung>(getActivity().getApplicationContext(),
                R.layout.spinner_item,kampungArrayList );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        if (bikinmap){

            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Log.d("kampungnya", selectedItem);
                    kampung = selectedItem;
                    for (int i = 0; i < nama_kampung.size(); i++){
                        if (kampung.equalsIgnoreCase(nama_kampung.get(i))){
                            lat = lat_kampung.get(i);
                            lng = lng_kampung.get(i);
                            latLng = new LatLng(lat,lng);

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
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
                                    try {

                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();
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
                                            builder.setTitle("Apakah ini lokasi UKM anda?")
                                                    .setMessage(alamatMap)
                                                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            latLngToko = latLng;
                                                        }
                                                    })
                                                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            googleMap.clear();
                                                            latLngToko = null;
                                                        }
                                                    })
                                                    .setCancelable(false)
                                                    .show();
                                        }
                                    });
                                }
                            });




                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        edit_nama = (EditText)root.findViewById(R.id.txt_nama_toko);
        edit_alamat = (EditText)root.findViewById(R.id.txt_alamat_toko);
        edit_telp = (EditText)root.findViewById(R.id.txt_telp);
        edit_deskripsi = (EditText)root.findViewById(R.id.txt_deskirpsi_toko);
        editGambar = (EditText)root.findViewById(R.id.txt_gambar);
        gambar = (ImageView)root.findViewById(R.id.gambar);
        addBtn = (Button)root.findViewById(R.id.butAdd);
        chooseBtn = (Button)root.findViewById(R.id.butChoose);
        cancelBtn = (Button)root.findViewById(R.id.butCancel);
        textPemilikToko = (TextView)root.findViewById(R.id.textPemilikToko);
        spinnerAnggota = (Spinner)root.findViewById(R.id.spinnerAnggota);
        session = new SessionManager(getActivity().getApplicationContext());

        edit_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_nama.getText().toString().startsWith(" ")){
                    edit_nama.setText(edit_nama.getText().toString().replaceFirst(" ",""));
                }
            }
        });
        edit_alamat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_alamat.getText().toString().startsWith(" ")){
                    edit_alamat.setText(edit_alamat.getText().toString().replaceFirst(" ",""));
                }
            }
        });
        edit_deskripsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_deskripsi.getText().toString().startsWith(" ")){
                    edit_deskripsi.setText(edit_deskripsi.getText().toString().replaceFirst(" ",""));
                }
                if (edit_deskripsi.getText().toString().startsWith("\n")){
                    edit_deskripsi.setText(edit_deskripsi.getText().toString().replaceFirst("\n",""));
                }
            }
        });

        if (session.isAdmin()){
            textPemilikToko.setVisibility(View.VISIBLE);
            spinnerAnggota.setVisibility(View.VISIBLE);
            AnggotaBelumPunyaToko();
            ArrayAdapter<Anggota> adapter1 = new ArrayAdapter<Anggota>(getActivity().getApplicationContext(),
                    R.layout.spinner_item,anggotaArrayList);
            spinnerAnggota.setAdapter(adapter1);
            spinnerAnggota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedAnggota = parent.getItemAtPosition(position).toString();
                    String split[];
                    split = selectedAnggota.split(" : ");
                    anggota = split[0];
                    Log.d("selectAnggota",anggota);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nama = edit_nama.getText().toString();
                alamat = edit_alamat.getText().toString();
                telp = edit_telp.getText().toString();
                deskripsi = edit_deskripsi.getText().toString();

                namaGambar = editGambar.getText().toString();
                if (namaGambar.matches("")){
                    namaGambar = UUID.randomUUID().toString().substring(0,4);
                }
                if (bitmap==null){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "silahkan masukkan gambar", Toast.LENGTH_LONG).show();
                }
                else if (nama.matches("") || alamat.matches("") || telp.matches("") || deskripsi.matches("") ||
                        kampung.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ada field yang masih kosong", Toast.LENGTH_LONG).show();
                }
                else if (latLngToko == null){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "silahkan beri tanda lokasi UKM anda di peta layar hp anda", Toast.LENGTH_LONG).show();
                }
                else {
                    new CreateToko().execute();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tambah UKM");
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



    class CreateToko extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog;


        String uploadId = UUID.randomUUID().toString();
//        String path = getPath(filePath);
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args)
        {
            try {
                String id_anggota;
                session = new SessionManager(getActivity().getApplicationContext());
                if (session.isAdmin()){
                    id_anggota = anggota;
                }
                else{
                    id_anggota = session.getUserDetails().get("id_anggota");
                    Log.d("id_anggota = ", id_anggota);
                }

                new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_create_toko)
                        .addFileToUpload(pet,"image")
                        .addParameter("name", namaGambar)
                        .addParameter("nama_toko", nama)
                        .addParameter("alamat_toko", alamat)
                        .addParameter("telp", telp)
                        .addParameter("deskripsi_toko", deskripsi)
                        .addParameter("id_kampung", kampung)
                        .addParameter("id_anggota", id_anggota)
//                        .addParameter("lat", String.valueOf(latLngToko.latitude))
//                        .addParameter("lng", String.valueOf(latLngToko.longitude))
                        .addParameter("lat", Double.toString(latLngToko.latitude))
                        .addParameter("lng", Double.toString(latLngToko.longitude))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "gagal";
            }
            return "sukses";
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if (result.equalsIgnoreCase("gagal"))
            {
                pDialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(),"Terjadi masalah. Silahkan periksa koneksi anda!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                pDialog.dismiss();
//                toko t = new toko();
//                FragmentTransaction ft = getFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, t, "toko");
//                ft.commit();
                Intent i = getActivity().getIntent();
                Bundle b = new Bundle();
                b.putInt("menudarifrag", R.id.toko_list);
                i.putExtras(b);
                getActivity().finish();
//            i.putExtra("menudarifrag", R.id.login);
                startActivity(i);
            }
        }
    }

    public String getKampungList(){
        Kampung k = new Kampung();

        List<NameValuePair> p = new ArrayList<NameValuePair>();
        try {
            JSONObject json = jParser.makeHttpRequest(url_read_kampung,"POST", p);
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1){
                daftarKampung = json.getJSONArray("kampung");
                for (int i = 0; i < daftarKampung.length(); i++){
                    JSONObject c = daftarKampung.getJSONObject(i);
                    k = new Kampung();
                    k.setId_kampung(c.getString("id_kampung"));

                    k.setNama_kampung(c.getString("nama_kampung"));

                    k.setAlamat_kampung(c.getString("alamat_kampung"));
                    k.setGambar_kampung(c.getString("gambar"));

                    kampungArrayList.add(k);
                    nama_kampung.add(c.getString("nama_kampung"));
                    lat_kampung.add(c.getDouble("lat"));
                    lng_kampung.add(c.getDouble("lng"));

                }
                bikinmap = true;

                return "ok";

            }
            else {
                return "no result";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Exception Caught";
        }
    }

    public String AnggotaBelumPunyaToko(){
        Anggota a = new Anggota();
        List<NameValuePair> p = new ArrayList<>();
        try {
            JSONObject json = jParser.makeHttpRequest(url_read_anggota_gak_ada_toko, "POST", p);
            int success = json.getInt("success");
            if (success == 1){
                daftarAnggota = json.getJSONArray("anggota");
                for (int i = 0; i < daftarAnggota.length(); i++){
                    JSONObject c = daftarAnggota.getJSONObject(i);
                    a = new Anggota();
                    a.setId(c.getString("id_anggota"));
                    a.setNama(c.getString("nama"));
                    anggotaArrayList.add(a);
                }
                return "ok";
            }
            else{
                return "kosong";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "exception";
        }
    }

}
