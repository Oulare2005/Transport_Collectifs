package uqac.dim.transportcollectifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Initialisation des boutons et définition des écouteurs de clic

        // Button to navigate to Connexion activity
        Button buttonCons = findViewById(R.id.buttonCons);
        buttonCons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the Connexion activity
                Intent intent = new Intent(AccueilActivity.this, ConnexionActivity.class);
                startActivity(intent);
            }
        });

        // Button to navigate to Inscription activity
        Button buttonIns = findViewById(R.id.buttonIns);
        buttonIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the Inscription activity
                Intent intent = new Intent(AccueilActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });
    }
}
