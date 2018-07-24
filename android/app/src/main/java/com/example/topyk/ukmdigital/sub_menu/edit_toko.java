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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.app.CustomMapView;
import com.example.topyk.ukmdigital.kelas.Kampung;
import com.example.topyk.ukmdigital.menu.dashboard;
import com.example.topyk.ukmdigital.menu.toko;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.topyk.ukmdigital.variabel.url_delete_toko;
import static com.example.topyk.ukmdigital.variabel.url_read_kampung;
import static com.example.topyk.ukmdigital.variabel.url_update_toko;

/**
 * Created by topyk on 7/16/2017.
 */

public class edit_toko extends Fragment {
    JSONParser jParser = new JSONParser();
    JSONArray daftarKampung = null;
    JSONArray kampungTerpilih = null;
    ArrayList<Kampung> kampungArrayList = new ArrayList<Kampung>();

    TextView txid_toko, txid_anggota, txid_kampung;
    EditText edit_nama, edit_alamat, edit_telp, edit_deskripsi, editGambar;
    Button update, delete, cancel, choose;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Uri filepath;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String selectedKampung;
    String id_toko, nama, alamat, telp, deskripsi, namaGambar, kampung, anggota;
    String asalFrag,pet;
    CustomMapView mapView;
    private GoogleMap googleMap;
    List<String> nama_kampung = new ArrayList<>();
    List<Double> lat_kampung = new ArrayList<>();
    List<Double> lng_kampung = new ArrayList<>();
    Boolean bikinmap = false;
    Double lat,lng;
    LatLng latLngDiAdapter, latLngToko;
    String alamatMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.edit_toko, container, false);
        new reqStoragePermission(getActivity().getApplicationContext(), getActivity(),PICK_IMAGE_REQUEST);
        mapView = (CustomMapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }
        getKampungList();
        Spinner dropdown = (Spinner)rootView.findViewById(R.id.spinner);
        ArrayAdapter<Kampung> adapter = new ArrayAdapter<Kampung>(getActivity().getApplicationContext(),
                R.layout.spinner_item, kampungArrayList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        String iid_toko = getArguments().getString("id_toko");
        final String inama_toko = getArguments().getString("nama_toko");
        String ialamat_toko = getArguments().getString("alamat_toko");
        String itelp = getArguments().getString("telp");
        String ideskripsi_toko = getArguments().getString("deskripsi_toko");
        String iid_kampung = getArguments().getString("id_kampung");
        String iid_anggota = getArguments().getString("id_anggota");
        String igambar = getArguments().getString("gambar");
        asalFrag = getArguments().getString("menudarifrag");
        final LatLng latLngx = getArguments().getParcelable("latlng");
        latLngToko = latLngx;
        if (bikinmap){

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.d("itemnya", selectedItem);
                kampung = selectedItem;
                for (int i = 0; i < nama_kampung.size(); i++){
                    if (kampung.equalsIgnoreCase(nama_kampung.get(i))){
                        lat = lat_kampung.get(i);
                        lng = lng_kampung.get(i);
                        latLngDiAdapter = new LatLng(lat,lng);
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
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLngDiAdapter).zoom(20).build();
                                try {

                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                }catch (Exception e){
                                    e.printStackTrace();
                                    cameraPosition = new CameraPosition.Builder().target(latLngDiAdapter).zoom(20).build();
                                    Log.d("kamera",String.valueOf(cameraPosition));
                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                }
                                googleMap.addMarker(new MarkerOptions().position(latLngx).title(inama_toko));
                                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                    @Override
                                    public void onMapLoaded() {
                                        pDialog.dismiss();
                                    }
                                });
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(final LatLng latLng) {
                                        ProgressDialog pd = new ProgressDialog(getActivity());
                                        pd.setMessage("Mohong tunggu...");
                                        pd.setIndeterminate(false);
                                        pd.setCancelable(false);
                                        pd.show();
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
                                            pd.dismiss();
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
                                                        latLngToko = latLngx;
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


        txid_toko = (TextView)rootView.findViewById(R.id.id_toko);
        edit_nama = (EditText)rootView.findViewById(R.id.txt_nama_toko);
        edit_alamat = (EditText)rootView.findViewById(R.id.txt_alamat_toko);
        edit_telp = (EditText)rootView.findViewById(R.id.txt_telp);
        edit_deskripsi = (EditText)rootView.findViewById(R.id.txt_deskirpsi_toko);
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar);
        editGambar = (EditText)rootView.findViewById(R.id.txt_gambar);
        txid_anggota = (TextView)rootView.findViewById(R.id.id_anggota);
        txid_kampung = (TextView)rootView.findViewById(R.id.id_kampung);

        choose = (Button)rootView.findViewById(R.id.butChoose);
        update = (Button)rootView.findViewById(R.id.butEdit);
        delete = (Button)rootView.findViewById(R.id.butDelete);
        cancel = (Button)rootView.findViewById(R.id.butCancel);



        txid_toko.setText(iid_toko);
        edit_nama.setText(inama_toko);
        edit_alamat.setText(ialamat_toko);
        edit_telp.setText(itelp);
        edit_deskripsi.setText(ideskripsi_toko);
        gambar.setImageUrl(igambar,imageLoader);
        txid_anggota.setText(iid_anggota);
        txid_kampung.setText(iid_kampung);

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

        getNamaKampung();
        Log.d("getNamaKampung", getNamaKampung());
        dropdown.setSelection(getIndex(dropdown,selectedKampung));

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_toko = txid_toko.getText().toString();
                nama = edit_nama.getText().toString();
                alamat = edit_alamat.getText().toString();
                telp = edit_telp.getText().toString();
                deskripsi = edit_deskripsi.getText().toString();
                namaGambar = editGambar.getText().toString();
                if (namaGambar.matches("")){

                    namaGambar = UUID.randomUUID().toString().substring(0,4);
                }
                if (nama.matches("") || alamat.matches("") || telp.matches("") || deskripsi.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ada field yang masih kosong",Toast.LENGTH_LONG).show();
                }

                else if (bitmap == null){
                    new UpdateToko().execute();
                }
                else {
                    uploadMultipart();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_toko = txid_toko.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Anda yakin akan menghapus UKM " + inama_toko)
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

        cancel.setOnClickListener(new View.OnClickListener() {
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
        getActivity().setTitle("Ubah UKM");
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

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equals(myString)){
                Log.d("spinnernya", spinner.getItemAtPosition(i).toString());
                index = i;
            }

        }
        return index;
    }

    public String getKampungList(){
        Kampung k = new Kampung();
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        try {
            JSONObject json = jParser.makeHttpRequest(url_read_kampung,"POST", p);
            int success = json.getInt("success");
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

    public String getNamaKampung(){
        Kampung k  = new Kampung();
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        p.add(new BasicNameValuePair("id_kampung", txid_kampung.getText().toString()));
        try {
            JSONObject js = jParser.makeHttpRequest(url_read_kampung,"POST", p);
            int success = js.getInt("success");
            if (success == 1){
                kampungTerpilih = js.getJSONArray("nama_kampung");
                for (int i = 0; i<kampungTerpilih.length(); i++){
                    JSONObject d = kampungTerpilih.getJSONObject(i);
                    k.setNama_kampung_sementara(d.getString("nama_kampung"));
                    selectedKampung = k.getNama_kampung_sementara();
                    Log.d("namakampung", selectedKampung);
                }
                return "ok";
            }
            else {
                return "no result";
            }
        }
        catch (Exception e){
            return "Exception";
        }
    }

    public void uploadMultipart(){
//        String namaGambar = editGambar.getText().toString().trim();

        try {
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_update_toko )
                    .addFileToUpload(pet,"image")
                    .addParameter("name",namaGambar)
                    .addParameter("id_toko", id_toko)
                    .addParameter("nama_toko", nama)
                    .addParameter("alamat_toko", alamat)
                    .addParameter("telp", telp)
                    .addParameter("deskripsi_toko", deskripsi)
                    .addParameter("id_kampung", kampung)
                    .addParameter("lat", Double.toString(latLngToko.latitude))
                    .addParameter("lng", Double.toString(latLngToko.longitude))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();


            Intent i = getActivity().getIntent();
            Bundle b = new Bundle();

            if (asalFrag != null && asalFrag.equalsIgnoreCase("tokoku")){
                b.putInt("menudarifrag", R.id.tokoku);
            }
            else {
                b.putInt("menudarifrag", R.id.toko);
            }
            i.putExtras(b);
            getActivity().finish();
            startActivity(i);
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    class UpdateToko extends AsyncTask<String, Void, String>{
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
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("id_toko", id_toko));
            p.add(new BasicNameValuePair("nama_toko", nama));
            p.add(new BasicNameValuePair("alamat_toko", alamat));
            p.add(new BasicNameValuePair("telp", telp));
            p.add(new BasicNameValuePair("deskripsi_toko", deskripsi));
            p.add(new BasicNameValuePair("id_kampung", kampung));
            p.add(new BasicNameValuePair("lat",Double.toString(latLngToko.latitude)));
            p.add(new BasicNameValuePair("lng",Double.toString(latLngToko.longitude)));

            try {
                JSONObject json = jParser.makeHttpRequest(url_update_toko,"POST", p);
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
            if (result.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to server,please check your internet connection!",
                        Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("FAIL"))
            {
                Toast.makeText(getActivity().getApplicationContext(), "Fail.. Try Again", Toast.LENGTH_LONG).show();
            }
            else {

                if (asalFrag != null && asalFrag.equalsIgnoreCase("tokoku")){
                    toko_ku t = new toko_ku();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, t, "tokoku");
                    fragmentTransaction.commit();
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

    class DeleteToko extends AsyncTask<String, Void, String>{
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
            parameter.add(new BasicNameValuePair("id_toko", id_toko));
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
                fragmentTransaction.replace(R.id.content_frame, t, "toko");
                fragmentTransaction.commit();
            }
        }
    }
}
