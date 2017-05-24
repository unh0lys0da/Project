package rnd.project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    DatabaseHelper db;
    public PieChart pieChartInkomsten;
    public PieChart pieChartUitgaven;
    private ArrayList<Integer> colors;
    private DisplayMetrics metrics;
    private int screenWidth;
    public List<PieEntry> bedragListInkomst;
    public List<String> categorieListInkomst;
    public List<PieEntry> bedragListUitgave;
    public List<String> categorieListUitgave;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // Moest even om de spinner te checken
        db.addAmount(12.0, "uit", "overig", 1995, 3);
        db.addAmount(40.0, "uit", "overig", 1995, 3);
        db.addAmount(50.0, "uit", "kaas", 1995, 3);
        db.addAmount(15.0, "uit", "overig", 1997, 5);
        Cursor mndJaar = db.getMaandJaar();
        spinner = (Spinner) findViewById(R.id.month_spinner);

        makeSpinner(spinner,mndJaar);

        colors = new ArrayList<>();
        bedragListInkomst = new ArrayList<>();
        categorieListInkomst = new ArrayList<>();
        bedragListUitgave = new ArrayList<>();
        categorieListUitgave = new ArrayList<>();
        addColors();

        //Tijdelijke shit om db te vullen
        db.addAmount(12.0, "uit", "overig", 1990, 2);
        db.addAmount(23.0, "uit", "overig", 1990, 2);
        db.addAmount(1.0, "uit", "Nog een", 1990, 2);
        db.addAmount(12.0, "in", "overig", 1990, 2);
        db.addAmount(23.0, "in", "overig", 1990, 2);
        db.addAmount(1.0, "in", "Nog een", 1990, 2);
        //readUitIn("uit");
        readUitIn("in", bedragListInkomst, categorieListInkomst);
        readUitIn("uit", bedragListUitgave, categorieListUitgave);
        //whereT();

        //Charts invullen


        setUpCharts();
    }

    String[] MONTHS = {
            "Januari",
            "Februari",
            "Maart",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Augustus",
            "September",
            "Oktober",
            "November",
            "December"
    };

    private void makeSpinner(Spinner spinner, Cursor mndJaar) {
        String[] mndJaarArray = new String[mndJaar.getCount()];
        Log.d("first","" + mndJaar.getCount());
        for(int i=0; mndJaar.moveToNext(); i++) {
            mndJaarArray[i] = MONTHS[mndJaar.getInt(0) - 1] + " " + mndJaar.getString(1);
            Log.d("mndjaar",mndJaar.getString(0) + "," + mndJaar.getString(1));
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mndJaarArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void addColors() {
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.DKGRAY);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        //Zijn voorlopig maar 5 kleuren, kan altijd makkelijk meer doen, wil ook liever hex-based kleuren gaan gebruiken.
        //RitsKanNietPullenLol
    }

    public void gotoInkomsten(View v){
        Intent inkomsten;
        inkomsten = new Intent(getBaseContext(),InkomstenActivity.class);
        startActivity(inkomsten);
    }

    public void gotoUitgaven(View v){
        Intent uitgaven;
        uitgaven = new Intent(getBaseContext(),UitgavenActivity.class);
        startActivity(uitgaven);
    }

    public void gotoLijst(View v){
        Intent lijst;
        lijst = new Intent(getBaseContext(),LijstActivity.class);
        startActivity(lijst);
    }

    public void addNewUserEntry(View v){
        Intent invoer;
        invoer = new Intent(getBaseContext(),InvoerActivity.class);
        startActivity(invoer);
    }

    public void gotoSettings(View v){
        Intent settings;
        settings = new Intent(getBaseContext(),SettingsActivity.class);
        startActivity(settings);
    }

    public void gotoHome(View v){
        setContentView(R.layout.lijst);
    }

    public void showBiggerLeftPie(View view) {
        pieChartUitgaven.setVisibility(View.GONE);
        pieChartInkomsten.setMinimumHeight((screenWidth * 3) / 4);
    }

    private void setUpCharts() {
        pieChartInkomsten = (PieChart) findViewById(R.id.PieChartLinks);
        pieChartUitgaven = (PieChart) findViewById(R.id.PieChartRechts);

        pieChartInkomsten.setMinimumHeight(screenWidth/2);
        pieChartInkomsten.setMinimumWidth(screenWidth/2);

        pieChartUitgaven.setMinimumHeight(screenWidth/2);
        pieChartUitgaven.setMinimumWidth(screenWidth/2);


        Description descInkomsten = new Description();
        Description descUitgaven = new Description(); //Maak de descriptions eerst zo aan

        descInkomsten.setText("Inkomsten per categorie in €");
        descUitgaven.setText("Uitgaven per categorie in €"); //Dan zo de text setten

        pieChartInkomsten.setDescription(descInkomsten);
        pieChartUitgaven.setDescription(descUitgaven); //En ze dan zo aan een pieChart toevoegen

        //Wat kleine grafische dingetjes
        pieChartInkomsten.setHoleRadius(0);
        pieChartUitgaven.setHoleRadius(0); //Lelijk gat in het midden van een piechart uitgezet

        fillCharts2(false); //DataSets toevoegen

    }

    private void fillCharts2(boolean setChanged) {
        PieDataSet inkomstenDataSet = new PieDataSet(bedragListInkomst, "Bedrag per categorie");
        inkomstenDataSet.setSliceSpace(0);
        inkomstenDataSet.setValueTextSize(14);
        inkomstenDataSet.setColors(colors);
        PieDataSet uitgavenDataSet = new PieDataSet(bedragListUitgave, "Bedrag per categorie");
        uitgavenDataSet.setSliceSpace(0);
        uitgavenDataSet.setValueTextSize(14);
        uitgavenDataSet.setColors(colors);

        PieData inkomstenPieData = new PieData(inkomstenDataSet);
        PieData uitgavenPieData = new PieData(uitgavenDataSet);

        pieChartInkomsten.setData(inkomstenPieData);
        pieChartUitgaven.setData(uitgavenPieData);

        if(setChanged) {
            pieChartUitgaven.notifyDataSetChanged();
            pieChartInkomsten.notifyDataSetChanged();
        }
        pieChartInkomsten.invalidate();
        pieChartUitgaven.invalidate();
    }

    /*
    private void fillCharts() {
        //Dit stuk voorzie ik nog van comments in de nabije toekomst
        ArrayList<PieEntry> yEntriesInkomsten = new ArrayList<>();
        ArrayList<PieEntry> yEntriesUitgaven = new ArrayList<>();
        ArrayList<String> xEntriesInkomsten = new ArrayList<>();
        ArrayList<String> xEntriesUitgaven = new ArrayList<>();

        //Deze prachtige forloops vullen de getallen in beide charts, had gekund met foreach loops,
        //maar dat werkt slordig omdat je ook een index mee wil geven, die het makkelijkst via i meegegeven wordt.
        for(int i = 0; i < inkomstenData.length; i++) { yEntriesInkomsten.add(new PieEntry(inkomstenData[i], i)); } //Getallen
        for(int i = 0; i < categoryInkomsten.length; i++) {xEntriesInkomsten.add(categoryInkomsten[i]); } //Categorie-naam
        for(int i = 0; i < uitgavenData.length; i++) { yEntriesUitgaven.add(new PieEntry(uitgavenData[i], i)); } //Getallen
        for(int i = 0; i < categoryUitgaven.length; i++) {xEntriesUitgaven.add(categoryUitgaven[i]); } //Categorie-naam


        //Maken van de DataSets
        PieDataSet inkomstenDataSet = new PieDataSet(yEntriesInkomsten, "Bedrag per categorie");
        inkomstenDataSet.setSliceSpace(0);
        inkomstenDataSet.setValueTextSize(14);
        inkomstenDataSet.setColors(colors);
        PieDataSet uitgavenDataSet = new PieDataSet(yEntriesUitgaven, "Bedrag per categorie");
        uitgavenDataSet.setSliceSpace(0);
        uitgavenDataSet.setValueTextSize(14);
        uitgavenDataSet.setColors(colors);

        //Maken van PieData (Vraag me niet waarom dit zo lomp is)
        PieData inkomstenPieData = new PieData(inkomstenDataSet);
        PieData uitgavenPieData = new PieData(uitgavenDataSet);

        //Toevoegen van de Data aan de charts
        pieChartInkomsten.setData(inkomstenPieData);
        pieChartUitgaven.setData(uitgavenPieData);

        //Invalidate de charts
        pieChartInkomsten.invalidate();
        pieChartUitgaven.invalidate();
    }
    */

    //Gets sum of bedrag and categorie from database for uitgaven or inkomsten
    private void readUitIn(String uitIn, List<PieEntry> bedrag, List<String> categorie) {
        Cursor data = db.getUitIn(uitIn);
        int saveBedrag = 0; //Index of the column in the select statement of the query; so not the index of the column in the table!
        int saveCat = 1;
        while(data.moveToNext()) { //moves to next row in query
            Float tempBedrag = data.getFloat(saveBedrag); // gets result from current in column saveBedrag
            String tempCategorie = data.getString(saveCat);
            bedrag.add(new PieEntry(tempBedrag));
            categorie.add(tempCategorie);
        }
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class MonthYear {
        private int month, year;
        public MonthYear(int month, int year) {
            this.month = month;
            this.year = year;
        }
        public int getMonth() {
            return month;
        }
        public int getYear() {
            return year;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public void setYear(int year) {
            this.year = year;
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Hello","hello");
        String itemSelected = (String) parent.getItemAtPosition(position);
        MonthYear my = new MonthYear(0, 0);
        parseMonthYearFromString(my,itemSelected);
        Log.d("Month","" + my.getMonth());
        Log.d("Year","" + my.getYear());
        bedragListInkomst.clear();
        //categorieListInkomst.clear();
        bedragListUitgave.clear();
        //categorieListUitgave.clear();
        readUitInMonthYear("in", bedragListInkomst, categorieListInkomst, my.getMonth(), my.getYear());
        readUitInMonthYear("uit", bedragListUitgave, categorieListUitgave, my.getMonth(), my.getYear());
        fillCharts2(true);

    }

    private void parseMonthYearFromString(MonthYear my, String toParse) {
        String[] results = toParse.split(" ");
        System.out.println(results[0]);
        System.out.println(results[1]);
        for(int i=0;i<MONTHS.length;i++) {
            if(results[0].equals(MONTHS[i])) {
                my.setMonth(i + 1);
                break;
            }
        }
        my.setYear(Integer.parseInt(results[1]));
    }

    //Gets sum of bedrag and categorie from database for uitgaven or inkomsten
    private void readUitInMonthYear(String uitIn, List<PieEntry> bedrag, List<String> categorie, int month, int year) {
        Cursor data = db.getUitInMonthYear(uitIn, month, year);
        int saveBedrag = 0; //Index of the column in the select statement of the query; so not the index of the column in the table!
        int saveCat = 1;
        while(data.moveToNext()) { //moves to next row in query
            toastMessage("" + data.getString(0) + "," + data.getString(1));
            Float tempBedrag = data.getFloat(saveBedrag); // gets result from current in column saveBedrag
            String tempCategorie = data.getString(saveCat);
            bedrag.add(new PieEntry(tempBedrag));
            categorie.add(tempCategorie);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
