package ict376.murdoch.edu.au.braid;

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

        MainMenuFragment mainMenuFragment = (MainMenuFragment) getFragmentManager().findFragmentById(R.id.menu_fragment_container);
        if (mainMenuFragment!=null){
            mainMenuFragment.refresh();
        }
        View detailsFrame = getActivity().findViewById(R.id.menu_fragment_container);
    }

    private void refresh() {

    }


}
