package rnd.project;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

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
        readUitgaven();
    }

    //Gets Bedrag per Categorie for all uitgaven
    private void readUitgaven () {
        Cursor data = db.getUitIn("uit");
        int saveBedrag = 0; //Index of the column in the select statement of the query; so not the index of the column in the table!
        int saveCat = 1;
        ArrayList<Double> bedragList = new ArrayList<>();
        ArrayList<String> categorieList = new ArrayList<>();
        while(data.moveToNext()) { //moves to next row in query
            Double bedrag = data.getDouble(saveBedrag); // gets result from current in column saveBedrag
            String categorie = data.getString(saveCat);
            bedragList.add(bedrag);
            categorieList.add(categorie);
        }
    }
}
