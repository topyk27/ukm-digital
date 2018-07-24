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
import com.example.topyk.ukmdigital.adapter.JenisBarangAdapter;
import com.example.topyk.ukmdigital.kelas.Jenis_Barang;
import com.example.topyk.ukmdigital.sub_menu.edit_jenis_barang;
import com.example.topyk.ukmdigital.sub_menu.tambah_jenis_barang;
import com.example.topyk.ukmdigital.util.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_jenis_barang;

/**
 * Created by topyk on 8/22/2017.
 */

public class kelola_jenis_barang extends Fragment {
    ListView lv;
    FloatingActionButton fab;
    ArrayList<Jenis_Barang> daftar_jenis = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarJenis = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.menu_kelola_jenis, container, false);
        lv = (ListView)root.findViewById(R.id.list_jenis_barang);
        fab = (FloatingActionButton)root.findViewById(R.id.fab);
        new BacaJenis().execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar_jenis.clear();
                tambah_jenis_barang tjb = new tambah_jenis_barang();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,tjb,"tambah_jenis_barang");
                ft.addToBackStack("jenis");
                ft.commit();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String iid = ((TextView)view.findViewById(R.id.id_jenis_barang)).getText().toString();
                String ijenis = ((TextView)view.findViewById(R.id.jenis_barang)).getText().toString();

                Bundle b = new Bundle();
                b.putString("id",iid);
                b.putString("jenis",ijenis);
                daftar_jenis.clear();

                edit_jenis_barang ejb = new edit_jenis_barang();
                ejb.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,ejb,"edit_jenis_barang");
                ft.addToBackStack("jenis");
                ft.commit();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Kelola Jenis Barang");
    }

    class BacaJenis extends AsyncTask<String,Void, String>{
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
            Jenis_Barang jb = new Jenis_Barang();
            List<NameValuePair> p = new ArrayList<>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_jenis_barang,"POST",p);
                int success = json.getInt("success");
                if (success == 1){
                    daftarJenis = json.getJSONArray("jenis_barang");
                    for (int i = 0; i < daftarJenis.length(); i++){
                        JSONObject c = daftarJenis.getJSONObject(i);
                        jb = new Jenis_Barang();
                        jb.setId_jenis_barang(c.getString("id_jenis_barang"));
                        jb.setJenis_barang(c.getString("jenis_barang"));
                        daftar_jenis.add(jb);
                    }
                    return "ok";
                }
                else {
                    return "kosong";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return "excepton";
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
                lv.setAdapter(new JenisBarangAdapter(getActivity(),daftar_jenis));
            }
        }
    }
}
