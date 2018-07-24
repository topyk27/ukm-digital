package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.menu.anggota;
import com.example.topyk.ukmdigital.menu.profil;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.example.topyk.ukmdigital.util.compress;
import com.example.topyk.ukmdigital.util.reqStoragePermission;
import com.example.topyk.ukmdigital.util.Email;

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
import static com.example.topyk.ukmdigital.variabel.url_delete_anggota;
import static com.example.topyk.ukmdigital.variabel.url_update_anggota;

/**
 * Created by topyk on 8/11/2017.
 */

public class edit_profil extends Fragment {
    EditText nama,alamat,no_hp,email,username,password, namaGambar,pass;
    TextView id;
    NetworkImageView gambar;
    Button pilih,ubah, batal, hapus;
    String ipassword;
    String pid,pnama,palamat,pno_hp,pemail,pusername,ppassword,pnamaGambar;
    JSONParser jParser = new JSONParser();

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Uri filepath;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    private static final int STORAGE_PERMISSION_CODE = 123;
    SessionManager session;
    String pet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.edit_profil, container, false);
        new reqStoragePermission(getActivity().getApplicationContext(),getActivity(),STORAGE_PERMISSION_CODE);
        session = new SessionManager(getActivity().getApplicationContext());

        id = (TextView)rootView.findViewById(R.id.id_anggota);
        nama = (EditText)rootView.findViewById(R.id.nama_anggota);
        alamat = (EditText)rootView.findViewById(R.id.alamat);
        no_hp = (EditText)rootView.findViewById(R.id.no_hp);
        email = (EditText)rootView.findViewById(R.id.email);
        username = (EditText)rootView.findViewById(R.id.username);
        password = (EditText)rootView.findViewById(R.id.password);
        namaGambar = (EditText)rootView.findViewById(R.id.namaGambar);
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar);
        pilih = (Button)rootView.findViewById(R.id.butChoose);
        ubah = (Button)rootView.findViewById(R.id.butUbah);
        batal = (Button)rootView.findViewById(R.id.butBatal);
        hapus = (Button)rootView.findViewById(R.id.hapus);
        pass = (EditText)rootView.findViewById(R.id.pass);



        String iid = getArguments().getString("id_anggota");
        String inama = getArguments().getString("nama");
        String ialamat = getArguments().getString("alamat");
        String ino_hp = getArguments().getString("no_hp");
        String iemail = getArguments().getString("email");
        String iusername = getArguments().getString("username");
        ipassword = getArguments().getString("password");
        String igambar = getArguments().getString("gambar");

        id.setText(iid);
        nama.setText(inama);
        alamat.setText(ialamat);
        no_hp.setText(ino_hp);
        email.setText(iemail);
        username.setText(iusername);
        gambar.setImageUrl(igambar,imageLoader);
        pass.setText(ipassword);
        pass.setKeyListener(null);
        username.setKeyListener(null);

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

            hapus.setVisibility(View.VISIBLE);


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
                pno_hp = no_hp.getText().toString();
                pemail = email.getText().toString();
                pusername = username.getText().toString();
                ppassword = password.getText().toString();
                pnamaGambar = namaGambar.getText().toString();

                if (pnamaGambar.matches("")){

                    pnamaGambar = UUID.randomUUID().toString().substring(0,4);
                }
                if (pnama.matches("") || palamat.matches("") || pno_hp.matches("") || pemail.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ada field yang masih kosong",Toast.LENGTH_LONG).show();
                }

                else if (!new Email().isEmailValid(pemail)){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Email tidak valid",Toast.LENGTH_LONG).show();
                }
                else {
                    if (!ppassword.matches("") && ppassword.length() < 6){
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Password terlalu pendek",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (bitmap == null){
                            new UbahProfil().execute();
                        }
                        else {
                            uploadMultipart();
                        }
                    }
                }

            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ntar dikasih fungsi hapus
                pid = id.getText().toString();
                pnama = nama.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Anda yakin akan menghapus akun " + pnama)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteProfil().execute();
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

        getActivity().setTitle("Ubah Profil");
    }

    public void uploadMultipart(){

        try {
            if (ppassword.matches("")){
                ppassword = ipassword;
            }
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_update_anggota )
                    .addFileToUpload(pet,"image")
                    .addParameter("name",pnamaGambar)
                    .addParameter("id_anggota",pid)
                    .addParameter("nama",pnama)
                    .addParameter("alamat",palamat)
                    .addParameter("no_hp",pno_hp)
                    .addParameter("email",pemail)
                    .addParameter("username",pusername)
                    .addParameter("password",ppassword)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
            Intent i = getActivity().getIntent();
            Bundle b = new Bundle();
            b.putInt("menudarifrag",R.id.anggota);
            i.putExtras(b);
            getActivity().finish();
            startActivity(i);
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
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

    class UbahProfil extends AsyncTask<String, Void, String>{
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
            if (ppassword.matches("")){
                ppassword = ipassword;
            }
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("id_anggota",pid));
            p.add(new BasicNameValuePair("nama",pnama));
            p.add(new BasicNameValuePair("alamat",palamat));
            p.add(new BasicNameValuePair("no_hp",pno_hp));
            p.add(new BasicNameValuePair("email",pemail));
            p.add(new BasicNameValuePair("username",pusername));
            p.add(new BasicNameValuePair("password",ppassword));

            try {
                JSONObject json = jParser.makeHttpRequest(url_update_anggota,"POST", p);
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
                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to server,please check your internet connection!",
                        Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("gagal")){
                Toast.makeText(getActivity().getApplicationContext(), "Fail.. Try Again", Toast.LENGTH_LONG).show();
            }
            else {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (session.isAdmin()){
                    anggota a = new anggota();
                    ft.replace(R.id.content_frame,a,"anggota");
                    ft.commit();
                }
                else {
                    profil p = new profil();
                    ft.replace(R.id.content_frame,p,"anggota");
                    ft.commit();
                }
            }
        }
    }

    class DeleteProfil extends AsyncTask<String, Void, String>{
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
            p.add(new BasicNameValuePair("id_anggota", pid));
            try {
                JSONObject json = jParser.makeHttpRequest(url_delete_anggota,"POST", p);
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
                if (!session.isAdmin() && pid.equals(session.getUserDetails().get("id_anggota"))){
                    session.logoutUser();
                    getActivity().finish();
                }
                else {
                    anggota a = new anggota();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, a, "anggota");
                    ft.commit();
                }

            }
        }
    }
}
