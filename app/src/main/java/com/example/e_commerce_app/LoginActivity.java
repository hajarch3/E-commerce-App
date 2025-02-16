package com.example.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        auth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
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
            FirebaseAuth.getInstance().signOut();
            auth.signInWithEmailAndPassword(useremail,userpass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"success",Toast.LENGTH_SHORT).show();
                        checkUserAccessLevel(task.getResult().getUser().getUid());

                    }else {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(LoginActivity.this,"fail"+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
        public void checkUserAccessLevel(String uid){
            DocumentReference df= fStore.collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG","onSuccess"+documentSnapshot.getData());
                    String isAdmin = documentSnapshot.getString("isAdmin");
                    if (isAdmin != null && isAdmin.equals("0")) {
                        startActivity(new Intent(getApplicationContext(), MenuAdminActivity.class));
                        finish();
                    }
                    if (isAdmin != null && isAdmin.equals("1")) {

                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        finish();
                    }
                }
            });
        }
        public void signup(View view){
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));

        }
    }

