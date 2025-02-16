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

public class MenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        imageButton = findViewById(R.id.menu_logo);

        // Ouvrir le tiroir de navigation lorsque l'icône est cliquée
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Utilisation de else if pour gérer les clics
                if (item.getItemId() == R.id.nav_meuble) {
                    selectedFragment = new MeubleFragment();
                } else if (item.getItemId() == R.id.nav_decoration) {
                    selectedFragment = new DecoFragment();
                } else if (item.getItemId() == R.id.nav_accessoires) {
                    selectedFragment = new AccessoiresFragment();
                } else if (item.getItemId() == R.id.nav_art) {
                    selectedFragment = new ArtFragment();
                } else if (item.getItemId() == R.id.nav_collection) {
                    selectedFragment = new ObjetFragment();
                } else if (item.getItemId() == R.id.nav_musique) {
                    selectedFragment = new MusiqueFragment();
                }
              else if (item.getItemId() == R.id.panierButton) {
                selectedFragment = new PanierFragment();}
//                else if (item.getItemId() == R.id.ADMIN) {
//                    selectedFragment = new UsersFragment();}
                 else if (item.getItemId() == R.id.loginButton) {
                    Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(intent);

            } else {
                    // Si aucune correspondance, afficher un message
                    Toast.makeText(MenuActivity.this, "Option non reconnue", Toast.LENGTH_SHORT).show();
                }

                // Remplacer le contenu du conteneur par le fragment sélectionné
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();
                }

                // Fermer le tiroir après la sélection
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    }

