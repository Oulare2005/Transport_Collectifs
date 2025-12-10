package uqac.dim.transportcollectifs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RechercheTrajetsActivity extends AppCompatActivity {

    private EditText etDepartureCity, etArrivalCity, etDate;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Initialisation des vues
        etDepartureCity = findViewById(R.id.etDepartureCity);
        etArrivalCity = findViewById(R.id.etArrivalCity);
        etDate = findViewById(R.id.etDate);
        btnSearch = findViewById(R.id.btnSearch);

        // Gestion du clic sur le bouton "Rechercher"
        btnSearch.setOnClickListener(v -> {
            resetErrors(); // Supprimer les erreurs existantes avant chaque validation

            String departureCity = etDepartureCity.getText().toString().trim();
            String arrivalCity = etArrivalCity.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            boolean isValid = true;

            // Validation des champs
            if (departureCity.isEmpty()) {
                etDepartureCity.setError("Veuillez entrer une ville de départ");
                isValid = false;
            }

            if (arrivalCity.isEmpty()) {
                etArrivalCity.setError("Veuillez entrer une ville d'arrivée");
                isValid = false;
            }

            if (date.isEmpty()) {
                etDate.setError("Veuillez entrer une date");
                isValid = false;
            } else if (!isValidDate(date)) {
                etDate.setError("Format de la date invalide. Utilisez JJ/MM/AAAA");
                isValid = false;
            } else if (!isYearValid(date)) {
                etDate.setError("L'année ne peut pas être inférieure à l'année actuelle");
                isValid = false;
            }

            if (isValid) {
                // Passer à l'activité des résultats si tout est valide
                Intent intent = new Intent(RechercheTrajetsActivity.this, ResultatsTrajetsActivity.class);
                intent.putExtra("departureCity", departureCity);
                intent.putExtra("arrivalCity", arrivalCity);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

    private void resetErrors() {
        // Réinitialiser les erreurs sur les champs
        etDepartureCity.setError(null);
        etArrivalCity.setError(null);
        etDate.setError(null);
    }

    private boolean isValidDate(String date) {
        // Valider le format de la date (JJ/MM/AAAA)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isYearValid(String date) {
        // Vérifier si l'année n'est pas inférieure à l'année actuelle
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Calendar enteredDate = Calendar.getInstance();
            enteredDate.setTime(dateFormat.parse(date));

            int enteredYear = enteredDate.get(Calendar.YEAR);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            return enteredYear >= currentYear;
        } catch (ParseException e) {
            return false;
        }
    }
}
