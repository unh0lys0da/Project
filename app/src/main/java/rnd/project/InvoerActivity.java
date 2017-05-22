package rnd.project;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
        readCategorie();
    }

    //Read categorie column from database and give toast of categorie for each row
    public void readCategorie (){
        Cursor data = db.readCat();
        int ColumnToShow = 0;
        while (data.moveToNext()) {
            toastMessage(data.getString(ColumnToShow));
        }
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
