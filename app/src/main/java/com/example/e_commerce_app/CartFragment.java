package com.example.e_commerce_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Produits> cartList; // Ne pas initialiser ici !
    private TextView totalPriceTextView;
    private TextView emptyCartText;

    public CartFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Utiliser la liste existante si elle existe
        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        cartAdapter = new CartAdapter(getContext(), cartList);
        recyclerView.setAdapter(cartAdapter);
        // Gérer la suppression d'un produit
        cartAdapter.setOnDeleteClickListener(position -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Supprimer le produit")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce produit ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        cartList.remove(position); // Supprimer le produit de la liste
                        cartAdapter.updateCartList(cartList); // Mettre à jour l'adaptateur
                        updateTotalPrice(); // Mettre à jour le prix total
                        updateEmptyState(); // Mettre à jour l'état du panier (vide ou non)
                    })
                    .setNegativeButton("Non", null) // Ne rien faire si l'utilisateur clique sur "Non"
                    .show(); // Afficher la boîte de dialogue
        });

        updateTotalPrice();
        emptyCartText = view.findViewById(R.id.emptyCartText);
        updateEmptyState();
        return view;
    }


    public void setCartList(List<Produits> cartList) {
        this.cartList = cartList;
        if (cartAdapter != null) {
            cartAdapter.updateCartList(cartList);
        }
    }

    public void updateCartList(List<Produits> cartList) {
        this.cartList = cartList;
        if (cartAdapter != null) {
            cartAdapter.updateCartList(cartList);
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Produits product : cartList) {
            try {
                double priceD = Double.parseDouble(product.getPrix());
                totalPrice += priceD;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        totalPriceTextView.setText("Prix total: " + totalPrice + " DH");
    }
    private void updateEmptyState() {
        if (cartList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartText.setVisibility(View.GONE);
        }
    }
}