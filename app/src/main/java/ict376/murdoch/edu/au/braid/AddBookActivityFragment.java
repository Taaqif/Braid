package ict376.murdoch.edu.au.braid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddBookActivityFragment extends Fragment {
    //View
    boolean mDualPane;
    View mLayoutView;
    private ListView obj;

    //Database
    DatabaseHelper mydb;

    //Fields for adding a book
    EditText mTitle;
    EditText mIsbn;
    EditText mCover;
    EditText mGenre;
    EditText mPublisher;
    EditText mDatePub;
    EditText mDate;
    EditText mRating;
    EditText mTotalPages;
    EditText mCurrentPage;

    //Button for adding book
    Button mAddButton;


    public static AddBookActivityFragment newInstance(){
        AddBookActivityFragment f = new AddBookActivityFragment();
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

        AddBookActivityFragment addBookFragment = (AddBookActivityFragment) getFragmentManager().findFragmentById(R.id.menu_fragment_container);
        if (addBookFragment!=null){
            addBookFragment.refresh();
        }
        View detailsFrame = getActivity().findViewById(R.id.menu_fragment_container);

        // set the onclick listener for the button
        mAddButton = (Button) getActivity().findViewById(R.id.button_addBook);

        //Assign name to each edit text
        mTitle = (EditText) getActivity().findViewById(R.id.et_title);
        mIsbn = (EditText) getActivity().findViewById(R.id.et_isbn);
        mCover = (EditText) getActivity().findViewById(R.id.et_cover);
        mGenre = (EditText) getActivity().findViewById(R.id.et_genre);
        mPublisher = (EditText) getActivity().findViewById(R.id.et_publisher);
        mDatePub = (EditText) getActivity().findViewById(R.id.et_pubdate);
        mDate = (EditText) getActivity().findViewById(R.id.et_date);
        mRating = (EditText) getActivity().findViewById(R.id.et_rating);
        mTotalPages = (EditText) getActivity().findViewById(R.id.et_totalpages);
        mCurrentPage = (EditText) getActivity().findViewById(R.id.et_currentpage);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting all the values from the edit texts
                String title = mTitle.getText().toString();
                String isbn = mIsbn.getText().toString();
                String cover = mCover.getText().toString();
                String genre = mGenre.getText().toString();
                String publisher = mPublisher.getText().toString();
                String datepub = mDatePub.getText().toString();
                String date = mDate.getText().toString();
                String rating = mRating.getText().toString();
                String totalpages = mTotalPages.getText().toString();
                String currentpage = mCurrentPage.getText().toString();

                //Test print
                Log.d("myTag", title+" "+isbn+" "+cover+" "+genre+" "+publisher+" "+datepub+" "+date+" "+rating+" "+totalpages+" "+currentpage);
            }
        });
    }

    private void refresh() {

    }

}
