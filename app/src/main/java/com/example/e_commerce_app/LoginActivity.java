package com.example.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth auth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        auth= FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
    }
        public void signin(View view){
            String useremail= email.getText().toString();
            String userpass= password.getText().toString();
            if(TextUtils.isEmpty(useremail)){
                Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(userpass)){
                Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
                return;
            }
            auth.signInWithEmailAndPassword(useremail,userpass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"success",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                    }else {
                        Toast.makeText(LoginActivity.this,"fail"+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
        public void signup(View view){
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));

        }
    }

