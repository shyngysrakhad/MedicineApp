package com.a4devspirit.a1.medicineapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Diagnoses extends AppCompatActivity {
    ListView listView;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    DatabaseReference data2 = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int idd = 0;
    final String[] disease_full = new String[10000];
    final String[] disease_age = new String[10000];
    final String[] disease_part = new String[10000];
    final String[] disease_id = new String[10000];
    final String[] disease_percentage = new String[10000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnoses);
        initialization();
        data.child("Users").child(user.getUid()).child("Diagnoses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, final String s) {
                idd++;
                disease_full[idd] = dataSnapshot.child("age").getValue(String.class) + dataSnapshot.child("part").getValue(String.class) + dataSnapshot.child("disease_id").getValue(String.class);
                disease_age[idd] = dataSnapshot.child("age").getValue(String.class);
                disease_part[idd] = dataSnapshot.child("part").getValue(String.class);
                disease_id[idd] = dataSnapshot.child("disease_id").getValue(String.class);
                disease_percentage[idd] = dataSnapshot.child("percentage").getValue(String.class);
                data2.child("Diseases/Male/" + dataSnapshot.child("age").getValue(String.class) + "/" + dataSnapshot.child("part").getValue(String.class) + "/" + dataSnapshot.child("disease_id").getValue(String.class)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot2) {
                        list.add(dataSnapshot2.child("name").getValue(String.class) + " (" + dataSnapshot.child("percentage").getValue(String.class) + "%)");
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Diagnoses.this, Result.class);
                intent.putExtra("percentage", disease_percentage[i + 1]);
                intent.putExtra("age", disease_age[i + 1]);
                intent.putExtra("part", disease_part[i + 1]);
                intent.putExtra("data", disease_id[i + 1]);
                intent.putExtra("check", "1");
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Diagnoses.this);
                builder.setTitle(getResources().getString(R.string.remove_diagnose))
                        .setMessage(getResources().getString(R.string.no_longer_sick_question))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.no_longer_sick),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, int id) {
                                        data.child("Users/" + user.getUid() + "/Diagnoses/" + disease_full[i + 1]).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    list.remove(i);
                                                    adapter.notifyDataSetChanged();
                                                    dialog.cancel();
                                                }else{
                                                    Toast.makeText(Diagnoses.this, "Error!", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                }
                                            }
                                        });
                                    }

                                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }
    private void initialization(){
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        listView = (ListView)findViewById(R.id.diagnoses_list);
        listView.setAdapter(adapter);
    }
}
