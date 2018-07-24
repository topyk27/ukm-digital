package com.example.topyk.ukmdigital.kelas;

/**
 * Created by topyk on 7/4/2017.
 */

public class Anggota {
    private String id, nama, alamat, no_hp, email, gambar, username, password;

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public String getAlamat(){
        return alamat;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    public String getNo_hp(){
        return no_hp;
    }
    public void setNo_hp(String no_hp){
        this.no_hp = no_hp;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getGambar(){
        return gambar;
    }
    public void setGambar(String gambar){
        gambar = gambar.replaceAll(" ","%20");
        this.gambar = gambar;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString(){
        return id + " : " + nama;
    }
}
