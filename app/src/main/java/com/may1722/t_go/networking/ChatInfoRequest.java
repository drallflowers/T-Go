package com.may1722.t_go.networking;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alxdaly on 3/22/2017.
 */

public class ChatInfoRequest extends Request {
    private int chatId = -1;
    private String user1 = "";
    private String user2 = "";

    protected URL getUrl() {
        try {
            URL url = new URL("http://may1722db.ece.iastate.edu/getchatroominfo.php");
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
                .appendQueryParameter("chatId", params[0])
                .appendQueryParameter("userId", params[1]);
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
                user1 = jsonObject.getString("user1");
                user2 = jsonObject.getString("user2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getChatId(){
        return chatId;
    }

    public String getUser1(){
        return user1;
    }

    public String getUser2(){
        return user2;
    }
}
