package com.pkr.account;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (getIntent().hasExtra("login"))
            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();

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
