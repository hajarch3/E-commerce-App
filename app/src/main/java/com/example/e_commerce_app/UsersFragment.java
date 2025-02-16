package com.example.e_commerce_app;//package com.example.e_commerce_app;
//
//import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce_app.R;
import com.example.e_commerce_app.User;
import com.example.e_commerce_app.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersFragment extends Fragment {

        private EditText emailInput, passwordInput, usernameInput;
        private Button addUserButton;
        private RecyclerView userRecyclerView;

        private FirebaseAuth auth;
        private FirebaseFirestore firestore;

        private UserAdapter userAdapter;
        private List<User> userList;

        public UsersFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_users, container, false);

            auth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

            emailInput = view.findViewById(R.id.emailInput);
            passwordInput = view.findViewById(R.id.passwordInput);
            usernameInput = view.findViewById(R.id.usernameInput);
            addUserButton = view.findViewById(R.id.addUserButton);




            userRecyclerView.setAdapter(userAdapter);

            addUserButton.setOnClickListener(v -> addUser());

            loadUsers();
            return view;
        }

        private void addUser() {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser newUser = task.getResult().getUser();
                    String uid = newUser.getUid();

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("email", email);
                    userMap.put("username", username);
                    userMap.put("isAdmin", "1");

                    firestore.collection("Users").document(uid).set(userMap)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "User Added", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(getContext(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void loadUsers() {
            firestore.collection("Users").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userList.clear();
                    for (DocumentSnapshot doc : task.getResult()) {
                        String id = doc.getId();
                        String email = doc.getString("email");
                        String username = doc.getString("username");
                        String isAdmin = doc.getString("isAdmin");

                        Log.d("AdminFragment", "Loaded user: " + email + ", " + username);
                        userList.add(new User(id, email, username, isAdmin));
                    }
                    userAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }