package uqac.dim.transportcollectifs;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResultatsTrajetsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTrajets;
    private TrajetAdapter trajetAdapter;
    private List<Trajet> trajetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_trajets);

        // Récupérer les données passées par l'intent
        String departureCity = getIntent().getStringExtra("departureCity");
        String arrivalCity = getIntent().getStringExtra("arrivalCity");
        String date = getIntent().getStringExtra("date");

        // Initialiser le RecyclerView
        recyclerViewTrajets = findViewById(R.id.recyclerViewTrajets);
        recyclerViewTrajets.setLayoutManager(new LinearLayoutManager(this));
        trajetAdapter = new TrajetAdapter(trajetList);
        recyclerViewTrajets.setAdapter(trajetAdapter);

        // Charger les trajets depuis Firestore
        loadTrajetsFromFirestore(departureCity, arrivalCity, date);
    }

    private void loadTrajetsFromFirestore(String departureCity, String arrivalCity, String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("annonces")
                .whereEqualTo("userId", departureCity)
                .whereEqualTo("tripId", arrivalCity)
                .whereEqualTo("departureDate", date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    trajetList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Trajet trajet = document.toObject(Trajet.class);
                        trajetList.add(trajet);
                    }

                    trajetAdapter.notifyDataSetChanged();

                    if (trajetList.isEmpty()) {
                        Toast.makeText(ResultatsTrajetsActivity.this, "Aucun trajet trouvé", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ResultatsTrajetsActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
