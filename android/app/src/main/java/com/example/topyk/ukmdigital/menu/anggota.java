package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.AnggotaAdapter;
import com.example.topyk.ukmdigital.kelas.Anggota;
import com.example.topyk.ukmdigital.sub_menu.cari_anggota;
import com.example.topyk.ukmdigital.sub_menu.edit_profil;
import com.example.topyk.ukmdigital.sub_menu.tambah_anggota;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_anggota;

/**
 * Created by topyk on 8/9/2017.
 */

public class anggota extends Fragment {
    ListView lv;
    FloatingActionButton fab,fab_cari;
    ArrayList<Anggota> daftar_anggota = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarAnggota = null;
//    String url_read_anggota = "http://192.168.43.192/ukm_digital/anggota/read_anggota.php";
    SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.kelola_anggota, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        lv = (ListView)root.findViewById(R.id.list_anggota);
        fab = (FloatingActionButton)root.findViewById(R.id.fab);
        fab_cari = (FloatingActionButton)root.findViewById(R.id.fab_cari);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar_anggota.clear();
                tambah_anggota ta = new tambah_anggota();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,ta,"tambah_anggota");
                ft.addToBackStack("anggota");
                ft.commit();
            }
        });
        fab_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar_anggota.clear();
                cari_anggota ca = new cari_anggota();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,ca,"cari_anggota");
                ft.addToBackStack("anggota");
                ft.commit();
            }
        });

        new BacaAnggota().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String id_anggota = ((TextView)view.findViewById(R.id.id_anggota)).getText().toString();
                String nama_anggota = ((TextView)view.findViewById(R.id.nama_anggota)).getText().toString();
                String alamat_anggota = ((TextView)view.findViewById(R.id.alamat_anggota)).getText().toString();
                String no_hp = ((TextView)view.findViewById(R.id.no_hp)).getText().toString();
                String email = ((TextView)view.findViewById(R.id.email)).getText().toString();
                String gambar_anggota = daftar_anggota.get(position).getGambar();
                String username = ((TextView)view.findViewById(R.id.username)).getText().toString();
                String password = ((TextView)view.findViewById(R.id.password)).getText().toString();

                Bundle b = new Bundle();
                b.putString("id_anggota", id_anggota);
                b.putString("nama", nama_anggota);
                b.putString("alamat", alamat_anggota);
                b.putString("no_hp", no_hp);
                b.putString("email",email);
                b.putString("gambar",gambar_anggota);
                b.putString("username", username);
                b.putString("password",password);

                daftar_anggota.clear();
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

        getActivity().setTitle("Kelola Anggota");
    }

    class BacaAnggota extends AsyncTask<String, Void, String>{
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
            Anggota a = new Anggota();
            List<NameValuePair> p = new ArrayList<>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_anggota,"POST",p);
                int success = json.getInt("success");
                if (success == 1){
                    daftarAnggota = json.getJSONArray("anggota");
                    for (int i = 0; i < daftarAnggota.length(); i++){
                        JSONObject c = daftarAnggota.getJSONObject(i);
                        a = new Anggota();
                        a.setId(c.getString("id_anggota"));
                        a.setNama(c.getString("nama"));
                        a.setAlamat(c.getString("alamat"));
                        a.setNo_hp(c.getString("no_hp"));
                        a.setEmail(c.getString("email"));
                        a.setGambar(c.getString("gambar"));
                        a.setUsername(c.getString("username"));
                        a.setPassword(c.getString("password"));
                        daftar_anggota.add(a);
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
                lv.setAdapter(new AnggotaAdapter(getActivity(),daftar_anggota));
            }
        }
    }

}
