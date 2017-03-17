package com.may1722.t_go.networking;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alxdaly on 3/16/2017.
 */

public class CreateChatRequest extends Request {
    private int chatId = -1;

    protected URL getUrl() {
        try {
            URL url = new URL("http://may1722db.ece.iastate.edu/makechatroom.php");
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
                .appendQueryParameter("user1", params[0])
                .appendQueryParameter("user2", params[1]);
        return builder;
    }

    protected void toast() {
//        Toast.makeText(this, "Connection problem.", Toast.LENGTH_LONG).show();
    }

    protected void handleResult(String result){
        if(result.equalsIgnoreCase("ERROR: Chat room not made.")){

        }
        else{
            try {
                JSONObject jsonObject = new JSONObject(result);
                chatId = jsonObject.getInt("chatId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getChatId(){
        return chatId;
    }
}
