package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Penjualan;
import com.example.topyk.ukmdigital.kelas.Toko;
import com.example.topyk.ukmdigital.kelas.Transaksi;
import com.example.topyk.ukmdigital.menu.pembelian;
import com.example.topyk.ukmdigital.menu.penjualan;
import com.example.topyk.ukmdigital.menu.profil;
import com.example.topyk.ukmdigital.menu.transaksi;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.example.topyk.ukmdigital.variabel.url_read_pemesanan;
import static com.example.topyk.ukmdigital.variabel.url_read_penjualan;
import static com.example.topyk.ukmdigital.variabel.url_read_transaksi;
import static com.example.topyk.ukmdigital.variabel.url_update_transaksi;

/**
 * Created by topyk on 7/20/2017.
 */

public class detail_transaksi extends Fragment {
    TextView id_transaksi, id_barang, waktu, id_anggota, nama_barang, nama_PembeliAtauPenjual, jumlah, total, detail, txstatus;
    TextView barisPembeliPenjual;
    EditText alamat;
    Spinner dropdown;
    Button ubah, hapus, batal;
    private String[] arraySpinner;
    String id, status, palamat;
    LinearLayout linearLayout;
//    String url_update_transaksi = "http://192.168.43.192/ukm_digital/transaksi/update_transaksi.php";
    JSONParser jParser = new JSONParser();
    String asal;
    JSONArray daftarPenjualan = null;
    JSONArray detailTransaksi = null;
    JSONArray pembeli = null;
    JSONArray penjual = null;
    ArrayList<Penjualan> daftar_penjualan = new ArrayList<Penjualan>();
    String xid_anggota, xnama, xwaktu, xalamat, xstatus, idx; //xid_anggota kalo sebagai admin jadi penjual
    String id_penjual, nama_penjual;
    int xjumlah_seluruh, xtotal;
    List<String> xnama_barang = new ArrayList<>();
    List<Integer> xjumlah_barang = new ArrayList<>();
    List<Integer> xharga = new ArrayList<>();
    String url,tag;
    TableRow tr;
    TextView namaPenjual;
    SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.detail_transaksi, container, false);
        detail = (TextView)rootView.findViewById(R.id.txDetail);
        id_transaksi = (TextView)rootView.findViewById(R.id.id_transaksi);
        id_barang = (TextView)rootView.findViewById(R.id.id_barang);
        waktu = (TextView)rootView.findViewById(R.id.waktu);
        id_anggota = (TextView)rootView.findViewById(R.id.id_anggota);
        nama_barang = (TextView)rootView.findViewById(R.id.nama_barang);
        nama_PembeliAtauPenjual = (TextView)rootView.findViewById(R.id.namaPembeliAtauPenjual);
        jumlah = (TextView)rootView.findViewById(R.id.jumlah_barang);
        total = (TextView)rootView.findViewById(R.id.total);
        alamat = (EditText) rootView.findViewById(R.id.alamat);
        txstatus = (TextView)rootView.findViewById(R.id.status);
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linSpin);
        barisPembeliPenjual = (TextView)rootView.findViewById(R.id.PembeliAtauPenjual);
        ubah = (Button)rootView.findViewById(R.id.butUbah);
//        hapus = (Button)rootView.findViewById(R.id.butHapus);
        batal = (Button)rootView.findViewById(R.id.butBatal);
        dropdown = (Spinner)rootView.findViewById(R.id.spinner);
        tr = (TableRow)rootView.findViewById(R.id.trPenjual);
        namaPenjual = (TextView)rootView.findViewById(R.id.nama_penjual);

        session = new SessionManager(getActivity().getApplicationContext());

        String iid_transkasi = getArguments().getString("id_transaksi");
        Log.d("id_trans",iid_transkasi);
        idx = iid_transkasi;
