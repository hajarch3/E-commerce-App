package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private UserActionListener actionListener;

    public UserAdapter(List<User> userList, UserActionListener actionListener) {
        this.userList = userList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.emailText.setText(user.getEmail());
        holder.usernameText.setText("Username: " + (user.getUsername() != null ? user.getUsername() : "No Username"));
        holder.isAdminText.setText(user.getIsAdmin());
        holder.deleteButton.setOnClickListener(v -> actionListener.onDelete(user.getUid()));
        holder.updateButton.setOnClickListener(v -> actionListener.onUpdate(user));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView emailText, usernameText, isAdminText;
        Button deleteButton,updateButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            emailText = itemView.findViewById(R.id.userEmail);
            usernameText = itemView.findViewById(R.id.userUsername);
            isAdminText = itemView.findViewById(R.id.userIsAdmin);
            deleteButton = itemView.findViewById(R.id.deleteUserButton);
            updateButton = itemView.findViewById(R.id.updateUserButton);
        }
    }

    public interface UserActionListener {
        void onDelete(String uid);
        void onUpdate(User user);
    }
}
