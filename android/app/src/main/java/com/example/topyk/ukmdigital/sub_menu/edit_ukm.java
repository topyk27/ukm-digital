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
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.util.JSONParser;
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
import static com.example.topyk.ukmdigital.variabel.url_update_profil;

/**
 * Created by topyk on 8/21/2017.
 */

public class edit_ukm extends Fragment {
    JSONParser jParser = new JSONParser();
    TextView id;
    EditText nama, deskripsi, link, namaGambar;
    Button pilih, ubah, batal;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Uri filepath;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    private static final int STORAGE_PERMISSION_CODE = 123;

    String pid, pnama, pdeskripsi, plink, pnamaGambar,pet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.edit_ukm, container, false);
        new reqStoragePermission(getActivity().getApplicationContext(), getActivity(),PICK_IMAGE_REQUEST);
        id = (TextView)rootView.findViewById(R.id.id_profil);
        nama = (EditText)rootView.findViewById(R.id.nama);
        deskripsi = (EditText)rootView.findViewById(R.id.deskripsi);
        link = (EditText)rootView.findViewById(R.id.link);
        namaGambar = (EditText)rootView.findViewById(R.id.namaGambar);
        gambar = (NetworkImageView)rootView.findViewById(R.id.gambar);
        pilih = (Button)rootView.findViewById(R.id.butChoose);
        ubah = (Button)rootView.findViewById(R.id.but_ubah);
        batal = (Button)rootView.findViewById(R.id.but_cancel);


        String iid = getArguments().getString("id");
        String inama = getArguments().getString("nama");
        String ideskripsi = getArguments().getString("deskripsi");
        String ilink = getArguments().getString("link");
        String igambar = getArguments().getString("gambar");

        id.setText(iid);
        nama.setText(inama);
        deskripsi.setText(ideskripsi);
        link.setText(ilink);
        gambar.setImageUrl(igambar,imageLoader);

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
        link.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (link.getText().toString().startsWith(" ")){
                    link.setText(link.getText().toString().replaceFirst(" ",""));
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
                pdeskripsi = deskripsi.getText().toString();
                plink = link.getText().toString();
                pnamaGambar = namaGambar.getText().toString();

                if (pnamaGambar.matches("")){
                    pnamaGambar = UUID.randomUUID().toString().substring(0,4);
                }
                if (pnama.matches("") || pdeskripsi.matches("") || plink.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ada field yang masih kosong",Toast.LENGTH_LONG).show();
                }
                else {
                    new UbahProfil().execute();
                }
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
        getActivity().setTitle("Ubah Profil");
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

    class UbahProfil extends AsyncTask<String,Void,String>{
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

            if (bitmap != null){
//                String path = getPath(filepath);
                try {
                    String uploadId = UUID.randomUUID().toString();
                    new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, url_update_profil)
                            .addFileToUpload(pet,"image")
                            .addParameter("name",pnamaGambar)
                            .addParameter("id",pid)
                            .addParameter("nama", pnama)
                            .addParameter("deskripsi", pdeskripsi)
                            .addParameter("link", plink)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
                    return "ok";
                }
                catch (Exception e){
                    e.printStackTrace();
                    return "exception";
                }

            }
            else {
                List<NameValuePair> p = new ArrayList<>();
                p.add(new BasicNameValuePair("id", pid));
                p.add(new BasicNameValuePair("nama",pnama));
                p.add(new BasicNameValuePair("deskripsi", pdeskripsi));
                p.add(new BasicNameValuePair("link", plink));

                try {
                    JSONObject json = jParser.makeHttpRequest(url_update_profil,"POST", p);
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
                Toast.makeText(getActivity().getApplicationContext(),"Data Kosong", Toast.LENGTH_LONG).show();
            }
            else {
                Intent i = getActivity().getIntent();
                Bundle b = new Bundle();
                if (pid.equalsIgnoreCase("1")){
                    b.putInt("menudarifrag", R.id.ukm);
                }
                else {
                    b.putInt("menudarifrag", R.id.unmul);
                }

                i.putExtras(b);
                getActivity().finish();
                startActivity(i);
            }
        }
    }
}
