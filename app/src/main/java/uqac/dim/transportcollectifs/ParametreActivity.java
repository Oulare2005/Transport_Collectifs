package uqac.dim.transportcollectifs;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ParametreActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfilePicture;
    private Button btnChangePhoto, btnChangeLanguage, btnChangeTheme, btnDeleteUser;

    private Uri imageUri;
    private StorageReference storageReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);

        // Initialisation des vues
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        btnChangeTheme = findViewById(R.id.btnChangeTheme); // Nouveau bouton pour changer le thème
        btnDeleteUser = findViewById(R.id.btnDeleteUser);   // Nouveau bouton pour supprimer l'utilisateur

        // Initialisation Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");

        // Gestion des clics
        btnChangePhoto.setOnClickListener(v -> openFileChooser());
        btnChangeLanguage.setOnClickListener(v -> showLanguageSelectionDialog());
        btnChangeTheme.setOnClickListener(v -> toggleTheme());
        btnDeleteUser.setOnClickListener(v -> deleteUserAccount());
    }

    // Ouvre le sélecteur d'image
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivProfilePicture.setImageURI(imageUri); // Prévisualisation de l'image
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null && currentUser != null) {
            StorageReference fileReference = storageReference.child(currentUser.getUid() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                Toast.makeText(this, "Photo de profil mise à jour", Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Échec du téléchargement : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLanguageSelectionDialog() {
        // Redirection vers un écran de sélection de langue
        Intent intent = new Intent(this,LanguageSelectionActivity.class);
        startActivity(intent);
    }

    private void toggleTheme() {
        // Alterner entre mode clair et mode sombre
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "Mode clair activé", Toast.LENGTH_SHORT).show();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(this, "Mode sombre activé", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUserAccount() {
        if (currentUser != null) {
            currentUser.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            finish(); // Fermer l'activité
                        } else {
                            Toast.makeText(this, "Échec de la suppression : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
