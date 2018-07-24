package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Jenis_Barang;
import com.example.topyk.ukmdigital.menu.kelola_jenis_barang;
import com.example.topyk.ukmdigital.util.JSONParser;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.topyk.ukmdigital.sub_menu.tambah_barang.TAG_ID_JENIS_Barang;
import static com.example.topyk.ukmdigital.sub_menu.tambah_barang.TAG_JENIS_BARANG;
import static com.example.topyk.ukmdigital.variabel.url_create_jenis_barang;
import static com.example.topyk.ukmdigital.variabel.url_read_jenis_barang;

/**
 * Created by topyk on 8/22/2017.
 */

public class tambah_jenis_barang extends Fragment {
    EditText jenis_barang;
    Button tambah, batal;
    String pjenis_barang;
    JSONParser jParser = new JSONParser();
    JSONArray daftarJenis;
    ArrayList<Jenis_Barang> jenis_barangArrayList = new ArrayList<>();
    Boolean boleh_tambah;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.tambah_jenis_barang, container, false);
        jenis_barang = (EditText)root.findViewById(R.id.jenis_barang);
        tambah = (Button)root.findViewById(R.id.but_tambah);
        batal = (Button)root.findViewById(R.id.butBatal);
        getJenis_BarangList();

        jenis_barang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (jenis_barang.getText().toString().startsWith(" ")){
                    jenis_barang.setText(jenis_barang.getText().toString().replaceFirst(" ",""));
                }
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pjenis_barang = jenis_barang.getText().toString();
                if (pjenis_barang.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ada field yang masih kosong",Toast.LENGTH_LONG).show();
                }
                else {
                    for (int i = 0; i < jenis_barangArrayList.size(); i++){
                        boleh_tambah = true;
                        if (pjenis_barang.equalsIgnoreCase(jenis_barangArrayList.get(i).toString())){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Jenis Barang Sudah Ada",Toast.LENGTH_LONG).show();
                            boleh_tambah = false;
                            break;
                        }

                    }
                    if (boleh_tambah){
                        new CreateJenis_Barang().execute();
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


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tambah Jenis Barang");
    }

    public String getJenis_BarangList(){
        Jenis_Barang tmpJenis = new Jenis_Barang();
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();

        try {
            JSONObject json = jParser.makeHttpRequest(url_read_jenis_barang, "POST", parameter);

            int success = json.getInt("success");
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

    class CreateJenis_Barang extends AsyncTask<String, Void, String>{
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

        @Override
        protected String doInBackground(String... sText){
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("jenis_barang",pjenis_barang));

            try {
//                new MultipartUploadRequest(getActivity().getApplicationContext(),uploadId,url_create_jenis_barang)
//                        .addParameter("jenis_barang",pjenis_barang)
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload();
                Log.d("xyz",pjenis_barang);

                JSONObject json = jParser.makeHttpRequest(url_create_jenis_barang,"POST",p);
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
//            return "ok";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")) {
                Toast.makeText(getActivity().getApplicationContext(), "Terjadi masalah. Silahkan periksa koneksi anda!",
                        Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "kosong",Toast.LENGTH_LONG).show();
            }

            else {
//                kelola_jenis_barang kjb = new kelola_jenis_barang();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, kjb, "jenis");
//                ft.commit();
                Intent i = getActivity().getIntent();
                Bundle b = new Bundle();
                b.putInt("menudarifrag",R.id.jenis);
                i.putExtras(b);
                getActivity().finish();
                startActivity(i);
            }
        }
    }
}
