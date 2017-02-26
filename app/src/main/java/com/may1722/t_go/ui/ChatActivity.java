package com.may1722.t_go.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.may1722.t_go.R;
import com.may1722.t_go.model.ChatObject;
import com.may1722.t_go.model.MessageObject;
import com.may1722.t_go.networking.ChatRequest;
import com.may1722.t_go.networking.SendChatRequest;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChatActivity extends AppCompatActivity {
    public static int msgCount = 0;
    public ChatObject chat;
    private String user1;
    private String user2;
    private int chatId;
    private ChatRequest chatRequest;
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
        setChatWith();
        chatRequest = new ChatRequest();
        try {
            chatRequest.execute(String.valueOf(chatId), user1, user2).get();
            chat = chatRequest.getChat();
            chatText = (EditText) findViewById(R.id.chatText);
            chatText.setText("");
            messagesListView = (ListView) findViewById(R.id.messagesListView);
            adapter = new ChatAdapter(this, chat.getMessages());
            messagesListView.setAdapter(adapter);
//        chat.updateMessages(findChat(chatId));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ChatObject findChat(int chatId){
        return chatRequest.getChat();
    }

    private void setChatWith(){
        TextView chatOtherUser = (TextView) findViewById(R.id.chatOtherUser);
        chatOtherUser.setText(" " + user2);
    }

    public void sendMsg(View view){
        String content = chatText.getText().toString();
        content = content.replaceAll("%", "a3rtfa");
        content = content.replaceAll("_", "b3rtfb");
        content = content.replaceAll("'", "c3rtfc");
        content = content.replaceAll("#", "d3rtfd");
        content = content.replaceAll("&", "e3rtfe");
        content = content.replace(' ', '_');
        new SendChatRequest().execute(String.valueOf(chatId), user1, content);
        chat.addMessage(new MessageObject(user1, content, user1));
        adapter.notifyDataSetChanged();
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
