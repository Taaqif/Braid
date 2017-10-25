package ict376.murdoch.edu.au.braid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AddBookFragment extends Fragment {
    //View
    boolean mDualPane;
    View mLayoutView;
    private ListView obj;

    //Database
    DatabaseHelper mydb;


    public static AddBookFragment newInstance(){
        AddBookFragment f = new AddBookFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.fragment_add_book, null);
        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AddBookFragment addBookFragment = (AddBookFragment) getFragmentManager().findFragmentById(R.id.menu_fragment_container);
        if (addBookFragment!=null){
            addBookFragment.refresh();
        }
        View detailsFrame = getActivity().findViewById(R.id.menu_fragment_container);
    }

    private void refresh() {

    }

}
