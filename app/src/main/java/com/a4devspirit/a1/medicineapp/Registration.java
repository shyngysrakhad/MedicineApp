package com.a4devspirit.a1.medicineapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button reg;
    EditText name, surname, mail, password, password2;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    String[] array_gender = {"Male", "Female"};
    String[] city_list ={"Astana","Almaty","Kokshetau","Aktobe","Taldykorgan","Atyrau","Oskemen","Taraz","Oral","Karagandy","Kostanay","Kyzylorda","Aktau","Pavlodar","Petropavl","Shymkent"};
    List age_list = new ArrayList<>();
    ArrayAdapter<String> adapter1;
    ArrayAdapter<Integer> adapter2;
    ArrayAdapter<String> adapter3;
    Spinner age, gender,city;
    String lon, lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        age = (Spinner) findViewById(R.id.age_spinner);
        gender = (Spinner) findViewById(R.id.gender_spinner);
        reg = (Button) findViewById(R.id.btn_sign_up);
        name = (EditText)findViewById(R.id.reg_name);
        surname = (EditText)findViewById(R.id.reg_surname);
        mail = (EditText)findViewById(R.id.reg_mail);
        password = (EditText)findViewById(R.id.reg_password);
        password2 = (EditText)findViewById(R.id.reg_password2);
        city = (Spinner) findViewById(R.id.city_spinner);
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
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().equals("") && !surname.getText().toString().equals("") && !mail.getText().toString().equals("") && !password.getText().toString().equals("") && !password2.getText().toString().equals("")){
                    if (password.getText().toString().equals(password2.getText().toString())){
                        final ProgressDialog progressDialog = ProgressDialog.show(Registration.this, getResources().getString(R.string.registration), getResources().getString(R.string.loading), true);
                        auth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    final FirebaseUser user = auth.getCurrentUser();
                                    assert user != null;
                                    city_geo();
                                    data.child("Users").child(user.getUid()).child("name").setValue(name.getText().toString());
                                    data.child("Users").child(user.getUid()).child("surname").setValue(surname.getText().toString());
                                    data.child("Users").child(user.getUid()).child("password").setValue(password.getText().toString());
                                    data.child("Users").child(user.getUid()).child("age").setValue(age.getSelectedItem().toString());
                                    data.child("Users").child(user.getUid()).child("gender").setValue(gender.getSelectedItem().toString());
                                    data.child("Users").child(user.getUid()).child("city").child("lon").setValue(lon);
                                    data.child("Users").child(user.getUid()).child("city").child("lat").setValue(lat);
                                    data.child("Users").child(user.getUid()).child("city").child("name").setValue(city.getSelectedItem().toString());
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(Registration.this, Profile.class);
                                    startActivity(intent);
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(Registration.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else Toast.makeText(Registration.this, getResources().getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                }else Toast.makeText(Registration.this, getResources().getString(R.string.fill_the_fields), Toast.LENGTH_SHORT).show();
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
