package com.example.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity  extends AppCompatActivity {

    EditText name,email,password;
    private FirebaseAuth auth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        auth=FirebaseAuth.getInstance();
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        fStore= FirebaseFirestore.getInstance();
    }

    public void signup(View view){
        String username= name.getText().toString();
        String useremail= email.getText().toString();
        String userpass= password.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(useremail)){
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userpass)){
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(useremail,userpass).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=auth.getCurrentUser();
                    Toast.makeText(SignupActivity.this,"success",Toast.LENGTH_SHORT).show();
                    DocumentReference df= fStore.collection("Users").document(user.getUid());
                    Map<String,Object> userinfo=new HashMap<>();
                    userinfo.put("username",name.getText().toString());
                    userinfo.put("email",email.getText().toString());

                    userinfo.put("isAdmin","1");
                    df.set(userinfo);

                    startActivity(new Intent(SignupActivity.this,MainActivity.class));
                    finish();

                }else {
                    Toast.makeText(SignupActivity.this,"fail"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //startActivity(new Intent(SignupActivity.this,MainActivity.class));

    }
}
