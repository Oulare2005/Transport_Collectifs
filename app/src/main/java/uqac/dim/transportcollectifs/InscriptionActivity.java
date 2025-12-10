package uqac.dim.transportcollectifs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class InscriptionActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword;
    private RadioGroup rgSex;
    private RadioButton rbMale, rbFemale;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        rgSex = findViewById(R.id.rgSex);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        final String firstName = etFirstName.getText().toString();  // Déclarez les variables comme finales
        final String lastName = etLastName.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        int selectedSexId = rgSex.getCheckedRadioButtonId();
        final String gender;  // Déclarez gender comme final
        if (selectedSexId == rbMale.getId()) {
            gender = "Homme";
        } else if (selectedSexId == rbFemale.getId()) {
            gender = "Femme";
        } else {
            gender = "";  // Assurez-vous que gender a toujours une valeur
        }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        } else {
            // Créer l'utilisateur avec Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // L'utilisateur a été créé avec succès
                            saveUserToFirestore(firstName, lastName, email, gender);  // Appel de la méthode pour enregistrer l'utilisateur
                        } else {
                            // L'authentification a échoué
                            Toast.makeText(InscriptionActivity.this, "Échec de l'inscription : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveUserToFirestore(String firstName, String lastName, String email, String gender) {
        String userId = mAuth.getCurrentUser().getUid();

        // Créer un objet utilisateur
        User user = new User(firstName, lastName, email, gender);

        // Sauvegarder l'utilisateur dans Firestore
        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    // Vous pouvez rediriger l'utilisateur vers une autre activité ici
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(InscriptionActivity.this, "Échec de l'enregistrement dans Firestore : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Classe pour l'utilisateur
    public static class User {
        public String firstName, lastName, email, gender;

        public User(String firstName, String lastName, String email, String gender) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.gender = gender;
        }
    }
}
