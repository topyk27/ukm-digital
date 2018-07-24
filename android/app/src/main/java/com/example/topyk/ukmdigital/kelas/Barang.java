package com.example.topyk.ukmdigital.kelas;

/**
 * Created by topyk on 6/6/2017.
 */

public class Barang {
    private String id_barang, nama_barang, deskripsi_barang, gambar_barang,
    id_jenis_barang, id_toko, ukuran, merk, khusus;
    private int stok, harga;

    public String getId_barang()
    {
        return id_barang;
    }
    public void setId_barang(String id_barang)
    {
        this.id_barang = id_barang;
    }
//    public String getKode_barang()
//    {
//        return kode_barang;
//    }
//    public void setKode_barang(String kode_barang)
//    {
//        this.kode_barang = kode_barang;
//    }
    public String getNama_barang()
    {
        return nama_barang;
    }
    public void setNama_barang(String nama_barang)
    {
        this.nama_barang = nama_barang;
    }
    public int getStok()
    {
        return stok;
    }
    public void setStok(int stok)
    {
        this.stok = stok;
    }
    public int getHarga(){
        return harga;
    }
    public void setHarga(int harga){
        this.harga = harga;
    }
    public String getDeskripsi_barang()
    {
        return deskripsi_barang;
    }
    public void setDeskripsi_barang(String deskripsi_barang)
    {
        this.deskripsi_barang = deskripsi_barang;
    }
    public String getGambar_barang()
    {
        return gambar_barang;
    }
    public void setGambar_barang(String gambar_barang)
    {
        gambar_barang = gambar_barang.replaceAll(" ", "%20");
        this.gambar_barang = gambar_barang;
    }
    public String getId_jenis_barang(){
        return id_jenis_barang;
    }
    public void setId_jenis_barang(String id_jenis_barang){
        this.id_jenis_barang = id_jenis_barang;
    }
    public String getId_toko(){
        return id_toko;
    }
    public void setId_toko(String id_toko){
        this.id_toko = id_toko;
    }
    public String getUkuran(){
        return ukuran;
    }
    public void setUkuran(String ukuran){
        this.ukuran = ukuran;
    }
    public String getMerk(){
        return merk;
    }
    public void setMerk(String merk){
        this.merk = merk;
    }
    public String getKhusus(){
        return khusus;
    }
    public void setKhusus(String khusus){
        this.khusus = khusus;
    }
}
