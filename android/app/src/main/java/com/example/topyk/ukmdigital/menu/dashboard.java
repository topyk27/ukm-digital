package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.BarangAdapter;
import com.example.topyk.ukmdigital.kelas.Barang;
import com.example.topyk.ukmdigital.sub_menu.cari_barang;
import com.example.topyk.ukmdigital.sub_menu.detail_barang;
import com.example.topyk.ukmdigital.sub_menu.edit_barang;
import com.example.topyk.ukmdigital.sub_menu.tambah_barang;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_barang;

/**
 * Created by topyk on 6/6/2017.
 */

public class dashboard extends Fragment{

    ListView lv;
    TextView tv;
    ArrayList<Barang> daftar_barang = new ArrayList<Barang>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarBar = null;
//    String url_read_barang = "http://192.168.43.192/ukm_digital/barang/read_barang.php";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_BAR = "barang";
    public static final String TAG_ID_BAR = "id_barang";
    public static final String TAG_KODE_BAR = "kode_barang";
    public static final String TAG_NAMA_BAR = "nama_barang";
    public static final String TAG_HARGA = "harga";
    public static final String TAG_STOK = "stok";
    public static final String TAG_DESKRIPSI = "deskripsi_barang";
    public static final String TAG_GAMBAR_BAR = "gambar";
    public static final String TAG_ID_JENIS = "id_jenis_barang";
    public static final String TAG_ID_TOKO = "id_toko";
    SessionManager session;
    String id_kampung;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.menu_dashboard, container, false);
//        View root = inflater.inflate(R.layout.menu_dashboard, container, false);
        session = new SessionManager(getActivity().getApplicationContext());

            lv = (ListView) root.findViewById(R.id.list_dashboard);

        Bundle args = getArguments();
        if (args != null){
            id_kampung = getArguments().getString("id_kampung");
        }

            daftar_barang.clear();
//                    new BarangAdapter().updateAdapter(daftar_barang);


//            tv = (TextView) root.findViewById(R.id.nama_barang);


            FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
            FloatingActionButton cari = (FloatingActionButton) root.findViewById(R.id.cari);
        if (!session.isAdmin()){

            fab.setVisibility(View.GONE);
        }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daftar_barang.clear();



                    tambah_barang tb = new tambah_barang();
                    FragmentTransaction ft = getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, tb, "tambah_barang");
                    ft.addToBackStack("dashboard");
                    ft.commit();
                }
            });

            cari.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daftar_barang.clear();
                    cari_barang cb = new cari_barang();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame,cb,"cari_barang");
                    ft.addToBackStack("dashboard");
                    ft.commit();
                }
            });

            new BacaBarang().execute();


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
                b.putString("ukuran",isi_ukuran);
                b.putString("merk",isi_merk);
                b.putString("khusus",isi_khusus);

                daftar_barang.clear();
                if (session.isAdmin()){
                    edit_barang db = new edit_barang();
                    db.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, db, "edit_barang");
                    ft.addToBackStack("dashboard");
                    ft.commit();
                }
                else {
                    detail_barang db = new detail_barang();
                    db.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, db, "detail_barang");
                    ft.addToBackStack("dashboard");
                    ft.commit();
                }

//                edit_barang db = new edit_barang();


            }
        });

        return root;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        String title;
        if (id_kampung != null){
            title = "Barang";
        }
        else if (session.isAdmin()){
            title = "Kelola Barang";
        }
        else {
            title = "Dashboard";
        }
        getActivity().setTitle(title);
    }

    class BacaBarang extends AsyncTask<String, Void, String>
    {
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
        protected String doInBackground(String... sText)
    {
        String returnResult = getBarangList();
        return returnResult;
    }

    @Override
        protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        pDialog.dismiss();
        if (result.equalsIgnoreCase("Exception Caught")){
            Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
        }

        else if (result.equalsIgnoreCase("no results")){
            Toast.makeText(getActivity().getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
        }
        else {

//            new BarangAdapter().updateAdapter(daftar_barang);
//            new BarangAdapter().clear();

            lv.setAdapter(new BarangAdapter(getActivity(), daftar_barang));



        }
    }
    }

    public String getBarangList(){
        Barang tmpBar = new Barang();
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if (id_kampung != null){
            parameter.add(new BasicNameValuePair("id_kampung", id_kampung));
        }

        try {
            JSONObject json = jParser.makeHttpRequest(url_read_barang, "POST", parameter);
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1){
                daftarBar = json.getJSONArray(TAG_BAR);
                for (int i = 0; i < daftarBar.length(); i++){
                    JSONObject c = daftarBar.getJSONObject(i);
                    tmpBar = new Barang();
                    tmpBar.setId_barang(c.getString(TAG_ID_BAR));
//                    tmpBar.setKode_barang(c.getString(TAG_KODE_BAR));
                    tmpBar.setNama_barang(c.getString(TAG_NAMA_BAR));
                    tmpBar.setUkuran(c.getString("ukuran"));
                    tmpBar.setMerk(c.getString("merk"));
                    tmpBar.setKhusus(c.getString("khusus"));
                    tmpBar.setHarga(c.getInt(TAG_HARGA));
//                    tmpBar.setStok(c.getInt(String.valueOf(TAG_STOK)));
                    tmpBar.setStok(c.getInt(TAG_STOK));
                    tmpBar.setDeskripsi_barang(c.getString(TAG_DESKRIPSI));
                    tmpBar.setGambar_barang(c.getString(TAG_GAMBAR_BAR));
                    tmpBar.setId_jenis_barang(c.getString(TAG_ID_JENIS));
                    tmpBar.setId_toko(c.getString(TAG_ID_TOKO));

                    daftar_barang.add(tmpBar);

                }
                return "OK";
            } else {
                return "no results";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Exception Caught";
        }
    }
}
