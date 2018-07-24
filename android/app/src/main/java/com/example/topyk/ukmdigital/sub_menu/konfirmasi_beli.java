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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.MainActivity;
import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.adapter.KBAdapter;
import com.example.topyk.ukmdigital.adapter.KonfirmasiBeliAdapter;
import com.example.topyk.ukmdigital.kelas.Konfirmasi_Beli_Kelas;
import com.example.topyk.ukmdigital.menu.dashboard;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_create_transaksi;
import static com.example.topyk.ukmdigital.variabel.url_max_id_transaksi;

/**
 * Created by topyk on 9/11/2017.
 */

public class konfirmasi_beli extends Fragment {
    TextView id,jumlah,total,id_anggota,stok, textViewnama_barang;
    EditText alamat;
    Button beli, batal;
    String pid,pjumlah,ptotal,pid_anggota,pstok,palamat,nama_barang;
    JSONParser jParser = new JSONParser();
    ArrayList<String> id_barangList = new ArrayList<>();
    ArrayList<String> nama_barangList = new ArrayList<>();
    ArrayList<Integer> jumlah_barangList = new ArrayList<>();
    ArrayList<Integer> totalList = new ArrayList<>();
    ArrayList<Integer> stokList = new ArrayList<>();
    int maxID, paramTotal;
    boolean adaID;
    JSONArray ID_TRANS = null;
    SessionManager session;
    ListView lv;
    ArrayList<Konfirmasi_Beli_Kelas> kbkList = new ArrayList<>();
    ScrollView sv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.konfirmasi_beli, container, false);
        id = (TextView)rootView.findViewById(R.id.id_barang);
        jumlah = (TextView)rootView.findViewById(R.id.jumlah_barang);
        total = (TextView)rootView.findViewById(R.id.total);
        stok = (TextView)rootView.findViewById(R.id.stok);
        id_anggota = (TextView)rootView.findViewById(R.id.id_anggota);
        alamat = (EditText)rootView.findViewById(R.id.alamat);
        textViewnama_barang = (TextView)rootView.findViewById(R.id.nama_barang);
        beli = (Button)rootView.findViewById(R.id.but_beli);
        batal = (Button)rootView.findViewById(R.id.but_batal);
        lv = (ListView)rootView.findViewById(R.id.list_konfirmasi_beli);
        sv = (ScrollView)rootView.findViewById(R.id.scroll_konfirm);
        session = new SessionManager(getActivity().getApplicationContext());

        new MaxIdTransaksi().execute();

        id_barangList = getArguments().getStringArrayList("id_barangList");
        nama_barangList = getArguments().getStringArrayList("nama_barangList");
        jumlah_barangList = getArguments().getIntegerArrayList("jumlah_barangList");
        totalList = getArguments().getIntegerArrayList("totalList");
        stokList = getArguments().getIntegerArrayList("stokList");
        if (nama_barangList.size() > 0){
//            Konfirmasi_Beli_Kelas kbk = new Konfirmasi_Beli_Kelas();
//            for (int i = 0; i < nama_barangList.size(); i++){
//                kbk = new Konfirmasi_Beli_Kelas();
//                kbk.setNama(String.valueOf(nama_barangList.get(i)));
//                kbk.setJumlah(String.valueOf(jumlah_barangList.get(i)));
//                kbk.setTotal(String.valueOf(totalList.get(i)));
//                kbkList.add(kbk);
//                Log.d("awe",String.valueOf(nama_barangList.get(i)));
//            }
//            for (int i = 0; i < kbkList.size(); i++){
//                Log.d("awx",String.valueOf(kbkList.get(i)));
//            }
//
//            lv.setAdapter(new KBAdapter(getActivity(),kbkList));
            new list_kon().execute();
            Toast.makeText(getActivity().getApplicationContext(),
                    "yay",Toast.LENGTH_LONG).show();
        }
        setListViewHeightBasedOnChildren(lv);
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

//        pid = getArguments().getString("id_barang");
//        pjumlah = getArguments().getString("jumlah");
//        ptotal = getArguments().getString("total");
//        pid_anggota = getArguments().getString("id_anggota");
//        pstok = getArguments().getString("stok");
//        nama_barang = getArguments().getString("nama_barang");

        for (int i = 0; i < nama_barangList.size(); i++){
            textViewnama_barang.setText(textViewnama_barang.getText().toString()
            + String.valueOf(i+1) + ". " + nama_barangList.get(i) + ";\n");
            jumlah.setText(jumlah.getText().toString()
            + String.valueOf(i+1) + ". " + jumlah_barangList.get(i) + " item;\n");
            paramTotal += totalList.get(i);
            Log.d("Stokx",String.valueOf(stokList.get(i)));
            Log.d("harga masing barang", String.valueOf(totalList.get(i)));
        }
        Log.d("paramTotal",String.valueOf(paramTotal));
        total.setText("Rp." + String.valueOf(paramTotal));

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

        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palamat = alamat.getText().toString();
                if (palamat.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Silahkan diisi alamat pengiriman",Toast.LENGTH_LONG).show();
                    alamat.requestFocus();
                }
                else if (!adaID){
                    new MaxIdTransaksi().execute();
                }

