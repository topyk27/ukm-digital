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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.PemesananAdapter;
import com.example.topyk.ukmdigital.adapter.PenjualanAdapter;
import com.example.topyk.ukmdigital.kelas.Pemesanan;
import com.example.topyk.ukmdigital.kelas.Penjualan;
import com.example.topyk.ukmdigital.kelas.Transaksi;
import com.example.topyk.ukmdigital.sub_menu.detail_transaksi;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_transaksi;

/**
 * Created by topyk on 7/17/2017.
 */

public class transaksi extends Fragment {
    ListView list_penjualan, list_pembelian;
    TextView penjualan_kosong, pemesanan_kosong;
    TextView id_transaksi, id_barang, waktu, id_anggota, nama_barang, nama_PembeliAtauPenjual, jumlah, total, status;
    TableLayout penjualan, pemesanan;
    ArrayList<Transaksi> daftar_transaksi = new ArrayList<Transaksi>();
    JSONParser jParser = new JSONParser();
    JSONArray daftarTransaksi = null;
//    String url_read_transaksi = "http://192.168.43.192/ukm_digital/admin/read_transaksi.php";
    String url_read_penjualan = "http://192.168.43.192/ukm_digital/transaksi/read_penjualan.php";
    String url_read_pemesanan = "http://192.168.43.192/ukm_digital/transaksi/read_pemesanan.php";
    SessionManager session;
    JSONArray daftarPenjualan = null;
    JSONArray daftarPemesanan = null;
    ArrayList<Penjualan> daftar_penjualan = new ArrayList<Penjualan>();
    ArrayList<Pemesanan> daftar_pemesanan = new ArrayList<Pemesanan>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View root = inflater.inflate(R.layout.menu_transaksi, container, false);

        list_penjualan = (ListView)root.findViewById(R.id.list_penjualan);
        penjualan_kosong = (TextView)root.findViewById(R.id.textBelumAdaPenjualan);

        penjualan = (TableLayout)root.findViewById(R.id.tabelPenjualan);


        id_transaksi = (TextView)root.findViewById(R.id.id_transaksi);
//        id_barang = (TextView)root.findViewById(R.id.id_barang);
        waktu = (TextView)root.findViewById(R.id.waktu);
//        id_anggota = (TextView)root.findViewById(R.id.id_anggota);
        nama_barang = (TextView)root.findViewById(R.id.nama_barang); //jadi id_transaksi
        nama_PembeliAtauPenjual = (TextView)root.findViewById(R.id.nama_pembeli); //jadi waktu
//        jumlah = (TextView)root.findViewById(R.id.jumlah_barang);
//        total = (TextView)root.findViewById(R.id.total);
        status = (TextView)root.findViewById(R.id.status);

        session = new SessionManager(getActivity().getApplicationContext());
        new BacaPenjualan().execute();


