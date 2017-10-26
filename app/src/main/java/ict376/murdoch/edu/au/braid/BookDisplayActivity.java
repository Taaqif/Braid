package ict376.murdoch.edu.au.braid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class BookDisplayActivity extends AppCompatActivity implements BookDisplayFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_display);
    }

    @Override
    public void onListFragmentInteraction(Book item) {
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
    }
}
