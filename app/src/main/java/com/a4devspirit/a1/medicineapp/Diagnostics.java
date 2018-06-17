package com.a4devspirit.a1.medicineapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Diagnostics extends AppCompatActivity {
    Button yes, no;
    TextView question;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int question_id = 1, disease_id = 1;
    String name_disease;
    long question_count, disease_count;
    int[] i = new int[100];
    int result1 = 0, result2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics);
        initialization();
        getQuestion();
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disease_id++;
                question_id = 1;
                getQuestion();
                if (disease_count < disease_id) {
                    for (int j = 1; j <= disease_count; j++) {
                        if (result1 < i[j]) {
                            result2 = j;
                            result1 = i[j];
                        }
                    }
                    Intent intent = new Intent(Diagnostics.this, Result.class);
                    intent.putExtra("data", String.valueOf(result2));
                    intent.putExtra("percentage", String.valueOf(result1));
                    intent.putExtra("part", getIntent().getStringExtra("data"));
                    intent.putExtra("age", getIntent().getStringExtra("age"));
                    intent.putExtra("check", "");
                    startActivity(intent);
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question_id++;
                setData();
                getQuestion();
                if (question_count < question_id) {
                    Intent intent = new Intent(Diagnostics.this, Result.class);
                    intent.putExtra("data", String.valueOf(disease_id));
                    intent.putExtra("percentage", String.valueOf(i[disease_id]));
                    intent.putExtra("part", getIntent().getStringExtra("data"));
                    intent.putExtra("age", getIntent().getStringExtra("age"));
                    intent.putExtra("check", "");
                    startActivity(intent);
                }
            }
        });
    }
    private void getQuestion() {
        data.child("Diseases/Male/" + getIntent().getStringExtra("age") + "/" + getIntent().getStringExtra("data")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                question.setText(dataSnapshot.child(String.valueOf(disease_id)).child("options").child(String.valueOf(question_id)).getValue(String.class));
                name_disease = dataSnapshot.child(String.valueOf(disease_id)).child("name").getValue(String.class);
                question_count = dataSnapshot.child(String.valueOf(disease_id)).child("options").getChildrenCount();
                disease_count = dataSnapshot.getChildrenCount() - 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setData() {
        i[disease_id] += 100 / question_count;
    }
    private void initialization() {
        yes = (Button) findViewById(R.id.button_yes);
        no = (Button) findViewById(R.id.button_no);
        question = (TextView) findViewById(R.id.question);
    }
    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(Diagnostics.this).create();
        alertDialog.setTitle("Exit");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }
}
