package com.example.topyk.ukmdigital.sub_menu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.BarangAdapter;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.kelas.Jenis_Barang;
import com.example.topyk.ukmdigital.menu.dashboard;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.compress;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.topyk.ukmdigital.menu.dashboard.TAG_HARGA;
import static com.example.topyk.ukmdigital.menu.dashboard.TAG_SUCCESS;
import static com.example.topyk.ukmdigital.sub_menu.tambah_barang.TAG_ID_JENIS_Barang;
import static com.example.topyk.ukmdigital.sub_menu.tambah_barang.TAG_JENIS_BARANG;
import static com.example.topyk.ukmdigital.variabel.url_delete_barang;
import static com.example.topyk.ukmdigital.variabel.url_read_jenis_barang;
import static com.example.topyk.ukmdigital.variabel.url_update_barang;

/**
 * Created by topyk on 7/5/2017.
 */

public class edit_barang extends Fragment{
    JSONParser jParser = new JSONParser();
    JSONArray daftarJenis = null;
    JSONArray namaJenis = null;
    ArrayList<Jenis_Barang> jenis_barangArrayList = new ArrayList<Jenis_Barang>();
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_ID_BARANG = "id_barang";
    public static final String TAG_NAMA_BARANG = "nama_barang";
    public static final String TAG_HARGA = "harga";
    public static final String TAG_STOK = "stok";
    public static final String TAG_DESKRIPSI_BARANG = "deskripsi_barang";

    EditText editNama, editHarga, editStok, editDeskripsi, editGambar, editUkuran, editMerk;
    TextView txtId_barang, txtId_jenis, txtId_toko;

    Button UpdateBtn, DeleteBtn, ChooseBtn, CancelBtn;
    String id_barang, nama_barang, harga, stok, deskripsi_barang, id_jenis_barang, namaGambar, ukuran, merk, khusus;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Uri filepath;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String selectedNama, isi_asalFrag;
    CheckBox cb_laki, cb_perem;


