package rnd.project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class InkomstenActivity extends AppCompatActivity {
    DatabaseHelper db;
    public PieChart chart;
    public List<Entry> categorie;
    float[] Data; //Wat te doen aan onbepaalde lengte, initialiseren uit db?
    private ArrayList<Integer> colors;
    private int screenWidth;
    private DisplayMetrics metrics;
    public List<Double> bedragList;
    public List<String> categorieList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inkomsten);

        db = new DatabaseHelper(this);
        readInkomsten();

        colors = new ArrayList<>();
        addColors();
        setPieChart();
        fillPieChart();
    }

    //Gets user to settings activity
    public void gotoSettings(View v){
        Intent settings;
        settings = new Intent(getBaseContext(),SettingsActivity.class);
        startActivity(settings);
    }
    //Gets user to main activity
    public void gotoHome(View v){
        Intent home;
        home = new Intent(getBaseContext(),MainActivity.class);
        startActivity(home);
    }

    /**
     * Hier wordt de piechart gemaakt en wat kleine grafische dingetjes
     */
    private void setPieChart() {
        chart = (PieChart) findViewById(R.id.pieChart);
        chart.setMinimumHeight(screenWidth/2);
        chart.setMinimumWidth(screenWidth/2);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(12);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(5);
        legend.setYEntrySpace(5);


        Description desc = new Description();
        desc.setText("Inkomsten per categorie");
        chart.setDescription(desc);

        chart.setHoleRadius(0);


    }

    /**
     * Hier moet de grafiek gevuld worden met data uit de sqli db.
     */
    private void fillPieChart() {
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        //Hier moeten die ArrayLists gevuld worden vanuit de database.
        //yEntries = de bedragen, xEntries = de categorieen.

        PieDataSet dataSet = new PieDataSet(yEntries, "Bedrag per categorie");
        dataSet.setSliceSpace(0);
        dataSet.setValueTextSize(14);
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
    }

    //Array met kleuren om piechart prachtig te maken
    public void addColors() {
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.DKGRAY);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        //Zijn voorlopig maar 5 kleuren, kan altijd makkelijk meer doen, wil ook liever hex-based kleuren gaan gebruiken.
    }

    //Gets Bedrag per Categorie for all uitgaven
    private void readInkomsten () {
        Cursor data = db.getUitIn("in");
        int saveBedrag = 0; //Index of the column in the select statement of the query; so not the index of the column in the table!
        int saveCat = 1;
        bedragList = new ArrayList<>();
        categorieList = new ArrayList<>();

        while(data.moveToNext()) { //moves to next row in query
            Double bedrag = data.getDouble(saveBedrag); // gets result from current in column saveBedrag
            String categorie = data.getString(saveCat);
            bedragList.add(bedrag);
            categorieList.add(categorie);
        }
    }
}
