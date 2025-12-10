package uqac.dim.transportcollectifs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrajetsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTrajets;
    private TrajetAdapter trajetAdapter;
    private List<Trajet> trajetList = new ArrayList<>();
    private Button btnSaveHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajets);

        recyclerViewTrajets = findViewById(R.id.recyclerViewTrajets);
        recyclerViewTrajets.setLayoutManager(new LinearLayoutManager(this));

        trajetAdapter = new TrajetAdapter(trajetList);
        recyclerViewTrajets.setAdapter(trajetAdapter);
        //btnSaveHistory = findViewById(R.id.btnenregistrer);
        //btnSaveHistory.setOnClickListener(v -> saveSelectedTrajets());

        loadTrajetsFromFirestore();
    }

    private void loadTrajetsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("annonces")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    trajetList.clear();
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Trajet trajet = document.toObject(Trajet.class);
                        trajetList.add(trajet);
                    }
                    trajetAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de la récupération : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveSelectedTrajets() {
        Map<Trajet, Integer> selectedTrajets = trajetAdapter.getSelectedSeatsMap();

        if (selectedTrajets.isEmpty()) {
            Toast.makeText(this, "Aucun trajet sélectionné", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (Map.Entry<Trajet, Integer> entry : selectedTrajets.entrySet()) {
            Trajet trajet = entry.getKey();
            int seats = entry.getValue();

            // Calcul du prix total
            double totalPrice = seats * trajet.getPrice();

            // Créer un objet pour l'historique
            Map<String, Object> historique = new HashMap<>();
            historique.put("userId", trajet.getUserId());
            historique.put("tripId", trajet.getTripId());
            historique.put("departureDate", trajet.getDepartureDate());
            historique.put("seats", seats);
            historique.put("totalPrice", totalPrice);

            // Ajouter à Firestore
            db.collection("historique").add(historique);
        }

        Toast.makeText(this, "Trajets enregistrés dans l'historique", Toast.LENGTH_SHORT).show();
    }
}