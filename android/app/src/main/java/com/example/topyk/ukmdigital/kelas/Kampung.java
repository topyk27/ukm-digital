package com.example.topyk.ukmdigital.kelas;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by topyk on 7/15/2017.
 */

public class Kampung {
    private String id_kampung, nama_kampung, alamat_kampung, gambar_kampung, nama_kampung_sementara;
    private LatLng latLng;
    public String getId_kampung(){
        return id_kampung;
    }
    public void setId_kampung(String id_kampung){
        this.id_kampung = id_kampung;
    }
    public String getNama_kampung(){
        return nama_kampung;
    }
    public void setNama_kampung(String nama_kampung){
        this.nama_kampung = nama_kampung;
    }
    public String getAlamat_kampung(){
        return alamat_kampung;
    }
    public void setAlamat_kampung(String alamat_kampung){
        this.alamat_kampung = alamat_kampung;
    }
    public String getGambar_kampung(){
        return gambar_kampung;
    }
    public void setGambar_kampung(String gambar_kampung){
        gambar_kampung = gambar_kampung.replaceAll(" ","%20");
        this.gambar_kampung = gambar_kampung;
    }

    public String getNama_kampung_sementara(){
        return nama_kampung_sementara;
    }
    public void setNama_kampung_sementara(String nama_kampung_sementara){
        this.nama_kampung_sementara = nama_kampung_sementara;
    }
    public LatLng getLatLng(){
        return latLng;
    }
    public void setLatLng(LatLng latLng){
        this.latLng = latLng;
    }

    @Override
    public String toString(){
        return nama_kampung;
    }
}
