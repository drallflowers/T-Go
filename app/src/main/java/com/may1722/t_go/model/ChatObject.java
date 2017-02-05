package com.may1722.t_go.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alxdaly on 1/30/2017.
 */

public class ChatObject {
    int chatId;
    ArrayList<MessageObject> messages;  //Holds all of the messages sent via chat
    String user1;                       //Username of user1
    String user2;                       //Username of user2

    /**
     * Initializes chat room
     *
     * @param user1 username of user1
     * @param user2 username of user2
     */
    public ChatObject(int chatId, String user1, String user2) {
        this.chatId = chatId;
        messages = new ArrayList<>();
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * Returns chatId
     *
     * @return  chatId
     */
    public int getChatId(){
        return chatId;
    }

    /**
     * Updates messages
     *
     * @param msgs  All of the messages sent via chat room
     */
    public void updateMessages(ArrayList<MessageObject> msgs){
        messages = msgs;
    }

    /**
     * Add the message to our list of messages
     *
     * @param msg   Message to be added
     */
    public void addMessage(MessageObject msg){
        messages.add(msg);
    }

    /**
     * Returns message at the i-th position
     *
     * @param i position of message to return
     * @return  message at i-th position
     */
    public MessageObject getMessage(int i){
        if(i < 0 || i >= messages.size()){
            return null;
        }
        return messages.get(i);
    }

    public List<MessageObject> getMessages(){
        return messages;
    }

}


