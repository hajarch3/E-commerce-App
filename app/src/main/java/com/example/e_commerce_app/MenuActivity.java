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

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton imageButton;
    private CartFragment cartFragment;
    private Fragment currentFragment;
    private List<Produits> cartList; // Liste partagée pour le panier

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        imageButton = findViewById(R.id.menu_logo);

        // Restaurer la liste après rotation
        if (savedInstanceState != null) {
            cartList = (List<Produits>) savedInstanceState.getSerializable("cartList");
        } else {
            cartList = new ArrayList<>();
        }

        // Récupérer l'instance existante de CartFragment si elle existe
        cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CartFragment");
        if (cartFragment == null) {
            cartFragment = new CartFragment();
        }

        // Passer la liste du panier au CartFragment
        cartFragment.setCartList(cartList);

        // Afficher HomePageFragment au démarrage
        if (savedInstanceState == null) {
            homeFragment HomePageFragment = new homeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, HomePageFragment, "HomePageFragment");
            transaction.commit();
        }
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
                } else if (item.getItemId() == R.id.panierButton) {
                    selectedFragment = cartFragment;
                }  else {
                    Toast.makeText(MenuActivity.this, "Option non reconnue", Toast.LENGTH_SHORT).show();
                }

                if (selectedFragment != null) {
                    currentFragment = selectedFragment;
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment, "CartFragment");
                    transaction.commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("cartList", new ArrayList<>(cartList));
    }

    public void addProductToCart(Produits product) {
        cartList.add(product);
        if (cartFragment != null) {
            cartFragment.updateCartList(cartList); // Forcer la mise à jour
        }

    }
}