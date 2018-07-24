package com.example.topyk.ukmdigital.sub_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.util.JSONParser;

/**
 * Created by topyk on 8/28/2017.
 */

public class cari_anggota extends Fragment {
    EditText nama, alamat;
    CheckBox check0,check1;
    Button cari, reset, batal;
    String pnama,palamat,ptoko;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.cari_anggota, container, false);
        nama = (EditText)root.findViewById(R.id.nama);
        alamat = (EditText)root.findViewById(R.id.alamat);
        check0 = (CheckBox)root.findViewById(R.id.check0);
        check1 = (CheckBox)root.findViewById(R.id.check1);
        cari = (Button)root.findViewById(R.id.but_cari);
        reset = (Button)root.findViewById(R.id.but_reset);
        batal = (Button)root.findViewById(R.id.but_batal);

        check0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check0.isChecked()){
                    check1.setChecked(false);
                }
            }
        });
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check1.isChecked()){
                    check0.setChecked(false);
                }
            }
        });
        nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nama.getText().toString().startsWith(" ")){
                    nama.setText(nama.getText().toString().replaceFirst(" ",""));
                }
            }
        });
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
            }
        });
        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnama = nama.getText().toString();
                palamat = alamat.getText().toString();
                if (check0.isChecked()){
                    ptoko = "0";
                }
                if (check1.isChecked()){
                    ptoko = "1";
                }
                if (pnama.matches("") && palamat.matches("") && ptoko == null){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Silahkan isi kolom pencarian",Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle b = new Bundle();
                    if (!pnama.matches("")){
                        b.putString("nama",pnama);
                    }
                    if (!palamat.matches("")){
                        b.putString("alamat",palamat);
                    }
                    if (ptoko != null){
                        b.putString("toko",ptoko);
                    }
                    hasil_cari_anggota hca = new hasil_cari_anggota();
                    hca.setArguments(b);
                    ptoko = null;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame,hca,"hasil_cari_anggota");
                    ft.addToBackStack("cari_anggota");
                    ft.commit();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama.setText("");
                alamat.setText("");
                check0.setChecked(false);
                check1.setChecked(false);
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
        getActivity().setTitle("Cari Anggota");
    }

}
