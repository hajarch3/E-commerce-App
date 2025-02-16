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
    private static final String ACCESS_TOKEN = "sl.u.AFhpprZnJrDiy2imJdv7f6E6uL0t1StqbEtOW5uYyFQMraKHUOLgxDkkLIRVDPDUEGQ1_uc6XhCOtERnd3ktPXHdfyxJ_aVMOOKgzRXgeDTRAHFgK5Xu1xDSrkhSvOpWufaii76mXXDFmtKTNgKj2FC1gwPcgEpj_5mN_4Owz_CRWjfAeWAog05-R-bn5CSByMLul2TKx-Q-wS8IjnZgzoN8tUZOCMcaLFjUgbc7ZA-HekLOYtWk5gXLC5lII7FIZVsP1_92iPnk2uglS_juCCWq3gXwkkhQCxVnNd-d50TY0c_qZ4m59mIXXzjxyO9_mgkKuvSlXJwGtEQNkURzlGssZuC6fOiXff99M4f9qsTOkl4qnuZODWum7aQ4LLbnuVJ0dUNR8VZDwJ1KH-tPL2HGRvLSVdt9WBDO39zcebgps4b52FEwYdHIgsQKeRx8GYXpldE2bbtYgCUjnkLn9cH4y8Jirp70cwrDmuDXFrDAGnavmO4gONp7-TxJ3RaE5ptIKgMiXFAakV204Zcasedzundp1DvIizNMM7KANjrPWhzlAhq21l22kb-HDBOerKRrmX0A15hoRCMZrAjnZUm6qBF__wdURK0Jqs4GEVw_S9JAJ-MHxDkEx-9SLtLeRKltauxUFIl7lIqY1zn9CWO-FCEeaj-nIoGeo0FK03Vxjv8NJy3d0CKLQIBlwZz8Vx1o7Dhba-nWZakymM9ZAXWEYMXTRchc7orog1i0SgzlNFyNWtAP9mBoq-YmTWCSQKJ_j3Pi6Dfp9bbXXQyeIqWILHXjuhXm-kob7BlkuvePRniecRIN9w37zrp45nKt_Q4ANYncIIeevoF5i-6pwQEFiurwhYJS8BfTDyZBf8Jjkdg4FwRJjIhQGM2u2BG9LqzC7hB_JVp3w63UYAwcjzmRy6A1K0S1uvmtTGIo-SW1WcYmWa2ZnIUSSg8DkILQp1n346-5QRjWOQYbV1s3MKaKwd53s_kYPCzJEr1_SxdM0RWPFP_r0-xDkKEcyvsRGyBY_Z79KYMnISHILIPi-LbT9vsHP66YvRibDD7N1BaAxkXJmNt1Et4b-vYGVH7XNMM60NrH3i7LEecW7MyfDJb5XWyZmHxNWbzdsiVri2NNozqKiwV-s6Sn6-BozMLnVdnC_6mbRlGERJBSmz7MrVIE1aZO3s_ONtn2SXqVGgcxvzUXkGultytq8rlvJcABkQ571SK6blWZYcjo8sBgkh75Sibu9Z2vimCEmdQa3OvVIWj3VK8SFfRZxQifW5-822nwO1i5fvWftDZPV84o5UU-fmj-89nUTGnY0pLZJtEl8szkvgnJ9hXaDkmfT4mT_tu1cDNf9pUFNruoKCPeCipTxJ9Cpg6iolDFJWhTeP_zaIubCDS9bqMrGGEDOesP7D6odEJVNg3wCEbYtFl9kpJmgZ2JNl8xJh9uDJW1IKKjUkNVQF_izQCobqVnm9W441o"; // Remplacez par votre nouveau jeton d'accès

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
                .replace(R.id.fragment_container, editProductFragment)
                .addToBackStack(null)
                .commit();
    }
}