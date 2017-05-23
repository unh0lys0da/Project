package rnd.project;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        db = new DatabaseHelper(this);
        readMonth(1);
    }

    private void readMonth (int month) {
        Cursor data = db.readMaand(month);
    }
}