        list_penjualan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String iid_transkasi = ((TextView)view.findViewById(R.id.id_transaksi)).getText().toString();
//                String iid_barang = ((TextView)view.findViewById(R.id.id_barang)).getText().toString();
//                String iwaktu = ((TextView)view.findViewById(R.id.waktu)).getText().toString();
//                String iid_anggota = ((TextView)view.findViewById(R.id.id_anggota)).getText().toString();
//                String inama_barang = ((TextView)view.findViewById(R.id.nama_barang)).getText().toString();
//                String inama_PembeliAtauPenjual = ((TextView)view.findViewById(R.id.nama_pembeli)).getText().toString();
//                String ijumlah = ((TextView)view.findViewById(R.id.jumlah_barang)).getText().toString();
//                String itotal = ((TextView)view.findViewById(R.id.total)).getText().toString();
//                String istatus = ((TextView)view.findViewById(R.id.status)).getText().toString();
//                String ialamat = ((TextView)view.findViewById(R.id.alamat)).getText().toString();
//                Log.d("namaPembeliDiTransaksi", inama_PembeliAtauPenjual);
                Bundle b = new Bundle();
                b.putString("id_transaksi", iid_transkasi);
//                b.putString("id_barang", iid_barang);
//                b.putString("waktu", iwaktu);
//                b.putString("id_anggota", iid_anggota);
//                b.putString("nama_barang", inama_barang);
//                b.putString("nama_pembeliAtauPenjual", inama_PembeliAtauPenjual);
//                b.putString("jumlah", ijumlah);
//                b.putString("total", itotal);
//                b.putString("status", istatus);
//                b.putString("alamat",ialamat);
                b.putString("asalFrag","transaksi");
//                daftar_pemesanan.clear();
                daftar_penjualan.clear();
                detail_transaksi dt = new detail_transaksi();
                dt.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, dt, "detail_transaksi");
                ft.addToBackStack("transaksi");
                ft.commit();
            }
        });
        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Transaksi");
    }

    class BacaPenjualan extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... sText){
//            getPenjualan();
//            getPemesanan();
            Penjualan tmpPenjualan = new Penjualan();

//            String id_anggota = session.getUserDetails().get("id_anggota");
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
//            parameter.add(new BasicNameValuePair("id_anggota", id_anggota));
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_transaksi, "POST", parameter);
                int success = json.getInt("success");
                if (success == 1){
                    daftarPenjualan = json.getJSONArray("transaksi");
                    for (int i = 0; i < daftarPenjualan.length(); i++){
                        JSONObject c = daftarPenjualan.getJSONObject(i);
                        tmpPenjualan = new Penjualan();
                        tmpPenjualan.setId_transaksi(c.getString("id_transaksi"));
//                        tmpPenjualan.setId_barang(c.getString("id_barang"));
//                        tmpPenjualan.setJumlah_barang(c.getInt("jumlah_barang"));
//                        tmpPenjualan.setTotal(c.getInt("total"));
                        tmpPenjualan.setWaktu(c.getString("waktu"));
//                        tmpPenjualan.setId_anggota(c.getString("id_anggota"));
                        tmpPenjualan.setStatus(c.getString("status"));
//                        tmpPenjualan.setAlamat(c.getString("alamat"));
//                        tmpPenjualan.setNama_barang(c.getString("nama_barang"));
//                        tmpPenjualan.setNama_pembeli(c.getString("nama"));
                        daftar_penjualan.add(tmpPenjualan);

                    }
                    return "ok";
                }
                else {
                    return "no result";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return "exception caught";
            }


        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("no result")){
                penjualan_kosong.setVisibility(View.VISIBLE);
                penjualan.setVisibility(View.GONE);
            }
            else{
                list_penjualan.setAdapter(new PenjualanAdapter(getActivity(), daftar_penjualan));
            }

        }
    }

