package com.example.login2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Splashscreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=900;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences(AdminLogin.PREFS_NAM,0);
                boolean hasLoggedin=sharedPreferences.getBoolean("hasLoggedIn",false);

                if(hasLoggedin){
                    Intent intent=new Intent(Splashscreen.this,AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent =new Intent(Splashscreen.this,AdminLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences(Login.PREFS_NAME,0);
                boolean hasLoggedin=sharedPreferences.getBoolean("hasLoggedIn",false);

                if(hasLoggedin){
                    Intent intent=new Intent(Splashscreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent =new Intent(Splashscreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);


    }
}
