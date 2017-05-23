package rnd.project;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class UitgavenActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uitgaven);

        String[] geld = {"200,-", "50,-", "18,88", "21,22"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, geld);
        getListView().setAdapter(adapter);
    }
    //Gets user to main activity
    public void gotoHome(View v){
        Intent home;
        home = new Intent(getBaseContext(),MainActivity.class);
        startActivity(home);
    }
    //Gets user to settings activity
    public void gotoSettings(View v){
        Intent settings;
        settings = new Intent(getBaseContext(),SettingsActivity.class);
        startActivity(settings);
    }
}
