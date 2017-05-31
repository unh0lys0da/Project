package rnd.project;

import android.database.Cursor;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by:
 * Rits Joosten
 * Tom van der Waa
 * Joey van den Eijnden
 * Auke Rosier
 * Niels van Velzen
 * Douwe Huijsmans
 * on 17/05/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    // Settings moet nog vorm worden gegeven #aandacht
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        db = new DatabaseHelper(this);
    }

    //Gets all values in database for given month
    private void readMonth (int month, int year) {
        Cursor data = db.getMaand(month, year);
    }
    //Gets user back to staring screen
    public void gotoHome(View v){
        Intent home;
        home = new Intent(getBaseContext(),MainActivity.class);
        startActivity(home);
    }

    public void gotoActivityCategory (View v)
    {
        Intent home;
        home = new Intent(getBaseContext(),CategoryActivity.class);
        startActivity(home);
    }

    public void gotoPeriodiekeMutaties (View v)
    {
        Intent home;
        home = new Intent(getBaseContext(),PeriodiekeMutatiesActivity.class);
        startActivity(home);
    }
}
