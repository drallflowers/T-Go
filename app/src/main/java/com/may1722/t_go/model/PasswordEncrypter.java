package com.may1722.t_go.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by alxdaly on 4/4/2017.
 */

public class PasswordEncrypter {
    private MessageDigest messageDigest;
    private int salt;
    private Random random;

    public PasswordEncrypter() {
        random = new Random(System.currentTimeMillis());
        salt = random.nextInt();
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public PasswordEncrypter(int salt) {
        random = new Random(System.currentTimeMillis());
        this.salt = salt;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String hash(String password){
        byte[] passwordBytes = password.getBytes();
        messageDigest.update((byte) salt);
        byte[] hashed = messageDigest.digest(passwordBytes);
        return new String(hashed);
    }

    public int getSalt(){
        return salt;
    }
}
