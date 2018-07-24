package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.topyk.ukmdigital.adapter.TokoAdapter;
import com.example.topyk.ukmdigital.kelas.Toko;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_toko;
import static com.example.topyk.ukmdigital.variabel.url_search_toko;

/**
 * Created by topyk on 8/28/2017.
 */

public class hasil_cari_toko extends Fragment {
    ListView lv;
    TextView tv;
    ArrayList<Toko> daftar_toko = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarToko = null;
    JSONArray tokoKu = null;
    String nama, id_toko;
    SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.hasil_cari_toko, container, false);
        lv = (ListView)root.findViewById(R.id.list_hasil_toko);
        tv = (TextView)root.findViewById(R.id.hasil_toko);
        session = new SessionManager(getActivity().getApplicationContext());
        nama = getArguments().getString("nama");
        new HasilToko().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String inama_pemilik_toko = ((TextView)view.findViewById(R.id.nama_pemilik_toko)).getText().toString();
                String iid_toko = ((TextView)view.findViewById(R.id.id_toko)).getText().toString();
                String inama_toko = ((TextView)view.findViewById(R.id.nama_toko)).getText().toString();
                String ialamat_toko = ((TextView)view.findViewById(R.id.alamat_toko)).getText().toString();
                String itelp = ((TextView)view.findViewById(R.id.telp)).getText().toString();
                String ideskripsi_toko = ((TextView)view.findViewById(R.id.deskripsi_toko)).getText().toString();
                String iid_kampung = ((TextView)view.findViewById(R.id.id_kampung)).getText().toString();
                String iid_anggota = ((TextView)view.findViewById(R.id.id_anggota)).getText().toString();
                String igambar = daftar_toko.get(position).getGambar();
                String ilat = ((TextView)view.findViewById(R.id.lat)).getText().toString();
                String ilng = ((TextView)view.findViewById(R.id.lng)).getText().toString();
                LatLng ilatlng = new LatLng(Double.parseDouble(ilat), Double.parseDouble(ilng));
                cekTokoAnggota();
                if (id_toko != null && id_toko.equalsIgnoreCase(iid_toko)){
                    toko_ku t = new toko_ku();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame,t,"tokoku");
                    ft.commit();
                }
                else {
                    Bundle b = new Bundle();
                    b.putString("nama_pemilik_toko",inama_pemilik_toko);
                    b.putString("id_toko", iid_toko);
                    b.putString("nama_toko", inama_toko);
                    b.putString("alamat_toko", ialamat_toko);
                    b.putString("telp", itelp);
                    b.putString("deskripsi_toko", ideskripsi_toko);
                    b.putString("id_kampung", iid_kampung);
                    b.putString("id_anggota", iid_anggota);
                    b.putString("gambar", igambar);
                    b.putString("asalFrag","toko");
                    b.putParcelable("latlng", ilatlng);
                    daftar_toko.clear();
                    detail_toko dt = new detail_toko();
//                    edit_toko dt = new edit_toko();
                    dt.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, dt, "detail_toko");
                    ft.addToBackStack("hasil_cari_toko");
                    ft.commit();
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("UKM");
    }

    class HasilToko extends AsyncTask<String,Void,String>{
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
            Toko t = new Toko();
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("nama",nama));
            try {
                JSONObject json = jParser.makeHttpRequest(url_search_toko,"POST",p);
                int success = json.getInt("success");
                if (success == 1){
                    daftarToko = json.getJSONArray("toko");
                    for (int i = 0; i < daftarToko.length(); i++){
                        JSONObject c = daftarToko.getJSONObject(i);
                        t = new Toko();
                        t.setNama(c.getString("nama"));
                        t.setId_toko(c.getString("id_toko"));
                        t.setNama_toko(c.getString("nama_toko"));
                        t.setAlamat_toko(c.getString("alamat_toko"));
                        t.setTelp(c.getString("telp"));
                        t.setDeskripsi_toko(c.getString("deskripsi_toko"));
                        t.setGambar(c.getString("gambar"));
                        t.setId_kampung(c.getString("id_kampung"));
                        t.setId_anggota(c.getString("id_anggota"));
                        t.setLatLng(new LatLng(c.getDouble("lat"),c.getDouble("lng")));
                        daftar_toko.add(t);
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
                tv.setVisibility(View.VISIBLE);
            }
            else if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else {
                lv.setAdapter(new TokoAdapter(getActivity(),daftar_toko));
            }
        }
    }

    public String cekTokoAnggota(){
//        session = new SessionManager(getActivity().getApplicationContext());
        String id_anggota = session.getUserDetails().get("id_anggota");
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        p.add(new BasicNameValuePair("id_anggota", id_anggota));
        try {
            JSONObject json = jParser.makeHttpRequest(url_read_toko, "POST", p);
            int success = json.getInt("success");
            if (success == 1){
                tokoKu = json.getJSONArray("toko");
                for (int i = 0; i < tokoKu.length(); i++){
                    JSONObject c = tokoKu.getJSONObject(i);
                    id_toko = c.getString("id_toko");
                }
                return "ok";
            }
            else{
                return "no result";
            }
        }
        catch (Exception e){
            return "Exception Caught";
        }
    }
}
