package com.example.topyk.ukmdigital.menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.BarangAdapter;
import com.example.topyk.ukmdigital.adapter.TokoAdapter;
import com.example.topyk.ukmdigital.kelas.Barang;
import com.example.topyk.ukmdigital.kelas.Toko;
import com.example.topyk.ukmdigital.sub_menu.detail_toko;
import com.example.topyk.ukmdigital.sub_menu.edit_toko;
import com.example.topyk.ukmdigital.sub_menu.hasil_cari_toko;
import com.example.topyk.ukmdigital.sub_menu.konfirmasi_bikin_toko;
import com.example.topyk.ukmdigital.sub_menu.tambah_toko;
import com.example.topyk.ukmdigital.sub_menu.toko_ku;
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

/**
 * Created by topyk on 7/15/2017.
 */

public class toko extends Fragment {
    ListView lv;
    TextView tv;
    FloatingActionButton fab_cari;
    EditText cari_toko;
    ArrayList<Toko> daftar_toko = new ArrayList<Toko>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarToko = null;
    JSONArray tokoKu = null;
//    String url_read_toko = "http://192.168.43.192/ukm_digital/toko/read_toko.php";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_TOKO = "toko";
    public static final String TAG_ID_TOKO = "id_toko";
    public static final String TAG_NAMA_TOKO = "nama_toko";
    public static final String TAG_ALAMAT_TOKO = "alamat_toko";
    public static final String TAG_TELP = "telp";
    public static final String TAG_DESKRIPSI_TOKO = "deskripsi_toko";
    public static final String TAG_GAMBAR = "gambar";
    public static final String TAG_ID_KAMPUNG = "id_kampung";
    public static final String TAG_ID_ANGGOTA = "id_anggota";
    SessionManager session;
    String id_toko, id_kampung;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View root = inflater.inflate(R.layout.menu_toko, container, false);
        lv = (ListView)root.findViewById(R.id.list_toko);
        fab_cari = (FloatingActionButton)root.findViewById(R.id.fab_cari);
        cari_toko = (EditText)root.findViewById(R.id.cari_toko);
        daftar_toko.clear();
        session = new SessionManager(getActivity().getApplicationContext());

//        tv = (TextView)root.findViewById(R.id.nama_toko);
        Bundle args = getArguments();
        if (args != null){
            id_kampung = getArguments().getString("id_kampung");
        }

        fab_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cari_toko.isShown()){
                    String text = cari_toko.getText().toString();
                    if (!text.matches("")){
                        Bundle b = new Bundle();
                        b.putString("nama",text);
                        hasil_cari_toko hct = new hasil_cari_toko();
                        hct.setArguments(b);
                        daftar_toko.clear();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame,hct,"hasil_cari_toko");
                        ft.addToBackStack("toko");
                        ft.commit();
                    }
                }
                else {
                    cari_toko.setVisibility(View.VISIBLE);
                    cari_toko.requestFocus();
                }

            }
        });
        cari_toko.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    cari_toko.setVisibility(View.GONE);
                }
            }
        });
        cari_toko.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cari_toko.getText().toString().startsWith(" ")){
                    cari_toko.setText(cari_toko.getText().toString().replace(" ",""));
                }
            }
        });
        cari_toko.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String text = cari_toko.getText().toString();
                    if (!text.matches("")){
                        Bundle b = new Bundle();
                        b.putString("nama",text);
                        hasil_cari_toko hct = new hasil_cari_toko();
                        hct.setArguments(b);
                        daftar_toko.clear();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame,hct,"hasil_cari_toko");
                        ft.addToBackStack("toko");
                        ft.commit();
                    }
                    return true;
                }

                return false;
            }
        });


        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.fab);
        if (session.isLoggedIn() && args == null){
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isAdmin()) {
                    daftar_toko.clear();
                    tambah_toko tk = new tambah_toko();
                    FragmentTransaction ft = getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, tk, "tambah_toko");
                    ft.addToBackStack("toko");
                    ft.commit();
                } else {
                    cekTokoAnggota();


                if (cekTokoAnggota().equalsIgnoreCase("no result")) {
                    daftar_toko.clear();
//                    tambah_toko tk = new tambah_toko();
//                    FragmentTransaction ft = getFragmentManager().beginTransaction()
//                            .replace(R.id.content_frame, tk, "tambah_toko");
//                    ft.addToBackStack("toko");
//                    ft.commit();
                    konfirmasi_bikin_toko kbt = new konfirmasi_bikin_toko();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame,kbt,"konfirmasi_bikin_toko");
                    ft.addToBackStack("toko");
                    ft.commit();
                } else if (cekTokoAnggota().equalsIgnoreCase("ok")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Anda sudah memiliki toko", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Terjadi masalah, harap periksa jaringan anda", Toast.LENGTH_LONG).show();
                }
            }


            }
        });
        new BacaToko().execute();
