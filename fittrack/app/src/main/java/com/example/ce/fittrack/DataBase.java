package com.example.ce.fittrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CE on 5/3/2016.
 */
public class DataBase {

    static AppHelper appHelper = new AppHelper();

    public static final String DATABASE_NAME = "activizor";
    public static final String DATABASE_TABLE = "activities";
    public static final String KEY_ROWID = "id";
    public static final String USERNAME = "username";
    public static final String ACTIVITY_NAME = "activity";

    public static final String DATABASE_TABLE_APT = "appointments";
    public static final String ADMIN = "admin";
    public static final String DATE = "date";
    public static final String STARTTIME = "start_time";
    public static final String ENDTIME = "end_time";
    public static final String LOCATION = "location";
    public static final String APT_MAINID = "apt_main_id";
    public static final String LAST_UPDATE = "last_update";
    public static final String ISDELETED = "is_deleted";

//    public static final String DATEFORMAT = appHelper.DATEFORMAT;
//    public static final String TIMEFORMAT = appHelper.TIMEFORMAT;

    private static final int DATABASE_VERSION = 6;

    private DbHelper myHelper;
    private Context myContext;
    private SQLiteDatabase myDataBase;

    private static class DbHelper extends SQLiteOpenHelper{

        public DbHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USERNAME + " TEXT NOT NULL, "
                    + ACTIVITY_NAME + " TEXT NOT NULL);"
            );

            db.execSQL("CREATE TABLE " + DATABASE_TABLE_APT + " ("
                            + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + USERNAME + " TEXT NOT NULL, "
                            + ADMIN + " INTEGER, "
                            + ACTIVITY_NAME + " TEXT NOT NULL, "
                            + DATE + " DATE NOT NULL, "
                            + STARTTIME + " TIME NOT NULL, "
                            + ENDTIME + " TIME, "
                            + LOCATION + " TEXT,"
                            + APT_MAINID + " INTEGER,"
                            + ISDELETED + " INTEGER,"
                            + LAST_UPDATE + " DATETIME);"
            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_APT);
            onCreate(db);

        }

    }

    public DataBase(Context c) {

        myContext = c;

    }

    public DataBase open() {

        myHelper = new DbHelper(myContext);
        myDataBase = myHelper.getWritableDatabase();

        return this;

    }

    public void close(){

        myHelper.close();

    }

    public long addEntry(Map<String, String>  dbEntries) {

        ContentValues cv = new ContentValues();

        for (String key : dbEntries.keySet()) {
            String value = dbEntries.get(key);
            System.out.println("Key = " + key + ", Value = " + value);
            cv.put(key, value);
        }

        return myDataBase.insert(DATABASE_TABLE, null, cv);

    }

    public long addEntry(ContentValues  dbEntries, String dbTable) {

        return myDataBase.insert(dbTable, null, dbEntries);

    }

    public boolean deleteEntry(String entryName) {

        return myDataBase.delete(DATABASE_TABLE, ACTIVITY_NAME + "=?", new String[]{entryName}) > 0;

    }

    public boolean deleteEntry(int key) {

        return myDataBase.delete(DATABASE_TABLE, KEY_ROWID + "=" + key, null) > 0;

    }


    public ArrayList<String> getUserActivities(String username) {

        String[] columns = new String[]{KEY_ROWID, USERNAME, ACTIVITY_NAME};
        Cursor c = myDataBase.query(DATABASE_TABLE, columns, USERNAME + " = ? ", new String[] {username}, null, null, null);

        ArrayList<String> list = new ArrayList<String>();

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iUser = c.getColumnIndex(USERNAME);
        int iAct = c.getColumnIndex(ACTIVITY_NAME);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            list.add(c.getString(iAct));

        }

        c.close();

        return list;

    }


    public ArrayList getAppointmentsByDate(String username, String date) {

        return getAppointmentsByDate(username, date, "%");

    }

    public ArrayList getAppointmentsByDate(String username, String date, String activity) {

        String[] columns = new String[]{KEY_ROWID, USERNAME, ACTIVITY_NAME, DATE, STARTTIME, ENDTIME, LOCATION};
        Cursor c = myDataBase.query(DATABASE_TABLE_APT, columns,
                USERNAME + " = ? and " +
                DATE + " = ? and " +
                ACTIVITY_NAME + " = ?", new String[] {username, date, activity}, null, null, null);

        ArrayList<Map> list = new ArrayList();

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iUser = c.getColumnIndex(USERNAME);
        int iAd = c.getColumnIndex(ADMIN);
        int iAct = c.getColumnIndex(ACTIVITY_NAME);
        int iDt = c.getColumnIndex(DATE);
        int iSt = c.getColumnIndex(STARTTIME);
        int iEt = c.getColumnIndex(ENDTIME);
        int iLoc = c.getColumnIndex(LOCATION);

        System.out.println("DEBUG: " + iRow);
        System.out.println("DEBUG: " + iUser);
        System.out.println("DEBUG: " + iAd);
        System.out.println("DEBUG: " + iAct);
        System.out.println("DEBUG: " + iDt);
        System.out.println("DEBUG: " + iSt);
        System.out.println("DEBUG: " + iEt);
        System.out.println("DEBUG: " + iLoc);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            System.out.println("DEBUG REC: " +  c.getString(iRow));
            System.out.println("DEBUG REC: " +  c.getString(iUser));
            System.out.println("DEBUG REC: " +  c.getString(iAct));
            System.out.println("DEBUG REC: " +  c.getString(iDt));
            System.out.println("DEBUG REC: " +  c.getString(iSt));
            System.out.println("DEBUG REC: " +  c.getString(iEt));
            System.out.println("DEBUG REC: " +  c.getString(iLoc));

            Map map = new HashMap();
            map.put(KEY_ROWID, c.getString(iRow));
            map.put(USERNAME, c.getString(iUser));
//            map.put(ADMIN, c.getString(iAd)); BREAKS FOR SOME REASON
            map.put(ACTIVITY_NAME, c.getString(iAct));
            map.put(DATE, c.getString(iDt));
            map.put(STARTTIME, c.getString(iSt));
            map.put(ENDTIME, c.getString(iEt));
            map.put(LOCATION, c.getString(iLoc));


            list.add(map);

        }

        c.close();

        return list;

    }


    public int getNumberOfEntries() {

        Cursor cursor = myDataBase.rawQuery(

                "SELECT COUNT(" + KEY_ROWID + ") FROM " + DATABASE_TABLE , null);

        if(cursor.moveToFirst()) {

            return cursor.getInt(0);

        } else { return 0; }

    };


    public String getDataAsString() {

        String[] columns = new String[] {KEY_ROWID, ACTIVITY_NAME};

        Cursor c = myDataBase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String result = "My resulst: \n";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iAct = c.getColumnIndex(ACTIVITY_NAME);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

//            result += c.getString(iRow) + " " + c.getString(iAct) + "\n";

        }

        c.close();

        return result;
    }
}
