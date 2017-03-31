package com.may1722.t_go.networking;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alxdaly on 2/25/2017.
 */

public class SendChatRequest extends Request {

    protected URL getUrl() {
        try {
            URL url = new URL("http://may1722db.ece.iastate.edu/sendchat.php");
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
                .appendQueryParameter("chat_id", params[0])
                .appendQueryParameter("sender", params[1])
                .appendQueryParameter("message", params[2]);
        return builder;
    }

    protected void toast() {
//        Toast.makeText(this, "Connection problem.", Toast.LENGTH_LONG).show();
    }

    protected void handleResult(String result){
    }
}
