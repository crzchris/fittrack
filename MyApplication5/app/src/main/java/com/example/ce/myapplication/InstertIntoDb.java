package com.example.ce.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ce.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class InstertIntoDb extends Activity {
    /** Called when the activity is first created. */
    private Button login;
    private EditText username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

        login = (Button) findViewById(R.id.login);
//        username = (EditText) findViewById(R.id.username);
//        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String   mUsername = username.getText().toString();
                String  mPassword = password.getText().toString();

                tryLogin(mUsername, mPassword);
            }
        });
    }

    protected void tryLogin(String mUsername, String mPassword)
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "username="+mUsername+"&password="+mPassword;

        try
        {
            url = new URL("your login URL");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            // You can perform UI operations here
            Toast.makeText(this,"Message from Server: \n"+ response, 0).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
        }
    }
}