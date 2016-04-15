package com.example.ce.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

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

//                    Intent goTo = new Intent("android.intent.action.LoginScreen");
//                    startActivity(goTo);

                    try {

                        Class target = Class.forName("com.example.ce.fittrack.LoginScreen");
                        Intent goTo = new Intent(WelcomeScreen.this, LoginScreen.class);
                        startActivity(goTo);


                    }

                    catch (ClassNotFoundException e) {

                        e.printStackTrace();
                        System.out.println("DEBUG:Menu");

                    }


                    /*
                    Intent goToMain = new Intent("android.intent.action.MAIN");
                    startActivity(goToMain);
                    */

                }
            };
        };

        timer.start();



    }
}
