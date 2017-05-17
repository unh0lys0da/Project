package rnd.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Entry> entriesInkomsten; //DataSet voor Linker Grafiek
    public List<Entry> entriesUitgaven; //DataSet voor rechtergrafiek.
    String[] categoryInkomsten = {"Lening", "Ouders", "Werk", "etc"}; //TestSet
    String[] categoryUitgaven = {"Eten", "Uitgaan", "Huur", "etc"}; //TestSet
    float[] inkomstenData = {400, 200, 300, 20}; //TestSet
    float[] uitgavenData = {200, 100, 300, 50}; //TestSet
    public PieChart pieChartInkomsten;
    public PieChart pieChartUitgaven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpCharts();






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

    public void gotoNieuweInvoer(View v){
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

    private void setUpCharts() {
        PieChart pieChartInkomsten = (PieChart) findViewById(R.id.PieChartLinks);
        PieChart pieChartUitgaven = (PieChart) findViewById(R.id.PieChartRechts);

        Description descInkomsten = new Description();
        Description descUitgaven = new Description(); //Maak de descriptions eerst zo aan

        descInkomsten.setText("Inkomsten per categorie in €");
        descUitgaven.setText("Uitgaven per categorie in €"); //Dan zo de text setten

        pieChartInkomsten.setDescription(descInkomsten);
        pieChartUitgaven.setDescription(descUitgaven);

    }
}
