package rnd.project;

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
        setContentView(R.layout.inkomsten);
    }

    public void gotoUitgaven(View v){
        setContentView(R.layout.lijst);
    }

    public void gotoLijst(View v){
        setContentView(R.layout.lijst);
    }

    public void gotoNieuweInvoer(View v){
        setContentView(R.layout.lijst);
    }

    public void gotoSettings(View v){
        setContentView(R.layout.lijst);
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
