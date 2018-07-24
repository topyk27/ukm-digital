package com.example.topyk.ukmdigital.sub_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topyk.ukmdigital.R;

/**
 * Created by topyk on 9/16/2017.
 */

public class konfirmasi_bikin_toko extends Fragment {
    TextView tv;
    Button but_buat_toko;
    CheckBox checkBox;
    String text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.konfirmasi_bikin_toko, container, false);
        tv = (TextView)rootView.findViewById(R.id.textBuatToko);
        checkBox = (CheckBox)rootView.findViewById(R.id.cekBuatToko);
        but_buat_toko = (Button)rootView.findViewById(R.id.but_buat_toko);
        text = "\t Dengan mendaftar sebagai Anggota UKM, maka pengguna tidak dapat melakukan pembelian di dalam sistem ini." +
                " Sebagai Anggota UKM, pengguna harus menjual barang dagangannya sesuai dengan prosedur." +
                " Penjual dilarang memanipulasi harga Barang dengan tujuan apapun." +
                " Penjual wajib memberikan foto dan informasi produk dengan lengkap dan jelas sesuai dengan kondisi dan kualitas produk." +
                " Apabila terdapat ketidaksesuaian antara foto dan informasi produk yang diunggah oleh penjual dengan produk" +
                " yang diterima oleh pembeli, maka pembeli berhak menuntut penjual." +
                " Penjual wajib memberikan balasan untuk menerima atau menolak pesanan pihak pembeli." +
                " Penjual memahami dan menyetujui bahwa pembayaran ongkos kirim sepenuhnya dibayar oleh penjual(tidak dimasukkan ke dalam harga produk)." +
                " Dalam keadaan penjual hanya dapat memenuhi sebagian dari jumlah barang yang dipesan pembeli," +
                " maka penjual wajib memberikan keterangan kepada pembeli." +
                " Penjual dilarang mempergunakan etalase(termasuk dan tidak terbatas pada informasi UKM dan informasi barang)" +
                " sebagai media untuk beriklan atau melakukan promosi ke halaman situs lain." +
                " Penamaan barang harus dilakukan sesuai dengan informasi detail,dengan demikian penjual tidak diperkenankan untuk" +
                " mencantumkan nama dan/atau kata yang tidak berkaitan dengan barang tersebut." +
                " Penjual tidak diperkenankan memperdagangkan jasa, atau barang non-fisik";
        tv.setText(text);

        but_buat_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked()){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Mohon setjui ketentuan di atas",Toast.LENGTH_LONG).show();
                }
                else {
                    tambah_toko tk = new tambah_toko();
                    FragmentTransaction ft = getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, tk, "tambah_toko");
                    ft.addToBackStack("konfirmasi_bikin_toko");
                    ft.commit();
                }

            }
        });

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Daftar UKM");
    }
}