//        String iid_barang = getArguments().getString("id_barang");
//        String iwaktu = getArguments().getString("waktu");
//        String iid_anggota = getArguments().getString("id_anggota");
//        String inama_barang = getArguments().getString("nama_barang");
//        String inama_PembeliAtauPenjual = getArguments().getString("nama_pembeliAtauPenjual");
//        String ijumlah = getArguments().getString("jumlah");
//        String itotal = getArguments().getString("total");
//        String ialamat = getArguments().getString("alamat");
//        final String istatus = getArguments().getString("status");
        String asalFrag = getArguments().getString("asalFrag");
        Log.d("asalFrag",asalFrag);

        asal = asalFrag;
        if (asal.equalsIgnoreCase("pembelian")){
            url = url_read_pemesanan;
            tag = "pemesanan";
        }
        else if (asal.equalsIgnoreCase("penjualan")){
            url = url_read_penjualan;
            tag = "penjualan";
        }else if (asal.equalsIgnoreCase("transaksi")){
            url = url_read_transaksi;

            tr.setVisibility(View.VISIBLE);
        }

        new BacaDetailTransaksi().execute();
        if (asalFrag.equalsIgnoreCase("pembelian")){
            detail.setText("Detail Pembelian");
            barisPembeliPenjual.setText("Penjual");
//            this.arraySpinner = new String[]{
//                    xstatus,"batal"
//            };
        }
        else {
            if (asal.equalsIgnoreCase("transaksi")){
                detail.setText("Detail Transaksi");
            }
            else {
                detail.setText("Detail Penjualan");
                alamat.setKeyListener(null);
            }

            if (!session.isAdmin()){
                this.arraySpinner = new String[]{
                        "menunggu", "dikirim", "batal"
                };
            }
            else {
                this.arraySpinner = new String[]{
                        "menunggu", "dikirim", "diterima", "batal"
                };
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    R.layout.spinner_item, arraySpinner);

            dropdown.setAdapter(adapter);
        }


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
//                R.layout.spinner_item, arraySpinner);
//
//        dropdown.setAdapter(adapter);


//        Log.d("namaPembeliPenjual", inama_PembeliAtauPenjual);
        id_transaksi.setText(iid_transkasi);
//        id_barang.setText(iid_barang);
//        waktu.setText(iwaktu);
//        id_anggota.setText(iid_anggota);
//        nama_barang.setText(inama_barang);
//        nama_PembeliAtauPenjual.setText(inama_PembeliAtauPenjual);
//        jumlah.setText(ijumlah);
//        total.setText(itotal);
//        alamat.setText(ialamat);


        alamat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (alamat.getText().toString().startsWith(" ")){
                    alamat.setText(alamat.getText().toString().replaceFirst(" ",""));
                }
                if (alamat.getText().toString().startsWith("\n")){
                    alamat.setText(alamat.getText().toString().replaceFirst("\n",""));
                }
            }
        });

