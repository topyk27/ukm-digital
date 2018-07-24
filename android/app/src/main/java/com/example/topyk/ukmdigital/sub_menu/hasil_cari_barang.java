package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.BarangAdapter;
import com.example.topyk.ukmdigital.kelas.Barang;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_search_barang;

/**
 * Created by topyk on 8/24/2017.
 */

public class hasil_cari_barang extends Fragment {
    ListView lv;
    TextView tv;
    ArrayList<Barang> daftar_barang = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarBar = null;
    String nama,harga0,harga1,jenis_barang;
    SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.hasil_cari_barang, container, false);
        lv = (ListView) root.findViewById(R.id.list_hasil_barang);
        tv = (TextView)root.findViewById(R.id.hasil_barang);
        session = new SessionManager(getActivity().getApplicationContext());

        nama = getArguments().getString("nama");
        harga0 = getArguments().getString("harga0");
        harga1 = getArguments().getString("harga1");
        jenis_barang = getArguments().getString("jenis_barang");

        new HasilBacaBarang().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isi_id_barang = ((TextView)view.findViewById(R.id.id_barang)).getText().toString();
                String isi_id_jenis_barang = ((TextView)view.findViewById(R.id.id_jenis_barang)).getText().toString();
                String isi_id_toko = ((TextView)view.findViewById(R.id.id_toko)).getText().toString();
                String isi_nama_barang = ((TextView)view.findViewById(R.id.nama_barang)).getText().toString();
                String isi_harga = ((TextView)view.findViewById(R.id.txtHarga)).getText().toString();
                String isi_stok = ((TextView)view.findViewById(R.id.stok)).getText().toString();
                String isi_deskripsi = ((TextView)view.findViewById(R.id.deskripsi_barang)).getText().toString();
                String isi_gambar = daftar_barang.get(position).getGambar_barang();
                String isi_ukuran = ((TextView)view.findViewById(R.id.ukuran)).getText().toString();
                String isi_merk = ((TextView)view.findViewById(R.id.merk)).getText().toString();
                String isi_khusus = ((TextView)view.findViewById(R.id.khusus)).getText().toString();

                Bundle b = new Bundle();
                b.putString("id_barang", isi_id_barang);
                b.putString("id_jenis_barang", isi_id_jenis_barang);
                b.putString("id_toko", isi_id_toko);
                b.putString("nama_barang", isi_nama_barang);
                b.putString("harga", isi_harga);
                b.putString("stok", isi_stok);
                b.putString("deskripsi_barang", isi_deskripsi);
                b.putString("gambar", isi_gambar);
                b.putString("ukuran", isi_ukuran);
                b.putString("merk", isi_merk);
                b.putString("khusus", isi_khusus);
                daftar_barang.clear();
                if (session.isAdmin()){
                    edit_barang db = new edit_barang();
                    db.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, db, "edit_barang");
                    ft.addToBackStack("hasil_cari_barang");
                    ft.commit();
                }
                else {
                    detail_barang db = new detail_barang();
                    db.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, db, "detail_barang");
                    ft.addToBackStack("hasil_cari_barang");
                    ft.commit();
                }
            }
        });

        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Barang");
    }

    class HasilBacaBarang extends AsyncTask<String, Void, String>{
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
            Barang b = new Barang();
            List<NameValuePair> p = new ArrayList<>();
            if (nama != null){
                p.add(new BasicNameValuePair("nama",nama));
            }
            if (harga0 != null && harga1 != null){
                p.add(new BasicNameValuePair("harga0",harga0));
                p.add(new BasicNameValuePair("harga1",harga1));
            }
            if (jenis_barang != null){
                p.add(new BasicNameValuePair("jenis_barang",jenis_barang));
            }

            try {
                JSONObject json = jParser.makeHttpRequest(url_search_barang,"POST",p);
                int success = json.getInt("success");
                if (success == 1){
                    daftarBar = json.getJSONArray("barang");
                    for (int i = 0; i < daftarBar.length(); i++){
                        JSONObject c = daftarBar.getJSONObject(i);
                        b = new Barang();
                        b.setId_barang(c.getString("id_barang"));
                        b.setNama_barang(c.getString("nama_barang"));
                        b.setUkuran(c.getString("ukuran"));
                        b.setMerk(c.getString("merk"));
                        b.setKhusus(c.getString("khusus"));
                        b.setHarga(c.getInt("harga"));
                        b.setStok(c.getInt("stok"));
                        b.setDeskripsi_barang(c.getString("deskripsi_barang"));
                        b.setGambar_barang(c.getString("gambar"));
                        b.setId_jenis_barang(c.getString("id_jenis_barang"));
                        b.setId_toko(c.getString("id_toko"));

                        daftar_barang.add(b);
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
                tv.setVisibility(View.VISIBLE);
            }
            else {
                lv.setAdapter(new BarangAdapter(getActivity(),daftar_barang));
            }
        }
    }
}
