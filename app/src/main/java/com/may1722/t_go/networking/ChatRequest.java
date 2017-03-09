package com.may1722.t_go.networking;

import android.net.Uri;
import android.widget.Toast;

import com.may1722.t_go.model.ChatObject;
import com.may1722.t_go.model.MessageObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alxdaly on 2/24/2017.
 */

public class ChatRequest extends Request {
    ChatObject chat;
    String[] params;

    /**
     * Set up URL to use chat.php
     *
     * @return
     */
    protected URL getUrl() {
        try {
            URL url = new URL("http://may1722db.ece.iastate.edu/chat.php");
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set as GET
     *
     * @return
     */
    protected String getPostGet(){
        return "GET";
    }

    /**
     * Build Uri.Builder to make php call
     *
     * @param params
     * @return
     */
    protected Uri.Builder getUriBuilder(String... params){
        this.params = params;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("chat_id", params[0]);
        return builder;
    }

    /**
     * Handle the results of running chat.php
     *
     * @param result
     */
    protected void handleResult(String result){
        if(result.equalsIgnoreCase("ERROR: Chat not found.")){

        }
        else if(result.equalsIgnoreCase("ERROR: User(s) not found.")){

        }
        else{
            chat = new ChatObject(Integer.parseInt(params[0]), params[1], params[2]);
            try {
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<MessageObject> msgs = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    MessageObject newMsg = new MessageObject(object.getString("from_user"), object.getString("message"), params[1]);
                    msgs.add(newMsg);
                }
                chat.updateMessages(msgs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ChatObject getChat(){
        return chat;
    }
}
