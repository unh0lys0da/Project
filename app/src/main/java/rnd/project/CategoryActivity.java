package rnd.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CategoryActivity extends AppCompatActivity {

    // Deze klasse kan vanuit 'Settings' worden aangeroepen. Hier kan de gebruiker een categorie toevoegen.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

    }

    public void removeCategory(View v) {
        DatabaseHelper db;
        db = new DatabaseHelper(this);
        EditText text = (EditText) findViewById(R.id.editText);
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
}