    String pet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.edit_barang, container, false);
        requestStoragePermission();
        getJenis_BarangList();

        Spinner dropdown = (Spinner)rootView.findViewById(R.id.spinner);
        ArrayAdapter<Jenis_Barang> adapter = new ArrayAdapter<Jenis_Barang>(getActivity().getApplicationContext(),
                R.layout.spinner_item, jenis_barangArrayList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.d("itemnya", selectedItem);
                id_jenis_barang = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtId_barang = (TextView)rootView .findViewById(R.id.id_barang);
        txtId_jenis = (TextView)rootView.findViewById(R.id.id_jenis_barang);
        txtId_toko = (TextView)rootView.findViewById(R.id.id_toko);
        editNama = (EditText)rootView.findViewById(R.id.editNamaBarang);
        editHarga = (EditText)rootView.findViewById(R.id.editHarga);
        editStok = (EditText)rootView.findViewById(R.id.editStok);
        editDeskripsi = (EditText)rootView.findViewById(R.id.editDeskripsi);
        gambar = (NetworkImageView)rootView.findViewById(R.id.imageView);
        editGambar = (EditText)rootView.findViewById(R.id.editTextName);
        editUkuran = (EditText)rootView.findViewById(R.id.ukuran);
        editMerk = (EditText)rootView.findViewById(R.id.merk);
        cb_laki = (CheckBox)rootView.findViewById(R.id.cb_laki);
        cb_perem = (CheckBox)rootView.findViewById(R.id.cb_perem);

        ChooseBtn = (Button) rootView.findViewById(R.id.buttonChoose);
        UpdateBtn = (Button) rootView.findViewById(R.id.buttonUpdate);
        DeleteBtn = (Button) rootView.findViewById(R.id.buttonDelete);
        CancelBtn = (Button) rootView.findViewById(R.id.buttonCancel);

        String isi_id_barang = getArguments().getString("id_barang");
        final String isi_nama_barang = getArguments().getString("nama_barang");
        String isi_harga = getArguments().getString("harga");
        String isi_stok = getArguments().getString("stok");
        String isi_deskripsi_barang = getArguments().getString("deskripsi_barang");
        String isi_gambar = getArguments().getString("gambar");
        String isi_id_jenis_barang = getArguments().getString("id_jenis_barang");
        String isi_id_toko = getArguments().getString("id_toko");
        String isi_ukuran = getArguments().getString("ukuran");
        String isi_merk = getArguments().getString("merk");
        String isi_khusus = getArguments().getString("khusus");
        isi_asalFrag = getArguments().getString("asalFrag");

        txtId_barang.setText(isi_id_barang);
        txtId_jenis.setText(isi_id_jenis_barang);
        Log.d("sempakqu", txtId_jenis.getText().toString());
        txtId_toko.setText(isi_id_toko);
        editNama.setText(isi_nama_barang);
        editHarga.setText(isi_harga);
        editStok.setText(isi_stok);
        editDeskripsi.setText(isi_deskripsi_barang);
        gambar.setImageUrl(isi_gambar, imageLoader);
        editUkuran.setText(isi_ukuran);
        editMerk.setText(isi_merk);
        Log.d("isikhusus",isi_khusus);
        if (isi_khusus != null){
            switch (isi_khusus){
                case "Laki-Laki" :
                    cb_laki.setChecked(true);
                    cb_perem.setChecked(false);
                    break;
                case "Perempuan" :
                    cb_laki.setChecked(false);
                    cb_perem.setChecked(true);
                    break;
                case "Laki-Laki dan Perempuan" :
                    cb_laki.setChecked(true);
                    cb_perem.setChecked(true);
                    break;
                default:
                    cb_laki.setChecked(false);
                    cb_perem.setChecked(false);
            }
        }

        getNamaJenisYangDiPilih();
        dropdown.setSelection(getIndex(dropdown,selectedNama));
                Log.d("sempaknya1", selectedNama);
        editNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editNama.getText().toString().startsWith(" ")){
                    editNama.setText(editNama.getText().toString().replaceFirst(" ",""));
                }
            }
        });
        editHarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editHarga.getText().toString().startsWith("0")){
                    editHarga.setText(editHarga.getText().toString().replaceFirst("0",""));
                }
            }
        });
        editStok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editStok.getText().toString().startsWith("0")){
                    editStok.setText(editStok.getText().toString().replaceFirst("0",""));
                }
            }
        });
        editDeskripsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editDeskripsi.getText().toString().startsWith(" ")){
                    editDeskripsi.setText(editDeskripsi.getText().toString().replaceFirst(" ",""));
                }
                if (editDeskripsi.getText().toString().startsWith("\n")){
                    editDeskripsi.setText(editDeskripsi.getText().toString().replaceFirst("\n",""));
                }
            }
        });

        ChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_barang = txtId_barang.getText().toString();
                nama_barang = editNama.getText().toString();
                harga = editHarga.getText().toString();
                stok = editStok.getText().toString();
                deskripsi_barang = editDeskripsi.getText().toString();
                namaGambar = editGambar.getText().toString();
                merk = editMerk.getText().toString();
                ukuran = editUkuran.getText().toString();

                if (namaGambar.matches("")){
                    namaGambar = UUID.randomUUID().toString().substring(0,4);
                }

                if (cb_laki.isChecked() && !cb_perem.isChecked()){
                    khusus = "Laki-Laki";
                }
                else if (!cb_laki.isChecked() && cb_perem.isChecked()){
                    khusus = "Perempuan";
                }
                else if (cb_laki.isChecked() && cb_perem.isChecked()){
                    khusus = "Laki-Laki dan Perempuan";
                }
                else {
                    khusus = "-";
                }
                if (merk.matches("")){
                    merk = "-";
                }

                if (nama_barang.matches("") || harga.matches("") || stok.matches("") || deskripsi_barang.matches("")
                        || ukuran.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ada field yang masih kosong",Toast.LENGTH_LONG).show();
                }
