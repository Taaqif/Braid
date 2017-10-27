package ict376.murdoch.edu.au.braid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookDisplayFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String BOOK_STATUS = "column-count";
    // TODO: Customize parameters
    private int mBookStatus = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDisplayFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
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

        if (getArguments() != null) {
            mBookStatus = getArguments().getInt(BOOK_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_display_list, container, false);
        DatabaseHelper mydb = new DatabaseHelper(getActivity());

        //get the variable here.
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            switch (mBookStatus){
                case 1:
                    recyclerView.setAdapter(new BookViewAdapter(mydb.getAllBooks(), mListener));
                    break;
                case 2:
                    recyclerView.setAdapter(new BookViewAdapter(mydb.getReadBooks(), mListener));
                    break;
                case 3:
                    recyclerView.setAdapter(new BookViewAdapter(mydb.getAllBooks(), mListener));
                    break;
                default:
                    recyclerView.setAdapter(new BookViewAdapter(mydb.getAllBooks(), mListener));


            }
//            if (mBookStatus <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mBookStatus));
//            }
//            recyclerView.setAdapter(new BookViewAdapter(mydb.getAllBooks(), mListener));
        }
        return view;
    }


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
