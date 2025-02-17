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
    private static final String ACCESS_TOKEN = "sl.u.AFhJ6upjvLav3LhXh12phkVRvD3gFhTva81zkXxr2gcNJ0F05lOupVjuhFgKey-NwzOudkkfS2kWN1KdxKEOjArIhemShYZ8m1ujHJIWmN-X3QgGt1XRDP90PKt1HNj0SsaO_Yj9zZ_VpusD6h9vwTJ1joMJEarmdnHziHXstywg4W9VPw7Dlq-gJ5H_RhLHhQGbnH-0rUp6CZ-qExSXy6pvm_kyS4iuTUZWHTGi3ndJE0kgPMsfCqmXESLF0ZIYMqwF0zbem3zZZcJPEolkmOg33miTRRu5PqYy3rvyGKh8ZxTJv-iFmVgxOm_-nT_5wJeOsKrtENxdOjsqdqeUtdE-VdqMxCzbcNcn7kywh4dMEkZhu_kuOv8waNd4GUgIEGAH3kPKHQ_MvYQZqV8GtiWQ9U_cZk4XDMLmxdQHnS5GMIwb7VcvWTZlEK5nIUqlidhazs9-UwXt2sZMs2E1vTd28CnF2hfJ2_8DRcTHbPu9MwFbW5nXVuNey6u_nCnENhou2QuPx53CiTc8xfYcdwWf3EWFAB687jYTPs7PdGCd2_gRhdVmNrtPO3xxr4eg-RC0CPh__WehRnql58c-vrPX_D1syqVisn2uKjewftqPzdM4O-P3MjnU29cHLVAsxbZVUFNbDdvPCPehmem8k4PpiF4UThBTIOiMO10mHKL62U1VOMZWvkWqfyRN30kCkGWYShZE7ynUfp7thxRACl-WVXZuHlmsvamvoDzI_s2TW9rLOkoSpuG_8WaqSaO6CmOPNbQO5h7mIN28Do-jgMhRqdKpEcjnNW-nAFPqwKPS10CoESqT099WiPmQ_yumsDR5sIDGA4V1FGu1cehUGImEVTaG0zFM7xhakGy4ReY-zIK00Jy78UPNRO8aWr9uRNR84PCz94DYc_vrDxdHKlhatGavUS-Bi2StWLjgEYkD_89YbN0bOliyGScRd8z_zl8CGqxQ7AqkTEmWu5ccPWy_5ev4KM74rDKzSEt8q9cr8bIG3zN6sr0QRVdntG_4zLxoAn_yUHFu6ApS_a96VJY1zfep2oCxhHKZTsWVHBinOocj4DYK_2zyO_qMhX9vqcX2MEi0Mn7QucEn2nzkqaxiknj_UQLjmF31r_Bl16qXgKzGYg0QY3ruIFIhIroTKe_EFft8O54YLqdUa57I0ebOrKg5hJGP5wuEm3fTtb2BBDwh_Ea31nhChdQv6oOn4A1f7NGkrcJd2CDfo3Iebm3l4Yug2xL28mNdtsg4kTNW34iIaPEA-3EC6hUWw-oDrxRzlQNlirtLiJyXg5qgDnm89_IKT3-C0DUnH7yLR14tL51GfLxPUoMvAUA9PzfC2dODNA_BiXzHritgftITcO0XyQoSu-DoM_9rT5KJFxkkXP4AkD0g8KfdzMuuhCjpltArzrjBzltx5MZSdAgjLQpllpEqdQxzCGXPHYoTlWOfKrXj_j4jPOUskpKdIK3EulU"; // Remplacez par votre nouveau jeton d'accès

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