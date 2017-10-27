package ict376.murdoch.edu.au.braid;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    public void showRead(View view) {
        Intent intent = new Intent(this.getApplicationContext(), BookTabbedDisplayActivity.class);

        startActivity(intent);

// BookDisplayFragment fragment = new BookDisplayFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment2, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }
}
