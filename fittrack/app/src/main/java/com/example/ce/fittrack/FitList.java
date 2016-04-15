package com.example.ce.fittrack;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FitList extends ListActivity implements View.OnClickListener {

    EditText etAdd;
    Button btnAdd;
    Button btnDel;
    AppHelper appHelper = new AppHelper();

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_list);

        btnAdd = (Button) findViewById(R.id.fit_list_btn_add);
        btnAdd.setOnClickListener(this);

        btnDel = (Button) findViewById(R.id.fit_list_btn_del);
        btnDel.setOnClickListener(this);

        // Adding adapter to create list of activities
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);

        createList();

    }

    public void createList() {

        listItems.clear();

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedpreferences.getString(AppHelper.SP_USERNAME, "");

        DataBase info = new DataBase(this);
        info.open();
        ArrayList<String> actList = info.getUserActivities(username);
        info.close();

        for (String temp : actList) {
            addItemsToList(temp);
        }
    }


    public void addItemsToList(String rowText) {
        listItems.add(rowText);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        Bundle storage = new Bundle();
        String txt = ((TextView) v).getText().toString();
        storage.putString(AppHelper.BUNDLE_ACTIVITYNAME, txt);
        gotoIntent(AppHelper.ACT_ACTIVITY_DETAILS, storage);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fit_list_btn_add:

                try {

                    Map<String, String> dbEntries = new HashMap<String, String>();

                    SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    String username = sharedpreferences.getString(AppHelper.SP_USERNAME, "");

                    etAdd = (EditText) findViewById(R.id.fit_list_et_add);
                    String newEntry = etAdd.getText().toString();

                    DataBase entry = new DataBase(this);

                    dbEntries.put(entry.USERNAME, username);
                    dbEntries.put(entry.ACTIVITY_NAME, newEntry);

                    entry.open();

                    entry.addEntry(dbEntries);

                    entry.close();

                    createList();

                    break;

                } catch (Exception e) {

                    System.out.println(e.toString());

                }

                break;

            case R.id.fit_list_btn_del:

                try {

                    etAdd = (EditText) findViewById(R.id.fit_list_et_add);
                    String newEntry = etAdd.getText().toString();

                    DataBase del = new DataBase(this);

                    del.open();

                    del.deleteEntry(newEntry);

                    del.close();

                    createList();

                } catch (Exception e) {

                    appHelper.showToastMessage(FitList.this, e.toString());

                }

                break;

        }
    }

    private void gotoIntent(String className, Bundle b) {

        try {

            Class target = Class.forName(className);
            Intent goTo = new Intent(this, target);
            goTo.putExtras(b);
            startActivity(goTo);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            System.out.println(className);

        }
    }

    private void gotoIntent(String className) {

        System.out.println("DEBUG: gotoIntent");

        try {

            Class target = Class.forName(className);
            Intent goTo = new Intent(this, target);
            startActivity(goTo);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            System.out.println(className);

        }
    }
}