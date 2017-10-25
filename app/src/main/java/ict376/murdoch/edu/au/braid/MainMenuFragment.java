package ict376.murdoch.edu.au.braid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import 	android.util.Pair;


public class MainMenuFragment extends Fragment {
    //View
    boolean mDualPane;
    View mLayoutView;
    private ListView obj;

    //Database
    DatabaseHelper mydb;

    //Buttons
    Button mButtonAdd;


    public static MainMenuFragment newInstance(){
        MainMenuFragment f = new MainMenuFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.fragment_main_menu, null);

        mButtonAdd = (Button) getActivity().findViewById(R.id.button_addBook);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*// Check whether the details frame is visible
                View detailsFrame = getActivity().findViewById(R.id.menu_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                AddBookFragment details = AddBookFragment.newInstance();

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.menu_fragment_container, details);

                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();*/
            }
        });

        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Setting onclick listener for the add button
        //mButtonAdd = (Button) getActivity().findViewById(R.id.button_addBook);

        super.onActivityCreated(savedInstanceState);

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
