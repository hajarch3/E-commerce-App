package com.example.e_commerce_app;

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
    private static final String ACCESS_TOKEN = "sl.u.AFhpprZnJrDiy2imJdv7f6E6uL0t1StqbEtOW5uYyFQMraKHUOLgxDkkLIRVDPDUEGQ1_uc6XhCOtERnd3ktPXHdfyxJ_aVMOOKgzRXgeDTRAHFgK5Xu1xDSrkhSvOpWufaii76mXXDFmtKTNgKj2FC1gwPcgEpj_5mN_4Owz_CRWjfAeWAog05-R-bn5CSByMLul2TKx-Q-wS8IjnZgzoN8tUZOCMcaLFjUgbc7ZA-HekLOYtWk5gXLC5lII7FIZVsP1_92iPnk2uglS_juCCWq3gXwkkhQCxVnNd-d50TY0c_qZ4m59mIXXzjxyO9_mgkKuvSlXJwGtEQNkURzlGssZuC6fOiXff99M4f9qsTOkl4qnuZODWum7aQ4LLbnuVJ0dUNR8VZDwJ1KH-tPL2HGRvLSVdt9WBDO39zcebgps4b52FEwYdHIgsQKeRx8GYXpldE2bbtYgCUjnkLn9cH4y8Jirp70cwrDmuDXFrDAGnavmO4gONp7-TxJ3RaE5ptIKgMiXFAakV204Zcasedzundp1DvIizNMM7KANjrPWhzlAhq21l22kb-HDBOerKRrmX0A15hoRCMZrAjnZUm6qBF__wdURK0Jqs4GEVw_S9JAJ-MHxDkEx-9SLtLeRKltauxUFIl7lIqY1zn9CWO-FCEeaj-nIoGeo0FK03Vxjv8NJy3d0CKLQIBlwZz8Vx1o7Dhba-nWZakymM9ZAXWEYMXTRchc7orog1i0SgzlNFyNWtAP9mBoq-YmTWCSQKJ_j3Pi6Dfp9bbXXQyeIqWILHXjuhXm-kob7BlkuvePRniecRIN9w37zrp45nKt_Q4ANYncIIeevoF5i-6pwQEFiurwhYJS8BfTDyZBf8Jjkdg4FwRJjIhQGM2u2BG9LqzC7hB_JVp3w63UYAwcjzmRy6A1K0S1uvmtTGIo-SW1WcYmWa2ZnIUSSg8DkILQp1n346-5QRjWOQYbV1s3MKaKwd53s_kYPCzJEr1_SxdM0RWPFP_r0-xDkKEcyvsRGyBY_Z79KYMnISHILIPi-LbT9vsHP66YvRibDD7N1BaAxkXJmNt1Et4b-vYGVH7XNMM60NrH3i7LEecW7MyfDJb5XWyZmHxNWbzdsiVri2NNozqKiwV-s6Sn6-BozMLnVdnC_6mbRlGERJBSmz7MrVIE1aZO3s_ONtn2SXqVGgcxvzUXkGultytq8rlvJcABkQ571SK6blWZYcjo8sBgkh75Sibu9Z2vimCEmdQa3OvVIWj3VK8SFfRZxQifW5-822nwO1i5fvWftDZPV84o5UU-fmj-89nUTGnY0pLZJtEl8szkvgnJ9hXaDkmfT4mT_tu1cDNf9pUFNruoKCPeCipTxJ9Cpg6iolDFJWhTeP_zaIubCDS9bqMrGGEDOesP7D6odEJVNg3wCEbYtFl9kpJmgZ2JNl8xJh9uDJW1IKKjUkNVQF_izQCobqVnm9W441o"; // Remplacez par votre nouveau jeton d'accès

    private EditText editTextTitle, editTextDescription, editTextType, editTextImageUrl;
    private FirebaseFirestore db;
    private DbxClientV2 dropboxClient;

    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        editTextTitle = view.findViewById(R.id.editTextTitle);
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
                    saveProductToFirestore(title, description, type, dropboxUrl[0]);
                });

            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'upload de l'image", e);
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Erreur lors de l'upload de l'image", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void saveProductToFirestore(String title, String description, String type, String imageUrl) {
        Produits produit = new Produits(description, title, type, imageUrl);
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