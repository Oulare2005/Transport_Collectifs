package uqac.dim.transportcollectifs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrajetAdapter extends RecyclerView.Adapter<TrajetAdapter.TrajetViewHolder> {

    private final List<Trajet> trajetList; // Liste des trajets
    private final Map<Trajet, Integer> selectedTrajets = new HashMap<>(); // Map des trajets sélectionnés avec sièges

    public TrajetAdapter(List<Trajet> trajetList) {
        this.trajetList = trajetList;
    }

    // Retourner la map des trajets sélectionnés et leurs sièges
    public Map<Trajet, Integer> getSelectedSeatsMap() {
        return selectedTrajets;
    }

    @NonNull
    @Override
    public TrajetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trajet, parent, false);
        return new TrajetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrajetViewHolder holder, int position) {
        Trajet trajet = trajetList.get(position);

        // Remplir les données de l'item
        holder.tvTripId.setText("Arriver : " + trajet.getTripId());
        holder.tvUserId.setText("Départ : " + trajet.getUserId());
        holder.tvDepartureDate.setText("Date de départ : " + trajet.getDepartureDate());
        holder.tvSeats.setText("Places disponibles : " + trajet.getSeats());
        holder.tvPrice.setText("Prix : " + trajet.getPrice() + " €");
        holder.tvSelectedSeats.setText("0");

        // Gestion des clics pour + et -
        holder.btnPlus.setOnClickListener(v -> {
            int currentSeats = Integer.parseInt(holder.tvSelectedSeats.getText().toString());
            if (currentSeats < trajet.getSeats()) {
                currentSeats++;
                holder.tvSelectedSeats.setText(String.valueOf(currentSeats));
                selectedTrajets.put(trajet, currentSeats);
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentSeats = Integer.parseInt(holder.tvSelectedSeats.getText().toString());
            if (currentSeats > 0) {
                currentSeats--;
                holder.tvSelectedSeats.setText(String.valueOf(currentSeats));
                selectedTrajets.put(trajet, currentSeats);
            }
        });

        // Sauvegarder dans Firestore en cliquant sur "Enregistrer"
        holder.btnEnregistrer.setOnClickListener(v -> {
            int seats = Integer.parseInt(holder.tvSelectedSeats.getText().toString());
            if (seats > 0) {
                double totalPrice = seats * trajet.getPrice();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> historique = new HashMap<>();
                historique.put("userId", trajet.getUserId());
                historique.put("tripId", trajet.getTripId());
                historique.put("departureDate", trajet.getDepartureDate());
                historique.put("seats", seats);
                historique.put("totalPrice", totalPrice);

                db.collection("historique")
                        .add(historique)
                        .addOnSuccessListener(documentReference ->
                                Toast.makeText(v.getContext(), "Trajet enregistré avec succès.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(v.getContext(), "Erreur lors de l'enregistrement : " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(v.getContext(), "Veuillez sélectionner au moins un siège.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trajetList.size();
    }

    // Classe ViewHolder
    static class TrajetViewHolder extends RecyclerView.ViewHolder {
        TextView tvTripId, tvUserId, tvDepartureDate, tvSeats, tvPrice, tvSelectedSeats;
        Button btnMinus, btnPlus, btnEnregistrer;

        public TrajetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTripId = itemView.findViewById(R.id.tvTripId);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvDepartureDate = itemView.findViewById(R.id.tvDepartureDate);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSelectedSeats = itemView.findViewById(R.id.tvSelectedSeats);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnEnregistrer = itemView.findViewById(R.id.btnenregistrer);
        }
    }
}
