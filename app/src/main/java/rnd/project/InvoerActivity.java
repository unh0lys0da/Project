package rnd.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class InvoerActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nieuwe_invoer);
        db = new DatabaseHelper(this);
    }

    //add given amount to database with current month + year
    public void addValues (double bedrag, String inUit, String cat) {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1; //Increment with 1 so that e.g. January has index 1 instead of 0
        int year = cal.get(Calendar.YEAR);
        boolean insert = db.addAmount(bedrag, inUit, cat, month, year); //Add new values to database
        if (insert) toastMessage("Insert correct");
        else toastMessage("Insert went wrong");
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
}
