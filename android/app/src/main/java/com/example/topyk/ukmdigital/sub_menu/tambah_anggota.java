package com.example.topyk.ukmdigital.sub_menu;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.menu.anggota;
import com.example.topyk.ukmdigital.menu.login;
import com.example.topyk.ukmdigital.util.Email;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.example.topyk.ukmdigital.util.Username;
import com.example.topyk.ukmdigital.util.compress;
import com.example.topyk.ukmdigital.util.reqStoragePermission;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.topyk.ukmdigital.variabel.url_cek_username;
import static com.example.topyk.ukmdigital.variabel.url_create_anggota;

/**
 * Created by topyk on 8/14/2017.
 */

public class tambah_anggota extends Fragment {
    EditText nama, alamat, no_hp, email, username, password, namaGambar;
    ImageView gambar;
    Button pilih, daftar;
    SessionManager session;
    int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String pnama, palamat, pno_hp, pemail, pusername, ppassword, pnamaGambar;
    Bitmap bitmap;
    Uri filePath;
    JSONParser jParser = new JSONParser();
    String pet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.tambah_anggota, container, false);
        new reqStoragePermission(getActivity().getApplicationContext(),getActivity(),STORAGE_PERMISSION_CODE);
        session = new SessionManager(getActivity().getApplicationContext());
        nama = (EditText)root.findViewById(R.id.nama_anggota);
        alamat = (EditText)root.findViewById(R.id.alamat);
        no_hp = (EditText)root.findViewById(R.id.no_hp);
        email = (EditText)root.findViewById(R.id.email);
        username = (EditText)root.findViewById(R.id.username);
        password = (EditText)root.findViewById(R.id.password);
        namaGambar = (EditText)root.findViewById(R.id.namaGambar);
        gambar = (ImageView) root.findViewById(R.id.gambar);

        pilih = (Button)root.findViewById(R.id.butChoose);
        daftar = (Button)root.findViewById(R.id.daftar);

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
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (email.getText().toString().startsWith(" ")){
                    email.setText(email.getText().toString().replaceFirst(" ",""));
                }
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.getText().toString().startsWith(" ")){
                    username.setText(username.getText().toString().replaceFirst(" ",""));
                }
                if (username.getText().toString().contains(" ")){
                    username.setText(username.getText().toString().replace(" ",""));
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().startsWith(" ")){
                    password.setText(password.getText().toString().replaceFirst(" ",""));
                }
            }
        });

        pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnama = nama.getText().toString();
                palamat = alamat.getText().toString();
                pno_hp = no_hp.getText().toString();
                pemail = email.getText().toString();
                pusername = username.getText().toString();
                ppassword = password.getText().toString();
                pnamaGambar = namaGambar.getText().toString();
                String valid = new Username().isUsernameValid(pusername);
                Log.d("uname", valid);
                if (pnamaGambar.matches("")){
                    pnamaGambar = UUID.randomUUID().toString().substring(0,4);
                }
                if (pnama.length() < 3){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Nama terlalu pendek",Toast.LENGTH_LONG).show();
                }
                else if (palamat.length() < 15){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Silahkan masukkan alamat lengkap anda",Toast.LENGTH_LONG).show();
                }
                else if (pno_hp.length() < 10 || pno_hp.length() > 13){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Nomor HP yang anda masukkan tidak valid",Toast.LENGTH_LONG).show();
                }
                else if (pusername.length() < 5){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Username terlalu pendek",Toast.LENGTH_LONG).show();
                }

                else if (bitmap == null){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "silahkan masukkan gambar", Toast.LENGTH_LONG).show();
                }
                else if (pnama.matches("") || palamat.matches("") || pno_hp.matches("") || pemail.matches("")
                        || pusername.matches("") || ppassword.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ada field yang masih kosong", Toast.LENGTH_LONG).show();

                }
                else if (!new Email().isEmailValid(pemail)){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Email tidak valid",Toast.LENGTH_LONG).show();

                }
//                else if (valid.equalsIgnoreCase("tolak")){
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "Username telah digunakan, silahkan coba dengan nama lain", Toast.LENGTH_LONG).show();
//                }
//                else if (valid.equalsIgnoreCase("exception")){
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "Koneksi gagal, silahkan periksa internet anda",Toast.LENGTH_LONG).show();
//                }

                else if (ppassword.length() < 6){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "password terlalu pendek", Toast.LENGTH_LONG).show();
                }
                else {
                    new uname().execute();
//                    new CreateAnggota().execute();
                }

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        String title;
        if (session.isAdmin()){
            title = "Tambah Anggota";
        }
        else {
            title = "Daftar";
        }
        getActivity().setTitle(title);
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

    class uname extends AsyncTask<String,String,String>{
        ProgressDialog pDialog;
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
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("username",pusername));
            try {
                JSONObject json = jParser.makeHttpRequest(url_cek_username,"POST",p);
                int success = json.getInt("success");
                if (success == 1){
                    return "tolak";
                }
                else {
                    return "terima";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return "exception";
            }
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("tolak")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Username telah digunakan, silahkan coba dengan nama lain", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Koneksi gagal, silahkan periksa internet anda",Toast.LENGTH_LONG).show();
            }
            else {
                new CreateAnggota().execute();
            }
        }
    }

    class CreateAnggota extends AsyncTask<String, String, String>{
        ProgressDialog pDialog;


        String uploadId = UUID.randomUUID().toString();

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
                new MultipartUploadRequest(getActivity().getApplicationContext(),uploadId, url_create_anggota)
                        .addFileToUpload(pet,"image")
                        .addParameter("name", pnamaGambar)
                        .addParameter("nama", pnama)
                        .addParameter("alamat", palamat)
                        .addParameter("no_hp", pno_hp)
                        .addParameter("email", pemail)
                        .addParameter("username", pusername)
                        .addParameter("password", ppassword)
//                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
                return "ok";
            }
            catch (Exception e){
                e.printStackTrace();
                return "gagal";
            }
//            return "ok";
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("gagal")){
                Toast.makeText(getActivity().getApplicationContext(),"Terjadi masalah. Silahkan periksa koneksi anda!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                if (session.isAdmin()){
                    anggota a = new anggota();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame,a,"anggota");
                    ft.commit();
                }
                else {
                    login l = new login();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame,l,"login");
                    ft.commit();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Silahkan Login", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
