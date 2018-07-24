package com.example.topyk.ukmdigital.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_cek_username;

/**
 * Created by topyk on 8/15/2017.
 */

public class Username {
    JSONParser jParser = new JSONParser();

    public Username(){

    }
    public String isUsernameValid(String username){

        List<NameValuePair> p = new ArrayList<>();
        p.add(new BasicNameValuePair("username", username));
        try {
            JSONObject json = jParser.makeHttpRequest(url_cek_username, "POST", p);
            int success = json.getInt("success");
            if (success == 1){
                return "tolak";
            }
            else {
                return "terima";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "exception";
        }
    }
}
