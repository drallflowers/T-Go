package com.may1722.t_go.networking;

import android.net.Uri;
import android.widget.Toast;

import com.may1722.t_go.model.ChatObject;
import com.may1722.t_go.model.MessageObject;
import com.may1722.t_go.ui.ChatActivity;

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

    protected URL getURL() {
        try {
            URL url = new URL("may1722db.ece.iastate.edu/chat.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    protected String getPostGet(){
        return "GET";
    }

    protected Uri.Builder getUriBuilder(String... params){
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("chat_id", params[0]);
        return builder;
    }

    protected void toast() {
//        Toast.makeText(this, "Connection problem.", Toast.LENGTH_LONG).show();
    }

    protected void handleResult(String result, String... params){
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
                    msgs.add(new MessageObject(object.getString("from_user"), object.getString("message"), params[1]));
                }
                chat.updateMessages(msgs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected ChatObject getChat(){
        return chat;
    }
}
