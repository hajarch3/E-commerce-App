package com.example.e_commerce_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MenuAdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        drawerLayout = findViewById(R.id.draweradmin_layout);
        imageButton = findViewById(R.id.menuadmin_logo);

       
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;


                if (item.getItemId() == R.id.nav_ajouter_prd) {
                    selectedFragment = new AddProductFragment();
                } else if (item.getItemId() == R.id.nav_lister_prd) {
                    selectedFragment = new ProductListFragment();
                } else if (item.getItemId() == R.id.nav_lister_user) {
                    selectedFragment = new UsersListFragment();
                } else if (item.getItemId() == R.id.nav_ajouter_user) {
                    selectedFragment = new UsersFragment();
                }


                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_admin, selectedFragment);
                    transaction.commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_admin, new homeFragment()); // Replace DefaultFragment with your default fragment
            transaction.commit();
        }
    }
}