package rnd.project;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class LijstActivity extends AppCompatActivity {
    // #aandacht
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lijst);
        listView = (ListView) findViewById(R.id.list);
        setupListView(listView);
    }
    //Gets user to main activity
    public void gotoHome(View v){
        Intent home;
        home = new Intent(getBaseContext(),MainActivity.class);
        startActivity(home);
    }
    //Gets user to settings activity
    public void gotoSettings(View v){
        Intent settings;
        settings = new Intent(getBaseContext(),SettingsActivity.class);
        startActivity(settings);
    }

    private void setupListView(ListView listView) {
        DatabaseHelper db;
        db = new DatabaseHelper(this);
        Bundle b = getIntent().getExtras();
        int month = b.getInt("monthKey");
        int year = b.getInt("yearKey");
        /*
           Column 0: Bedrag
           Column 1: Uit of in
           Column 2: Categorie
           Column 3: Dag
         */
        Cursor res = db.getInUitAndDay(month, year);
        String[] inUitArray = new String[res.getCount()];
        for(int i=0; res.moveToNext(); i++) {
            String inuit = res.getString(1).equals("in") ? "+" : "-";
            String row = inuit + res.getString(0) + "\t" + res.getString(2) + "\t" + res.getInt(3) + "-" + month + "-" + year;
            inUitArray[i] = row;
        }
        ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, inUitArray);
        listView.setAdapter(listArrayAdapter);
    }
}
