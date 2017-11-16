package com.pkr.account;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pkr.account.db.TaskContract;
import com.pkr.account.db.TaskDBHelper;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    TaskDBHelper mHelper = new TaskDBHelper(this);
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ArrayList<String> accountList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        SQLiteDatabase accDb = mHelper.getReadableDatabase();
        Cursor accDbCur = accDb.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_EMAIL, TaskContract.TaskEntry.COL_PASS, TaskContract.TaskEntry.COL_LOGIN},
                null, null, null, null, null);
        while (accDbCur.moveToNext()){
            int emailInd = accDbCur.getColumnIndex(TaskContract.TaskEntry.COL_EMAIL);
            int passInd = accDbCur.getColumnIndex(TaskContract.TaskEntry.COL_PASS);
            accountList.add(accDbCur.getString(emailInd) + " - " + accDbCur.getString(passInd));
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this,
                R.layout.singleitem,
                R.id.acc_single,
                accountList);
        listView.setAdapter(mAdapter);
        accDbCur.close();
        accDb.close();

    }
    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit)
            finish(); //finish activity
        else{
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
