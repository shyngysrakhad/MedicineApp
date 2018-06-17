package com.a4devspirit.a1.medicineapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.a4devspirit.a1.medicineapp.Diagnose.Man_Diagnose;
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

public class Medicalcard extends AppCompatActivity {
    TabHost tabhost;
    TabHost.TabSpec tabSpec;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    EditText name, address, blood, allergies, medications, donor, notes;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalcard);
        setTitle("Medical Card");
        initialization();
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,list);
        listView.setAdapter(adapter);
        tabhost.setup();
        tabSpec = tabhost.newTabSpec("tag1");
        tabSpec.setIndicator("Data");
        tabSpec.setContent(R.id.tab1);
        tabhost.addTab(tabSpec);
        tabSpec = tabhost.newTabSpec("tag2");
        tabSpec.setIndicator("Contacts");
        tabSpec.setContent(R.id.tab2);
        tabhost.addTab(tabSpec);
        data.child("Users").child(user.getUid()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("full_name")) name.setText(dataSnapshot.child("full_name").getValue(String.class));
                if (dataSnapshot.hasChild("address")) address.setText(dataSnapshot.child("address").getValue(String.class));
                if (dataSnapshot.hasChild("blood_type")) blood.setText(dataSnapshot.child("blood_type").getValue(String.class));
                if (dataSnapshot.hasChild("allergies")) allergies.setText(dataSnapshot.child("allergies").getValue(String.class));
                if (dataSnapshot.hasChild("medications")) medications.setText(dataSnapshot.child("medications").getValue(String.class));
                if (dataSnapshot.hasChild("donor")) donor.setText(dataSnapshot.child("donor").getValue(String.class));
                if (dataSnapshot.hasChild("notes")) notes.setText(dataSnapshot.child("notes").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        data.child("Users").child(user.getUid()).child("Contacts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getKey()+": "+dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
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
        update_info(blood,"blood_type");
        update_info(name,"full_name");
        update_info(address,"address");
        update_info(allergies,"allergies");
        update_info(medications,"medications");
        update_info(donor,"donor");
        update_info(notes,"notes");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = String.valueOf(adapterView.getAdapter().getItem(i));
                int a = s.indexOf(":");
                if (a>0) s=s.substring(a+1,s.length());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + s.trim()));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Medicalcard.this);
                builder.setTitle(getResources().getString(R.string.delete_contact))
                        .setMessage(getResources().getString(R.string.sure))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, int id) {
                                        final ProgressDialog progressDialog = ProgressDialog.show(Medicalcard.this, getResources().getString(R.string.deleting), getResources().getString(R.string.loading), true);
                                        String s = String.valueOf(adapterView.getAdapter().getItem(i));
                                        int a = s.indexOf(":");
                                        if (a>0) s = s.substring(0,a);
                                        data.child("Users").child(user.getUid()).child("Contacts").child(s).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(Medicalcard.this, getResources().getString(R.string.contact_removed), Toast.LENGTH_SHORT).show();
                                                    list.remove(i);
                                                    adapter.notifyDataSetChanged();
                                                }else{
                                                    progressDialog.dismiss();
                                                    Toast.makeText(Medicalcard.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Medicalcard.this);
                dialog.setContentView(R.layout.dialog_contact);
                dialog.setTitle(getResources().getString(R.string.add_contact));
                dialog.setCancelable(true);
                final EditText name = (EditText)dialog.findViewById(R.id.contact_name);
                final EditText number = (EditText)dialog.findViewById(R.id.contact_number);
                Button add_contact = (Button)dialog.findViewById(R.id.button_add_contact);
                dialog.show();
                add_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!name.getText().toString().equals("") && !number.getText().toString().equals("")){
                            final ProgressDialog progressDialog = ProgressDialog.show(Medicalcard.this, getResources().getString(R.string.add_contact), getResources().getString(R.string.loading), true);
                            data.child("Users").child(user.getUid()).child("Contacts").child(name.getText().toString()).setValue(number.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Medicalcard.this, getResources().getString(R.string.contact_added), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(Medicalcard.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(Medicalcard.this, getResources().getString(R.string.fill_the_fields), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void initialization(){
        tabhost = (TabHost)findViewById(R.id.tabhost);
        listView = (ListView)findViewById(R.id.contact);
        name = (EditText)findViewById(R.id.info_name);
        address = (EditText)findViewById(R.id.info_address);
        blood = (EditText)findViewById(R.id.info_blood);
        allergies = (EditText)findViewById(R.id.info_allergies);
        medications = (EditText)findViewById(R.id.info_medications);
        donor = (EditText)findViewById(R.id.info_donor);
        notes = (EditText)findViewById(R.id.info_notes);
        add = (Button)findViewById(R.id.add_contact);
    }
    private void update_info(final EditText editText, final String s){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.child("Users").child(user.getUid()).child("info").child(s).setValue(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
