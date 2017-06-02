package rnd.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;

import static java.lang.Integer.parseInt;

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

public class MaandBewerkenActivity extends AppCompatActivity {
    // #aandacht
    //Deze activity laat de gehele lijst aan in- en uitgaven in een maand zien
    ListView listView;
    String maandJaar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maand_bewerken);
        listView = (ListView) findViewById(R.id.list);
        setupListView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                maandJaar = (listView.getItemAtPosition(position).toString());
                gotoLijst(v);
            }
        });
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

    //Haalt de gegevens uit de database in zet ze in de listView
    private void setupListView(ListView listView) {
        DatabaseHelper db;
        db = new DatabaseHelper(this);

        /*
           Column 0: Maand
           Column 1: Jaar
         */
        Cursor res = db.getMaandJaar();
        String[] inUitArray = new String[res.getCount()];
        for(int i=0; res.moveToNext(); i++) {
            String row = res.getString(0) + " " + res.getString(1);
            inUitArray[i] = row;
        }
        ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, inUitArray);
        listView.setAdapter(listArrayAdapter);
    }

    public void gotoLijst (View v) {
        String maand = maandJaar.substring(0, maandJaar.indexOf(' '));
        String jaar = maandJaar.substring(maandJaar.indexOf(' ')+1);
        int month = parseInt(maand);
        int year = parseInt(jaar);
        String yearKey = "yearKey";
        String monthKey = "monthKey";

        Intent lijst;
        lijst = new Intent(getBaseContext(),LijstActivity.class);
        lijst.putExtra(yearKey, year);
        lijst.putExtra(monthKey, month);
        startActivity(lijst);
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