//    public String getPenjualan(){
//        Penjualan tmpPenjualan = new Penjualan();
//
//        String id_anggota = session.getUserDetails().get("id_anggota");
//        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
//        parameter.add(new BasicNameValuePair("id_anggota", id_anggota));
//        try {
//            JSONObject json = jParser.makeHttpRequest(url_read_penjualan, "POST", parameter);
//            int success = json.getInt("success");
//            if (success == 1){
//                daftarPenjualan = json.getJSONArray("penjualan");
//                for (int i = 0; i < daftarPenjualan.length(); i++){
//                    JSONObject c = daftarPenjualan.getJSONObject(i);
//                    tmpPenjualan = new Penjualan();
//                    tmpPenjualan.setId_transaksi(c.getString("id_transaksi"));
//                    tmpPenjualan.setId_barang(c.getString("id_barang"));
//                    tmpPenjualan.setJumlah_barang(c.getInt("jumlah_barang"));
//                    tmpPenjualan.setTotal(c.getInt("total"));
//                    tmpPenjualan.setWaktu(c.getString("waktu"));
//                    tmpPenjualan.setId_anggota(c.getString("id_anggota"));
//                    tmpPenjualan.setStatus(c.getString("status"));
//                    tmpPenjualan.setNama_barang(c.getString("nama_barang"));
//                    tmpPenjualan.setNama_pembeli(c.getString("nama"));
//                    daftar_penjualan.add(tmpPenjualan);
//                }
//                return "ok";
//            }
//            else {
//                return "no result";
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return "exception caught";
//        }
//    }
    class BasaPemesanan extends AsyncTask<String, Void, String>{
    ProgressDialog pDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mohon Tunggu..");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    @Override
    protected String doInBackground(String... sText){
        Pemesanan tempPem = new Pemesanan();

        String id_anggota = session.getUserDetails().get("id_anggota");
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair("id_anggota", id_anggota));
        try {
            JSONObject json = jParser.makeHttpRequest(url_read_pemesanan, "POST", parameter);
            int success = json.getInt("success");
            if (success == 1){
                daftarPemesanan = json.getJSONArray("pemesanan");
                for (int i = 0; i< daftarPemesanan.length(); i++){
                    JSONObject c = daftarPemesanan.getJSONObject(i);
                    tempPem = new Pemesanan();
                    tempPem.setId_transaksi(c.getString("id_transaksi"));
                    tempPem.setId_barang(c.getString("id_barang"));
                    tempPem.setJumlah_barang(c.getInt("jumlah_barang"));
                    tempPem.setTotal(c.getInt("total"));
                    tempPem.setWaktu(c.getString("waktu"));
                    tempPem.setId_anggota(c.getString("id_anggota"));
                    tempPem.setStatus(c.getString("status"));
                    tempPem.setNama_barang(c.getString("nama_barang"));
                    tempPem.setNama_pembeli(c.getString("nama"));
                    daftar_pemesanan.add(tempPem);
                }
                return "ok";

            }
            else {
                return "no result";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "Exception caught";
        }
    }
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        pDialog.dismiss();
        if (result.equalsIgnoreCase("Exception Caught")){
            Toast.makeText(getActivity().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
        }
        else if (result.equalsIgnoreCase("no result")){
            pemesanan_kosong.setVisibility(View.VISIBLE);
            pemesanan.setVisibility(View.GONE);
        }
        else{
            list_pembelian.setAdapter(new PemesananAdapter(getActivity(), daftar_pemesanan));
        }
    }

}

//    public String getPemesanan(){
//        Pemesanan tempPem = new Pemesanan();
//
//        String id_anggota = session.getUserDetails().get("id_anggota");
//        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
//        parameter.add(new BasicNameValuePair("id_anggota", id_anggota));
//        try {
//            JSONObject json = jParser.makeHttpRequest(url_read_pemesanan, "POST", parameter);
//            int success = json.getInt("success");
//            if (success == 1){
//                daftarPemesanan = json.getJSONArray("pemesanan");
//                for (int i = 0; i< daftarPemesanan.length(); i++){
//                    JSONObject c = daftarPemesanan.getJSONObject(i);
//                    tempPem = new Pemesanan();
//                    tempPem.setId_transaksi(c.getString("id_transaksi"));
//                    tempPem.setId_barang(c.getString("id_barang"));
//                    tempPem.setJumlah_barang(c.getInt("jumlah_barang"));
//                    tempPem.setTotal(c.getInt("total"));
//                    tempPem.setWaktu(c.getString("waktu"));
//                    tempPem.setId_anggota(c.getString("id_anggota"));
//                    tempPem.setStatus(c.getString("status"));
//                    tempPem.setNama_barang(c.getString("nama_barang"));
//                    tempPem.setNama_pembeli(c.getString("nama"));
//                    daftar_pemesanan.add(tempPem);
//                }
//                return "ok";
//
//            }
//            else {
//                return "no result";
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return "Exception caught";
//        }
//
//    }
}
