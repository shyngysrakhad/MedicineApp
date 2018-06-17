package com.a4devspirit.a1.medicineapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {
    Button sign_out, remove, refresh;
    EditText name, surname;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String[] array_gender = {"Male", "Female"};
    String[] city_list = {"Astana","Almaty","Kokshetau","Aktobe","Taldykorgan","Atyrau","Oskemen","Taraz","Oral","Karagandy","Kostanay","Kyzylorda","Aktau","Pavlodar","Petropavl","Shymkent"};
    List age_list = new ArrayList<>();
    ArrayAdapter<String> adapter1;
    ArrayAdapter<Integer> adapter2;
    ArrayAdapter<String> adapter3;
    Spinner age, gender, city;
    String lon, lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setTitle("Settings");
        sign_out = (Button)findViewById(R.id.button_sign_out);
        remove = (Button)findViewById(R.id.button_remove_user);
        refresh = (Button)findViewById(R.id.button_refresh_user);
        name = (EditText)findViewById(R.id.edit_user_name);
        surname = (EditText)findViewById(R.id.edit_user_surname);
        surname = (EditText)findViewById(R.id.edit_user_surname);
        age = (Spinner)findViewById(R.id.edit_spinner_age);
        gender = (Spinner)findViewById(R.id.edit_spinner_gender);
        city = (Spinner)findViewById(R.id.edit_spinner_city);
        for (int i = 1; i <= 100; i++) {
            age_list.add(Integer.toString(i));
        }
        adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,age_list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age.setAdapter(adapter2);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,array_gender);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter1);
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,city_list);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapter3);
        data.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue(String.class));
                surname.setText(dataSnapshot.child("surname").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        data.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("city")){
                    ArrayAdapter arrayAdapter = (ArrayAdapter)city.getAdapter();
                    int spinner =  arrayAdapter.getPosition(dataSnapshot.child("city").child("name").getValue(String.class));
                    city.setSelection(spinner);
                }
                if (dataSnapshot.hasChild("gender")){
                    ArrayAdapter arrayAdapter2 = (ArrayAdapter)gender.getAdapter();
                    int spinner2 = arrayAdapter2.getPosition(dataSnapshot.child("gender").getValue(String.class));
                    gender.setSelection(spinner2);
                }
                if (dataSnapshot.hasChild("age")){
                    ArrayAdapter arrayAdapter3 = (ArrayAdapter)age.getAdapter();
                    int spinner2 = arrayAdapter3.getPosition(dataSnapshot.child("age").getValue(String.class));
                    age.setSelection(spinner2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_geo();
                data.child("Users").child(user.getUid()).child("name").setValue(name.getText().toString());
                data.child("Users").child(user.getUid()).child("surname").setValue(surname.getText().toString());
                data.child("Users").child(user.getUid()).child("age").setValue(age.getSelectedItem().toString());
                data.child("Users").child(user.getUid()).child("gender").setValue(gender.getSelectedItem().toString());
                data.child("Users").child(user.getUid()).child("city").child("lon").setValue(lon);
                data.child("Users").child(user.getUid()).child("city").child("lat").setValue(lat);
                data.child("Users").child(user.getUid()).child("city").child("name").setValue(city.getSelectedItem().toString());
                Toast.makeText(Settings.this, "Data is updated successfully", Toast.LENGTH_SHORT).show();
                String text = city.getSelectedItem().toString();
                SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                editor.putString("city", text);
                editor.apply();

            }
        });
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle(getResources().getString(R.string.sign_out))
                        .setMessage(getResources().getString(R.string.sure))
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(Settings.this, Login.class);
                                        startActivity(intent);
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle(getResources().getString(R.string.delete_account))
                        .setMessage(getResources().getString(R.string.sure))
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, int id) {
                                        final ProgressDialog progressDialog = ProgressDialog.show(Settings.this, getResources().getString(R.string.delete_account), getResources().getString(R.string.loading), true);
                                        data.child("Users").child(user.getUid()).removeValue();
                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent = new Intent(Settings.this, Login.class);
                                                    startActivity(intent);
                                                }else{
                                                    progressDialog.dismiss();
                                                    dialog.cancel();
                                                    Toast.makeText(Settings.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    public void city_geo(){
        switch (city.getSelectedItem().toString()){
            case "Astana":
                lon = "71.5";
                lat = "51.166672";
                break;
            case "Almaty":
                lon = "78";
                lat = "44.5";
                break;
            case "Kokshetau":
                lon = "69.388336";
                lat = "53.2775";
                break;
            case "Aktobe":
                lon = "70.400002";
                lat = "43.466671";
                break;
            case "Taldykorgan":
                lon = "77.916672";
                lat = "45";
                break;
            case "Atyrau":
                lon = "51.883331";
                lat = "47.116669";
                break;
            case "Oskemen":
                lon = "82.610283";
                lat = "49.978889";
                break;
            case "Taraz":
                lon = "71.366669";
                lat = "42.900002";
                break;
            case "Oral":
                lon = "51.366669";
                lat = "51.233334";
                break;
            case "Karagandy":
                lon = "54.866669";
                lat = "50.066669";
                break;
            case "Kostanay":
                lon = "64";
                lat = "51.5";
                break;
            case "Kyzylorda":
                lon = "65.509171";
                lat = "44.852779";
                break;
            case "Aktau":
                lon = "51.200001";
                lat = "43.650002";
                break;
            case "Pavlodar":
                lon = "76.949997";
                lat = "52.299999";
                break;
            case "Petropavl":
                lon = "69.162781";
                lat = "54.875278";
                break;
            case "Shymkent":
                lon = "69.599998";
                lat = "42.299999";
                break;
        }
    }
}
