package com.example.ce.fittrack;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by CE on 5/3/2016.
 */
public class Menu extends ListActivity {

//    String menuItems[] =  getResources().getStringArray(R.array.main_menu_items);
    String menuItems[] = {"FitList", "test", "Other Stuff"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, menuItems));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        System.out.println("DEBUG:com.example.ce.fittrack." + menuItems[position]);

        try {

            Class target = Class.forName("com.example.ce.fittrack." + menuItems[position]);
            Intent goTo = new Intent(Menu.this, target);
            startActivity(goTo);

        }

        catch (ClassNotFoundException e) {

            e.printStackTrace();
            System.out.println("DEBUG:com.example.ce.fittrack." + menuItems[position]);

        }
    }


}
