package com.pkr.account.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prashant on 10/23/2017.
 */

public class TaskDBHelper extends SQLiteOpenHelper {
    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_EMAIL + " TEXT, " +
                TaskContract.TaskEntry.COL_PASS + " TEXT, " +
                TaskContract.TaskEntry.COL_LOGIN + " TEXT);";

        String admin = "INSERT INTO " + TaskContract.TaskEntry.TABLE + " (" + TaskContract.TaskEntry.COL_EMAIL + "," + TaskContract.TaskEntry.COL_PASS + "," + TaskContract.TaskEntry.COL_LOGIN + ") VALUES\n" +
                "('pk@admin.com','adminlogin','n');";
        db.execSQL(createTable);
        db.execSQL(admin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
