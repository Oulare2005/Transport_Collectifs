package uqac.dim.transportcollectifs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.HistoriqueViewHolder> {

    private List<Map<String, Object>> historiqueList;
    private List<DocumentSnapshot> documents;
    private List<DocumentSnapshot> selectedDocuments = new ArrayList<>();
    private OnSelectionChangeListener listener;

    public interface OnSelectionChangeListener {
        void onSelectionChanged(int selectedCount);
    }

    public HistoriqueAdapter(List<Map<String, Object>> historiqueList, List<DocumentSnapshot> documents, OnSelectionChangeListener listener) {
        this.historiqueList = historiqueList;
        this.documents = documents;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoriqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historique, parent, false);
        return new HistoriqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriqueViewHolder holder, int position) {
        Map<String, Object> historique = historiqueList.get(position);

        holder.tvTripId.setText("Trip ID: " + historique.get("tripId"));
        holder.tvUserId.setText("User ID: " + historique.get("userId"));
        holder.tvSeats.setText("Seats: " + historique.get("seats"));
        holder.tvTotalPrice.setText("Total Price: " + historique.get("totalPrice"));

        holder.checkboxDelete.setOnCheckedChangeListener(null);
        holder.checkboxDelete.setChecked(selectedDocuments.contains(documents.get(position)));
        holder.checkboxDelete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DocumentSnapshot document = documents.get(position);
            if (isChecked) {
                selectedDocuments.add(document);
            } else {
                selectedDocuments.remove(document);
            }
            if (listener != null) {
                listener.onSelectionChanged(selectedDocuments.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return historiqueList.size();
    }

    public List<DocumentSnapshot> getSelectedDocuments() {
        return selectedDocuments;
    }

    public static class HistoriqueViewHolder extends RecyclerView.ViewHolder {
        TextView tvTripId, tvUserId, tvSeats, tvTotalPrice;
        CheckBox checkboxDelete;

        public HistoriqueViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTripId = itemView.findViewById(R.id.tvTripId);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            checkboxDelete = itemView.findViewById(R.id.checkboxDelete);
        }
    }
}
