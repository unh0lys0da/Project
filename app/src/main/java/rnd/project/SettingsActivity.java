package rnd.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
    //Gets user back to staring screen
    public void gotoHome(View v){
        Intent home;
        home = new Intent(getBaseContext(),MainActivity.class);
        startActivity(home);
    }
}
