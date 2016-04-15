package com.example.ce.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private Login mAuthTask = null;

    // UI references.
    private EditText mPasswordView;
    private EditText mUserNameView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserNameView = (EditText) findViewById(R.id.login_userName);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btn = (Button)findViewById(R.id.email_sign_in_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String username = mUserNameView.getText().toString();
                editor.putString("username", username);
                editor.commit();

                attemptLogin();
//                UserLoginServer uls = new UserLoginServer("admin", "admin");
//                uls.login();


            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Store values at the time of the login attempt.
        String userPw = mPasswordView.getText().toString();
        String userName = mUserNameView.getText().toString();

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(userPw)) {
            mPasswordView.setError("Password required");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError("UserName required");
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            UserLoginServer uls = new UserLoginServer(userName, userPw);
            uls.login();
        }
    }

    public class UserLoginServer {

        private final String mUserName;
        private final String mUserPw;

        UserLoginServer(String userName, String userPw) {

            mUserName = userName;
            mUserPw = userPw;

        }

        private void login() {

            new Login(MainActivity.this).execute(mUserName, mUserPw);

        }
    }

    private void showToastMessage(String msg) {

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);

        toast.show();

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

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
            showProgress(false);
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
}
