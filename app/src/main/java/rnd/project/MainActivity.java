package rnd.project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    public List<Entry> entriesInkomsten; //DataSet voor Linker Grafiek
    public List<Entry> entriesUitgaven; //DataSet voor rechtergrafiek.
    String[] categoryInkomsten = {"Lening", "Ouders", "Werk", "etc"}; //TestSet
    String[] categoryUitgaven = {"Eten", "Uitgaan", "Huur", "etc"}; //TestSet
    float[] inkomstenData = {400, 200, 300, 20}; //TestSet
    float[] uitgavenData = {200, 100, 300, 50}; //TestSet
    public PieChart pieChartInkomsten;
    public PieChart pieChartUitgaven;
    private ArrayList<Integer> colors;
    private DisplayMetrics metrics;
    private int screenWidth;
    private int screenHeight;
    public List<Double> bedragList;
    public List<String> categorieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colors = new ArrayList<>();
        addColors();
        setUpCharts();

        db = new DatabaseHelper(this);
        db.addAmount(12.0, "uit", "overig", 1990, 2);
        db.addAmount(23.0, "uit", "overig", 1990, 2);
        db.addAmount(1.0, "uit", "Nog een", 1990, 2);
        db.addAmount(12.0, "in", "overig", 1990, 2);
        db.addAmount(23.0, "in", "overig", 1990, 2);
        db.addAmount(1.0, "in", "Nog een", 1990, 2);
        //readUitIn("uit");
        readUitIn("in");
        //whereT();
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

        fillCharts(); //DataSets toevoegen

    }

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

    //Gets sum of bedrag and categorie from database for uitgaven or inkomsten
    private void readUitIn (String uitIn) {
        Cursor data = db.getUitIn(uitIn);
        int saveBedrag = 0; //Index of the column in the select statement of the query; so not the index of the column in the table!
        int saveCat = 1;
        categorieList = new ArrayList<>();
        bedragList = new ArrayList<>();
        while(data.moveToNext()) { //moves to next row in query
            Double bedrag = data.getDouble(saveBedrag); // gets result from current in column saveBedrag
            String categorie = data.getString(saveCat);
            bedragList.add(bedrag);
            categorieList.add(categorie);
        }
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
