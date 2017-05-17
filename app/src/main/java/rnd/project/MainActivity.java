package rnd.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    //Douwe is een flikkert.
    //klopt, groetjes rits
    //test, groetjes Auke
    //ook ff een test
    //Ik vind Douwe best oke hoor.
}
