package com.example.topyk.ukmdigital.sub_menu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Jenis_Barang;
import com.example.topyk.ukmdigital.kelas.Toko;
import com.example.topyk.ukmdigital.menu.dashboard;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.example.topyk.ukmdigital.util.compress;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.topyk.ukmdigital.menu.dashboard.TAG_SUCCESS;
import static com.example.topyk.ukmdigital.variabel.url_create_barang;
import static com.example.topyk.ukmdigital.variabel.url_read_jenis_barang;
import static com.example.topyk.ukmdigital.variabel.url_read_toko;

/**
 * Created by topyk on 6/7/2017.
 */

public class tambah_barang extends Fragment {
    public static final String TAG_JENIS_BARANG = "jenis_barang";
    public static final String TAG_ID_JENIS_Barang = "id_jenis_barang";
    SessionManager session;
    EditText editNama_Barang, editHarga, editStok, editDeskripsi, editGambar, editUkuran, editMerk;
    Button addBtn, chooseBtn, cancelBtn;
    ImageView gambar;
    String nama, harga, stok, deskripsi, namagambar, jenis_barang, id_toko, bundle_id_toko, ukuran, merk, khusus;
    int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    Bitmap bitmap;
    Uri filePath;
    ArrayList<Jenis_Barang> jenis_barangArrayList = new ArrayList<Jenis_Barang>();
    ArrayList<Toko> tokoArrayList = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarJenis = null;
    JSONArray daftarToko = null;
    Spinner spinnerToko;
    TextView textPemilikBarang;
    String pet;
    CheckBox cb_laki, cb_perem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.tambah_barang, container, false);


        requestStoragePermission();
        getJenis_BarangList();
        Spinner dropdown = (Spinner)root.findViewById(R.id.spinner);
        ArrayAdapter<Jenis_Barang> adapter = new ArrayAdapter<Jenis_Barang>(getActivity().getApplicationContext(),
                R.layout.spinner_item, jenis_barangArrayList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.d("itemnya", selectedItem);
                jenis_barang = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editNama_Barang = (EditText)root.findViewById(R.id.txtNama_Barang);
        editHarga = (EditText)root.findViewById(R.id.txtHarga);
        editStok = (EditText)root.findViewById(R.id.txtStok);
        editDeskripsi = (EditText)root.findViewById(R.id.txtDeskripsi_Barang);
        editGambar = (EditText)root.findViewById(R.id.txtGambar);
        gambar = (ImageView)root.findViewById(R.id.gambar_tambah_barang);
//        editJenis_Barang= (EditText)root.findViewById(R.id.txtJenis_barang);
        addBtn = (Button)root.findViewById(R.id.butAdd);
        chooseBtn = (Button)root.findViewById(R.id.butChoose);
        cancelBtn = (Button)root.findViewById(R.id.butCancel);
        spinnerToko = (Spinner)root.findViewById(R.id.spinnerToko);
        textPemilikBarang = (TextView)root.findViewById(R.id.textPemilikBarang);
        editUkuran = (EditText)root.findViewById(R.id.ukuran);
        editMerk = (EditText)root.findViewById(R.id.merk);
        cb_laki = (CheckBox)root.findViewById(R.id.cb_laki);
        cb_perem = (CheckBox)root.findViewById(R.id.cb_perem);


        Bundle args = getArguments();
        if (args != null){
            bundle_id_toko = getArguments().getString("id_toko");
        }


        session = new SessionManager(getActivity().getApplicationContext());
        if (session.isAdmin() && bundle_id_toko == null){
            textPemilikBarang.setVisibility(View.VISIBLE);
            spinnerToko.setVisibility(View.VISIBLE);
            getTokoList();
            ArrayAdapter<Toko> arrayAdapter = new ArrayAdapter<Toko>(getActivity().getApplicationContext(),
                    R.layout.spinner_item,tokoArrayList);
            spinnerToko.setAdapter(arrayAdapter);
            spinnerToko.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedToko = parent.getItemAtPosition(position).toString();
                    Log.d("selTok",selectedToko);
                    String[] spelit;
                    spelit = selectedToko.split(" : ");

                    id_toko = spelit[0];
                    Log.d("sepelit",id_toko);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if (session.isAdmin() && bundle_id_toko != null){
            id_toko = bundle_id_toko;
        }

        editNama_Barang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editNama_Barang.getText().toString().startsWith(" ")){
                    editNama_Barang.setText(editNama_Barang.getText().toString().replaceFirst(" ",""));
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


        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = editNama_Barang.getText().toString();
                harga = editHarga.getText().toString();
                stok = editStok.getText().toString();
                deskripsi = editDeskripsi.getText().toString();
                namagambar = editGambar.getText().toString();
                ukuran = editUkuran.getText().toString();
                merk = editMerk.getText().toString();
                if (namagambar .matches("")){
                    namagambar = UUID.randomUUID().toString().substring(0,4);
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

//                jenis_barang = editJenis_Barang.getText().toString();

                if (bitmap == null)
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "silahkan masukkan gambar", Toast.LENGTH_LONG).show();
                }
                else if (nama.matches("") || harga.matches("") || stok.matches("") || deskripsi.matches("")
                        || jenis_barang.matches("") || ukuran.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ada field yang masih kosong", Toast.LENGTH_LONG).show();
                }
                else {
                    new CreateBarang().execute();
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
        getActivity().setTitle("Tambah Barang");
    }



    private void showFileChooser(){

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
            Log.d("filePath", String.valueOf(filePath));

            compress c = new compress(getActivity().getApplicationContext());
//            String ext = getPath(filePath);
            String ext = c.getPath(getActivity().getApplicationContext(),filePath);
            ext = ext.substring(ext.lastIndexOf(".") + 1);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                gambar.setImageBitmap(bitmap);
//                pet = c.rz(getRealPath(filePath), ext);
                pet = c.rz(c.getPath(getActivity().getApplicationContext(),filePath), ext);

            }
            catch (IOException e)
            {
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
//            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int index = cursor.getColumnIndex(OpenableColumns.SIZE);
//            String size = null;
//            if (!cursor.isNull(index)){
//                size = cursor.getString(index);
//            }
//            Log.d("size", cursor.getString(index));
//            return cursor.getString(index);
//            Log.d("size",size);
//            return size;
            String size = cursor.getString(index);
            Log.d("size",size);
            return size;
        }
    }

    public String getPath(Uri uri)
    {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();
        String path;

        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        try {

        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        }
        catch (Exception e){
//            cursor.close();
//            Cursor cursorx = getActivity().getContentResolver().query(uri, null, null, null, null);
//            cursor = getActivity().getContentResolver().query(
//                    uri, null,
//                    MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null
//            );
//            cursorx.moveToFirst();
//            path = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
        }
        cursor.close();
        Log.d("size",path);
        return path;
    }

    private void requestStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {

        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);

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

    class CreateBarang extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog;

        String id_jenis_barang = jenis_barang.toString().trim();
        String uploadId = UUID.randomUUID().toString();