//                else if (palamat.matches("\n")){
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "new line",Toast.LENGTH_LONG).show();
//                }
                else if (adaID){
                    new createTransaksi().execute();
                }
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Apakah anda ingin membatalkan pemesanan?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dashboard d = new dashboard();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame,d,"dashboard");
                                ft.commit();
                                ((MainActivity)getActivity()).bersihkan();
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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Konfirmasi Pembelian");
    }
    class createTransaksi extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String id_anggota = session.getUserDetails().get("id_anggota");

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
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            p.add(new BasicNameValuePair("id_transaksi",String.valueOf(maxID)));
            for (int i = 0; i < id_barangList.size(); i ++){
                p.add(new BasicNameValuePair("id_barang" + "[" + i + "]",String.valueOf(id_barangList.get(i))));
                p.add(new BasicNameValuePair("jumlah_barang" + "[" + i + "]", String.valueOf(jumlah_barangList.get(i))));
                p.add(new BasicNameValuePair("total" + "[" + i + "]", String.valueOf(totalList.get(i))));
                p.add(new BasicNameValuePair("stok" + "[" + i + "]", String.valueOf(stokList.get(i))));
            }
            p.add(new BasicNameValuePair("id_anggota",id_anggota));
            p.add(new BasicNameValuePair("alamat",palamat));

//            p.add(new BasicNameValuePair("jumlah_barang",pjumlah));
//            p.add(new BasicNameValuePair("total", ptotal));
//            p.add(new BasicNameValuePair("id_anggota", pid_anggota));
//            p.add(new BasicNameValuePair("stok", pstok));
//            p.add(new BasicNameValuePair("alamat",palamat));
            try {
                JSONObject json = jParser.makeHttpRequest(url_create_transaksi, "POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    return "ok";
                }
                else {
                    return "gagal";
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
                Toast.makeText(getActivity().getApplicationContext(),
                        "Gagal terhubung, mohon periksa jaringan internet anda", Toast.LENGTH_LONG).show();
            }

            else if (result.equalsIgnoreCase("gagal")){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Pembelian Gagal, silahkan coba beberapa saat lagi", Toast.LENGTH_LONG).show();
            }
            else {

                Toast.makeText(getActivity().getApplicationContext(),
                        "Permintaan anda berhasil dikirim, tunggu proses selanjutnya", Toast.LENGTH_LONG).show();
                dashboard d = new dashboard();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, d, "dashboard");
                fragmentTransaction.commit();
                ((MainActivity)getActivity()).bersihkan();
            }
        }
    }

    class MaxIdTransaksi extends AsyncTask<String,Void,String>{
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
            List<NameValuePair> p = new ArrayList<>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_max_id_transaksi, "POST", p);
                int success = json.getInt("success");
                if (success == 1){
                    ID_TRANS = json.getJSONArray("max_ID");
                    for (int i = 0; i < ID_TRANS.length(); i++){
                        JSONObject c = ID_TRANS.getJSONObject(i);
                        maxID = c.getInt("id_transaksi") + 1;
                        adaID = true;
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
                adaID = false;
                Toast.makeText(getActivity().getApplicationContext(),
                        "Gagal terhubung, silahkan periksa jaringan internet anda",Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("kosong")){
                maxID = 1;
                adaID = true;
            }

        }
    }

    class list_kon extends AsyncTask<String, Void, String>{
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
            Konfirmasi_Beli_Kelas kbk = new Konfirmasi_Beli_Kelas();
            for (int i = 0; i < nama_barangList.size(); i++){
                kbk = new Konfirmasi_Beli_Kelas();
                kbk.setNama(String.valueOf(nama_barangList.get(i)));
                kbk.setJumlah(String.valueOf(jumlah_barangList.get(i)));
                kbk.setTotal(String.valueOf(totalList.get(i)));
                kbkList.add(kbk);
                Log.d("awe",String.valueOf(nama_barangList.get(i)));
            }
            return "ok";
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            lv.setAdapter(new KonfirmasiBeliAdapter(getActivity(),kbkList));
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
