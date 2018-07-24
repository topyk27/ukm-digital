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
import com.example.topyk.ukmdigital.adapter.KampungAdapter;
import com.example.topyk.ukmdigital.kelas.Kampung;
import com.example.topyk.ukmdigital.sub_menu.edit_kampung;
import com.example.topyk.ukmdigital.sub_menu.tambah_kampung;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_kampung;

/**
 * Created by topyk on 8/15/2017.
 */

public class kelola_kampung extends Fragment {
    ListView lv;
    FloatingActionButton fab;
    ArrayList<Kampung> daftar_kampung = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarKampung = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.menu_kelola_kampung, container, false);
        lv = (ListView)root.findViewById(R.id.list_kampung);
        fab = (FloatingActionButton)root.findViewById(R.id.fab);

        new BacaKampung().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String id_kampung = ((TextView)view.findViewById(R.id.id_kampung)).getText().toString();
                String nama = ((TextView)view.findViewById(R.id.nama_kampung)).getText().toString();
                String alamat = ((TextView)view.findViewById(R.id.alamat_kampung)).getText().toString();
                String gambar = daftar_kampung.get(position).getGambar_kampung();
                String lat = ((TextView)view.findViewById(R.id.lat)).getText().toString();
                String lng = ((TextView)view.findViewById(R.id.lng)).getText().toString();
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                Bundle b = new Bundle();
                b.putString("id_kampung", id_kampung);
                b.putString("nama", nama);
                b.putString("alamat", alamat);
                b.putString("gambar", gambar);
                b.putParcelable("latlng",latLng);
                daftar_kampung.clear();

                edit_kampung ek = new edit_kampung();
                ek.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,ek,"edit_kampung");
                ft.addToBackStack("kampung");
                ft.commit();


            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar_kampung.clear();
                tambah_kampung tk = new tambah_kampung();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, tk, "tambah_kampung");
                ft.addToBackStack("kampung");
                ft.commit();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Kelola Kampung");
    }

    class BacaKampung extends AsyncTask<String,Void, String>{
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
            Kampung k = new Kampung();
            List<NameValuePair> p = new ArrayList<>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_kampung, "POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    daftarKampung = json.getJSONArray("kampung");
                    for (int i = 0; i < daftarKampung.length(); i++){
                        JSONObject c = daftarKampung.getJSONObject(i);
                        k = new Kampung();
                        k.setId_kampung(c.getString("id_kampung"));
                        k.setNama_kampung(c.getString("nama_kampung"));
                        k.setAlamat_kampung(c.getString("alamat_kampung"));
                        k.setGambar_kampung(c.getString("gambar"));
                        k.setLatLng(new LatLng(c.getDouble("lat"), c.getDouble("lng")));
                        daftar_kampung.add(k);
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
                lv.setAdapter(new KampungAdapter(getActivity(),daftar_kampung));
            }
        }
    }
}
