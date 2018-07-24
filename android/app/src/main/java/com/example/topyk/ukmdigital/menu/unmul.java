package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.sub_menu.edit_ukm;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_profil;

/**
 * Created by topyk on 8/22/2017.
 */

public class unmul extends Fragment {
    TextView id, nama, deskirpsi, link;
    NetworkImageView gambar;
    Button ubah;
    JSONParser jParser = new JSONParser();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SessionManager session;
    String pnama, pdeskirpsi, plink, pgambar;
    String pid = "2";
    JSONArray profil = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.menu_ukm, container, false);
        id = (TextView)root.findViewById(R.id.id_profil);
        nama = (TextView)root.findViewById(R.id.nama);
        deskirpsi = (TextView)root.findViewById(R.id.deskripsi);
        link = (TextView)root.findViewById(R.id.link);
        gambar = (NetworkImageView)root.findViewById(R.id.gambar);
        ubah = (Button)root.findViewById(R.id.but_ubah);

        session = new SessionManager(getActivity().getApplicationContext());
        if (session.isAdmin()){
            ubah.setVisibility(View.VISIBLE);
        }

        new BacaProfil().execute();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!plink.startsWith("http://") && !plink.startsWith("https://")){
                    plink = "http://" + plink;
                }
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(plink));
                startActivity(browser);
            }
        });

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id",pid);
                b.putString("nama", pnama);
                b.putString("deskripsi", pdeskirpsi);
                b.putString("link", plink);
                b.putString("gambar",pgambar);

                edit_ukm ek = new edit_ukm();
                ek.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,ek,"edit_ukm");
                ft.addToBackStack("unmul");
                ft.commit();
            }
        });



        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("About");
    }

    class BacaProfil extends AsyncTask<String,Void,String> {
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
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("id", pid));

            try {
                JSONObject json = jParser.makeHttpRequest(url_read_profil, "POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    profil = json.getJSONArray("profil");
                    for (int i = 0; i < profil.length(); i++){
                        JSONObject c = profil.getJSONObject(i);
                        pnama = c.getString("nama");
                        pdeskirpsi = c.getString("deskripsi");
                        pgambar = c.getString("gambar");
                        pgambar = pgambar.replaceAll(" ","%20");
                        plink = c.getString("link");
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
            if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(getActivity().getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else {
                id.setText(pid);
                nama.setText(pnama.toUpperCase());
                deskirpsi.setText(pdeskirpsi);
                gambar.setImageUrl(pgambar,imageLoader);
                link.setText(plink);
            }
        }
    }
}
