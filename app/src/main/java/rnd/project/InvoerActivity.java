package rnd.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by:
 * Rits Joosten
 * Tom van der Waa
 * Joey van den Eijnden
 * Auke Rosier
 * Niels van Velzen
 * Douwe Huijsmans
 * on 17/05/2017
 */

public class InvoerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper db;
    private String itemSelected;
    private String uitin;
    private Spinner spinner;
    private boolean herhaald;
    private boolean dagelijks;
    private boolean wekelijks;
    private boolean maandelijks;
    private boolean dagEnable;

    private boolean weekEnable;
    private boolean maandEnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        herhaald = false;
        dagelijks = false;
        wekelijks = false;
        maandelijks = false;
        dagEnable = false;
        weekEnable = false;
        maandEnable = false;
        itemSelected = "leeg";

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

        if (isEmpty(bedragInput)) toastMessage("Er is geen bedrag ingevoerd.");
        else {
            // Converteer de data naar de goede types:
            double bedrag = parseBedrag(bedragInput.getText().toString());
            int dag = isEmpty(dagInput) ?
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH) :
                    Integer.parseInt(dagInput.getText().toString());
            int maand = isEmpty(maandInput) ?
                    Calendar.getInstance().get(Calendar.MONTH) + 1 :
                    Integer.parseInt(maandInput.getText().toString());
            int jaar = isEmpty(jaarInput) ?
                    Calendar.getInstance().get(Calendar.YEAR) :
                    Integer.parseInt(jaarInput.getText().toString());
            String datum = dag + "/" + maand + "/" + jaar;
            if (!chechDatum(datum)) toastMessage("Ongeldige datum ingevoerd"); //Chech op geldigheid van de datum
            else {

                if (itemSelected == "leeg") {itemSelected = "Overige..";}
                // Voer de data in in de database:
                boolean insert = db.addAmount(bedrag, uitin, itemSelected, jaar, maand, dag);
                if (insert) {
                    if(herhaald) {
                        repeatInUit(bedrag,uitin, itemSelected, jaar, maand, dag);
                    }
                    toastMessage("Bedrag toegevoegd");
                }
                else toastMessage("Er ging iets mis met het toevoegen van het bedrag");
            }
        }

    }

    private void repeatInUit(double bedrag, String uitin, String item, int jaar, int maand, int dag) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, jaar);
        calendar.set(Calendar.MONTH, maand);
        calendar.set(Calendar.DAY_OF_MONTH, dag);
        if(dagelijks)
            while (calendar.get(Calendar.YEAR) < jaar + 2) {
                calendar.add(Calendar.DAY_OF_YEAR,1);
                if(calendar.get(Calendar.MONTH) == maand && calendar.get(Calendar.YEAR) == jaar) {
                    db.addAmount(bedrag, uitin, item, jaar, maand, calendar.get(Calendar.DAY_OF_MONTH));
                }
                else {
                    db.addAmountFut(bedrag, uitin, item, jaar, maand, calendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, jaar);
        calendar.set(Calendar.MONTH, maand);
        calendar.set(Calendar.DAY_OF_MONTH, dag);
        if(wekelijks)
            while (calendar.get(Calendar.YEAR) < jaar + 2) {
                calendar.add(Calendar.DAY_OF_YEAR,7);
                if(calendar.get(Calendar.MONTH) == maand && calendar.get(Calendar.YEAR) == jaar) {
                    db.addAmount(bedrag, uitin, item, jaar, maand, calendar.get(Calendar.DAY_OF_MONTH));
                }
                else {
                    db.addAmountFut(bedrag, uitin, item, jaar, maand, calendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        if(maandelijks)
            while (calendar.get(Calendar.YEAR) < jaar + 5) {
                calendar.add(Calendar.MONTH,1);
                if(calendar.get(Calendar.MONTH) == maand && calendar.get(Calendar.YEAR) == jaar) {
                    db.addAmount(bedrag, uitin, item, jaar, calendar.get(Calendar.MONTH), dag);
                }
                else {
                    db.addAmountFut(bedrag, uitin, item, jaar, calendar.get(Calendar.MONTH), dag);
                }
            }
    }

    //Controlleer of een meegegeven datum geldig is. Return false bij ongeldige datum
    private boolean chechDatum(String datum) {
        SimpleDateFormat dateCheck = new SimpleDateFormat("dd/MM/yyyy");
        dateCheck.setLenient(false);
        try {
            dateCheck.parse(datum);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private double parseBedrag(String s) {
        return Double.parseDouble(s.replace(',','.'));
    }

    private boolean isEmpty(EditText edittext) {
        return edittext.getText().toString().trim().length() == 0;
    }

    public void onCheckBoxClicked(View v) {
        switch(v.getId()) {
            case R.id.herhaaldCheck:
                herhaald = !herhaald;
                dagEnable = !dagEnable;
                weekEnable = !weekEnable;
                maandEnable = !maandEnable;
                CheckBox dgCheck = (CheckBox) findViewById(R.id.dagCheck);
                dgCheck.setEnabled(dagEnable);
                CheckBox wkCheck = (CheckBox) findViewById(R.id.weekCheck);
                wkCheck.setEnabled(weekEnable);
                CheckBox mndCheck = (CheckBox) findViewById(R.id.maandCheck);
                mndCheck.setEnabled(maandEnable);

                break;
            case R.id.dagCheck:
                dagelijks = !dagelijks;
                weekEnable = !weekEnable;
                maandEnable = !maandEnable;
                CheckBox wdCheck = (CheckBox) findViewById(R.id.weekCheck);
                wdCheck.setEnabled(weekEnable);
                CheckBox mdCheck = (CheckBox) findViewById(R.id.maandCheck);
                mdCheck.setEnabled(maandEnable);
                break;
            case R.id.weekCheck:
                wekelijks = !wekelijks;
                maandEnable = !maandEnable;
                dagEnable = !dagEnable;
                CheckBox mwCheck = (CheckBox) findViewById(R.id.maandCheck);
                mwCheck.setEnabled(maandEnable);
                CheckBox dwCheck = (CheckBox) findViewById(R.id.dagCheck);
                dwCheck.setEnabled(dagEnable);
                break;
            case R.id.maandCheck:
                maandelijks = !maandelijks;
                dagEnable = !dagEnable;
                weekEnable = !weekEnable;
                CheckBox dmCheck = (CheckBox) findViewById(R.id.dagCheck);
                dmCheck.setEnabled(dagEnable);
                CheckBox wmCheck = (CheckBox) findViewById(R.id.weekCheck);
                wmCheck.setEnabled(weekEnable);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemSelected = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
