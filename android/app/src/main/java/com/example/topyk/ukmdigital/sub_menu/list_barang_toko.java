package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.MainActivity;
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

import static com.example.topyk.ukmdigital.variabel.url_read_barang;

/**
 * Created by topyk on 8/4/2017.
 */

public class list_barang_toko extends Fragment {
    SessionManager session;
    String param_id_toko, nama_toko,asalFrag;
    JSONParser jParser = new JSONParser();
//    String url_read_barang = "http://192.168.43.192/ukm_digital/barang/read_barang.php";
    JSONArray daftarBar;
    ArrayList<Barang> daftar_barang = new ArrayList<>();
    TextView tvKosong;
    ListView lv;
    FloatingActionButton fab, endBelanja;

    ArrayList<String> id_barangList = new ArrayList<>();
    ArrayList<String> nama_barangList = new ArrayList<>();
    ArrayList<Integer> jumlah_barangList = new ArrayList<>();
    ArrayList<Integer> totalList = new ArrayList<>();
    ArrayList<Integer> stokList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.menu_barang_toko, container, false);
        tvKosong = (TextView)root.findViewById(R.id.txtBarangKosong);
        lv = (ListView)root.findViewById(R.id.list_barang);
        fab = (FloatingActionButton)root.findViewById(R.id.fab);
        endBelanja = (FloatingActionButton)root.findViewById(R.id.endBelanja);
        session = new SessionManager(getActivity().getApplicationContext());

        if (((MainActivity)getActivity()).getBelanja()){
            endBelanja.setVisibility(View.VISIBLE);
        }
        else {
            endBelanja.setVisibility(View.GONE);
        }

        param_id_toko = getArguments().getString("id_toko");
        nama_toko = getArguments().getString("nama_toko");
        asalFrag = getArguments().getString("asalFrag");

        if (asalFrag != null && asalFrag.equalsIgnoreCase("detail_barang")){
            id_barangList = getArguments().getStringArrayList("id_barangList");
            nama_barangList = getArguments().getStringArrayList("nama_barangList");
            jumlah_barangList = getArguments().getIntegerArrayList("jumlah_barangList");
            totalList = getArguments().getIntegerArrayList("totalList");
            stokList = getArguments().getIntegerArrayList("stokList");
//            ((MainActivity)getActivity()).belanja(true);
//            ((MainActivity)getActivity()).id_barangList(id_barangList);
//            ((MainActivity)getActivity()).nama_barangList(nama_barangList);
//            ((MainActivity)getActivity()).jumlahBarangList(jumlah_barangList);
//            ((MainActivity)getActivity()).totalList(totalList);
//            ((MainActivity)getActivity()).stokList(stokList);
        }

        endBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Apakah sudah tidak ada yang ingin anda pesan?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle b = new Bundle();
                                b.putStringArrayList("id_barangList",id_barangList);
                                b.putStringArrayList("nama_barangList",nama_barangList);
                                b.putIntegerArrayList("jumlah_barangList",jumlah_barangList);
                                b.putIntegerArrayList("totalList",totalList);
                                b.putIntegerArrayList("stokList",stokList);
                                konfirmasi_beli kb = new konfirmasi_beli();
                                kb.setArguments(b);
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame,kb,"konfirmasi");
                                ft.addToBackStack("list_barang_toko");
                                ft.commit();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        session = new SessionManager(getActivity().getApplicationContext());
        if (asalFrag != null && asalFrag.equalsIgnoreCase("tokoku") || session.isAdmin()){
            fab.setVisibility(View.VISIBLE);
        }
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
                b.putString("merk", isi_merk);
                b.putString("khusus", isi_khusus);
                daftar_barang.clear();
                if (asalFrag != null && asalFrag.equalsIgnoreCase("tokoku") || session.isAdmin()){
                    edit_barang eb = new edit_barang();
                    eb.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame,eb,"edit_barang");
                    ft.addToBackStack("list_barang_toko");
                    ft.commit();
                }
                else {

                    if (asalFrag != null && asalFrag.equalsIgnoreCase("detail_barang")){
                        b.putStringArrayList("id_barangList",id_barangList);
                        b.putStringArrayList("nama_barangList",nama_barangList);
                        b.putIntegerArrayList("jumlah_barangList",jumlah_barangList);
                        b.putIntegerArrayList("totalList",totalList);
                        b.putIntegerArrayList("stokList",stokList);
                        b.putString("asalFrag","list_barang_toko");
                    }
                    detail_barang db = new detail_barang();
                    db.setArguments(b);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, db, "detail_barang");
                    ft.addToBackStack("list_barang_toko");
                    ft.commit();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar_barang.clear();
                Bundle b = new Bundle();
                b.putString("id_toko",param_id_toko);
                tambah_barang tb = new tambah_barang();
                tb.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,tb,"tambah_barang");
                ft.addToBackStack("list_barang_toko");
                ft.commit();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(nama_toko);
    }

    class BacaBarang extends AsyncTask<String, Void, String>{
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
            p.add(new BasicNameValuePair("id_toko",param_id_toko));
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_barang,"POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    daftarBar = json.getJSONArray("barang");
                    for (int i = 0; i<daftarBar.length(); i++){
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
            Log.d("result", result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                tvKosong.setVisibility(View.VISIBLE);
            }
            else {
                lv.setAdapter(new BarangAdapter(getActivity(),daftar_barang));
            }
        }
    }
}
