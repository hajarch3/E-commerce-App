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
    private static final String ACCESS_TOKEN = "sl.u.AFhpprZnJrDiy2imJdv7f6E6uL0t1StqbEtOW5uYyFQMraKHUOLgxDkkLIRVDPDUEGQ1_uc6XhCOtERnd3ktPXHdfyxJ_aVMOOKgzRXgeDTRAHFgK5Xu1xDSrkhSvOpWufaii76mXXDFmtKTNgKj2FC1gwPcgEpj_5mN_4Owz_CRWjfAeWAog05-R-bn5CSByMLul2TKx-Q-wS8IjnZgzoN8tUZOCMcaLFjUgbc7ZA-HekLOYtWk5gXLC5lII7FIZVsP1_92iPnk2uglS_juCCWq3gXwkkhQCxVnNd-d50TY0c_qZ4m59mIXXzjxyO9_mgkKuvSlXJwGtEQNkURzlGssZuC6fOiXff99M4f9qsTOkl4qnuZODWum7aQ4LLbnuVJ0dUNR8VZDwJ1KH-tPL2HGRvLSVdt9WBDO39zcebgps4b52FEwYdHIgsQKeRx8GYXpldE2bbtYgCUjnkLn9cH4y8Jirp70cwrDmuDXFrDAGnavmO4gONp7-TxJ3RaE5ptIKgMiXFAakV204Zcasedzundp1DvIizNMM7KANjrPWhzlAhq21l22kb-HDBOerKRrmX0A15hoRCMZrAjnZUm6qBF__wdURK0Jqs4GEVw_S9JAJ-MHxDkEx-9SLtLeRKltauxUFIl7lIqY1zn9CWO-FCEeaj-nIoGeo0FK03Vxjv8NJy3d0CKLQIBlwZz8Vx1o7Dhba-nWZakymM9ZAXWEYMXTRchc7orog1i0SgzlNFyNWtAP9mBoq-YmTWCSQKJ_j3Pi6Dfp9bbXXQyeIqWILHXjuhXm-kob7BlkuvePRniecRIN9w37zrp45nKt_Q4ANYncIIeevoF5i-6pwQEFiurwhYJS8BfTDyZBf8Jjkdg4FwRJjIhQGM2u2BG9LqzC7hB_JVp3w63UYAwcjzmRy6A1K0S1uvmtTGIo-SW1WcYmWa2ZnIUSSg8DkILQp1n346-5QRjWOQYbV1s3MKaKwd53s_kYPCzJEr1_SxdM0RWPFP_r0-xDkKEcyvsRGyBY_Z79KYMnISHILIPi-LbT9vsHP66YvRibDD7N1BaAxkXJmNt1Et4b-vYGVH7XNMM60NrH3i7LEecW7MyfDJb5XWyZmHxNWbzdsiVri2NNozqKiwV-s6Sn6-BozMLnVdnC_6mbRlGERJBSmz7MrVIE1aZO3s_ONtn2SXqVGgcxvzUXkGultytq8rlvJcABkQ571SK6blWZYcjo8sBgkh75Sibu9Z2vimCEmdQa3OvVIWj3VK8SFfRZxQifW5-822nwO1i5fvWftDZPV84o5UU-fmj-89nUTGnY0pLZJtEl8szkvgnJ9hXaDkmfT4mT_tu1cDNf9pUFNruoKCPeCipTxJ9Cpg6iolDFJWhTeP_zaIubCDS9bqMrGGEDOesP7D6odEJVNg3wCEbYtFl9kpJmgZ2JNl8xJh9uDJW1IKKjUkNVQF_izQCobqVnm9W441o"; // Remplacez par votre nouveau jeton d'accès

    private EditText editTextTitle, editTextDescription, editTextType, editTextImageUrl;
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

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(type) || TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        product.setTitre(title);
        product.setDescription(description);
        product.setType(type);

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