//        if (xstatus.equalsIgnoreCase("dikirm")){
//            alamat.setKeyListener(null);
//        }
//
//        if (xstatus.equalsIgnoreCase("batal")){
//
////            dropdown.setEnabled(false);
////            dropdown.setVisibility(GONE);
//            linearLayout.setVisibility(GONE);
//            txstatus.setVisibility(View.VISIBLE);
//            alamat.setKeyListener(null);
//        }

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                status = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xstatus.equalsIgnoreCase("batal")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Anda tidak dapat mengubah transaksi yang sudah dibatalkan", Toast.LENGTH_LONG).show();
                }
                else if (xstatus.equalsIgnoreCase("dikirim") && !status.equalsIgnoreCase("diterima") && !session.isAdmin()){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Anda tidak dapat mengubah transaksi yang sedang dikirim",Toast.LENGTH_LONG).show();
                }
                else if (xstatus.equalsIgnoreCase("diterima")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Barang sudah diterima, tidak perlu diubah",Toast.LENGTH_LONG).show();
                }
                else {
                    id = id_transaksi.getText().toString();
                    palamat = alamat.getText().toString();
                    if (status.equalsIgnoreCase("batal")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Anda yakin akan membatalkan pemesanan?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new UbahTransaksi().execute();
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        new UbahTransaksi().execute();
                    }


                }

            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        nama_PembeliAtauPenjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id_anggota",id_anggota.getText().toString());
                profil p = new profil();
                p.setArguments(b);
                xnama_barang.clear();
                xjumlah_barang.clear();
                xharga.clear();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,p,"detail_anggota");
                ft.addToBackStack("detail_transaksi");
                ft.commit();
            }
        });
        namaPenjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("id_penjual",id_penjual);
                Bundle b = new Bundle();
                b.putString("id_anggota",id_penjual);
                profil p = new profil();
                p.setArguments(b);
                xnama_barang.clear();
                xjumlah_barang.clear();
                xharga.clear();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,p,"detail_anggota");
                ft.addToBackStack("detail_transaksi");
                ft.commit();
            }
        });

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detail Transaksi");
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equals(myString)){
                Log.d("spinnernya", spinner.getItemAtPosition(i).toString());
                index = i;
            }

        }
        return index;
    }

    class BacaDetailTransaksi extends AsyncTask<String,Void,String>{
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
            Penjualan p = new Penjualan();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("id_transaksi", idx));


