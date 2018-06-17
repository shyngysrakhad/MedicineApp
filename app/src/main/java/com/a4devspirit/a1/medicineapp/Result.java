package com.a4devspirit.a1.medicineapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Result extends AppCompatActivity {
    TextView result, treatment, information, percentage, treatment2, text_treat2, text_treat;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialization();
        if (getIntent().getStringExtra("percentage").equals("0")) {
            percentage.setVisibility(View.INVISIBLE);
        }else percentage.setText("~" + getIntent().getStringExtra("percentage") + "%");
        data.child("Diseases/Male/" + getIntent().getStringExtra("age") + "/" + getIntent().getStringExtra("part")).child(getIntent().getStringExtra("data")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("treatment") || dataSnapshot.hasChild("description")) {
                    result.setText(dataSnapshot.child("name").getValue(String.class).replace("\\n", "\n"));
                    treatment.setText(dataSnapshot.child("treatment").getValue(String.class).replace("\\n", "\n"));
                    information.setText(dataSnapshot.child("description").getValue(String.class).replace("\\n", "\n"));
                    final Diagnose_data diagnose_data = new Diagnose_data();
                    diagnose_data.setAge(getIntent().getStringExtra("age"));
                    diagnose_data.setPart(getIntent().getStringExtra("part"));
                    diagnose_data.setDisease_id(getIntent().getStringExtra("data"));
                    diagnose_data.setPercentage(getIntent().getStringExtra("percentage"));
                    if (getIntent().getStringExtra("check").equals("")) {
                        data.child("Users").child(user.getUid()).child("Diagnoses").child(getIntent().getStringExtra("age") + getIntent().getStringExtra("part") + getIntent().getStringExtra("data")).setValue(diagnose_data);
                    }
                    if (dataSnapshot.hasChild("treatment2")) {
                        text_treat2.setVisibility(View.VISIBLE);
                        treatment2.setVisibility(View.VISIBLE);
                        treatment2.setText(dataSnapshot.child("treatment2").getValue(String.class));
                    }

                }else {
                    text_treat.setVisibility(View.GONE);
                    treatment.setVisibility(View.GONE);
                    percentage.setVisibility(View.GONE);
                    result.setText("Nothing is found");
                    information.setText("Unfortunately at the moment we can not determine your diagnosis. We recommend to see a doctor");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initialization() {
        result = (TextView) findViewById(R.id.result);
        treatment = (TextView) findViewById(R.id.treatment);
        treatment2 = (TextView) findViewById(R.id.treatment2);
        text_treat = (TextView) findViewById(R.id.text_treatment);
        text_treat2 = (TextView) findViewById(R.id.text_treatment2);
        information = (TextView) findViewById(R.id.information);
        percentage = (TextView) findViewById(R.id.percentage);
    }
    @Override
    public void onBackPressed()
    {
        if (getIntent().getStringExtra("check").equals("")){
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
            finish();
        }else super.onBackPressed();
    }
}
