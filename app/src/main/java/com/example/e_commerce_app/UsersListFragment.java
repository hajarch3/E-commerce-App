package com.example.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersListFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private UserAdapter userAdapter;
    private List<User> userList;

    public UsersListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        userRecyclerView = view.findViewById(R.id.userRecyclerView);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, new UserAdapter.UserActionListener() {
            @Override
            public void onDelete(String uid) {
                deleteUser(uid);
            }

            @Override
            public void onUpdate(User user) {
                showUpdateDialog(user);
            }
        });

        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userRecyclerView.setAdapter(userAdapter);

        loadUsers();

        return view;
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

                    Log.d("UsersListFragment", "Loaded user: " + email + ", " + username);
                    userList.add(new User(id, email, username, isAdmin));
                }
                userAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser(String uid) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getUid().equals(uid)) {
//            startActivity(new Intent(getActivity(), AdminActivity.class));
            getParentFragmentManager().popBackStack();
            return;
        }

        firestore.collection("Users").document(uid).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "User Deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showUpdateDialog(User user) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Modifier Utilisateur");

        View view = getLayoutInflater().inflate(R.layout.update_user, null);
        builder.setView(view);

        EditText usernameInput = view.findViewById(R.id.updateUsername);
        EditText isAdminInput = view.findViewById(R.id.updateIsAdmin);

        usernameInput.setText(user.getUsername());
        isAdminInput.setText(user.getIsAdmin());

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newUsername = usernameInput.getText().toString().trim();
            String newIsAdmin = isAdminInput.getText().toString().trim();
            updateUser(user.getUid(), user.getEmail(), newUsername, newIsAdmin);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateUser(String uid, String email, String username, String isAdmin) {
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("username", username);
        updatedUser.put("isAdmin", isAdmin);

        firestore.collection("Users").document(uid).update(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "User Updated", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
