package com.may1722.t_go.model;

import java.util.ArrayList;

/**
 * Created by alxdaly on 1/30/2017.
 */

public class ChatObject {
    ArrayList<MessageObject> messages;  //Holds all of the messages sent via chat
    String user1;                       //Username of user1
    String user2;                       //Username of user2

    /**
     * Initializes chat room
     *
     * @param user1 username of user1
     * @param user2 username of user2
     */
    public ChatObject(String user1, String user2) {
        messages = new ArrayList<>();
        this.user1 = user1;
        this.user2 = user2;
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
}