//        String path = getPath(filePath);
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Tambah Barang");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args)
        {
            try {

                session = new SessionManager(getActivity().getApplicationContext());
                if (session.isAdmin()){
                    new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_create_barang)
                            .addFileToUpload(pet,"image")
                            .addParameter("name", namagambar)
                            .addParameter("nama_barang", nama)
                            .addParameter("ukuran",ukuran)
                            .addParameter("merk",merk)
                            .addParameter("khusus",khusus)
                            .addParameter("harga", harga)
                            .addParameter("stok", stok)
                            .addParameter("deskripsi_barang", deskripsi)
                            .addParameter("id_jenis_barang", id_jenis_barang)
                            .addParameter("id_toko", id_toko)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
                    return "sukses";
                }
                else {
                    String id_anggota = session.getUserDetails().get("id_anggota");
                    Log.d("id_anggota = ", id_anggota);
                    new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_create_barang)
                            .addFileToUpload(pet,"image")
                            .addParameter("name", namagambar)
                            .addParameter("nama_barang", nama)
                            .addParameter("ukuran",ukuran)
                            .addParameter("merk",merk)
                            .addParameter("khusus",khusus)
                            .addParameter("harga", harga)
                            .addParameter("stok", stok)
                            .addParameter("deskripsi_barang", deskripsi)
                            .addParameter("id_jenis_barang", id_jenis_barang)
                            .addParameter("id_anggota", id_anggota)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
                    return "sukses";
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "gagal";
            }

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
//                dashboard d = new dashboard();
//                FragmentTransaction ft = getFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, d, "dashboard");
//                ft.commit();
                Intent i = getActivity().getIntent();
                Bundle b = new Bundle();
                b.putInt("menudarifrag", R.id.dashboard);
                i.putExtras(b);
                getActivity().finish();

                startActivity(i);
            }
        }
    }

    public String getJenis_BarangList(){
        ProgressDialog pDialog;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mohon Tunggu..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
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
                pDialog.dismiss();
                return "ok";
            }
            else {
                pDialog.dismiss();
                return "no result";
            }
        } catch (Exception e){
            e.printStackTrace();
            pDialog.dismiss();
            return "exception caught";
        }
    }

    public String getTokoList(){
        ProgressDialog pDialog;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mohon Tunggu..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        Toko t = new Toko();
        List<NameValuePair> p = new ArrayList<>();
        try {
            JSONObject json = jParser.makeHttpRequest(url_read_toko,"POST",p);
            int success = json.getInt("success");
            if (success == 1){
                daftarToko = json.getJSONArray("toko");
                for (int i = 0; i < daftarToko.length(); i++){
                    JSONObject c = daftarToko.getJSONObject(i);
                    t = new Toko();
                    t.setId_toko(c.getString("id_toko"));
                    t.setNama_toko(c.getString("nama_toko"));
                    tokoArrayList.add(t);
                }
                pDialog.dismiss();
                return "ok";
            }
            else {
                pDialog.dismiss();
                return "kosong";
            }
        }
        catch (Exception e){
            pDialog.dismiss();
            e.printStackTrace();
            return "exception";
        }
    }

}
