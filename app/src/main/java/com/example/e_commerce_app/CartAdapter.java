package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Produits> cartList;
    private OnDeleteClickListener onDeleteClickListener;

    public CartAdapter(Context context, List<Produits> cartList) {
        this.context = context;
        this.cartList = cartList;

    }

    public void updateCartList(List<Produits> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }
    // Interface pour gérer la suppression
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Produits product = cartList.get(position);
        holder.itemTitle.setText(product.getTitre());
        holder.itemPrix.setText(product.getPrix() + " DH");

        // Modifier l'URL de Dropbox
        String originalUrl = product.getUrl();
        String modifiedUrl = originalUrl.replace("www.dropbox.com", "dl.dropboxusercontent.com").replace("?dl=0", "");

        Glide.with(context)
                .load(modifiedUrl)
                .error(R.drawable.music) // Image à afficher en cas d'erreur
                .into(holder.itemImage);
        // Gérer le clic sur le bouton Supprimer
        holder.buttonDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitle, itemPrix;
        ImageView itemImage;
        ImageButton buttonDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_cart_title);
            itemPrix = itemView.findViewById(R.id.item_cart_prix);
            itemImage = itemView.findViewById(R.id.item_cart_image);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

    }
}