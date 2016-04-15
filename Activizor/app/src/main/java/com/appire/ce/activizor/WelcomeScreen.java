package com.appire.ce.activizor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    private AppHelper appHelper = new AppHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Thread timer = new Thread(){
            public void run(){

                try{

                    sleep(1000);

                }

                catch (InterruptedException e){

                    e.printStackTrace();

                }

                finally{

                    Intent goTo = new Intent(WelcomeScreen.this, LoginScreen.class);
                    startActivity(goTo);

//                    try {
//
//                        String targetClass =  appHelper.PROJECT + ".LoginScreen";
//
//                        Class target = Class.forName(targetClass);
////                        Class target = Class.forName("LoginScreen");
//                        Intent goTo = new Intent(WelcomeScreen.this, LoginScreen.class);
//                        startActivity(goTo);
//
//
//                    }
//
//                    catch (ClassNotFoundException e) {
//
//                        e.printStackTrace();
//                        System.out.println("DEBUG:Menu");
//
//                    }
                }
            };
        };

        timer.start();



    }
}
