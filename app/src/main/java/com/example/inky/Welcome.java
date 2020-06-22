package com.example.inky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        startMainActivity();
    }
    private void startMainActivity(){

        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Welcome.this,MainActivity.class);
                startActivity(mainIntent);
                Welcome.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask,2000);//延时两秒执行 run 里面的操作
    }
}