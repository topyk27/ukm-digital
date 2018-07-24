package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.sub_menu.edit_profil;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.example.topyk.ukmdigital.variabel.url_read_anggota;

/**
 * Created by topyk on 8/10/2017.
 */

public class profil extends Fragment {
    TextView id,nama,alamat,no_hp,email,username,password;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Button ubah;
    SessionManager session;
//    String url_read_anggota = "http://192.168.43.192/ukm_digital/anggota/read_anggota.php";
    JSONArray daftarAnggota;
    JSONParser jParser = new JSONParser();
    String sid, snama, salamat, sno_hp, semail, sgambar, susername, spassword, idx;
    LinearLayout LLusername, LLpassword;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.menu_profil, container, false);
        id = (TextView)root.findViewById(R.id.id_anggota);
        gambar = (NetworkImageView)root.findViewById(R.id.gambar);
        nama = (TextView)root.findViewById(R.id.nama_anggota);
        alamat = (TextView)root.findViewById(R.id.alamat_anggota);
        no_hp = (TextView)root.findViewById(R.id.no_hp);
        email = (TextView)root.findViewById(R.id.email);
        username = (TextView)root.findViewById(R.id.username);
        password = (TextView)root.findViewById(R.id.password);
        ubah = (Button)root.findViewById(R.id.ubah);
        LLusername = (LinearLayout)root.findViewById(R.id.LLusername);
        LLpassword = (LinearLayout)root.findViewById(R.id.LLpassword);
        Bundle args = getArguments();
        if (args != null){
            idx = getArguments().getString("id_anggota");
            Log.d("idx",idx);
            ubah.setVisibility(GONE);
            LLusername.setVisibility(GONE);
            LLpassword.setVisibility(GONE);
        }

        new BacaAnggota().execute();

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putString("id_anggota",sid);
                b.putString("nama",snama);
                b.putString("alamat",salamat);
                b.putString("no_hp",sno_hp);
                b.putString("email",semail);
                b.putString("username",susername);
                b.putString("password",spassword);
                b.putString("gambar",sgambar);

                edit_profil ep = new edit_profil();
                ep.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,ep,"edit_profil");
                ft.addToBackStack("anggota");
                ft.commit();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Profil");
    }

    class BacaAnggota extends AsyncTask<String, Void, String>{
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
            session = new SessionManager(getActivity().getApplicationContext());
            List<NameValuePair> p = new ArrayList<>();
            if (idx != null){
                p.add(new BasicNameValuePair("id_anggota",idx));
            }
            else {
                p.add(new BasicNameValuePair("id_anggota",session.getUserDetails().get("id_anggota")));
            }

            try {
                JSONObject json = jParser.makeHttpRequest(url_read_anggota,"POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    daftarAnggota = json.getJSONArray("anggota");
                    for (int i = 0; i < daftarAnggota.length(); i++){
                        JSONObject c = daftarAnggota.getJSONObject(i);
                        sid = c.getString("id_anggota");
                        snama = c.getString("nama");
                        salamat = c.getString("alamat");
                        sno_hp = c.getString("no_hp");
                        semail = c.getString("email");
                        sgambar = c.getString("gambar");
                        sgambar = sgambar.replace(" ","%20");
                        susername = c.getString("username");
                        spassword = c.getString("password");
                    }
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
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(getActivity().getApplicationContext(),"Data kosong", Toast.LENGTH_LONG).show();
            }
            else {
                id.setText(sid);
                nama.setText(snama);
                alamat.setText(salamat);
                no_hp.setText(sno_hp);
                email.setText(semail);
                gambar.setImageUrl(sgambar,imageLoader);
                username.setText(susername);
                password.setText(spassword);
            }
        }
    }
}