//        cekTokoAnggota();
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
//                if (session.isAdmin()){
//                    Bundle b = new Bundle();
//                    b.putString("id_toko", iid_toko);
//                    b.putString("nama_toko", inama_toko);
//                    b.putString("alamat_toko", ialamat_toko);
//                    b.putString("telp", itelp);
//                    b.putString("deskripsi_toko", ideskripsi_toko);
//                    b.putString("id_kampung", iid_kampung);
//                    b.putString("id_anggota", iid_anggota);
//                    b.putString("gambar", igambar);
//                    b.putString("menudarifrag","toko");
//                    daftar_toko.clear();
//                    edit_toko et = new edit_toko();
//                    et.setArguments(b);
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, et,"edit_toko");
//                    ft.addToBackStack("toko");
//                    ft.commit();
//                }
//                else
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
                    ft.addToBackStack("toko");
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


    class BacaToko extends AsyncTask<String, Void, String>
    {
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
        protected String doInBackground(String... sText)
        {
            String returnResult = null;
            Toko tmpToko = new Toko();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            if (id_kampung != null){
                parameter.add(new BasicNameValuePair("id_kampung",id_kampung));
            }

            try {
                JSONObject json = jParser.makeHttpRequest(url_read_toko, "POST", parameter);
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1){
                    daftarToko = json.getJSONArray(TAG_TOKO);
                    for (int i = 0; i < daftarToko.length(); i++){
                        JSONObject c = daftarToko.getJSONObject(i);
                        tmpToko = new Toko();
                        tmpToko.setNama(c.getString("nama"));
                        tmpToko.setId_toko(c.getString(TAG_ID_TOKO));
                        tmpToko.setNama_toko(c.getString(TAG_NAMA_TOKO));
                        tmpToko.setAlamat_toko(c.getString(TAG_ALAMAT_TOKO));
                        tmpToko.setTelp(c.getString(TAG_TELP));
                        tmpToko.setDeskripsi_toko(c.getString(TAG_DESKRIPSI_TOKO));
                        tmpToko.setGambar(c.getString(TAG_GAMBAR));
                        tmpToko.setId_kampung(c.getString(TAG_ID_KAMPUNG));
                        tmpToko.setId_anggota(c.getString(TAG_ID_ANGGOTA));
                        tmpToko.setLatLng(new LatLng(c.getDouble("lat"),c.getDouble("lng")));
                        daftar_toko.add(tmpToko);


                    }
                    returnResult = "OK";
//                    return "OK";
                } else {
//                    return "no results";
                    returnResult = "no results";
                }
            }catch (Exception e){
                e.printStackTrace();
//                return "Exception Caught";
                returnResult = "Exception Caught";
            }
            return returnResult;
        }
//            String returnResult = getBarangList();
//            return returnResult;


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

                lv.setAdapter(new TokoAdapter(getActivity(), daftar_toko));



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
