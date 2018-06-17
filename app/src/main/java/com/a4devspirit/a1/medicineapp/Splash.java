package com.a4devspirit.a1.medicineapp;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView iv = (ImageView) findViewById(R.id.splash_image);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (isNetworkAvailable()){
                    if (user == null) {
                        startActivity(new Intent(Splash.this, Login.class));
                    }else {
                        startActivity(new Intent(Splash.this, Profile.class));
                    }
                }else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                    builder.setTitle("Internet is not working")
                            .setMessage("Please, check your internet connection and click RETRY")
                            .setCancelable(false)
                            .setPositiveButton("Retry",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (isNetworkAvailable()) {
                                                if (user == null) {
                                                    startActivity(new Intent(Splash.this, Login.class));
                                                    finish();
                                                }else {
                                                    startActivity(new Intent(Splash.this, Profile.class));
                                                    finish();
                                                }
                                            }else {
                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();
                                                Toast.makeText(Splash.this, "No connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
