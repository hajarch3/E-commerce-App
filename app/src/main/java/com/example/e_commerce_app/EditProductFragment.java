package com.example.e_commerce_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.util.UUID;

public class EditProductFragment extends Fragment {

    private static final String TAG = "EditProductFragment";
    private static final String ACCESS_TOKEN = "sl.u.AFiSP1o1ekvXcDcfFtJhjPtf3nfnRO6-ysWDp6xPLdIh8pMI_3ltEorvvrMDECANoJd8UO_d2prfwd0jf3aX3KWO0J5fzpM-L7Vvh6G-4x_ThxXkFEZs6Au7jXgaVKzN1S34inR2YaGvXlx6nJNR4QPZG1mSK28ucT962kBkIKb54YEAAJUcAeycDO2HD-25HTNmh9lWx7nmEhv6YrEKRm0rDCum8rxehnyGB2fT-DnxuWsI_hgzryNNEnuqvNY45QU20XbEDCXCwsieQG_sJ6Kk48HQN-odfP1agFzt-MInEz242yx_qrGdHuVrRqJmfJTQ9oSuNU7YHZ6m2W4QonIC4Dua7UuMXmr65Jko60C5yL88dhHOzURqC9lGUGiokRiIzHcKzufu90roGjn-QqXxxZxgDI1gNn6hCvDJKnF-5hHcBWDWUYidsI5ZYxFZ_w22G8ydIFkOCGkZuVkdNblGKloSt4Llj1UOe7AJ78s9pAy0-MaiWX_IvkdqjBc4412IA0ltoM_vhgvViqMOQub7-h0FiKbLU4CUaf2XaSSglXxhK9M9BgkEAlYsvy7fnNikWFHY__K9Ow5U3QvHNkrtA50PETof7BUfXXWgk2By0SYVpE7um8YrE1BNLS41zFkXXfTKGE-QoF_S59CATZnq7E9YDxfUqMqcHXA7jRFvGLPv_cA8Qi5xKudyTu0SQ0QLoKM88S5Q8LUCGpwZ_3KEcpEchoJOmsp3nXAoHojfUIEI66rVC4ZBpNXhjI5rrrb6ijv16q1c-gPFe_koe8QEF7lYnc_o0EAXdqmx8oYJNOjdYjWGIb-hMCXBTB_5KNtIQekdrlsZ6m1wuUkn_W_ldShZv3cYMDf5gxIhb7JiYTI0nCKFlQQwNw4HbGd7lKoDNPkb_lVZAZmqCQY6USE9oA7VcVe4pjVlEGanAMNucWKs1helRDT1V55FNN3JBz4O_9L1xgMxsbfGgeFUnaQR4ocJnxF5NorQMJKGGDinpaZnjitWNor3B34vn125MLwUm7tPG_7PxKETPUI1ZdSFLD7YuceCdxhZNc_lwWFPqvFd8DbnWilNpR2JcmTs8h1gLzMW-Jovr7jEJBfHWtq06QAw4V1uyVielQtx2f-buLusl5z81I_kJN0kyNa7no43oxJssGva7604VthX3O1UsktQACN5BXe2XCI1aUmLkjcXHUzWHz12PZ2IJhN-3NFdzKoJywOEsyBqRjoXadTXi_fdwAz1M4J73ebtC-fwuXprGo57mO6VTkrBOufhK952L9gtaS6qfYBhL2QGNfSRy9w5sEGhZujV9fH4H_gQ_iHroWS_2LC2ehxgSJzylBxoV6ubwNMcaIqoaUKkiYQ4xMvefsE4wS4Tfd0WPKBQjP5CTLCGteaH4QVPW5Bg7KOzdYrKqoEGMrsPKzBAaQ2p012V_itKZrsMmPNwt8WTW8H5_1694qNYd2AsmBB6o88";
    private EditText editTextTitle, edittexyprix, editTextDescription, editTextType, editTextImageUrl;
    private Button buttonSelectImage, buttonSave;
    private FirebaseFirestore db;
    private DbxClientV2 dropboxClient;

    private Produits product;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextType = view.findViewById(R.id.editTextType);
        editTextImageUrl = view.findViewById(R.id.editTextImageUrl);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        buttonSave = view.findViewById(R.id.buttonSave);
        edittexyprix = view.findViewById(R.id.editTextPrix);
        db = FirebaseFirestore.getInstance();

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        dropboxClient = new DbxClientV2(config, ACCESS_TOKEN);

        if (getArguments() != null) {
            product = (Produits) getArguments().getSerializable("product");
            if (product != null) {
                editTextTitle.setText(product.getTitre());
                editTextDescription.setText(product.getDescription());
                editTextType.setText(product.getType());
                editTextImageUrl.setText(product.getUrl());
                edittexyprix.setText(product.getPrix());
            }
        }

        buttonSelectImage.setOnClickListener(v -> selectImage());
        buttonSave.setOnClickListener(v -> saveProduct());

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri = result.getData().getData();
                        editTextImageUrl.setText(imageUri.toString());
                    }
                }
            });

    private void saveProduct() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String imageUrl = editTextImageUrl.getText().toString().trim();
        String prix = edittexyprix.getText().toString().trim();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(type) || TextUtils.isEmpty(prix) || TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        product.setTitre(title);
        product.setDescription(description);
        product.setType(type);
        product.setPrix(prix);

        if (imageUri != null) {
            uploadImageToDropbox(imageUri, new DropboxUploadCallback() {
                @Override
                public void onUploadSuccess(String url) {
                    product.setUrl(url);
                    saveProductToFirestore();
                }

                @Override
                public void onUploadFailure(Exception e) {
                    Log.e(TAG, "Erreur lors du téléchargement de l'image sur Dropbox", e);
                    Toast.makeText(getActivity(), "Erreur lors du téléchargement de l'image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            saveProductToFirestore();
        }
    }

    private void saveProductToFirestore() {
        db.collection("Produits").document(product.getId()).set(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Produit mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de la mise à jour du produit", e);
                    Toast.makeText(getActivity(), "Erreur lors de la mise à jour du produit", Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImageToDropbox(Uri imageUri, DropboxUploadCallback callback) {
        new Thread(() -> {
            try (InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri)) {
                String dropboxPath = "/Dossier de l'équipe bazarly/" + System.currentTimeMillis() + ".jpg";
                final String[] dropboxUrl = new String[1]; // Utilisation d'un tableau pour encapsuler la variable

                FileMetadata metadata = dropboxClient.files().uploadBuilder(dropboxPath)
                        .uploadAndFinish(inputStream);

                dropboxUrl[0] = dropboxClient.sharing().createSharedLinkWithSettings(metadata.getPathLower()).getUrl();
                dropboxUrl[0] = dropboxUrl[0].replace("?dl=0", "?raw=1"); // Modifier l'URL pour un accès direct à l'image
                dropboxUrl[0] = dropboxUrl[0].replace("www.dropbox.com", "dl.dropboxusercontent.com"); // Modifier le domaine pour obtenir un lien direct

                getActivity().runOnUiThread(() -> callback.onUploadSuccess(dropboxUrl[0]));
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> callback.onUploadFailure(e));
            }
        }).start();
    }

    private interface DropboxUploadCallback {
        void onUploadSuccess(String url);
        void onUploadFailure(Exception e);
    }
}