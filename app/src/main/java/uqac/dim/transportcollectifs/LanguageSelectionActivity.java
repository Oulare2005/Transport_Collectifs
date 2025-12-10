package uqac.dim.transportcollectifs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LanguageSelectionActivity extends AppCompatActivity {

    private RadioGroup rgLanguages;
    private Button btnSaveLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Appliquer la langue sauvegardée avant de charger le layout
        applySavedLanguage();

        // Définir le layout
        setContentView(R.layout.activity_language_selection);

        // Initialiser les composants
        rgLanguages = findViewById(R.id.rgLanguages);
        btnSaveLanguage = findViewById(R.id.btnSaveLanguage);

        // Charger la langue actuelle et mettre à jour les radios
        loadCurrentLanguage();

        btnSaveLanguage.setOnClickListener(v -> {
            int selectedId = rgLanguages.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadio = findViewById(selectedId);
                String languageCode = selectedRadio.getTag().toString();
                saveAndApplyLanguage(languageCode);
            }
        });
    }

    private void applySavedLanguage() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String languageCode = preferences.getString("app_language", "en"); // Anglais par défaut
        updateLocale(languageCode);
    }

    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void loadCurrentLanguage() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String currentLanguage = preferences.getString("app_language", "en");

        for (int i = 0; i < rgLanguages.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) rgLanguages.getChildAt(i);
            if (radioButton.getTag().toString().equals(currentLanguage)) {
                radioButton.setChecked(true);
                break;
            }
        }
    }

    private void saveAndApplyLanguage(String languageCode) {
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("app_language", languageCode);
        editor.apply();

        // Redémarrer l'activité pour appliquer la langue
        restartActivity();
    }

    private void restartActivity() {
        Intent intent = new Intent(this, LanguageSelectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
