package rnd.project;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class UitgavenActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uitgaven);
        db = new DatabaseHelper(this);
    }

    //Gets Bedrag per Categorie for all uitgaven
    private void readUitgaven () {
        Cursor data = db.getUitIn("uit");
    }
}
