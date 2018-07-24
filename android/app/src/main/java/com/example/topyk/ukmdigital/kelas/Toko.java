package com.example.topyk.ukmdigital.kelas;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by topyk on 7/8/2017.
 */

public class Toko {
    public String nama, id_toko, nama_toko, alamat_toko, telp, deskripsi_toko, gambar, id_kampung, id_anggota;
    public LatLng latLng;

    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public String getId_toko(){
        return id_toko;
    }
    public void setId_toko(String id_toko){
        this.id_toko = id_toko;
    }
    public String getNama_toko(){
        return nama_toko;
    }
    public void setNama_toko(String nama_toko){
        this.nama_toko = nama_toko;
    }
    public String getAlamat_toko(){
        return alamat_toko;
    }
    public void setAlamat_toko(String alamat_toko){
        this.alamat_toko = alamat_toko;
    }
    public String getTelp(){
        return telp;
    }
    public void setTelp(String telp){
        this.telp = telp;
    }
    public String getDeskripsi_toko(){
        return deskripsi_toko;
    }
    public void setDeskripsi_toko(String deskripsi_toko){
        this.deskripsi_toko = deskripsi_toko;
    }
    public String getGambar(){
        return gambar;
    }
    public void setGambar(String gambar){
        gambar = gambar.replaceAll(" ", "%20");
        this.gambar = gambar;
    }
    public String getId_kampung(){
        return id_kampung;
    }
    public void setId_kampung(String id_kampung){
        this.id_kampung = id_kampung;
    }
    public String getId_anggota(){
        return id_anggota;
    }
    public void setId_anggota(String id_anggota){
        this.id_anggota = id_anggota;
    }
    public LatLng getLatLng(){
        return latLng;
    }
    public void setLatLng(LatLng latLng){
        this.latLng = latLng;
    }
    @Override
    public String toString(){
        return id_toko + " : " + nama_toko;
    }



}
