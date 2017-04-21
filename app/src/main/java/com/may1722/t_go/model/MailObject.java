package com.may1722.t_go.model;

/**
 * Created by David on 3/28/2017.
 */

public class MailObject {

    private int mail_id;
    private int user_id;
    private String username;
    private int user_type;
    private String content;
    private int mail_type;
    private int from_userid;

    public MailObject(int id, int user, String name, int usertype, String c, int type,int fromid){
        mail_id = id;
        user_id = user;
        username = name;
        user_type = usertype;
        content = c;
        mail_type = type;
        from_userid = fromid;
    }


    public int getMail_id(){
        return mail_id;
    }
    public int getUser_id(){
        return user_id;
    }
    public String getUserName(){
        return username;
    }
    public int getUserType(){
        return user_type;
    }
    public String getContent(){
        return content;
    }
    public int getMail_type(){
        return mail_type;
    }
    public int getFrom_userid(){
        return from_userid;
    }
}
