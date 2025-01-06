package com.example.e_commerce_app;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        // Configuration du DrawerLayout
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Gestion de la navigation
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Utilisez des conditions if-else pour éviter l'erreur
            if (item.getItemId() == R.id.nav_meuble) {
                selectedFragment = new meuble_fragment();
            } else if (item.getItemId() == R.id.nav_decoration) {
                selectedFragment = new deco_fragment();
            } else if (item.getItemId() == R.id.nav_accessoires) {
                selectedFragment = new acce_fragment();
            } else if (item.getItemId() == R.id.nav_art) {
                selectedFragment = new art_fragment();
            } else if (item.getItemId() == R.id.nav_collection) {
                selectedFragment = new objet_fragment();
            } else if (item.getItemId() == R.id.nav_musique) {
                selectedFragment = new musique_fragment();
            }

            // Remplacez le fragment actuel par le fragment sélectionné
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            // Fermez le tiroir de navigation
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });




    }

}
