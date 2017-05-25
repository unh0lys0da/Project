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


public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {

        // Deze klasse gaat de SQL database maken:
        DatabaseHelper db;

        // Dit zijn de cirkeldiagrammen (en de lijst met kleuren die ze gebruiken):
        public PieChart pieChartInkomsten;
        public PieChart pieChartUitgaven;
        private ArrayList<Integer> colors;

        // Dit helpt om de scherm verhoudingen te bepalen
        private DisplayMetrics metrics;
        private int screenWidth;

        // Deze lijsten houden entries voor de cirkeldiagrammen bij, en de inkomsten/uitgaven
        public List<PieEntry> bedragListInkomst;
        public List<String> categorieListInkomst;
        public List<PieEntry> bedragListUitgave;
        public List<String> categorieListUitgave;

        // Dit wordt het drop-down menu waar we de maand selecteren
        private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // screenWidth wordt gebruikt om straks de grootte van de cirkeldiagrammen te bepalen
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nieuw database object aanmaken:
        db = new DatabaseHelper(this);

        /*
        Nu volgen wat 'test-invullingen' van de database ter controle van de diagrammen en de spinner:
        Deze dingen staan nu in de onCreate methode, dus als je ze wil veranderen moet je eerst de
        app herinstalleren en weer opnieuw opstarten. Dit deel kan je wegdenken.
        */
        db.addAmount(12.0, "uit", "categorie1", 1990, 2, 1);
        db.addAmount(23.0, "uit", "categorie1", 1990, 2, 2);
        db.addAmount(1.0, "uit", "categorie2", 1990, 2, 3);
        db.addAmount(12.0, "in", "categorie1", 1990, 2, 4);
        db.addAmount(23.0, "in", "categorie1", 1995, 3, 5);
        db.addAmount(1.0, "in", "categorie2", 1995, 3, 6);
        db.addAmount(12.0, "uit", "categorie1", 1995, 3, 2);
        db.addAmount(40.0, "uit", "categorie2", 1995, 3, 3);
        db.addAmount(50.0, "uit", "categorie3", 1997, 4, 4);
        db.addAmount(15.0, "uit", "categorie1", 1997, 4, 5);
        db.addAmount(50.0, "uit", "categorie2", 1997, 4, 4);
        db.addAmount(15.0, "uit", "categorie1", 1997, 4, 5);

        /*
        Cursor hoort bij SQL, en selecteert een tabel
        Het spinner object wordt gekoppeld aan de spinner in de layout d.m.v. ID
        Vervolgens wordt de spinner aangemaakt en ingevuld op basis van de cursor.
        */
        Cursor mndJaar = db.getMaandJaar();
        spinner = (Spinner) findViewById(R.id.month_spinner);
        makeSpinner(spinner,mndJaar);

        // Een aantal Lists moeten worden gedefinieerd.
        // Wat gebeurt hier precies mee? Hoe worden deze gevuld? (kan het niet makkelijk terug vinden) #aandacht
        colors = new ArrayList<>();
        bedragListInkomst = new ArrayList<>();
        categorieListInkomst = new ArrayList<>();
        bedragListUitgave = new ArrayList<>();
        categorieListUitgave = new ArrayList<>();

        // De kleuren die de cirkeldiagrammen gebruiken worden hier aangemaakt:
        addColors();


        // IEMAND UITLEG!! #aandacht
        readUitIn("in", bedragListInkomst, categorieListInkomst);
        readUitIn("uit", bedragListUitgave, categorieListUitgave);

        // De volgende functie vult de diagrammen:
        setUpCharts();
    }


        // Array van strings met de maanden, om makkelijk naar te kunnen verwijzen.
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

        /*
            Array van strings aanmaken met alle maanden
            Vervolgens wordt de spinner ingesteld met de maanden (en jaren)
            Wat doet Log.d precies? #aandacht
        */
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
        /*
            De kleuren worden aan de array toegevoegd zodat ze later gelezen kunnen worden door
            de cirkeldiagrammen
         */
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.DKGRAY);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        //Zijn voorlopig maar 5 kleuren, kan altijd makkelijk meer doen, wil ook liever hex-based kleuren gaan gebruiken.
    }

    public void gotoInkomsten(View v){
        /*
            Deze moet nog gekoppeld worden aan het 'inkomsten' cirkeldiagram. Deze verwijst door naar
            een layout die alleen inkomsten laat zien. Waarschijnlijk door middel van een onClickListener
            op het cirkeldiagram als het lukt
         */
        Intent inkomsten;
        inkomsten = new Intent(getBaseContext(),InkomstenActivity.class);
        startActivity(inkomsten);
    }

    public void gotoUitgaven(View v){
        /*
            Zelfde verhaal als gotoUitgaven
         */
        Intent uitgaven;
        uitgaven = new Intent(getBaseContext(),UitgavenActivity.class);
        startActivity(uitgaven);
    }

    public void gotoLijst(View v){
        /*
            Deze moet nog gekoppeld worden aan de lijst onder in beeld. Als hier op wordt gedrukt
            verschijnt er een full-screen lijst.
         */
        Intent lijst;
        lijst = new Intent(getBaseContext(),LijstActivity.class);
        startActivity(lijst);
    }

    public void addNewUserEntry(View v){
        /*
            Deze zorgt er voor dat wanneer er op de 'Nieuwe invoer' knop wordt gedrukt het nieuwe-invoer
            scherm verschijnt.
         */
        Intent invoer;
        invoer = new Intent(getBaseContext(),InvoerActivity.class);
        startActivity(invoer);
    }

    public void gotoSettings(View v){
         /*
            Deze zorgt er voor dat wanneer er op de 'Settings' knop wordt gedrukt het settings
            scherm verschijnt.
         */
        Intent settings;
        settings = new Intent(getBaseContext(),SettingsActivity.class);
        startActivity(settings);
    }

    public void gotoHome(View v){
        setContentView(R.layout.lijst);
    }

    public void showBiggerLeftPie(View view) {
        /*
            Wat doet dit precies? Ik zie nergens dat het wordt aangeroepen #aandacht
         */
        pieChartUitgaven.setVisibility(View.GONE);
        pieChartInkomsten.setMinimumHeight((screenWidth * 3) / 4);
    }

    private void setUpCharts() {

        // De piecharts worden gedefinieerd:
        pieChartInkomsten = (PieChart) findViewById(R.id.PieChartLinks);
        pieChartUitgaven = (PieChart) findViewById(R.id.PieChartRechts);

        // De afmetingen van de piecharts
        pieChartInkomsten.setMinimumHeight(screenWidth/2);
        pieChartInkomsten.setMinimumWidth(screenWidth/2);
        pieChartUitgaven.setMinimumHeight(screenWidth/2);
        pieChartUitgaven.setMinimumWidth(screenWidth/2);

        // De beschrijvingen voor onder de diagrammen:
        Description descInkomsten = new Description();
        Description descUitgaven = new Description(); //Maak de descriptions eerst zo aan

        descInkomsten.setText("Inkomsten per categorie in €");
        descUitgaven.setText("Uitgaven per categorie in €"); //Dan zo de text setten

        pieChartInkomsten.setDescription(descInkomsten);
        pieChartUitgaven.setDescription(descUitgaven); //En ze dan zo aan een pieChart toevoegen

        // Wat kleine grafische dingetjes
        pieChartInkomsten.setHoleRadius(0);
        pieChartUitgaven.setHoleRadius(0); //Lelijk gat in het midden van een piechart uitgezet

        //DataSets toevoegen:
        fillCharts2(false);

    }

    private void fillCharts2(boolean setChanged) {

        // Er worden twee PieDataSets aangemaakt en de opties worden ingesteld:
        // Graag nog wat extra uitleg; hoe leest dit precies de data uit de database? #aandacht
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



    //Gets sum of bedrag and categorie from database for uitgaven or inkomsten
    private void readUitIn(String uitIn, List<PieEntry> bedrag, List<String> categorie) {

        // De volgende cursor filtert de database op 'in' of 'uit' (inkomsten of uitgaven)
        Cursor data = db.getUitIn(uitIn);
        int saveBedrag = 0; // Index van de kolom in de SELECT statement van de query, dus NIET de index van de kolom in de tabel!
        int saveCat = 1;

        // Deze leest alle bedragen uit 'inkomsten' of 'uitgaven'
        while(data.moveToNext()) { // gaat naar de volgende rij in de query
            Float tempBedrag = data.getFloat(saveBedrag); // haalt het resultaat op uit de bijbehorende rij in kolom saveBedrag
            String tempCategorie = data.getString(saveCat); // haalt de categorie op uit de bijbehorende rij in kolom saveCat
            bedrag.add(new PieEntry(tempBedrag)); // Bedrag wordt toegevoegd aan het diagram
            categorie.add(tempCategorie); // Categorie wordt toegevoegd aan het diagram
        }
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class MonthYear {
        /*
            Een klasse voor maand en jaar
            #aandacht
         */
        private int month, year;

        public MonthYear(int month, int year) {
            this.month = month;
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() { return year;}

        public void setMonth(int month) {
            this.month = month;
        }

        public void setYear(int year) {
            this.year = year;
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Kan iemand deze nog van commentaar voorzien? #aandacht
        // Deze methode verwerkt de spinner.

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
        // Haalt deze methode jaar en maand uit een string? #aandacht

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

    // Deze methode leest de som van het bedrag en de categorieen uit de database voor utigaven of inkomsten
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
