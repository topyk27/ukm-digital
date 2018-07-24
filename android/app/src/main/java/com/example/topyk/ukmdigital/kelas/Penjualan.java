package com.example.topyk.ukmdigital.kelas;

import com.example.topyk.ukmdigital.util.SessionManager;

/**
 * Created by topyk on 7/17/2017.
 */

public class Penjualan {
    private String id_transaksi, id_barang, waktu, id_anggota, status, nama_barang, nama_pembeli, alamat;
    private int jumlah_barang, total,harga,jumlah_seluruh;

    public String getId_transaksi(){
        return id_transaksi;
    }
    public void setId_transaksi(String id_transaksi){
        this.id_transaksi = id_transaksi;
    }
    public String getId_barang(){
        return id_barang;
    }
    public void setId_barang(String id_barang){
        this.id_barang = id_barang;
    }
    public String getWaktu(){
        return waktu;
    }
    public void setWaktu(String waktu){
        this.waktu = waktu;
    }
    public String getId_anggota(){
        return id_anggota;
    }
    public void setId_anggota(String id_anggota){
        this.id_anggota = id_anggota;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public int getJumlah_barang(){
        return jumlah_barang;
    }
    public void setJumlah_barang(int jumlah_barang){
        this.jumlah_barang = jumlah_barang;
    }
    public int getTotal(){
        return total;
    }
    public void setTotal(int total){
        this.total = total;
    }

    public String getNama_barang(){
        return nama_barang;
    }
    public void setNama_barang(String nama_barang){
        this.nama_barang = nama_barang;
    }
    public String getNama_pembeli(){
        return nama_pembeli;
    }
    public void setNama_pembeli(String nama_pembeli){
        this.nama_pembeli = nama_pembeli;
    }
    public String getAlamat(){
        return alamat;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    public int getHarga(){
        return harga;
    }
    public void setHarga(int harga){
        this.harga = harga;
    }
    public int getJumlah_seluruh(){
        return jumlah_seluruh;
    }
    public void setJumlah_seluruh(int jumlah_seluruh){
        this.jumlah_seluruh = jumlah_seluruh;
    }
}
