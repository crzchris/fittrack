package com.example.ce.fittrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Appointments extends Activity {

    static AppHelper appHelper = new AppHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        Bundle b = getIntent().getExtras();
        final String currentAct = b.getString(appHelper.BUNDLE_ACTIVITYNAME);

        CalendarView cv = (CalendarView) findViewById(R.id.cv_acitivtyDetails);
        cv.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                final String dateString = AppHelper.datePiecesToString(year, month + 1, dayOfMonth);
                final Date javaDate = appHelper.stringToJavaDate(dateString);

                CharSequence options[] = new CharSequence[]{"Show", "Add", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Appointments.this);
                builder.setTitle("Appointments");
                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        switch (position) {

                            case 0:

                                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(Appointments.this);
                                String username = sharedpreferences.getString("username", "");

                                DataBase info = new DataBase(Appointments.this);
                                info.open();
                                ArrayList<Map> actList = info.getAppointmentsByDate(username, dateString, currentAct);
                                String cActivityName = info.ACTIVITY_NAME;
                                String cStartTime = info.STARTTIME;
                                String cEndTime = info.ENDTIME;
                                String cLocation = info.LOCATION;
                                info.close();

                                int activityCount = actList.size();

                                if (activityCount == 0) {

                                    Dialog d = new Dialog(Appointments.this);
                                    d.setTitle("DISPLAY");
                                    TextView tv = new TextView(Appointments.this);
                                    tv.setText("No activities on " + dateString);
                                    d.setContentView(tv);
                                    d.show();

                                } else {

                                    for (Map map : actList) {

                                        String actName = currentAct;
                                        String startTime = map.get(cStartTime).toString();
                                        String endTime = map.get(cEndTime).toString();
                                        String location = map.get(cLocation).toString();

                                        Dialog d = new Dialog(Appointments.this);
                                        d.setTitle("DISPLAY");
                                        TextView tv = new TextView(Appointments.this);
                                        tv.setText(actName);
                                        tv.setText(actName + "\n" + "StartTime: " + startTime + "\n" + "EndTime: " + endTime + "\n" + "Location: " + location);
                                        d.setContentView(tv);
                                        d.show();

                                    }
                                }

                                break;

                            case 1:

                                AlertDialog.Builder builder = new AlertDialog.Builder(Appointments.this);
                                LayoutInflater inflater = Appointments.this.getLayoutInflater();
                                builder.setTitle("Add Activity Appointment");
                                builder.setMessage(currentAct);

                                final View popupView = inflater.inflate(R.layout.activity_appointments_popup_add_activity, null);
                                builder.setView(popupView);

                                final EditText etDate = (EditText) popupView.findViewById(R.id.et_appointment_add_date);
                                final EditText etStart = (EditText) popupView.findViewById(R.id.et_appointment_add_start_time);
                                final EditText etEnd = (EditText) popupView.findViewById(R.id.et_appointment_add_end_time);
                                final EditText etLoc = (EditText) popupView.findViewById(R.id.et_appointment_add_location);
                                etDate.setText(dateString);

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        addAppointment(currentAct, etDate.getText().toString(), etStart.getText().toString(), etEnd.getText().toString(), etLoc.getText().toString(), 1);
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog b = builder.create();
                                b.show();

                            case 2:

                                break;

                        }

//
//                        Dialog d = new Dialog(Appointments.this);
//                        d.setTitle("DISPLAY");
//                        TextView tv = new TextView(Appointments.this);
//                        tv.setText(Integer.toString(position));
//                        d.setContentView(tv);
//                        d.show();

                    }
                });
                builder.show();

            }
        });

    }


    private void addAppointment(String activity, String date, String startTime, String endTime, String location, int admin) {

        ContentValues cv = new ContentValues();

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedpreferences.getString(appHelper.SP_USERNAME, "");

        ServerInt servInt = new ServerInt();
        servInt.newApt(Appointments.this, appHelper.PHP_INSERT_APT, username, activity, date,
                startTime, endTime, location);

        DataBase entry = new DataBase(this);

        cv.put(entry.USERNAME, username);
        cv.put(entry.ACTIVITY_NAME, activity);
        cv.put(entry.DATE, date);
        cv.put(entry.STARTTIME, startTime);
        cv.put(entry.ENDTIME, endTime);
        cv.put(entry.LOCATION, location);
        cv.put(entry.ADMIN, admin);

        entry.open();

        entry.addEntry(cv, entry.DATABASE_TABLE_APT);

        entry.close();

        appHelper.showToastMessage(this, "ADDED: " + activity + " " + date + " " + startTime + " " + endTime + " " + location);



    }



}
