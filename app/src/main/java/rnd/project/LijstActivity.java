package rnd.project;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

/**
 * Created by Gebruiker on 17/05/2017.
 */

public class LijstActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lijst);

        String[] geld = {"200,-", "50,-", "18,88", "21,22"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, geld);
        getListView().setAdapter(adapter);
    }
}
