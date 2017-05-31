package rnd.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Auke on 5/31/2017.
 */

public class PeriodiekeMutatiesActivity extends AppCompatActivity{
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.periodieke_mutaties);
        db = new DatabaseHelper(this);
    }
}
