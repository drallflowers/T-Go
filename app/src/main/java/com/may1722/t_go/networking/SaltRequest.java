package com.may1722.t_go.networking;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alxdaly on 4/4/2017.
 */

public class SaltRequest extends Request {
    private int salt;

    protected URL getUrl() {
        try {
            URL url = new URL("http://may1722db.ece.iastate.edu/getsalt.php");
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getPostGet(){
        return "POST";
    }

    protected Uri.Builder getUriBuilder(String... params){
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", params[0]);
        return builder;
    }

    protected void toast() {
//        Toast.makeText(this, "Connection problem.", Toast.LENGTH_LONG).show();
    }

    protected void handleResult(String result){
        if(result.equalsIgnoreCase("ERROR: Chat room not made.")){

        }
        else {
            salt = Integer.parseInt(result);
        }
    }

    public int getSalt() {
        return salt;
    }
}
