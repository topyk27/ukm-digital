package com.example.topyk.ukmdigital.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.example.topyk.ukmdigital.MainActivity;
import java.util.HashMap;

/**
 * Created by topyk on 3/31/2017.
 */
@SuppressLint("ComitPrefEdits")
public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Sesi";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_ADMIN = "AdminLogin";
    public static final String KEY_NAME = "nama";
    public static final String KEY_EMAIl = "email";
    public static final String KEY_ID_ADMIN = "id_admin";
    public static final String KEY_ID = "id_anggota";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_NO_HP = "no_hp";
    public static final String KEY_GAMBAR = "gambar";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String name, String email, String gambar, String username, String password)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIl, email);
        gambar = gambar.replaceAll(" ","%20");
        editor.putString(KEY_GAMBAR, gambar);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void createAdminLogin(String id, String name, String email, String gambar, String username, String password){
        editor.putBoolean(IS_ADMIN, true);
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID_ADMIN, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIl, email);
        gambar = gambar.replaceAll(" ","%20");
        editor.putString(KEY_GAMBAR, gambar);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void checkLogin()
    {
        if (!this.isLoggedIn())
        {
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);

        }

    }

    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIl, pref.getString(KEY_EMAIl, null));
        user.put(KEY_GAMBAR, pref.getString(KEY_GAMBAR, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    public HashMap<String, String> getAdminDetails(){
        HashMap<String,String> admin = new HashMap<String, String>();
        admin.put(KEY_ID_ADMIN, pref.getString(KEY_ID_ADMIN, null));
        admin.put(KEY_NAME, pref.getString(KEY_NAME, null));
        admin.put(KEY_EMAIl, pref.getString(KEY_EMAIl, null));
        admin.put(KEY_GAMBAR, pref.getString(KEY_GAMBAR, null));
        admin.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        admin.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return admin;
    }

    public void logoutUser()
    {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isAdmin(){
        return pref.getBoolean(IS_ADMIN, false);
    }
}

