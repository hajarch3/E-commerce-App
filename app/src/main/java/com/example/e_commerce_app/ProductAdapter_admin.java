package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductAdapter_admin extends RecyclerView.Adapter<ProductAdapter_admin.ProductViewHolder> {

    private Context context;
    private List<Produits> productList;
    private FirebaseFirestore db;
    private OnProductClickListener onProductClickListener;

    public interface OnProductClickListener {
        void onProductDelete(Produits product);
        void onProductEdit(Produits product);
    }

    public ProductAdapter_admin(Context context, List<Produits> productList, OnProductClickListener onProductClickListener) {
        this.context = context;
        this.productList = productList;
        this.db = FirebaseFirestore.getInstance();
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Produits product = productList.get(position);

        holder.textViewTitle.setText(product.getTitre());
        holder.textViewDescription.setText(product.getDescription());
        holder.textViewType.setText(product.getType());

        Glide.with(context)
                .load(product.getUrl())
                .into(holder.imageViewProduct);

        holder.buttonDelete.setOnClickListener(v -> onProductClickListener.onProductDelete(product));
        holder.buttonEdit.setOnClickListener(v -> onProductClickListener.onProductEdit(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProductList(List<Produits> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription, textViewType;
        ImageView imageViewProduct;
        Button buttonDelete, buttonEdit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewType = itemView.findViewById(R.id.textViewType);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}