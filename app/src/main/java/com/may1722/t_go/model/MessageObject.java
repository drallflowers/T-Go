package com.may1722.t_go.model;

import android.content.Context;

/**
 * Created by alxdaly on 1/30/2017.
 */

public class MessageObject {
    String user;        //Holds username of user who sent message
    String message;     //Holds message
    boolean self;

    /**
     * Constructor method
     *
     * @param user      username of user who sent message
     * @param message   message sent
     */
    public MessageObject(String user, String message, String currUser) {
        this.user = user;
        this.message = fixMessage(message);
        if(user.equals(currUser)){
            this.self = true;
        }
        else{
            this.self = false;
        }
    }

    /**
     * Returns username of sender
     *
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns message sent
     *
     * @return  message
     */
    public String getMessage() {
        return message;
    }

    public boolean isSelf() {
        return self;
    }

    /**
     * Fix messages from database to show symbols.
     *
     * @param message
     * @return  message
     */
    private String fixMessage(String message){
        message = message.replaceAll("a3rtfa", "%");
        message = message.replaceAll("b3rtfb", "_");
        message = message.replaceAll("c3rtfc", "'");
        message = message.replaceAll("d3rtfd", "#");
        message = message.replaceAll("e3rtfe", "&");
        message = message.replace('_', ' ');
        return message;
    }
}
