package rnd.project;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;

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
    //Deze activity laat de gehele lijst aan in- en uitgaven in een maand zien
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lijst);
        Bundle b = getIntent().getExtras(); //Voor het ophalen van de meegegeven maand en jaar int
        int month = b.getInt("monthKey");
        int year = b.getInt("yearKey");
        TextView textView = (TextView) findViewById(R.id.textView);
        String maand = new DateFormatSymbols().getMonths()[month-1];
        String text = maand + " " + year;
        textView.setText(text);
        listView = (ListView) findViewById(R.id.list);
        setupListView(listView, month, year);



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
    private void setupListView(ListView listView, int month, int year) {
        final DatabaseHelper db;
        db = new DatabaseHelper(this);

        /*
           Column 0: Bedrag
           Column 1: Uit of in
           Column 2: Categorie
           Column 3: Dag
         */
        Cursor res = db.getInUitAndDay(month, year);
        String[] inUitArray = new String[res.getCount()];
        for(int i=0, j = 0; res.moveToNext(); i++, j++) {
            String inuit = res.getString(1).equals("in") ? "+" : "-";
            String row = inuit + res.getString(0) + "\t" + res.getString(2) + "\t" + res.getInt(3) + "-" + month + "-" + year + " ID: " + res.getInt(4);
            inUitArray[i] = row;
        }
        final ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, inUitArray);
        listView.setAdapter(listArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    String str = (String) adapterView.getItemAtPosition(position);
                    final int id = parseId(str);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(LijstActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(LijstActivity.this);
                }
                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteEntry(Integer.toString(id));
                                toastMessage("Bedrag verwijderd");
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }


    public int parseId(String str) {
        String[] strr = str.split(" ");
        int id = Integer.parseInt(strr[strr.length-1]);
        return id;
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}


