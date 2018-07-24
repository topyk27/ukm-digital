package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.topyk.ukmdigital.variabel.url_login_admin;

/**
 * Created by topyk on 7/24/2017.
 */

public class login_admin extends Fragment {
    String url, success;
    SessionManager session;
    EditText EditEmail, EditPassword;
    Button login;
    JSONParser jParser = new JSONParser();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.login_admin, container, false);
        EditEmail = (EditText)rootView.findViewById(R.id.email);
        EditPassword = (EditText)rootView.findViewById(R.id.password);
        login = (Button)rootView.findViewById(R.id.but_login);
        session = new SessionManager(getActivity().getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = url_login_admin + "?" + "email="
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


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Login Admin");
    }

    class Masuk extends AsyncTask<String, String, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText){
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                success = json.getString("success");
                JSONArray hasil = json.getJSONArray("login");
                if (success.equalsIgnoreCase("1")){
                    for (int i = 0; i < hasil.length(); i++){
                        JSONObject c = hasil.getJSONObject(i);
                        String id = c.getString("id_anggota").trim();
                        String nama = c.getString("nama").trim();
//                        String alamat = c.getString("alamat").trim();
//                        String no_hp = c.getString("no_hp").trim();
                        String email = c.getString("email").trim();
                        String gambar = c.getString("gambar").trim();
                        String username = c.getString("username").trim();
                        String password = c.getString("password").trim();
                        session.createAdminLogin(id, nama, email, gambar, username, password);
                        Log.e("ok", " ambil data");

                    }
                }
                else {
                    Log.e("erro", "tidak bisa ambil data 0");

                }
            }
            catch (Exception e){
                Log.e("erro", "exception");

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (success.equalsIgnoreCase("1")){
                Intent a = new Intent(getActivity(), MainActivity.class);
                startActivity(a);
                getActivity().finish();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "Username/password salah gan.!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
