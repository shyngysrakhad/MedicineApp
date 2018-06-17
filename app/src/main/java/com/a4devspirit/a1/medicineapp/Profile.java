package com.a4devspirit.a1.medicineapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.a4devspirit.a1.medicineapp.Diagnose.Boys_Diagnose;
import com.a4devspirit.a1.medicineapp.Diagnose.Man_Diagnose;
import com.a4devspirit.a1.medicineapp.Reminder.AlarmMe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class Profile extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    TextView user1, user2;
    RingProgressBar ringProgressBar;
    String age, gender;
    Button check, symptoms, tips, reminder;
    String lon, lat;
    int percentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        check = (Button)findViewById(R.id.button_check);
        symptoms = (Button)findViewById(R.id.button_symptoms);
        tips = (Button)findViewById(R.id.button_tips);
        reminder = (Button)findViewById(R.id.button_reminder);
        user1 = (TextView) findViewById(R.id.user_option1);
        user2 = (TextView) findViewById(R.id.user_option2);
            data.child("Users").child(user.getUid()).child("city").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lon = dataSnapshot.child("lon").getValue(String.class);
                    lat = dataSnapshot.child("lat").getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            ringProgressBar = (RingProgressBar) findViewById(R.id.progress);
            ringProgressBar.setProgress(100);
            data.child("Users").child(user.getUid()).child("Diagnoses").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    percentage = percentage + Integer.parseInt(dataSnapshot.child("percentage").getValue(String.class));
                    ringProgressBar = (RingProgressBar) findViewById(R.id.progress);
                    ringProgressBar.setProgress(100 - percentage / 20);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            data.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    user1.setText(dataSnapshot.child("name").getValue(String.class) + " " + dataSnapshot.child("surname").getValue(String.class));
                    user2.setText(dataSnapshot.child("gender").getValue(String.class) + ", " + dataSnapshot.child("age").getValue(String.class) + " y.o.");
                    age = dataSnapshot.child("age").getValue(String.class);
                    gender = dataSnapshot.child("gender").getValue(String.class);
                    tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Profile.this, Tips.class);
                            intent.putExtra("lon", lon);
                            intent.putExtra("lat", lat);
                            intent.putExtra("gender", gender);
                            SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                            editor.putString("city", dataSnapshot.child("city").child("name").getValue(String.class));
                            editor.apply();
                            startActivity(intent);
                        }
                    });
                    symptoms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Profile.this, Diagnoses.class);
                            startActivity(intent);
                        }
                    });
                    reminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Profile.this, AlarmMe.class);
                            startActivity(intent);
                        }
                    });
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Integer.parseInt(age) <= 10) {
                                Intent intent = new Intent(Profile.this, Boys_Diagnose.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Profile.this, Man_Diagnose.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch (menu.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(Profile.this, Medicalcard.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent2 = new Intent(Profile.this, Settings.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }
    @Override
    public void onBackPressed()
    {
        finishAffinity();
        super.onBackPressed();
    }
}
