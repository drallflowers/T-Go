package com.may1722.t_go.ui;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.may1722.t_go.R;
import com.may1722.t_go.model.ChatObject;
import com.may1722.t_go.networking.ChatRequest;
import com.may1722.t_go.networking.SendChatRequest;

import java.util.concurrent.ExecutionException;

public class ChatActivity extends AppCompatActivity {
    public ChatObject chat;
    private String user1;
    private String user2;
    private int chatId;
    private ChatRequest chatRequest;
    private EditText chatText;
    private ListView messagesListView;
    private ChatAdapter adapter;
    private boolean ContinueChat = true;

    /**
     * Does all of the initial setup for Chat
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = getIntent().getExtras();
        user1 = extras.getString("user1");                                      //Sets up user1
        user2 = extras.getString("user2");                                      //Sets up user2
        chatId = extras.getInt("chatID");                                       //Sets up chatId
        setChatWith();
        chatRequest = new ChatRequest();
        try {
            chatRequest.execute(String.valueOf(chatId), user1, user2).get();    //Initial setup of messages listview
            chat = chatRequest.getChat();
            chatText = (EditText) findViewById(R.id.chatText);
            chatText.setText("");
            messagesListView = (ListView) findViewById(R.id.messagesListView);
            adapter = new ChatAdapter(this, chat.getMessages());
            messagesListView.setAdapter(adapter);
            new MessageUpdater().execute();                                      //Runs thread to update messagesListView as new messages are sent
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up title of window to show user who they are talking to.
     */
    private void setChatWith() {
        TextView chatOtherUser = (TextView) findViewById(R.id.chatOtherUser);
        chatOtherUser.setText(" " + user2);
    }

    /**
     * Records message from user in database.
     *
     * @param view
     */
    public void sendMsg(View view) {
        String content = chatText.getText().toString();
        content = content.replaceAll("%", "a3rtfa");
        content = content.replaceAll("_", "b3rtfb");
        content = content.replaceAll("'", "c3rtfc");
        content = content.replaceAll("#", "d3rtfd");
        content = content.replaceAll("&", "e3rtfe");
        content = content.replace(' ', '_');
        new SendChatRequest().execute(String.valueOf(chatId), user1, content);
        chatText.setText("");
    }

    @Override
    public void onBackPressed() {
        ContinueChat = false;
        super.onBackPressed();
    }

    /**
     * AsyncTask to update messagesListView as messages are sent
     */
    private class MessageUpdater extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params){
            return null;
        }

        /**
         * Sets up new thread to update messagesListView
         *
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(ContinueChat){                                                                    //While chat window is still open...
                        try {
                            chatRequest.cancel(true);
                            chatRequest = null;
                            chatRequest = new ChatRequest();                                                //Run ChatRequest again
                            chatRequest.execute(String.valueOf(chatId), user1, user2).get();
                            if (chat.getMessages().size() < chatRequest.getChat().getMessages().size()) {   //If a new message has been set,...
                                ChatActivity.this.runOnUiThread(new Runnable() {                            //On the UI Thread
                                    @Override
                                    public void run() {
                                        chat = chatRequest.getChat();                                       //Update chat object
                                        adapter = new ChatAdapter(ChatActivity.this, chat.getMessages());   //Update messagesListView
                                        messagesListView.setAdapter(adapter);
                                    }
                                });
                            }
                            Thread.sleep(500);                                                               //Wait half a second
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        }
    }
}
