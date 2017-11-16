package com.pkr.account;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pkr.account.db.TaskContract;
import com.pkr.account.db.TaskDBHelper;

public class MainActivity extends AppCompatActivity {

    private TaskDBHelper mHelper = new TaskDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText email = (EditText) findViewById(R.id.emailEditText);
        final EditText password = (EditText) findViewById(R.id.passwordEditText);
        Button login = (Button) findViewById(R.id.login);

        if (getIntent().hasExtra("signup"))
            Toast.makeText(getBaseContext(), "Sign up successful", Toast.LENGTH_LONG).show();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailCheck = email.getText().toString();
                String passCheck = password.getText().toString();

                SQLiteDatabase loginDb = mHelper.getReadableDatabase();
                Cursor cursor = loginDb.query(TaskContract.TaskEntry.TABLE,
                        new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_EMAIL, TaskContract.TaskEntry.COL_PASS, TaskContract.TaskEntry.COL_LOGIN},
                        null, null, null, null, null);
                if(!emailCheck.equals("") && !passCheck.equals("")) {
                    if(emailCheck.equals("pk@admin.com") && passCheck.equals("adminlogin")){
                        Intent intent2 = new Intent(getApplicationContext(), Admin.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent2.putExtra("login", 1);
                        startActivity(intent2);
                    }
                    else {
                        while (cursor.moveToNext()) {
                            int emailIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_EMAIL);
                            int passIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_PASS);
                            if (emailCheck.equals(cursor.getString(emailIndex)) && passCheck.equals(cursor.getString(passIndex))) {
                                Intent intent1 = new Intent(getApplicationContext(), Welcome.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent1.putExtra("login", 1);
                                loginStatus("y");
                                startActivity(intent1);
                            } else {
                                Snackbar snackbar = Snackbar.make(v, "Email or Password combination is not available..!!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }
                }
                else if (emailCheck.equals("") && passCheck.equals("")) {
                    email.setError("cant be left empty");
                    password.setError("cant be left empty");
                }
                else if (emailCheck.equals(""))
                    email.setError("cant be left empty");
                else if (passCheck.equals(""))
                    password.setError("cant be left empty");
            }
        });

    }

    public boolean loginStatusCheck(){
        String check="n";
        SQLiteDatabase lscDb = mHelper.getReadableDatabase();
        Cursor lscDbCv = lscDb.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_EMAIL, TaskContract.TaskEntry.COL_PASS, TaskContract.TaskEntry.COL_LOGIN},
                null, null, null, null, null);
        while (lscDbCv.moveToNext()){
            int index = lscDbCv.getColumnIndex(TaskContract.TaskEntry.COL_LOGIN);
            check = lscDbCv.getString(index);
        }
        if (check.equals("y"))
            return true;
        else
            return false;
    }

    public void loginStatus(String s){
        SQLiteDatabase lsDb = mHelper.getWritableDatabase();
        ContentValues lsDbCv = new ContentValues();
        lsDbCv.put(TaskContract.TaskEntry.COL_LOGIN, s);
        lsDb.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                lsDbCv,
                SQLiteDatabase.CONFLICT_REPLACE);
        lsDb.close();
    }

    public void registerClick (View v){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
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
