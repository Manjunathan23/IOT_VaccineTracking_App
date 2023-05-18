package com.example.login2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;

public class Login extends AppCompatActivity {

    public static String PREFS_NAME="MyPrefsFile";

    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-cded5-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText email=findViewById(R.id.email);
        final EditText phone=findViewById(R.id.phnnum);
        final EditText password=findViewById(R.id.password);
        final Button loginBtn=findViewById(R.id.loginBtn);
        final TextView registerNowBtn=findViewById(R.id.registerNowBtn);
        final TextView adLoginNowBtn=findViewById(R.id.adloginNowBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();

                SharedPreferences sharedPreferences=getSharedPreferences(AdminLogin.PREFS_NAM,0);
                SharedPreferences sharedPreferences1=getSharedPreferences(Login.PREFS_NAME,0);
                SharedPreferences.Editor editor2=sharedPreferences1.edit();
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn",true);
                editor2.putBoolean("hasLoggedIn",true);
                editor.commit();

                final String emailTxt = email.getText().toString();
                final String passTxt = password.getText().toString();
                final String phoneTxt = phone.getText().toString();

                if (emailTxt.isEmpty() || passTxt.isEmpty() || phoneTxt.isEmpty()) {
                    Toast.makeText(Login.this, "Enter all inputs", Toast.LENGTH_SHORT).show();
                } else {
                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.hasChild(phoneTxt)){
                                    final String getPassword=snapshot.child(phoneTxt).child("password").getValue(String.class);

                                    if(getPassword.equals(passTxt)){
                                        Toast.makeText(Login.this,"Successfully logged in",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this,MainActivity.class));
                                        finish();
                                    }else Toast.makeText(Login.this,"Wrong password", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Login.this,"Wrong Phone number",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        adLoginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, AdminLogin.class));
                finish();
            }
        });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}