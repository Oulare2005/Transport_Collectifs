package uqac.dim.transportcollectifs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoriqueActivity extends AppCompatActivity {

    private HistoriqueAdapter adapter;
    private RecyclerView recyclerViewHistorique;
    private Button btnDeleteSelected, btnPaySelected;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        recyclerViewHistorique = findViewById(R.id.recyclerViewHistorique);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);
        btnPaySelected = findViewById(R.id.btnPaySelected);
        db = FirebaseFirestore.getInstance();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedTrips());
        btnPaySelected.setOnClickListener(v -> payForSelectedTrip());

        loadHistoriqueFromFirestore();
    }

    private void loadHistoriqueFromFirestore() {
        db.collection("historique")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> historiqueList = new ArrayList<>();
                    List<DocumentSnapshot> documents = new ArrayList<>();

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> historique = document.getData();
                        if (historique != null) {
                            historiqueList.add(historique);
                            documents.add(document);
                        }
                    }

                    adapter = new HistoriqueAdapter(historiqueList, documents, selectedCount -> {
                        btnDeleteSelected.setVisibility(selectedCount > 0 ? View.VISIBLE : View.GONE);
                        btnPaySelected.setVisibility(selectedCount == 1 ? View.VISIBLE : View.GONE);
                    });

                    recyclerViewHistorique.setLayoutManager(new LinearLayoutManager(this));
                    recyclerViewHistorique.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteSelectedTrips() {
        List<DocumentSnapshot> selectedDocuments = adapter.getSelectedDocuments();

        for (DocumentSnapshot document : selectedDocuments) {
            db.collection("historique").document(document.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Trajet supprimé", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        loadHistoriqueFromFirestore();
    }

    private void payForSelectedTrip() {
        DocumentSnapshot selectedDocument = adapter.getSelectedDocuments().get(0);
        if (selectedDocument != null) {
            Toast.makeText(this, "Paiement réussi pour le trajet ID : " + selectedDocument.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}
