//package com.example.e_commerce_app;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class UsersListActivity extends AppCompatActivity {
//
//
//    private Button addUserButton,goToUsersListButton;
//    private RecyclerView userRecyclerView;
//
//    private FirebaseAuth auth;
//    private FirebaseFirestore firestore;
//
//    private UserAdapter userAdapter;
//    private List<User> userList;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_users_list);
//        userRecyclerView = findViewById(R.id.userRecyclerView);
//
//
//        auth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();
//
//        userList = new ArrayList<>();
////        userAdapter = new UserAdapter(userList, this::deleteUser);
//        userAdapter = new UserAdapter(userList, new UserAdapter.UserActionListener() {
//            @Override
//            public void onDelete(String uid) {
//                deleteUser(uid);
//            }
//
//            @Override
//            public void onUpdate(User user) {
//                showUpdateDialog(user);
//            }
//        });
//
//
//        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        userRecyclerView.setAdapter(userAdapter);
//
//        loadUsers();
//
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
//    private void deleteUser(String uid) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null && currentUser.getUid().equals(uid)) {
//            // Optionally, you can navigate to login screen after signOut
//            startActivity(new Intent(UsersListActivity.this, AdminActivity.class));
//            finish();
//            return;
//        }
//
//        firestore.collection("Users").document(uid).delete()
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(UsersListActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
//                    loadUsers();
//                })
//                .addOnFailureListener(e -> Toast.makeText(UsersListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//    private void showUpdateDialog(User user) {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//        builder.setTitle("Update User");
//
//        View view = getLayoutInflater().inflate(R.layout.update_user, null);
//        builder.setView(view);
//
//        EditText usernameInput = view.findViewById(R.id.updateUsername);
//        EditText isAdminInput = view.findViewById(R.id.updateIsAdmin);
//
//        usernameInput.setText(user.getUsername());
//        isAdminInput.setText(user.getIsAdmin());
//
//        builder.setPositiveButton("Update", (dialog, which) -> {
//
//            String newUsername = usernameInput.getText().toString().trim();
//            String newIsAdmin = isAdminInput.getText().toString().trim();
//
//            updateUser(user.getUid(),user.getEmail(), newUsername, newIsAdmin);
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//
//        android.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//    private void updateUser(String uid, String email, String username, String isAdmin) {
//        Map<String, Object> updatedUser = new HashMap<>();
//
//        updatedUser.put("username", username);
//        updatedUser.put("isAdmin", isAdmin);
//
//        firestore.collection("Users").document(uid).update(updatedUser)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(UsersListActivity.this, "User Updated", Toast.LENGTH_SHORT).show();
//                    loadUsers();
//                })
//                .addOnFailureListener(e -> Toast.makeText(UsersListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//
//
//
//}
//
