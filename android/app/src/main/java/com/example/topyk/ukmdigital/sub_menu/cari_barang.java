package com.example.topyk.ukmdigital.sub_menu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Jenis_Barang;
import com.example.topyk.ukmdigital.util.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_read_jenis_barang;

/**
 * Created by topyk on 8/24/2017.
 */

public class cari_barang extends Fragment {
    EditText nama_barang, harga0, harga1, harga0antara, harga1antara;
    Spinner spinner;
    CheckBox checkharga0, checkharga1, checkhargaantara;
    Button cari, reset, batal;
    JSONParser jParser = new JSONParser();
    JSONArray daftarJenis = null;
    ArrayList<Jenis_Barang> jenis_barangArrayList = new ArrayList<>();
    String pjenis_barang,pnama,pharga0,pharga1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.cari_barang, container, false);
        nama_barang = (EditText)root.findViewById(R.id.nama_barang);
        harga0 = (EditText)root.findViewById(R.id.harga0);
        harga1 = (EditText)root.findViewById(R.id.harga1);
        harga0antara = (EditText)root.findViewById(R.id.harga0Antara);
        harga1antara = (EditText)root.findViewById(R.id.harga1Antara);
        spinner = (Spinner)root.findViewById(R.id.spinner);
        checkharga0 = (CheckBox)root.findViewById(R.id.checkharga0);
        checkharga1 = (CheckBox)root.findViewById(R.id.checkharga1);
        checkhargaantara = (CheckBox)root.findViewById(R.id.checkHargaAntara);
        cari = (Button)root.findViewById(R.id.butCari);
        reset = (Button)root.findViewById(R.id.butReset);
        batal = (Button)root.findViewById(R.id.butBatal);

        new BacaJenisBarang().execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pjenis_barang = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        harga0.setEnabled(false);
        harga1.setEnabled(false);
        harga0antara.setEnabled(false);
        harga1antara.setEnabled(false);



        checkharga0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkharga0.isChecked()){
                    checkharga1.setChecked(false);
                    checkhargaantara.setChecked(false);
                    harga0.setEnabled(true);

                    harga1.setEnabled(false);
                    harga0antara.setEnabled(false);
                    harga1antara.setEnabled(false);
                }

                if (!checkharga0.isChecked()){
                    harga0.setText("");
                    harga0.setEnabled(false);

                }

            }
        });
        checkharga1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkharga1.isChecked()){
                    checkharga0.setChecked(false);
                    checkhargaantara.setChecked(false);
                    harga0.setEnabled(false);
                    harga1.setEnabled(true);

                    harga0antara.setEnabled(false);
                    harga1antara.setEnabled(false);
                }



                if (!checkharga1.isChecked()){
                    harga1.setText("");
                    harga1.setEnabled(false);

                }
            }
        });
        checkhargaantara.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkhargaantara.isChecked()){
                    checkharga0.setChecked(false);
                    checkharga1.setChecked(false);
                    harga0.setEnabled(false);
                    harga1.setEnabled(false);
                    harga0antara.setEnabled(true);
                    harga1antara.setEnabled(true);

                }



                if (!checkhargaantara.isChecked()){
                    harga0antara.setText("");
                    harga1antara.setText("");
                    harga0antara.setEnabled(false);
                    harga1antara.setEnabled(false);

                }
            }
        });

        harga0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (harga0.getText().toString().startsWith("0")){
                    harga0.setText(harga0.getText().toString().replaceFirst("0",""));
                }
            }
        });
        harga1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (harga1.getText().toString().startsWith("0")){
                    harga1.setText(harga1.getText().toString().replaceFirst("0",""));
                }
            }
        });
        harga0antara.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (harga0antara.getText().toString().startsWith("0")){
                    harga0antara.setText(harga0antara.getText().toString().replaceFirst("0",""));
                }
            }
        });
        harga1antara.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (harga1antara.getText().toString().startsWith("0")){
                    harga1antara.setText(harga1antara.getText().toString().replaceFirst("0",""));
                }
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnama = nama_barang.getText().toString();
                if (checkharga0.isChecked()) {
                    pharga0 = harga0.getText().toString();
                    pharga1 = "0";
//                    if (pharga0.equalsIgnoreCase("0")){
//                        pharga0 = null;
//                    }
                }
                if (checkharga1.isChecked()) {
                    pharga0 = "0";
                    pharga1 = harga1.getText().toString();
//                    if (pharga1.equalsIgnoreCase("0")){
//                        pharga1 = null;
//                    }
                }
                if (checkhargaantara.isChecked()) {
                    pharga0 = harga0antara.getText().toString();
                    pharga1 = harga1antara.getText().toString();
//                    if (pharga0.equalsIgnoreCase("0") || pharga1.equalsIgnoreCase("0")){
//                        pharga0 = null;
//                        pharga1 = null;
//                    }
                }


                if (checkharga0.isChecked() || checkharga1.isChecked() || checkhargaantara.isChecked()) {
                    if (checkharga0.isChecked() && pharga0.matches("")) {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                "Silahkan isi kolom harga0",Toast.LENGTH_LONG).show();
                        pharga0 = null;
                        pharga1 = null;

                    } else if (checkharga1.isChecked() && pharga1.matches("")) {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                "Silahkan isi kolom harga1",Toast.LENGTH_LONG).show();
                        pharga0 = null;
                        pharga1 = null;
                    } else if (checkhargaantara.isChecked()
                            && pharga0.matches("") || pharga1.matches("")) {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                "Silahkan isi kolom harga2",Toast.LENGTH_LONG).show();
                        pharga0 = null;
                        pharga1 = null;
                    }

                }
                if (pjenis_barang != null) {


                if (pnama.matches("") && pharga0 == null && pjenis_barang.equalsIgnoreCase("jenis barang")) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Silahkan isi kolom yang ingin anda cari", Toast.LENGTH_LONG).show();
                }
