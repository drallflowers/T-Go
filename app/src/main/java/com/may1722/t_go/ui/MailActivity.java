package com.may1722.t_go.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.MailObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;


public class MailActivity extends AppCompatActivity {

    private String userID;
    private static int pos;
    static MailAdapter m;
    static String user_type;
    static ArrayList<MailObject> mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        userID = getIntent().getExtras().getString("userID");

        ListView mailList = (ListView) findViewById(R.id.mailView);
        mail = new ArrayList<>();
        m = new MailAdapter(this, mail);
        mailList.setAdapter(m);
        new AsyncGetMail().execute(userID);
    }

    public void viewMailFrag(){
        System.out.println(pos);
        if(mail.get(pos).getMail_type() == 0){
            DialogFragment newFragment = new MailItemFragment();
            newFragment.show(getSupportFragmentManager(), "test");
        }
        else{
            DialogFragment newFragment = new MailActionItemFragment();
            newFragment.show(getSupportFragmentManager(), "tag");
        }
    }


    private class AsyncGetMail extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getmail.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0]);
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
                // TODO Auto-generated catch block
                e1.printStackTrace();
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
                    //Toast.makeText(JobBoardActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MailActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("connection failure")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(MailActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    addItemsToList(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MailActivity.this, "OOPs! Something went wrong. Bad Results Returned.", Toast.LENGTH_LONG).show();
                }

            }
        }

    }
    private static class AsyncDeleteMail extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/deletemail.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0]);
                System.out.println(params[0]);
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
                // TODO Auto-generated catch block
                e1.printStackTrace();
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
                    //Toast.makeText(JobBoardActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("fail");
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("connection failure")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                System.out.println("fail");
            } else {
                mail.remove(pos);
                m.notifyDataSetChanged();

            }
        }

    }

    private static class AsyncUpdateUser extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/upgradeuser.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                System.out.println("params:"+ params[1]);

                int type2 = Integer.parseInt(params[1])+1;
                String type = String.valueOf(type2);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", params[0]).appendQueryParameter("type", type);
                System.out.println(params[0]);
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
                // TODO Auto-generated catch block
                e1.printStackTrace();
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
                    //Toast.makeText(JobBoardActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("fail");
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("connection failure")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                System.out.println("fail");
            } else {

                m.notifyDataSetChanged();

            }
        }

    }

    private static class AsyncGetInfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getuserinfo.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                System.out.println("id2: "+ params[0]);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", params[0]);
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
                // TODO Auto-generated catch block
                e1.printStackTrace();
                //return "exception";
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
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "connection failuer";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("connection failure")) {
            }
             else{
                    String[] result2 = new String[10];
                    result2 = result.split(",");

                    user_type = result2[5];

                String temp = String.valueOf(mail.get(pos).getFrom_userid());
                System.out.println("type:" +user_type);
                System.out.println(temp);
                new AsyncUpdateUser().execute(temp,user_type);

                }
            }
        }



    public class MailAdapter extends ArrayAdapter<MailObject> {

        public MailAdapter(Context context, ArrayList<MailObject> mail) {
            super(context, 0, mail);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MailObject mailItem = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_mail_item_2, parent, false);
            }

            Button viewMail = (Button) convertView.findViewById(R.id.buttonView);

            viewMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    pos = position;
                    viewMailFrag();

                }
            });

            TextView text = (TextView) convertView.findViewById(R.id.TextPreview);
            if(mailItem.getContent().length()<15){
                text.setText(mailItem.getContent());
            }
            else {
                text.setText(mailItem.getContent().substring(0, 15) + "...");
            }
            return convertView;
        }

    }

    public void addItemsToList(String result) throws JSONException {
        System.out.println(result);
        JSONArray jArray = new JSONArray(result);


        for(int i=0; i<jArray.length(); i++){
            JSONObject jObject = jArray.getJSONObject(i);
            int id = jObject.getInt("mail_id");
            int user_id = jObject.getInt("userid");
            String content = jObject.getString("content");
            int mail_type = jObject.getInt("mail_type");
            int from_userid = jObject.getInt("from_userid");
            mail.add(new MailObject(id, user_id, content, mail_type,from_userid));
        }
        m.notifyDataSetChanged();


    }


    public static class MailActionItemFragment extends DialogFragment {
        MailActionItemFragment newInstance(String title) {
            MailActionItemFragment fragment = new MailActionItemFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.mail_action_item, null);

            final TextView id = (TextView) view.findViewById(R.id.UserLabel);
            final TextView applyingFor = (TextView) view.findViewById(R.id.ApplyingForLabel);

            //connect to the edit_quantity to get original amount


            Button b = (Button) view.findViewById(R.id.buttonAccept);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = String.valueOf(mail.get(pos).getFrom_userid());


                    new AsyncGetInfo().execute(temp);
                    System.out.println(user_type);

                    MailActionItemFragment.this.dismiss();
                    new AsyncDeleteMail().execute(temp);
                }
            });

            Button b2 = (Button) view.findViewById(R.id.buttonDecline);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String temp = String.valueOf(mail.get(pos).getMail_id());
                    System.out.println("id: " +temp);
                    MailActionItemFragment.this.dismiss();
                     new AsyncDeleteMail().execute(temp);

                }
            });
            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle("")
                    .setView(view)
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            }).create();
        }
    }
    public static class MailItemFragment extends DialogFragment {
        MailItemFragment newInstance(String title) {
            MailItemFragment fragment = new MailItemFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.mail_item, null);

            final TextView content = (TextView) view.findViewById(R.id.textContent);

            content.setText(mail.get(pos).getContent());



            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle("")
                    .setView(view)
                    .setPositiveButton("Back",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {


                                }
                            })
                    .setNegativeButton("Delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    String temp = String.valueOf(mail.get(pos).getMail_id());
                                    System.out.println("id: " +temp);
                                new AsyncDeleteMail().execute(temp);
                                }
                            }).create();
        }
    }
}


