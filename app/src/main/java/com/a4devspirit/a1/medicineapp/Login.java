package com.a4devspirit.a1.medicineapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button sign_in, sign_up;
    EditText mail, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_in = (Button)findViewById(R.id.btn_sign_in);
        mail = (EditText)findViewById(R.id.sign_in_mail);
        password = (EditText)findViewById(R.id.sign_in_password);
        sign_up = (Button)findViewById(R.id.btn_reg);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mail.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(Login.this, getResources().getString(R.string.fill_the_fields), Toast.LENGTH_SHORT).show();
                }else{
                    final ProgressDialog progressDialog = ProgressDialog.show(Login.this, getResources().getString(R.string.login), getResources().getString(R.string.loading), true);
                    auth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Login.this, getResources().getString(R.string.successful), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, Profile.class);
                                startActivity(intent);
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        finishAffinity();
        super.onBackPressed();
    }
}
