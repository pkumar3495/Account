package com.pkr.account;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pkr.account.db.TaskContract;
import com.pkr.account.db.TaskDBHelper;

public class Register extends AppCompatActivity {

    TaskDBHelper mHelper = new TaskDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText emailReg = (EditText) findViewById(R.id.emailEditTextReg);
        final EditText passReg = (EditText) findViewById(R.id.passwordEditTextReg);
        final EditText confirmPassReg = (EditText) findViewById(R.id.confirmPasswordEditTextReg);
        Button signup = (Button) findViewById(R.id.registerButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailRegString = emailReg.getText().toString();
                String passRegString = passReg.getText().toString();
                String confirmPassRegString = confirmPassReg.getText().toString();

                if (emailRegString.equals("") || passRegString.equals("") || confirmPassRegString.equals("")) {
                    Snackbar snackbar1 = Snackbar.make(v, "All fields are mandatory", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }
                else if (emailRegString.contains("@") && passRegString.equals(confirmPassRegString)){

                    if (duplicateCheck(emailRegString)) {
                        SQLiteDatabase saveDb = mHelper.getWritableDatabase();
                        ContentValues saveDbCv = new ContentValues();
                        saveDbCv.put(TaskContract.TaskEntry.COL_EMAIL, emailRegString);
                        saveDbCv.put(TaskContract.TaskEntry.COL_PASS, passRegString);
                        saveDb.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                null,
                                saveDbCv,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        saveDb.close();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("signup", 1);

//                    Toast.makeText(getBaseContext(), "Sign up successful", Toast.LENGTH_LONG).show();

                        startActivity(intent);
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(v, "This email is already registered !", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                else if (!emailRegString.contains("@") || !passRegString.equals(confirmPassRegString)) {
                    if (!emailRegString.contains("@"))
                        emailReg.setError("Email is invalid");
                    if (!passRegString.equals(confirmPassRegString)) {
                        passReg.setError("Password did not match");
                        confirmPassReg.setError("Password did not match");
                    }
                }
            }
        });

    }

    public boolean duplicateCheck(String s){
        boolean check = true;
        SQLiteDatabase dpCheckDb = mHelper.getReadableDatabase();
        Cursor dpCheckDbcursor = dpCheckDb.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_EMAIL, TaskContract.TaskEntry.COL_PASS, TaskContract.TaskEntry.COL_LOGIN},
                null, null, null, null, null);
        while (dpCheckDbcursor.moveToNext()){
            int index = dpCheckDbcursor.getColumnIndex(TaskContract.TaskEntry.COL_EMAIL);
            if (s.equals(dpCheckDbcursor.getString(index)))
                check = false;
            else
                check = true;
        }
        return check;
    }
}
