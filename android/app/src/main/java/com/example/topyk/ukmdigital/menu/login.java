package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Anggota;
import com.example.topyk.ukmdigital.sub_menu.tambah_anggota;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_login;

/**
 * Created by topyk on 7/4/2017.
 */

public class login extends Fragment {
    String url;
    String success = "0";
    SessionManager session;
    EditText EditEmail, EditPassword;
    TextView tv;
    Button login, daftar;
    JSONParser jParser = new JSONParser();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.login, container, false);
        EditEmail = (EditText)rootView.findViewById(R.id.email);
        EditPassword = (EditText)rootView.findViewById(R.id.password);
        login = (Button)rootView.findViewById(R.id.but_login);
        daftar = (Button)rootView.findViewById(R.id.daftar);
        tv = (TextView)rootView.findViewById(R.id.adm);
        session = new SessionManager(getActivity().getApplicationContext());

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_admin l = new login_admin();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,l,"login_admin");
                ft.addToBackStack("login");
                ft.commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = url_login + "?" + "email="
                        + EditEmail.getText().toString() + "&password="
                        + EditPassword.getText().toString();
                if (EditEmail.getText().toString().trim().length() > 0
                        && EditPassword.getText().toString().trim().length() > 0)
                {
                    new Masuk().execute();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Username/Password masih kosong",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambah_anggota ta = new tambah_anggota();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,ta,"tambah_anggota");
                ft.addToBackStack("login");
                ft.commit();
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Login");
    }

    public class Masuk extends AsyncTask<String, String, String>
    {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
//        protected String doInBackground(String... arg0) {
        protected String doInBackground(String... sText) {
            Anggota tempU = new Anggota();
            List<NameValuePair> p = new ArrayList<NameValuePair>();
//            JSONParser jParser = new JSONParser();
            Boolean kosong = false;
            JSONObject json = jParser.getJSONFromUrl(url);

            try {

                success = json.getString("success");
                JSONArray hasil = json.getJSONArray("login");
                if (success.equals("1")) {

                        for (int i = 0; i < hasil.length(); i++) {

                            JSONObject c = hasil.getJSONObject(i);
                            tempU = new Anggota();
                            tempU.setId(c.getString("id_anggota"));
                            tempU.setNama(c.getString("nama"));

                            tempU.setEmail(c.getString("email"));
                            tempU.setGambar(c.getString("gambar"));
                            tempU.setUsername(c.getString("username"));
                            tempU.setPassword(c.getString("password"));
                            String id = c.getString("id_anggota").trim();
                            String nama = c.getString("nama").trim();

                            String email = c.getString("email").trim();
                            String gambar = c.getString("gambar").trim();
                            String username = c.getString("username").trim();
                            String password = c.getString("password").trim();
                            session.createLoginSession(id, nama, email, gambar, username, password);
                            Log.e("okx", " ambil data");
                    }


//                    else{
//                        for (int i = 0; i < h.length(); i++){

//                        }
//                    }
                } else {
                    Log.e("erro", "tidak bisa ambil data 0");


                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("erro", "tidak bisa ambil data 1");
                kosong = true;
            }
            if (kosong){
                try {
                    success = json.getString("success");
                    JSONArray h = json.getJSONArray("login_admin");
                    if (success.equals("1")){
                        for (int i = 0; i < h.length(); i++){
                            JSONObject c = h.getJSONObject(i);
                            String id = c.getString("id_admin");
                            String nama = c.getString("nama");
                            String email = c.getString("email");
                            String gambar = c.getString("gambar");
                            String username = c.getString("username");
                            String password = c.getString("password");
                            session.createAdminLogin(id,nama,email,gambar,username,password);
                            Log.e("okx", " admin");
                        }
                    }
                    else {
                        Log.e("error","admin kosong");
                    }
                }
                catch (Exception e){
                    Log.d("error","tidak bisa login admin");
                }
            }


            return null;

        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            if (success.equals("1")) {
//                getFragmentManager().popBackStack();
//                int index = getFragmentManager().getBackStackEntryCount() - 1;
//                FragmentManager.BackStackEntry backStackEntry = getFragmentManager().getBackStackEntryAt(index);
//                String tag = backStackEntry.getName();
//                Fragment f = getFragmentManager().findFragmentByTag(tag);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, f, tag);
                Intent a = new Intent(getActivity(), MainActivity.class);
                startActivity(a);
                getActivity().finish();

            } else {

                Toast.makeText(getActivity().getApplicationContext(), "Username/password salah", Toast.LENGTH_LONG).show();
            }

        }

    }
}
