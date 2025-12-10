package uqac.dim.transportcollectifs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AnnonceDepartActivity extends AppCompatActivity {
    private EditText etUserId, etTripId, etSeats, etDepartureDate, etPrice;
    private Button btnAnnonce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annoncer);

        // Initialisation des champs de texte et du bouton
        etUserId = findViewById(R.id.etUserId);
        etTripId = findViewById(R.id.etTripId);
        etDepartureDate = findViewById(R.id.etDepartureDate); // Champ pour la date de départ
        etPrice = findViewById(R.id.etPrix);  // Champ pour le prix
        etSeats = findViewById(R.id.etSeats); // Initialisation du champ pour les places
        btnAnnonce = findViewById(R.id.btnAnnonce);

        // Logique du clic sur le bouton "Annonce"
        btnAnnonce.setOnClickListener(v -> {
            // Réinitialisation des erreurs
            resetErrors();

            String userId = etUserId.getText().toString().trim();
            String tripId = etTripId.getText().toString().trim();
            String seats = etSeats.getText().toString().trim();
            String departureDate = etDepartureDate.getText().toString().trim();
            String price = etPrice.getText().toString().trim();

            boolean isValid = true;

            // Validation des champs
            if (userId.isEmpty()) {
                etUserId.setError("Le champ utilisateur est requis");
                isValid = false;
            }

            if (tripId.isEmpty()) {
                etTripId.setError("Le champ trajet est requis");
                isValid = false;
            }

            if (seats.isEmpty()) {
                etSeats.setError("Veuillez spécifier le nombre de places");
                isValid = false;
            } else if (!isNumeric(seats) || Integer.parseInt(seats) <= 0) {
                etSeats.setError("Le nombre de places doit être un entier positif");
                isValid = false;
            }

            if (departureDate.isEmpty()) {
                etDepartureDate.setError("La date de départ est requise");
                isValid = false;
            } else if (!isValidDate(departureDate)) {
                etDepartureDate.setError("Veuillez entrer une date valide au format JJ/MM/AAAA");
                isValid = false;
            }

            if (price.isEmpty()) {
                etPrice.setError("Veuillez spécifier un prix");
                isValid = false;
            } else if (!isNumeric(price) || Double.parseDouble(price) <= 0) {
                etPrice.setError("Le prix doit être un nombre positif");
                isValid = false;
            }

            // Si tout est valide, envoyer les données
            if (isValid) {
                int seatsInt = Integer.parseInt(seats);
                double priceDouble = Double.parseDouble(price);
                annonceTrip(userId, tripId, seatsInt, departureDate, priceDouble);
            }
        });
    }

    private void annonceTrip(String userId, String tripId, int seats, String departureDate, double price) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> annonce = new HashMap<>();
        annonce.put("userId", userId);
        annonce.put("tripId", tripId);
        annonce.put("seats", seats);
        annonce.put("departureDate", departureDate);
        annonce.put("price", price);

        db.collection("annonces")
                .add(annonce)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Annonce ajoutée avec succès!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de l'ajout : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void resetErrors() {
        // Supprimer les erreurs sur les champs
        etUserId.setError(null);
        etTripId.setError(null);
        etSeats.setError(null);
        etDepartureDate.setError(null);
        etPrice.setError(null);
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            Calendar currentDate = Calendar.getInstance();
            Calendar enteredDate = Calendar.getInstance();
            enteredDate.setTime(sdf.parse(date));

            // Vérifier si la date est dans le passé
            return !enteredDate.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }
}
