package ict376.murdoch.edu.au.braid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    MainMenuFragment mainMenuFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mainMenuFragment = MainMenuFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.menu_fragment_container, mainMenuFragment).commit();
        }else{
            mainMenuFragment = (MainMenuFragment) getFragmentManager().findFragmentById(R.id.menu_fragment_container);
        }
    }
}
