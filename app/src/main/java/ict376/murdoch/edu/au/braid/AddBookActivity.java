package ict376.murdoch.edu.au.braid;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AddBookActivity extends AppCompatActivity {

    private final static String ISBN_KEY = "ISBN";
    private final static String ID_KEY = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_book);
        // add AddManuallyActivityFragment to the container layout
        if(savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString(ISBN_KEY, getIntent().getStringExtra(ISBN_KEY));
            bundle.putSerializable(ID_KEY, getIntent().getSerializableExtra(ID_KEY));
            AddManuallyActivityFragment addbookfragment = new AddManuallyActivityFragment();
            addbookfragment.setArguments(bundle);
            AddManuallyActivityFragment fragment = addbookfragment;

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.add_book_fragment, fragment);
            fragmentTransaction.commit();
        }
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.addBookToolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.add_book_fragment);

            if (fragment instanceof AddManuallyActivityFragment){
                ((AddManuallyActivityFragment)fragment).SaveBook();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
