package com.may1722.t_go.model;

/**
 * Created by alxdaly on 1/30/2017.
 */

class MessageObject {
    private String user;        //Holds username of user who sent message
    private String message;     //Holds message

    /**
     * Constructor method
     *
     * @param user      username of user who sent message
     * @param message   message sent
     */
    public MessageObject(String user, String message) {
        this.user = user;
        this.message = message;
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
}
