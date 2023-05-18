package com.example.login2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;

public class AdminLogin extends AppCompatActivity {
    public static String PREFS_NAM="MyPrefsFile";
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-cded5-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText email=findViewById(R.id.ademail);
        final EditText phone=findViewById(R.id.adphnnum);
        final EditText password=findViewById(R.id.adpassword);
        final Button loginBtn=findViewById(R.id.adloginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Toast.makeText(AdminLogin.this, "Enter all inputs", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("admin").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);
                            if(snapshot.hasChild(phoneTxt)){
                                    if (getPassword.equals(passTxt)) {
                                        Toast.makeText(AdminLogin.this, "Welcome admin", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AdminLogin.this, AdminMainActivity.class));
                                    }else{
                                        Toast.makeText(AdminLogin.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }
                            }else{
                                Toast.makeText(AdminLogin.this,"Wrong Phone number",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}