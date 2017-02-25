package com.may1722.t_go.networking;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alxdaly on 2/23/2017.
 */

public abstract class Request extends AsyncTask<String, String, String> {
    HttpURLConnection conn;
    URL url = null;

    @Override
    protected String doInBackground(String... params) {
        url = getUrl();
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(getPostGet());

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = getUriBuider(params);
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            e1.printStackTrace();
            toast();
            return "exception";
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
                // Pass data to onPostExecute method

            } else {
                toast();
                return "connection failure";
            }

        } catch (IOException e) {
            e.printStackTrace();
            toast();
            return "connection failure";
        } finally {
            conn.disconnect();
        }

    }

    @Override
    protected void onPostExecute(String result) {

        handleResult(result);

    }

    protected URL getUrl(){
        try {
            URL url = new URL("");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    protected String getPostGet(){
        return "POST";
    }

    protected Uri.Builder getUriBuider(String... params){
        Uri.Builder builder = new Uri.Builder();
        return builder;
    }

    protected void toast(){

    }

    protected void handleResult(String result, String... parmas){

    }

}
