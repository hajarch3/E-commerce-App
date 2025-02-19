package com.example.e_commerce_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;

public class AddProductFragment extends Fragment {

    private static final String TAG = "AddProductFragment";
    private static final String ACCESS_TOKEN = "sl.u.AFi7Q5B5A5C5NfxAAQHzw-HyS9GrNRziv_r9sEUdF1JgwTJU4oWRL3JImPB71uDeNWBibwRWXEKsKDjPWYwIy3YkAGXqu-5JKCMqiq-b2qUBBAKmZQfCL1s98Eos9q2BcKhj2aU6cv8n0DaxvnZXwPRM8tKyuMzcs3VMeV6UOXg-cBLR0JQLiSQrU4KHbYC0WZSZ343qTt-EK_aU5tiSPWp9FB2KZp7kuj4x_DYpXX8xikll9yAy13wGIxXJqDjzNqyPuCCE16vjaINl_gbSWPdpSt-FYIINTR5rdv00hhvwNGJjkX3R3Z8HK8tOhkFx86eVhSptahZBp3EoRq3QksF6iMH9CIBjyXE0Pq2xqMNvNH2oc4MpbfE-qnuJoszU5UjPzPk92Qfp2Vz6G_QjDwlRkxr_mm6nC7tWcV1TzMA-dWXmv46YxfYlV2U3ek8x93r8ZpB9EsU7zoVFaLW9AUAT6JC-gp00CVJZv-_jhKwI7AmHu00qEdyJhi8rQtDAFsAE4-1BD7Q4bnueGQWAMy0W4CBRU9tIuXxLJyS6Zis4mr7htWlQ-1rGksBSENQ0U9TknDO2A6LQyBVy8vUHkuuM5vA29Taj-muT7petNEPM90cwopto7NBaoUrwIE6QklR3KQ3Psl0cbZgUVP2HzXIEkhZnVfBPLDkt3BvkC6LnrPuBhKLI-iuyyfSqzEALaevOrPD4BbKV1QZeTG6_GhNAE0CSI54-sMWapokChSJlo-lUUE_kPmIg7yXVWnCdgQV72kU24M0ziKKGsiiLBN-JfPUMLddZ7smHckpn5Y-BP1MQllVCC_vr8kckSyxZAEVQ0QkKmyuGpz68KGfJ3GiDeSx7Yt7dxTFlpDdsv8_JAE_GUHAb6j2-MPivDMOCwSXWKoSTNOCkShMK3XWIUcKurHVTDnUuMPevv3kuMXMLwDntNkKfL9r5dcJZ6CXsqgCGtDy2p--ZGbLghGer30ez2dYEZZBPA6_oFgq9OUnR64J4LkSBZYJvPi5WZgVeJXIDA2U2Vm_yKLIiRUtW7DL59Bcj_Ww3jpfL2QSONxFaBtmh27gpA4F54gHn9lexhKzLRb4SjQyk_jfUf_FyZq65GJDBgz8gtiBviRcfs9KwQBfzgw8HaxMYxq-554MbxGsDIOAmIVBL9hO05Vz5XkaX1AVfihUynqOnvw1TYVk86m1a-HekFBnJ0cO5hz9HhPGVD20Ii0UAdIPuQSVMft9rHVaApKqtetu84wAhIkACMw3NcRr9jvRpGAADWjj7EP481iG_SI73LgYIaL3vcx2A3P22C-vFEbGfUvmgHveLg7h80d4vsY-6u1Eq0tRb5UUkcyWBLfsvPNKrQ2Kp1ADfUwrNvPpeYYRIAAiq4fLaaWRVnnql52cjsmerk2hPW7QD5ZD3KnSwWWS2lF7Ayr6jaOg7Q0rtg5wOL3GgMM11KlH7EPL9b4WsKZvB_ooH0DE";
    private EditText  editTextPrix, editTextTitle, editTextDescription, editTextType, editTextImageUrl;
    private FirebaseFirestore db;
    private DbxClientV2 dropboxClient;

    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextPrix = view.findViewById(R.id.editTextPrix);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextType = view.findViewById(R.id.editTextType);
        editTextImageUrl = view.findViewById(R.id.editTextImageUrl);
        Button buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        db = FirebaseFirestore.getInstance();

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        dropboxClient = new DbxClientV2(config, ACCESS_TOKEN);

        buttonSelectImage.setOnClickListener(v -> selectImage());
        buttonSave.setOnClickListener(v -> {
            Log.d(TAG, "Bouton 'Enregistrer' cliqué");
            saveProduct();
        });

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
        Log.d(TAG, "saveProduct() appelé");

        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String prix = editTextPrix.getText().toString().trim();
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Description: " + description);
        Log.d(TAG, "Type: " + type);
        Log.d(TAG, "Image URI: " + imageUri);

        if (title.isEmpty() || description.isEmpty() || type.isEmpty() || imageUri == null) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try (InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri)) {
                if (inputStream == null) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Erreur lors de l'obtention de l'image", Toast.LENGTH_SHORT).show());
                    return;
                }

                String dropboxPath = "/Dossier de l'équipe bazarly/" + System.currentTimeMillis() + ".jpg";
                final String[] dropboxUrl = new String[1]; // Utilisation d'un tableau pour encapsuler la variable

                FileMetadata metadata = dropboxClient.files().uploadBuilder(dropboxPath)
                        .uploadAndFinish(inputStream);

                dropboxUrl[0] = dropboxClient.sharing().createSharedLinkWithSettings(metadata.getPathLower()).getUrl();
                dropboxUrl[0] = dropboxUrl[0].replace("?dl=0", "?raw=1"); // Modifier l'URL pour un accès direct à l'image
                dropboxUrl[0] = dropboxUrl[0].replace("www.dropbox.com", "dl.dropboxusercontent.com"); // Modifier le domaine pour obtenir un lien direct

                getActivity().runOnUiThread(() -> {
                    Log.d(TAG, "Image téléchargée sur Dropbox, URL: " + dropboxUrl[0]);
                    saveProductToFirestore(title, description, type, dropboxUrl[0] , prix );
                });

            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'upload de l'image", e);
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Erreur lors de l'upload de l'image", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void saveProductToFirestore(String title, String description, String type, String imageUrl , String prix) {
        Produits produit = new Produits(description, title, type , imageUrl , prix);
        db.collection("Produits").add(produit).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "Produit enregistré dans Firestore");
            Toast.makeText(getActivity(), "Produit enregistré avec succès", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Erreur lors de l'enregistrement du produit", e);
            Toast.makeText(getActivity(), "Erreur lors de l'enregistrement du produit", Toast.LENGTH_SHORT).show();
        });
    }
}