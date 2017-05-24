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
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class InvoerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper db;
    private String itemSelected;
    private String uitin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nieuwe_invoer);
        db = new DatabaseHelper(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        setUpSpinner(spinner);
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

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    //Gets user back to starting screen
    public void gotoHome(View v) {
        Intent home;
        home = new Intent(getBaseContext(),MainActivity.class);
        startActivity(home);
    }
    //submit button moet nog werkend worden.
    //Add given amount to database
    public void submitEntry(View view) {
        EditText bedragInput = (EditText) findViewById(R.id.bedragInput);
        EditText dagInput = (EditText) findViewById(R.id.dagInput);
        EditText maandInput = (EditText) findViewById(R.id.maandInput);
        EditText jaarInput = (EditText) findViewById(R.id.jaarInput);

        double bedrag = parseBedrag(bedragInput.getText().toString());
        int dag = Integer.parseInt(dagInput.getText().toString());
        int maand = Integer.parseInt(maandInput.getText().toString());
        int jaar = Integer.parseInt(jaarInput.getText().toString());

        boolean insert = db.addAmount(bedrag,uitin,itemSelected,jaar,maand,dag);
        if (insert) toastMessage("Insert correct");
        else toastMessage("Insert went wrong");
    }

    private double parseBedrag(String s) {
        return Double.parseDouble(s.replace(',','.'));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemSelected = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