//            else {
//                // nanti dikasih url admin
//            }
            try {

                JSONObject json = jParser.makeHttpRequest(url,"POST",param);
                Log.d("urlnya",url);
                int success = json.getInt("success");
                Log.d("sukses", String.valueOf(success));
                if (success == 1){
                    if (asal.equalsIgnoreCase("transaksi")){
                        penjual = json.getJSONArray("penjual");
                        for (int i = 0; i < penjual.length(); i++){
                            JSONObject c = penjual.getJSONObject(i);
                            id_penjual = c.getString("id_anggota");
                            nama_penjual = c.getString("penjual");
//                            xid_anggota = c.getString("id_anggota");
//                            xnama = c.getString("penjual");
                            xjumlah_seluruh = c.getInt("jumlah_seluruh");
                            xtotal = c.getInt("total_bayar");
                            xwaktu = c.getString("waktu");
                            xalamat = c.getString("alamat");
                            xstatus = c.getString("status");
                        }
                        pembeli = json.getJSONArray("pembeli");
                        for (int i = 0; i < pembeli.length(); i++){
                            JSONObject d = pembeli.getJSONObject(i);
//                            id_pembeli = d.getString("id_anggota");
//                            nama_pembeli = d.getString("pembeli");
                            xid_anggota = d.getString("id_anggota");
                            xnama = d.getString("pembeli");
                        }
                    }else{
                        daftarPenjualan = json.getJSONArray(tag);
                        Log.d("tagx",tag);
                        for (int i = 0; i < daftarPenjualan.length(); i++){
                            JSONObject c = daftarPenjualan.getJSONObject(i);


                            xid_anggota = c.getString("id_anggota");

                            xnama = c.getString("nama");
                            xjumlah_seluruh = c.getInt("jumlah_seluruh");
                            xtotal = c.getInt("total_bayar");
                            xwaktu = c.getString("waktu");
                            xalamat = c.getString("alamat");
                            xstatus = c.getString("status");
                            Log.d("isinya", xid_anggota + xnama + xjumlah_seluruh + xtotal + xwaktu + xalamat + xstatus);
                        }
                    }


                    detailTransaksi = json.getJSONArray("detail_transaksi");

                    for (int i = 0; i < detailTransaksi.length(); i++){
                        JSONObject d = detailTransaksi.getJSONObject(i);
                        xnama_barang.add(d.getString("nama_barang"));

                        xjumlah_barang.add(d.getInt("jumlah_barang"));
                        xharga.add(d.getInt("harga"));
                        Log.d("isiarray",xnama_barang.get(i) + String.valueOf(xjumlah_barang.get(i)) + String.valueOf(xharga.get(i)));
                    }
                    Log.d("xid",xid_anggota);
                    return "ok";

                }
                else {
                    Log.d("xid1",xid_anggota);
                    return "kosong";
                }
            }
            catch (Exception e){
                Log.d("xid2",xid_anggota);
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result){
            Log.d("xid3",xid_anggota);
            super.onPostExecute(result);
            Log.d("resultx",result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sambungan terputus, mohon periksa jaringan internet anda",Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Data kosong, silahkan coba beberapa saat lagi",Toast.LENGTH_LONG).show();
            }
            else {
                if (asal.equalsIgnoreCase("transaksi")){
                    namaPenjual.setText(nama_penjual);

                }
                id_anggota.setText(xid_anggota);
                nama_PembeliAtauPenjual.setText(xnama);
                total.setText("Rp. " + String.valueOf(xtotal));
                waktu.setText(xwaktu);
                alamat.setText(xalamat);
                dropdown.setSelection(getIndex(dropdown,xstatus)); //nanti dipinahkan ke asynctask
                if (xstatus.equalsIgnoreCase("dikirm")){
                    alamat.setKeyListener(null);
                }

                if (xstatus.equalsIgnoreCase("batal")){

//            dropdown.setEnabled(false);
//            dropdown.setVisibility(GONE);
                    linearLayout.setVisibility(GONE);
                    txstatus.setVisibility(View.VISIBLE);
                    alamat.setKeyListener(null);
                }
                if (xstatus.equalsIgnoreCase("diterima")){
                    linearLayout.setVisibility(GONE);
                    txstatus.setVisibility(View.VISIBLE);
                    txstatus.setText("diterima");
                    alamat.setKeyListener(null);
                }
                //nama barang
                int nomor = 1;
                for (int i = 0; i < xnama_barang.size(); i++){
                    nama_barang.setText(nama_barang.getText().toString()
                            + String.valueOf(i+nomor) + ". " + xnama_barang.get(i) + ";\n");
                }
//                if (nama_barang.getText().toString().endsWith("\n")){
//                    nama_barang.setText(nama_barang.getText().toString().substring(0,
//                            nama_barang.getText().toString().length() - 1)
//                    .replace("\n",""));
//                }
                //jumlah barang
                for (int i = 0; i < xjumlah_barang.size(); i++){
                    jumlah.setText(jumlah.getText().toString()
                            + String.valueOf(i+nomor) + ". " + xjumlah_barang.get(i) + " item;\n");
                }
//                if (jumlah.getText().toString().endsWith("\n")){
//                    jumlah.setText(jumlah.getText().toString().substring(0,
//                            jumlah.getText().toString().length() - 1)
//                    .replace("\n",""));
//                }
                if (asal.equalsIgnoreCase("pembelian") && !session.isAdmin()){
                    arraySpinner = new String[]{
                    xstatus,"batal", "diterima"
                    };
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                            R.layout.spinner_item, arraySpinner);

                    dropdown.setAdapter(adapter);
                }



            }
        }
    }

    class UbahTransaksi extends AsyncTask<String, Void, String>{
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
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("id_transaksi",id));
            p.add(new BasicNameValuePair("status", status));
            p.add(new BasicNameValuePair("alamat",palamat));
            try {
                JSONObject json = jParser.makeHttpRequest(url_update_transaksi,"POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    return "ok";
                }
                else {
                    return "fail";
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
                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to server,please check your internet connection!",
                        Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("FAIL"))
            {
                Toast.makeText(getActivity().getApplicationContext(), "Fail.. Try Again", Toast.LENGTH_LONG).show();
            }
            else {
                if (asal.equalsIgnoreCase("penjualan")){
                    penjualan p = new penjualan();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, p, "penjualan");
                    ft.commit();

                }
                else if(asal.equalsIgnoreCase("pembelian")){
                    pembelian p = new pembelian();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, p, "pembelian");
                    ft.commit();
                }
                else {
                    transaksi t = new transaksi();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, t, "transaksi");
                    ft.commit();
                }

            }
        }
    }
}
