package com.example.ce.fittrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SQLView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlview);

        TextView tv = (TextView)findViewById(R.id.sqlview_out);
        DataBase info = new DataBase(this);
        info.open();
        String data = info.getDataAsString();
        info.close();
        tv.setText(data);



    }

}
