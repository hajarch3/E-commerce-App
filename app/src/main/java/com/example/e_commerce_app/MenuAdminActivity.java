package com.example.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

        // Ouvrir le tiroir de navigation lorsque l'icône est cliquée
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

                // Utilisation de else if pour gérer les clics
                if (item.getItemId() == R.id.nav_ajouter_prd) {
                    selectedFragment = new AddProductFragment();
                } else if (item.getItemId() == R.id.nav_lister_prd) {
                    selectedFragment = new ProductListFragment();
                }
                else if (item.getItemId() == R.id.nav_lister_user) {
                    selectedFragment = new UsersListFragment();
                }else if (item.getItemId() == R.id.nav_ajouter_user) {
                    selectedFragment = new UsersFragment();
                }


                // Remplacer le contenu du conteneur par le fragment sélectionné
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_admin, selectedFragment);
                    transaction.commit();
                }

                // Fermer le tiroir après la sélection
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
