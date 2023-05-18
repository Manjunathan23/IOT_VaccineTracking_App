package com.example.login2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-cded5-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText username=findViewById(R.id.username);
        final EditText email=findViewById(R.id.email);
        final EditText password=findViewById(R.id.password);
        final EditText conpassword=findViewById(R.id.conpassword);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText phone = findViewById(R.id.phonenum);

        final Button registerBtn=findViewById(R.id.registerBtn);
        final TextView loginNowBtn=findViewById(R.id.LoginNow);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String usernameTxt= username.getText().toString();
                final String emailTxt=email.getText().toString();
                final String passTxt=password.getText().toString();
                final String connPassTxt=conpassword.getText().toString();
                final String phoneTxt=phone.getText().toString();

                if(usernameTxt.isEmpty()||emailTxt.isEmpty()||passTxt.isEmpty()||connPassTxt.isEmpty()||phoneTxt.isEmpty()){
                    Toast.makeText(Register.this,"All fields haven't been filled", Toast.LENGTH_SHORT).show();
                }else if(!passTxt.equals(connPassTxt)){
                    Toast.makeText(Register.this,"Passwords are not matching", Toast.LENGTH_SHORT).show();
                }
                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phoneTxt)){
                                Toast.makeText(Register.this,"This account already exists",Toast.LENGTH_SHORT).show();
                            }else{
                                databaseReference.child("users").child(phoneTxt).child("username").setValue(usernameTxt);
                                databaseReference.child("users").child(phoneTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(phoneTxt).child("password").setValue(passTxt);

                                Toast.makeText(Register.this,"User registration successful",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}