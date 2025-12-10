package uqac.dim.transportcollectifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;




import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Votre code existant pour les boutons
        Button buttonNavigate = findViewById(R.id.buttonNavigate);
        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RechercheTrajetsActivity.class);
                startActivity(intent);
            }
        });

        Button buttonNavigater = findViewById(R.id.buttonNavigater);
        buttonNavigater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenta = new Intent(MainActivity.this, annoncer.class);
                startActivity(intenta);
            }
        });
    }

    // Méthode pour créer le menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    // Méthode pour gérer la sélection d'un élément dans le menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intentAccueil = new Intent(MainActivity.this, AccueilActivity.class);
                startActivity(intentAccueil);
                return true;
            case R.id.menu2:
                Intent intentRecherche = new Intent(MainActivity.this, RechercheActivity.class);
                startActivity(intentRecherche);
                return true;
            case R.id.menu3:
                Intent intentAnnonce = new Intent(MainActivity.this, AnnonceDepartActivity.class);
                startActivity(intentAnnonce);
                return true;
            case R.id.menu4:
                Intent intentHistorique = new Intent(MainActivity.this, HistoriqueActivity.class);
                startActivity(intentHistorique);
                return true;
            case R.id.menu5:
                Intent intentProfil = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intentProfil);
                return true;
            case R.id.menu6:
                Intent intentParametre = new Intent(MainActivity.this, ParametreActivity.class);
                startActivity(intentParametre);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}