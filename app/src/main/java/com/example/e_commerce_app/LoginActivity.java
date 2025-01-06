package com.example.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void signin(View view){
        startActivity(new Intent(LoginActivity.this,MainActivity.class));

    }
    public void signup(View view){
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));

    }
}

