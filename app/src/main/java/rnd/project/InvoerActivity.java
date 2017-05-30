package rnd.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

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

public class InvoerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper db;
    private String itemSelected;
    private String uitin;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        uitin = "in";
        setContentView(R.layout.nieuwe_invoer);
        db = new DatabaseHelper(this);

        // Spinner wordt gelinkt.
        spinner = (Spinner) findViewById(R.id.spinner);
        setUpSpinner(spinner);

        // Deze toggle button bepaalt of het ingevoerde bedrag een 'inkomsten' of een 'uitgaven' is.
        // Moet dit in de onCreate blijven staan? #aandacht
        ToggleButton uitIn = (ToggleButton) findViewById(R.id.inUitToggle);
        uitIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    uitin = "in";
                }
                else
                    uitin = "uit";
            }
        });
    }

    private void setUpSpinner(Spinner spinner) {
        // Spinner wordt ingesteld:
        Cursor data = db.getCategories();
        String categories[] = new String[data.getCount()];

        for(int i=0;data.moveToNext();i++) {
            categories[i] = data.getString(0);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }




    @Override
    protected void onResume() {
        super.onResume();
        setUpSpinner(spinner);
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void nieuweCategorie(View view) {
        Intent category;
        category = new Intent(getApplicationContext(),CategoryActivity.class);
        startActivity(category);
    }

    //Gets user back to starting screen
    public void gotoHome(View v) {
        Intent home;
        home = new Intent(getBaseContext(),MainActivity.class);
        startActivity(home);
    }

    //Add given amount to database
    public void submitEntry(View view) {
        // Lees de input text-fields:
        EditText bedragInput = (EditText) findViewById(R.id.bedragInput);
        EditText dagInput = (EditText) findViewById(R.id.dagInput);
        EditText maandInput = (EditText) findViewById(R.id.maandInput);
        EditText jaarInput = (EditText) findViewById(R.id.jaarInput);

        // Converteer de data naar de goede types:
        double bedrag = parseBedrag(bedragInput.getText().toString());

        int dag;
        if (isEmpty(dagInput)) { dag = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);}
            else { dag = Integer.parseInt(dagInput.getText().toString());}
        int maand;
        if (isEmpty(maandInput)) { maand = Calendar.getInstance().get(Calendar.MONTH);}
            else { maand = Integer.parseInt(maandInput.getText().toString());}
            maand++;
        int jaar;
        if (isEmpty(jaarInput)) { jaar = Calendar.getInstance().get(Calendar.YEAR);}
            else { jaar = Integer.parseInt(jaarInput.getText().toString());}

        // Voer de data in in de database:
        boolean insert = db.addAmount(bedrag,uitin,itemSelected,jaar,maand,dag);
        if (insert) toastMessage("Insert correct");
        else toastMessage("Insert went wrong");
    }

    private double parseBedrag(String s) {
        return Double.parseDouble(s.replace(',','.'));
    }

    private boolean isEmpty(EditText edittext) {
        return edittext.getText().toString().trim().length() == 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemSelected = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
