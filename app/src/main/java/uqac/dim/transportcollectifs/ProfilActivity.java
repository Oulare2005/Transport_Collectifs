package uqac.dim.transportcollectifs;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {

    private ImageView ivProfilePicture;
    private EditText etFirstName, etLastName, etEmail;
    private Spinner spinnerGender;
    private Button btnSave, btnLogout;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Vérification si l'utilisateur est connecté
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Veuillez vous connecter", Toast.LENGTH_SHORT).show();
            finish();  // Fermer l'activité
            return;
        }

        // Lier les vues
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);

        // Initialisation de Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Charger les informations utilisateur
        loadUserProfile();

        // Gérer le clic sur le bouton "Enregistrer"
        btnSave.setOnClickListener(v -> saveUserProfile());

        // Gérer le clic sur le bouton de déconnexion
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Récupérer les informations de l'utilisateur depuis Firestore
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Charger les informations utilisateur
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String email = documentSnapshot.getString("email");
                            String gender = documentSnapshot.getString("gender");
                            String photoUrl = documentSnapshot.getString("photoUrl");

                            // Remplir les champs
                            etFirstName.setText(firstName != null ? firstName : "");
                            etLastName.setText(lastName != null ? lastName : "");
                            etEmail.setText(email != null ? email : "");

                            // Sélectionner le genre dans le Spinner
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                    android.R.layout.simple_spinner_item,
                                    new String[]{"Homme", "Femme", "Autre"});
                            spinnerGender.setAdapter(adapter);
                            if (gender != null) {
                                int position = adapter.getPosition(gender);
                                spinnerGender.setSelection(position);
                            }

                            // Charger l'image de profil
                            if (photoUrl != null && !photoUrl.isEmpty()) {
                                Glide.with(this).load(photoUrl).into(ivProfilePicture);
                            } else {
                                ivProfilePicture.setImageResource(R.drawable.logo2); // Image par défaut
                            }
                        } else {
                            Toast.makeText(this, "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void saveUserProfile() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        // Vérifier que les champs ne sont pas vides
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Préparer les données à mettre à jour
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("firstName", firstName);
        userUpdates.put("lastName", lastName);
        userUpdates.put("email", email);
        userUpdates.put("gender", gender);

        // Mettre à jour les données dans Firestore
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
