package rnd.project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryActivity extends AppCompatActivity {

    // Deze klasse kan vanuit 'Settings' worden aangeroepen. Hier kan de gebruiker een categorie toevoegen.
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = (ListView) findViewById(R.id.listCat);
        setupListView(listView);
    }

    //Laat de al toegevoegde categorieën zien in de scrollview
    public void setupListView (ListView listview) {
        DatabaseHelper db;
        db = new DatabaseHelper(this);
        Cursor data = db.getCategories(); //Haalt de categorieën uit de database
        String[] catArray = new String[data.getCount()];
        for (int i = 0; data.moveToNext(); i++) {
            catArray[i] = data.getString(0);
        }
        ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, catArray);
        listview.setAdapter(listArrayAdapter);
    }

    public void removeCategory(View v) {
        DatabaseHelper db;
        db = new DatabaseHelper(this);
        EditText text = (EditText) findViewById(R.id.editText);
        if(db.removeCategory(text.getText().toString())) {
            toastMessage("Success!");
        }
        else {
            toastMessage("Nothing to delete");
        }
    }

    public void addCategory(View v) {
        DatabaseHelper db;
        db = new DatabaseHelper(this);
        EditText text = (EditText) findViewById(R.id.editText);
        if(db.addCategory(text.getText().toString())) {
            toastMessage("Success!");
        }
        else {
            toastMessage("Failed to add category, is text empty?");
        }
    }

    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    /*
    private void hernoemCategorie () {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + spinner + " FROM table_name " +
                "WHERE categorie = " + nieuweCategorieNaam.getText();
        Cursor data = db.rawQuery(query,null);
        return data;
    }*/
}
