package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProduitsAdapter extends RecyclerView.Adapter<ProduitsAdapter.ViewHolder> {

    private Context context;
    private List<Produits> produitsList;

    public ProduitsAdapter(Context context, List<Produits> produitsList) {
        this.context = context;
        this.produitsList = produitsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produits produit = produitsList.get(position);
        holder.titleTextView.setText(produit.getTitre());
        holder.descriptionTextView.setText(produit.getDescription());
        holder.prixTextView.setText(produit.getPrix() + " DH");

        // Modifier l'URL de Dropbox
        String originalUrl = produit.getUrl();
        String modifiedUrl = originalUrl.replace("www.dropbox.com", "dl.dropboxusercontent.com").replace("?dl=0", "");

        Glide.with(context)
                .load(modifiedUrl)
                .error(R.drawable.music) // Image à afficher en cas d'erreur
                .into(holder.imageView);

        holder.buttonEdit.setOnClickListener(v -> {
            if (context instanceof MenuActivity) {
                ((MenuActivity) context).addProductToCart(produit);
                Toast.makeText(context, "Le produit est ajouté au panier", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return produitsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, prixTextView;
        TextView descriptionTextView;
        ImageView imageView;
        ImageButton buttonEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            imageView = itemView.findViewById(R.id.item_image);
            prixTextView = itemView.findViewById(R.id.item_prix);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}