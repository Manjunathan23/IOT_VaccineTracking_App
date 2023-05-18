package com.example.login2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;


public class AdminPage extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    int backpress;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-cded5-default-rtdb.firebaseio.com/");

    public void onBackPressed(){
        backpress = (backpress + 1);
        startActivity(new Intent(AdminPage.this,AdminMainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText vaccname = findViewById(R.id.vaccname);
        final EditText tripno = findViewById(R.id.tripnum);
        final TextView alltrip = findViewById(R.id.alltrips);

        final Button addBtn = findViewById(R.id.addBtn);




        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String vaccTxt = vaccname.getText().toString();
                final String tripTxt = tripno.getText().toString();

                if (vaccTxt.isEmpty() || tripTxt.isEmpty()) {
                    Toast.makeText(AdminPage.this, "All fields haven't been filled", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("vaccination").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(tripTxt)) {
                                Toast.makeText(AdminPage.this, "Trip already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("vaccination").child(tripTxt).child("vaccine").setValue(vaccTxt);
                                databaseReference.child("vaccination").child(tripTxt).child("date").setValue("Not yet dispatched");
                                databaseReference.child("vaccination").child(tripTxt).child("time").setValue("Not yet dispatched");
                                databaseReference.child("vaccination").child(tripTxt).child("location").setValue("Not yet dispatched");
                                databaseReference.child("vaccination").child(tripTxt).child("temperature").setValue("");
                                databaseReference.child("vaccination").child(tripTxt).child("Humidity").setValue("");
                                databaseReference.child("trips").child("trips available").child(tripTxt).setValue("");

                                Toast.makeText(AdminPage.this, "Trip added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        alltrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("trips").child("trips available").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TextView a = findViewById(R.id.heading);
                        a.setText("Available trips:");

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            int i;
                            ArrayList may = new ArrayList<String>();
                            may.add(snapshot.getKey().toString()+"\r\n");
                            LinearLayout list = (LinearLayout) findViewById(R.id.list);
                            for (i = 0; i < may.size(); i++) {
                                TextView wordView = new TextView(AdminPage.this);
                                wordView.setText((CharSequence) may.get(i));
                                list.addView(wordView);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
    });
}
}