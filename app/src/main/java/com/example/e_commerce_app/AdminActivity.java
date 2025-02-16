//
//
//package com.example.e_commerce_app;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.auth.AuthResult;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AdminActivity extends AppCompatActivity {
//
//    private EditText emailInput, passwordInput,usernameInput;
//    private Button addUserButton,goToUsersListButton;
//    private RecyclerView userRecyclerView;
//
//    private FirebaseAuth auth;
//    private FirebaseFirestore firestore;
//
//    private UserAdapter userAdapter;
//    private List<User> userList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin);
//        auth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();
//
//        emailInput = findViewById(R.id.emailInput);
//        passwordInput = findViewById(R.id.passwordInput);
//        usernameInput = findViewById(R.id.usernameInput);
//
//        addUserButton = findViewById(R.id.addUserButton);
//
//
//
//        addUserButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addUser();
//            }
//        });
//
//   }
//
//    private void addUser() {
//        String email = emailInput.getText().toString().trim();
//        String password = passwordInput.getText().toString().trim();
//        String username = usernameInput.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    FirebaseUser newUser = task.getResult().getUser();
//                    String uid = newUser.getUid();
//
//                    Map<String, Object> userMap = new HashMap<>();
//                    userMap.put("email", email);
//                    userMap.put("username", username);
//                    userMap.put("isAdmin", "1");
//
//                    firestore.collection("Users").document(uid).set(userMap)
//                            .addOnSuccessListener(aVoid -> {
//                                Toast.makeText(AdminActivity.this, "User Added", Toast.LENGTH_SHORT).show();
//
//                            })
//                            .addOnFailureListener(e -> Toast.makeText(AdminActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                } else {
//                    Toast.makeText(AdminActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void loadUsers() {
//        firestore.collection("Users").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                userList.clear();
//                for (DocumentSnapshot doc : task.getResult()) {
//                    String id = doc.getId();
//                    String email = doc.getString("email");
//                    String username = doc.getString("username");
//                    String isAdmin = doc.getString("isAdmin");
//
//                    Log.d("AdminActivity", "Loaded user: " + email + ", " + username);
//                    userList.add(new User(id, email, username, isAdmin));
//                }
//                userAdapter.notifyDataSetChanged();
//            } else {
//                Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//
//}
