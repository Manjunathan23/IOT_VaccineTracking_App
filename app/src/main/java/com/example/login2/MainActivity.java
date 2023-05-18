package com.example.login2;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    TextView a, b, c, d, e, f, g, h,i,j,k,l,m,n,o,p;
    int backpress;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static String PREFS_NAME="MyPrefsFile";
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-cded5-default-rtdb.firebaseio.com/");

    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    String formattedTime = dateFormat.format(currentTime);

    public void onBackPressed(){
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

        if (backpress>1) {
            this.finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Dialog[] dialog = new Dialog[1];
        final EditText tripno=findViewById(R.id.tripid);
        final Button goBtn=findViewById(R.id.goBtn);
        final ImageButton adminBtn=findViewById(R.id.adminBtn);
        final TextView logOut=findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();

                SharedPreferences sharedPreferences=getSharedPreferences(AdminLogin.PREFS_NAM,0);
                SharedPreferences sharedPreferences1=getSharedPreferences(Login.PREFS_NAME,0);
                SharedPreferences.Editor editor2=sharedPreferences1.edit();
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn",false);
                editor2.putBoolean("hasLoggedIn",false);
                editor.commit();
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AdminLogin.class);
                startActivity(intent);
            }
            });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String tripTxt=tripno.getText().toString();

                if(tripTxt.isEmpty()){
                    Toast.makeText(MainActivity.this,"Enter Trip Id to continue",Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("vaccination").child(tripTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                TextView a =findViewById(R.id.locview);
                                TextView b =findViewById(R.id.dateview);
                                TextView c =findViewById(R.id.timeview);
                                TextView d = findViewById(R.id.tempview);
                                TextView e=findViewById(R.id.nameview);
                                TextView f=findViewById(R.id.humiview);
                                TextView g=findViewById(R.id.humiview);
                                TextView h=findViewById(R.id.humiview);




                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = new Date();

                                String temp = dataSnapshot.child("temperature").getValue().toString();
                                String humi = dataSnapshot.child("Humidity").getValue().toString();
                                String name = dataSnapshot.child("vaccine").getValue().toString();
                                String latitude = dataSnapshot.child("Latitude").getValue().toString();
                                String longitude = dataSnapshot.child("Longitude").getValue().toString();
                                double lat=Double.parseDouble(latitude);
                                double lon=Double.parseDouble(longitude);
                                b.setText(formatter.format(date));
                                c.setText(formattedTime);
                                d.setText(temp);
                                e.setText(name);
                                f.setText(humi);


                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());


                                try {
                                    addresses = geocoder.getFromLocation(lat, lon, 1);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();
                                    a.setText(city);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }

                                StringBuilder sb= new StringBuilder();
                                for (Address address:addresses){
                                    sb.append(address);
                                }
                                String string=sb.toString();


                                String address = addresses.get(0).getAddressLine(0);
                                i=(TextView)findViewById(R.id.setloc);
                                j=(TextView)findViewById(R.id.setdate);
                                k=(TextView)findViewById(R.id.settime);
                                l=(TextView)findViewById(R.id.settemp);
                                m=(TextView)findViewById(R.id.vaccname);
                                n=(TextView)findViewById(R.id.sethumi);
                                o=(TextView)findViewById(R.id.setlat);
                                p=(TextView)findViewById(R.id.setlong);


                                i.setText("Location:");
                                j.setText("Date:");
                                k.setText("Time:");
                                l.setText("Temperature:");
                                m.setText("Name");
                                n.setText("Humidity");
                                }else{
                                    Toast.makeText(MainActivity.this,"Trip id not found",Toast.LENGTH_SHORT).show();
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
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
