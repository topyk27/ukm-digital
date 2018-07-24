package com.example.topyk.ukmdigital.kelas;

/**
 * Created by topyk on 7/3/2017.
 */

public class Jenis_Barang {
    private String id_jenis_barang, jenis_barang, nama_jenis_barang_sementara;

    public Jenis_Barang(){

    }

    public String getId_jenis_barang(){
        return id_jenis_barang;
    }
    public void setId_jenis_barang(String id_jenis_barang){
        this.id_jenis_barang = id_jenis_barang;
    }
    public String getJenis_barang(){
        return jenis_barang;
    }
    public void setJenis_barang(String jenis_barang){
        this.jenis_barang = jenis_barang;
    }

    public String getNama_jenis_barang_sementara(){
        return nama_jenis_barang_sementara;
    }
    public void setNama_jenis_barang_sementara(String nama_jenis_barang_sementara){
        this.nama_jenis_barang_sementara = nama_jenis_barang_sementara;
    }

    @Override
    public String toString(){
        return jenis_barang;
    }
    public void getSelectedJenis(String id_jenis_barang){

    }
}