//                id_toko = txtId_toko.getText().toString();
                else if (bitmap == null){
                    new UpdateBarang().execute();
                }
                else {
                    uploadMultipart();

                }

            }
        });

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_barang = txtId_barang.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Anda yakin akan menghapus " + isi_nama_barang)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteBarang().execute();
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

        CancelBtn.setOnClickListener(new View.OnClickListener() {
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

        getActivity().setTitle("Ubah Barang");
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

    public void uploadMultipart(){

        String jenis_barang = id_jenis_barang;
//bikin temporary file habis itu dipilih ke filepath
        try {
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_update_barang )
                    .addFileToUpload(pet,"image")
                    .addParameter("name", namaGambar)
                    .addParameter("nama_barang", nama_barang)
                    .addParameter("ukuran", ukuran)
                    .addParameter("merk", merk)
                    .addParameter("khusus", khusus)
                    .addParameter("harga", harga)
                    .addParameter("stok", stok)
                    .addParameter("deskripsi_barang", deskripsi_barang)
                    .addParameter("id_jenis_barang", jenis_barang)
                    .addParameter("id_barang", id_barang)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
            Intent i = getActivity().getIntent();
            Bundle b = new Bundle();
            if (isi_asalFrag != null && isi_asalFrag.equalsIgnoreCase("tokoku")){
                b.putInt("menudarifrag", R.id.tokoku);

            }
            if (isi_asalFrag != null && isi_asalFrag.equalsIgnoreCase("detail_toko")){
                b.putInt("menudarifrag", R.id.toko_list);
            }

            else {
                b.putInt("menudarifrag", R.id.dashboard);

            }
            i.putExtras(b);


            getActivity().finish();
            startActivity(i);

        }
        catch (Exception exc){
            Toast.makeText(getActivity().getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();

        }
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
                gambar.setImageBitmap(bitmap);
//                pet = c.rz(getRealPath(filepath), ext);
                pet = c.rz(c.getPath(getActivity().getApplicationContext(),filepath), ext);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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

    public String getJenis_BarangList(){
        Jenis_Barang tmpJenis = new Jenis_Barang();
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();

        try {
            JSONObject json = jParser.makeHttpRequest(url_read_jenis_barang, "POST", parameter);

            int success = json.getInt(TAG_SUCCESS);
            if (success == 1){
                daftarJenis = json.getJSONArray(TAG_JENIS_BARANG);
                for (int i = 0; i < daftarJenis.length(); i++){
                    JSONObject c = daftarJenis.getJSONObject(i);
                    tmpJenis = new Jenis_Barang();
                    tmpJenis.setId_jenis_barang(c.getString(TAG_ID_JENIS_Barang));
                    tmpJenis.setJenis_barang(c.getString(TAG_JENIS_BARANG));
                    jenis_barangArrayList.add(tmpJenis);

                }
                return "ok";
            }
            else {
                return "no result";
            }
        } catch (Exception e){
            e.printStackTrace();
            return "exception caught";
        }

    }

    public String getNamaJenisYangDiPilih(){
        Jenis_Barang tmpJenis = new Jenis_Barang();
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        p.add(new BasicNameValuePair(TAG_ID_JENIS_Barang,txtId_jenis.getText().toString()));

        try {
            JSONObject js = jParser.makeHttpRequest(url_read_jenis_barang,"POST", p);
            int success = js.getInt(TAG_SUCCESS);
            if (success == 1){
                namaJenis = js.getJSONArray("nama_jenis");
                for (int i = 0; i< namaJenis.length(); i++){
                    JSONObject d = namaJenis.getJSONObject(i);
                    tmpJenis.setNama_jenis_barang_sementara(d.getString("jenis_barang"));
                    selectedNama = tmpJenis.getNama_jenis_barang_sementara();
//                    selectedNama = d.getString("jenis_barang");
                    Log.d("asdf", selectedNama);
//                    namajenisbanyak[i] = d.getString("jenis_barang");
//                    Log.d("asdf", namajenisbanyak[0]);


                }
                return "ok";
            }
            else {
                return "no result";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Exception";
        }
    }

    class UpdateBarang extends AsyncTask<String, Void, String>{
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
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(TAG_ID_BARANG, id_barang));
            parameter.add(new BasicNameValuePair(TAG_NAMA_BARANG, nama_barang));
            parameter.add(new BasicNameValuePair("ukuran",ukuran));
            parameter.add(new BasicNameValuePair("merk",merk));
            parameter.add(new BasicNameValuePair("khusus",khusus));
            parameter.add(new BasicNameValuePair(TAG_HARGA, harga));
            parameter.add(new BasicNameValuePair(TAG_STOK, stok));
            parameter.add(new BasicNameValuePair(TAG_DESKRIPSI_BARANG, deskripsi_barang));
            parameter.add(new BasicNameValuePair(TAG_ID_JENIS_Barang, id_jenis_barang));

            try {
                JSONObject json = jParser.makeHttpRequest(url_update_barang,"POST", parameter);
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1){
                    return "OK";
                }
                else {
                    return "FAIL";
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
                if (isi_asalFrag != null && isi_asalFrag.equalsIgnoreCase("tokoku")){
                    toko_ku tk = new toko_ku();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, tk, "tokoku");
                    ft.commit();
                }
                if (isi_asalFrag != null && isi_asalFrag.equalsIgnoreCase("detail_toko")){
                    getFragmentManager().popBackStack();
                }
                else {
                    dashboard d = new dashboard();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, d, "dashboard");
                    fragmentTransaction.commit();
                }

            }
        }
    }



    class DeleteBarang extends AsyncTask<String, Void, String>{
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
        protected String doInBackground(String... sText) {

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(TAG_ID_BARANG, id_barang));

            try {
                JSONObject json = jParser.makeHttpRequest(url_delete_barang,"POST", parameter);

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    return "OK";
                }
                else {

                    return "FAIL";

                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }

        }

        @Override
        protected void onPostExecute(String result) {

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
                if (isi_asalFrag != null && isi_asalFrag.equalsIgnoreCase("tokoku")){
                    toko_ku tk = new toko_ku();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, tk, "tokoku");
                    ft.commit();
                }
                else {
                    dashboard d = new dashboard();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, d, "dashboard");
                    fragmentTransaction.commit();
                }

            }

        }
    }


}
