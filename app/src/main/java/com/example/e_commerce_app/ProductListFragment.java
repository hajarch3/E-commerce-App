package com.example.e_commerce_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ProductAdapter_admin.OnProductClickListener {

    private static final String TAG = "ProductListFragment";
    private static final String ACCESS_TOKEN = "sl.u.AFiSP1o1ekvXcDcfFtJhjPtf3nfnRO6-ysWDp6xPLdIh8pMI_3ltEorvvrMDECANoJd8UO_d2prfwd0jf3aX3KWO0J5fzpM-L7Vvh6G-4x_ThxXkFEZs6Au7jXgaVKzN1S34inR2YaGvXlx6nJNR4QPZG1mSK28ucT962kBkIKb54YEAAJUcAeycDO2HD-25HTNmh9lWx7nmEhv6YrEKRm0rDCum8rxehnyGB2fT-DnxuWsI_hgzryNNEnuqvNY45QU20XbEDCXCwsieQG_sJ6Kk48HQN-odfP1agFzt-MInEz242yx_qrGdHuVrRqJmfJTQ9oSuNU7YHZ6m2W4QonIC4Dua7UuMXmr65Jko60C5yL88dhHOzURqC9lGUGiokRiIzHcKzufu90roGjn-QqXxxZxgDI1gNn6hCvDJKnF-5hHcBWDWUYidsI5ZYxFZ_w22G8ydIFkOCGkZuVkdNblGKloSt4Llj1UOe7AJ78s9pAy0-MaiWX_IvkdqjBc4412IA0ltoM_vhgvViqMOQub7-h0FiKbLU4CUaf2XaSSglXxhK9M9BgkEAlYsvy7fnNikWFHY__K9Ow5U3QvHNkrtA50PETof7BUfXXWgk2By0SYVpE7um8YrE1BNLS41zFkXXfTKGE-QoF_S59CATZnq7E9YDxfUqMqcHXA7jRFvGLPv_cA8Qi5xKudyTu0SQ0QLoKM88S5Q8LUCGpwZ_3KEcpEchoJOmsp3nXAoHojfUIEI66rVC4ZBpNXhjI5rrrb6ijv16q1c-gPFe_koe8QEF7lYnc_o0EAXdqmx8oYJNOjdYjWGIb-hMCXBTB_5KNtIQekdrlsZ6m1wuUkn_W_ldShZv3cYMDf5gxIhb7JiYTI0nCKFlQQwNw4HbGd7lKoDNPkb_lVZAZmqCQY6USE9oA7VcVe4pjVlEGanAMNucWKs1helRDT1V55FNN3JBz4O_9L1xgMxsbfGgeFUnaQR4ocJnxF5NorQMJKGGDinpaZnjitWNor3B34vn125MLwUm7tPG_7PxKETPUI1ZdSFLD7YuceCdxhZNc_lwWFPqvFd8DbnWilNpR2JcmTs8h1gLzMW-Jovr7jEJBfHWtq06QAw4V1uyVielQtx2f-buLusl5z81I_kJN0kyNa7no43oxJssGva7604VthX3O1UsktQACN5BXe2XCI1aUmLkjcXHUzWHz12PZ2IJhN-3NFdzKoJywOEsyBqRjoXadTXi_fdwAz1M4J73ebtC-fwuXprGo57mO6VTkrBOufhK952L9gtaS6qfYBhL2QGNfSRy9w5sEGhZujV9fH4H_gQ_iHroWS_2LC2ehxgSJzylBxoV6ubwNMcaIqoaUKkiYQ4xMvefsE4wS4Tfd0WPKBQjP5CTLCGteaH4QVPW5Bg7KOzdYrKqoEGMrsPKzBAaQ2p012V_itKZrsMmPNwt8WTW8H5_1694qNYd2AsmBB6o88"; // Remplacez par votre nouveau jeton d'accès

    private FirebaseFirestore db;
    private DbxClientV2 dropboxClient;
    private RecyclerView recyclerView;
    private ProductAdapter_admin productAdapter;
    private List<Produits> productList;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        db = FirebaseFirestore.getInstance();

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        dropboxClient = new DbxClientV2(config, ACCESS_TOKEN);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        searchView = view.findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter_admin(getContext(), productList, this);
        recyclerView.setAdapter(productAdapter);

        loadProducts();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });

        return view;
    }

    private void loadProducts() {
        db.collection("Produits").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Produits product = document.toObject(Produits.class);
                    product.setId(document.getId());
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    private void filterProducts(String text) {
        List<Produits> filteredList = new ArrayList<>();
        for (Produits product : productList) {
            if (!TextUtils.isEmpty(product.getTitre()) && product.getTitre().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateProductList(filteredList);
    }

    @Override
    public void onProductDelete(Produits product) {
        new AlertDialog.Builder(getContext())
                .setTitle("Supprimer le produit")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce produit?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    db.collection("Produits")
                            .whereEqualTo("titre", product.getTitre())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    QuerySnapshot documents = task.getResult();
                                    for (QueryDocumentSnapshot document : documents) {
                                        db.collection("Produits").document(document.getId()).delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    productList.remove(product);
                                                    productAdapter.notifyDataSetChanged();
                                                    Log.d(TAG, "DocumentSnapshot supprimé avec succès!");
                                                })
                                                .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la suppression du document", e));
                                    }
                                } else {
                                    Log.w(TAG, "Erreur lors de la recherche du document", task.getException());
                                }
                            });
                })
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    public void onProductEdit(Produits product) {
        EditProductFragment editProductFragment = new EditProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) product);
        editProductFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_admin, editProductFragment)
                .addToBackStack(null)
                .commit();
    }
}