package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Jenis_Barang;
import com.example.topyk.ukmdigital.menu.kelola_jenis_barang;
import com.example.topyk.ukmdigital.util.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.sub_menu.tambah_barang.TAG_ID_JENIS_Barang;
import static com.example.topyk.ukmdigital.sub_menu.tambah_barang.TAG_JENIS_BARANG;
import static com.example.topyk.ukmdigital.variabel.url_delete_jenis_barang;
import static com.example.topyk.ukmdigital.variabel.url_read_jenis_barang;
import static com.example.topyk.ukmdigital.variabel.url_update_jenis_barang;

/**
 * Created by topyk on 8/22/2017.
 */

public class edit_jenis_barang extends Fragment {
    EditText jenis_barang;
    TextView id;
    Button ubah, hapus, batal;
    String pid, pjenis_barang;
    JSONParser jParser = new JSONParser();
    JSONArray daftarJenis;
    ArrayList<Jenis_Barang> jenis_barangArrayList = new ArrayList<>();
    Boolean boleh_ubah;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.edit_jenis_barang, container, false);
        id = (TextView)rootView.findViewById(R.id.id_jenis_barang);
        jenis_barang = (EditText)rootView.findViewById(R.id.jenis_barang);
        ubah = (Button)rootView.findViewById(R.id.but_ubah);
        hapus = (Button)rootView.findViewById(R.id.but_hapus);
        batal = (Button)rootView.findViewById(R.id.but_batal);

        String iid = getArguments().getString("id");
        final String ijenis = getArguments().getString("jenis");

        id.setText(iid);
        jenis_barang.setText(ijenis);
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

        getJenis_BarangList();

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid = id.getText().toString();
                pjenis_barang = jenis_barang.getText().toString();
                if (pjenis_barang.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ada field yang masih kosong",Toast.LENGTH_LONG).show();
                }
                else {
                    for (int i = 0; i < jenis_barangArrayList.size(); i++){
                        boleh_ubah = true;
                        if (pjenis_barang.equalsIgnoreCase(jenis_barangArrayList.get(i).toString())){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Jenis Barang Sudah Ada",Toast.LENGTH_LONG).show();
                            boleh_ubah = false;
                            break;
                        }

                    }
                    if (boleh_ubah){
                        new UbahJenisBarang().execute();
                    }
                }
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid = id.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Anda yakin akan menghapus " + ijenis)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new HapusJenisBarang().execute();
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

        getActivity().setTitle("Ubah Jenis Barang");
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

    class UbahJenisBarang extends AsyncTask<String,Void,String>{
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
            p.add(new BasicNameValuePair("id_jenis_barang",pid));
            p.add(new BasicNameValuePair("jenis_barang",pjenis_barang));
            try {
                JSONObject json = jParser.makeHttpRequest(url_update_jenis_barang,"POST",p);
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
                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to server,please check your internet connection!",
                        Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Data kosong", Toast.LENGTH_LONG).show();
            }
            else {
                kelola_jenis_barang kjb = new kelola_jenis_barang();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, kjb, "jenis");
                ft.commit();
            }
        }
    }

    class HapusJenisBarang extends AsyncTask<String, Void, String>{
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
            p.add(new BasicNameValuePair("id_jenis_barang",pid));
            try {
                JSONObject json = jParser.makeHttpRequest(url_delete_jenis_barang,"POST",p);
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
                kelola_jenis_barang kjb = new kelola_jenis_barang();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, kjb, "jenis");
                ft.commit();
            }
        }
    }
}
