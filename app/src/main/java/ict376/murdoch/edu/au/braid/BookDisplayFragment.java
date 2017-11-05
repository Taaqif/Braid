package ict376.murdoch.edu.au.braid;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


//displays list of items
public class BookDisplayFragment extends Fragment {

    //keys
    private static final String BOOK_STATUS = "book-status";
    private int mBookStatus = 1;

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private int mColumnCount =1;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDisplayFragment() {
    }

    public static BookDisplayFragment newInstance(int status) {
        BookDisplayFragment fragment = new BookDisplayFragment();
        Bundle args = new Bundle();
        args.putInt(BOOK_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentOrientation = getResources().getConfiguration().orientation;
        //sets the col count
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mColumnCount = 2;
        }
        else {
           mColumnCount =1;
        }
        if (getArguments() != null) {
            mBookStatus = getArguments().getInt(BOOK_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_display_list, container, false);
        //set up and refresh the view
        recyclerView = (RecyclerView) view;
        refresh();
        return view;
    }
    //refreshes the view with new data
    public void refresh(){
        DatabaseHelper mydb = new DatabaseHelper(getActivity());

        // Set the adapter
        recyclerView.getRecycledViewPool().clear();

        //if orientation is landscape, display 2 row grid else display 1 linear layout
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        //display the correct books based on the type
        recyclerView.setAdapter(null);
        switch (mBookStatus){
            case 1:
                recyclerView.setAdapter(new BookViewAdapter(mydb.getAllBooks(), mListener, BookDisplayFragment.this));
                break;
            case 2:
                recyclerView.setAdapter(new BookViewAdapter(mydb.getReadBooks(), mListener, BookDisplayFragment.this));
                break;
            case 3:
                recyclerView.setAdapter(new BookViewAdapter(mydb.getUnreadBooks(), mListener, BookDisplayFragment.this));
                break;
            default:
                recyclerView.setAdapter(new BookViewAdapter(mydb.getAllBooks(), mListener, BookDisplayFragment.this));
        }
    }

    //makes sure the adaptor is interaction listner is implmented
    //for future use
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //refresh content here
        refresh();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Book item);

    }
}
