package ict376.murdoch.edu.au.braid;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


public class MainMenuFragment extends Fragment {
    //View
    boolean mDualPane;
    View mLayoutView;
    private ListView obj;

    //Database
    DatabaseHelper mydb;

    //Buttons
    Button mButtonAdd;
    Button mButtonShowAll;
    Button mButtonCompleted;
    Button mButtonToBeRead;
    Button mButtonReading;


    public static MainMenuFragment newInstance(){
        MainMenuFragment f = new MainMenuFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.fragment_main_menu, null);



        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Onclick listener for the add book button
        mButtonAdd = (Button) getActivity().findViewById(R.id.button_addBook);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle dataBundle = new Bundle();
                //possibly put something useful here
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getActivity().getApplicationContext(), AddBookActivity.class);
                intent.putExtras(dataBundle);   //

                startActivity(intent);
            }
        });

        //Onclick listener for the show all button
        mButtonShowAll = (Button) getActivity().findViewById(R.id.button_showAll);
        mButtonShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle dataBundle = new Bundle();
                //possibly put something useful here
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getActivity().getApplicationContext(), BookDisplayActivity.class);
                intent.putExtras(dataBundle);   //

                startActivity(intent);

            }
        });

        //On click listener for reading button
        mButtonReading = (Button) getActivity().findViewById(R.id.button_reading);
        mButtonReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle dataBundle = new Bundle();
                //possibly put something useful here
                dataBundle.putInt("id", 0);

                //Change to show reading activity
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDisplayActivity.class);
                intent.putExtras(dataBundle);   //

                startActivity(intent);

            }
        });

        //Onclick listener for the completed button
        mButtonCompleted = (Button) getActivity().findViewById(R.id.button_completed);
        mButtonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle dataBundle = new Bundle();
                //possibly put something useful here
                dataBundle.putInt("id", 0);

                //Change to show completed activity
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDisplayActivity.class);
                intent.putExtras(dataBundle);   //

                startActivity(intent);

            }
        });

        //Onclick listener for the to read button
        mButtonToBeRead = (Button) getActivity().findViewById(R.id.button_toBeRead);
        mButtonToBeRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle dataBundle = new Bundle();
                //possibly put something useful here
                dataBundle.putInt("id", 0);

                //Change to show to be read activity
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDisplayActivity.class);
                intent.putExtras(dataBundle);   //

                startActivity(intent);

            }
        });

        MainMenuFragment mainMenuFragment = (MainMenuFragment) getFragmentManager().findFragmentById(R.id.menu_fragment_container);
        if (mainMenuFragment!=null){
            mainMenuFragment.refresh();
        }

    }

    /*Made me add this not 100% sure why or what goes in here. I think it can be removed if we get rid of the if statement in the onactivitycreated */
    private void refresh() {

    }
    /* */


}
