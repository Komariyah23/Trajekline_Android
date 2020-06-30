package com.project.trajekline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.project.trajekline.akun.Sign_In;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Log.d("pesan", "disini");
        Date strDate = null;
        try {
            strDate = sdf.parse("2020-06-28");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() >= strDate.getTime()) {
            Log.d("pesan", "disini");
        }
        //        membuat intent ketika waktu sudah habis maka akan diarahkan ke halaman login
        final Intent i = new Intent(this, Sign_In.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
//        memulai timer splash screen
        timer.start();
    }
}