//                if (!pnama.matches("") || pharga0 != null || !pjenis_barang.equalsIgnoreCase("jenis_barang"))
                else {
                    Bundle b = new Bundle();
                    if (!pnama.matches("")) {
                        b.putString("nama", pnama);
                    }
                    if (checkharga0.isChecked() || checkharga1.isChecked() || checkhargaantara.isChecked()) {
                        b.putString("harga0", pharga0);
                        b.putString("harga1", pharga1);
                    }
                    if (!pjenis_barang.equalsIgnoreCase("jenis barang")) {
                        b.putString("jenis_barang", pjenis_barang);
                    }
//                    Log.d("harga0",pharga0);
//                    Log.d("harga1",pharga1);
                    hasil_cari_barang hcb = new hasil_cari_barang();
                    hcb.setArguments(b);
                    jenis_barangArrayList.clear();
                    pharga0 = null; //direset aja :v
                    pharga1 = null;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, hcb, "hasil_cari_barang");
                    ft.addToBackStack("cari_barang");
                    ft.commit();

                }
            }
            else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Gagal terhubung, mohon perisa jaringan internet anda",Toast.LENGTH_LONG).show();
                    new BacaJenisBarang().execute();
                }




            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama_barang.setText("");
                harga0.setText("");
                harga1.setText("");
                harga0antara.setText("");
                harga1antara.setText("");
                spinner.setSelection(getIndex(spinner,"jenis barang"));
                checkharga0.setChecked(false);
                checkharga1.setChecked(false);
                checkhargaantara.setChecked(false);

            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cari Barang");
    }

    private int getIndex(Spinner spinner, String sJenis){
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equals(sJenis)){
                index =i;
            }
        }
        return index;
    }


    class BacaJenisBarang extends AsyncTask<String, Void, String>{
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
                        jenis_barangArrayList.add(jb);
                    }
                    jb = new Jenis_Barang();
                    jb.setJenis_barang("jenis barang");
                    jenis_barangArrayList.add(0,jb);

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
                Toast.makeText(getActivity().getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
            }
            else {
                ArrayAdapter<Jenis_Barang> adapter = new ArrayAdapter<Jenis_Barang>(getActivity().getApplicationContext(),
                        R.layout.spinner_item,jenis_barangArrayList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }
    }


}
