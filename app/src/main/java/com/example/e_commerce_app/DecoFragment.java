package com.example.e_commerce_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.e_commerce_app.R;

import java.util.ArrayList;
import java.util.List;

public class DecoFragment extends Fragment {

    private static final String TAG = "DecoFragment";
    private RecyclerView recyclerView;
    private ProduitsAdapter adapter;
    private List<Produits> produitsList;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        produitsList = new ArrayList<>();
        adapter = new ProduitsAdapter(getContext(), produitsList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deco, container, false);
        recyclerView = view.findViewById(R.id.recylerview_deco);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        fetchItemsFromFirestore();
        return view;
    }

    private void fetchItemsFromFirestore() {
        db.collection("Produits")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Produits produit = document.toObject(Produits.class);
                                Log.d(TAG, "Document ID: " + document.getId() + ", Data: " + document.getData());
                                if ("deco".equals(produit.getType())) {
                                    produitsList.add(produit);
                                    Log.d(TAG, "Produit ajouté: " + produit.getTitre());
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Total produits: " + produitsList.size());
                    } else {
                        Log.e(TAG, "Erreur lors de la récupération des produits", task.getException());
                    }
                });
    }
}