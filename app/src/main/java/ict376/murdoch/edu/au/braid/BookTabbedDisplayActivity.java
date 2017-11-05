package ict376.murdoch.edu.au.braid;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BookTabbedDisplayActivity extends AppCompatActivity implements BookDisplayFragment.OnListFragmentInteractionListener {

    //the adaptor for the tabbed display
    private SectionsPagerAdapter mSectionsPagerAdapter;

    //keys
    private final static String ISBN_KEY = "ISBN";


    //the pager to use
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_tabbed_display);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookTabbedDisplayActivity.this.getApplicationContext(), AddBookActivity.class);

                startActivity(intent);
            }
        });
        //sets up the ISBN fab
        findViewById(R.id.fabISBN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show a dialogue asking the user for an ISBN number
            AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(view.getContext());
            // Set up the input
            View dialogview = LayoutInflater.from(view.getContext()).inflate(R.layout.add_isbn_layout, null);
            final EditText input = (EditText) dialogview.findViewById(R.id.editText);

            builder.setTitle(R.string.title_add_book_ISBN)
            .setMessage(R.string.prompt_isbn)
            .setView(dialogview)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue
                    Intent intent = new Intent(BookTabbedDisplayActivity.this.getApplicationContext(), AddBookActivity.class);
                    intent.putExtra(ISBN_KEY, input.getText().toString());
                    startActivity(intent);
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .show();


            }
        });

        //sets up the Book fab action
        findViewById(R.id.fabcamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the ZXING service here
                //checks permission
                //https://developer.android.com/training/permissions/requesting.html
                if (ContextCompat.checkSelfPermission(BookTabbedDisplayActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(BookTabbedDisplayActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                }else{
                    //assume perimsiion granted
                    IntentIntegrator scanIntegrator = new IntentIntegrator(BookTabbedDisplayActivity.this);
                    scanIntegrator.initiateScan();
                }
            }
        });


    }
    //this is the callback that executes on the users response to the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    //start the service
                    IntentIntegrator scanIntegrator = new IntentIntegrator(BookTabbedDisplayActivity.this);
                    scanIntegrator.initiateScan();

                } else {
                    // permission denied,
                    Toast.makeText(this, R.string.error_camera_permission_not_granted, Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }
    //this is the callback that ZXING executes on completion
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();

            // continue
            //make sure data is read
            if(scanContent != null && !scanContent.isEmpty()){
                Intent newintent = new Intent(BookTabbedDisplayActivity.this.getApplicationContext(), AddBookActivity.class);
                newintent.putExtra(ISBN_KEY, scanContent);
                startActivity(newintent);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        //no action bar items yet
        //for future use

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Book item) {

    }


    //adaptor that returns a fragment corresponding to one of the sections/tabs/pages.

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return BookDisplayFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //corresponds with tabbed display options
            switch (position) {
                case 0:
                    return "ALL BOOKS";
                case 1:
                    return "READ BOOKS";
                case 2:
                    return "UNREAD BOOKS";
            }
            return null;
        }
    }
}
