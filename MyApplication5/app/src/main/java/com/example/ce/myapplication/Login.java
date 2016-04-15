package com.example.ce.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends AsyncTask<String, Void, String> {

    private Context context;

    public Login(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }


    @Override
    protected String doInBackground(String... arg0) {

        String userName = arg0[0];
        String userPw = arg0[1];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;


        try {
            data =  "?user_name=" + URLEncoder.encode(userName, "UTF-8");
            data += "&user_pw=" + URLEncoder.encode(userPw, "UTF-8");

//            link = "http://localhost/phptest/login.php" + data;
            link = "http://10.0.2.2/phptest/login.php" + data;
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            System.out.println("DEBUG: " + link);

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("DEBUG: BREK HERE");
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        MainActivity  MA = new MainActivity();
        MA.showProgress(false);
        boolean success = false;
        String jsonStr = result;
        System.out.println("DEBUG: " + jsonStr);
        if (jsonStr != null) {


            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                int query_result = jsonObj.getInt("success");
                if (query_result == 1) {
                    Toast.makeText(context, "user/pw good", Toast.LENGTH_SHORT).show();
                    success = true;
                } else if (query_result == 0) {
                    Toast.makeText(context, "user/pw bad", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Something else", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }

        if (success) {

            try {

                Class target = Class.forName("com.example.ce.fittrack.Menu");
                Intent goTo = new Intent(context, target);
                context.startActivity(goTo);

            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }


        } else {

            showToastMessage("Wrong userName/pw");

        }



    }

    private void showToastMessage(String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }
}