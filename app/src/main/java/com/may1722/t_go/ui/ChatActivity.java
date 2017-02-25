package com.may1722.t_go.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.may1722.t_go.R;
import com.may1722.t_go.model.ChatObject;
import com.may1722.t_go.model.MessageObject;
import com.may1722.t_go.networking.ChatRequest;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    public static int msgCount = 0;
    public ChatObject chat;
    private String user1;
    private String user2;
    private int chatId;
//    private ChatRequest chatRequest= new ChatRequest();
    private EditText chatText;
    private ListView messagesListView;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = getIntent().getExtras();
        user1 = extras.getString("user1");
        user2 = extras.getString("user2");
        chatId = extras.getInt("chatId");
        new ChatRequest().execute(String.valueOf(chatId), user1, user2);
        chat = new ChatObject(chatId, user1, user2);
        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setText("");
        messagesListView = (ListView) findViewById(R.id.messagesListView);
        adapter = new ChatAdapter(this, chat.getMessages());
        messagesListView.setAdapter(adapter);
        chat.updateMessages(findChat(chatId));
    }

    public ArrayList<MessageObject> findChat(int chatId){
        //Setup search for messages
        return new ArrayList<>();
    }

    public void sendMsg(View view){
        String content = chatText.getText().toString();
        content = content.replaceAll("_", "b3rtfb");
        content = content.replaceAll("'", "c3rtfc");
        content = content.replaceAll("#", "d3rtfd");
        content = content.replaceAll("&", "e3rtfe");
        content = content.replace(' ', '_');
        content = content.replaceAll("%", "a3rtfa");
        //Handle communicate with database
        chatText.setText("");
    }

    public void addMessage(final MessageObject msg){
        runOnUiThread(new Runnable() {

            @Override
            public void run(){
                chat.addMessage(msg);
                //
                msgCount++;
            }
        });
    }

    @Override
    public void onBackPressed() {
        //
        super.onBackPressed();
    }
}
