package ict376.murdoch.edu.au.braid;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

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
    ImageView mCoverThumbnail;

    //Button for adding book
    Button mAddButton;
    //Button for taking image
    Button mTakePhotoButton;

    //Variable for the taking photo
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    Uri imageUri;


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
        mydb = new DatabaseHelper(getActivity());

        AddBookActivityFragment addBookFragment = (AddBookActivityFragment) getFragmentManager().findFragmentById(R.id.menu_fragment_container);
        if (addBookFragment!=null){
            addBookFragment.refresh();
        }
        View detailsFrame = getActivity().findViewById(R.id.menu_fragment_container);

        // set the onclick listeners for the buttons
        mAddButton = (Button) getActivity().findViewById(R.id.button_addBook);
        mTakePhotoButton = (Button) getActivity().findViewById(R.id.button_takeCoverPhoto);

        //Assign name to each edit text
        mTitle = (EditText) getActivity().findViewById(R.id.et_title);
        mIsbn = (EditText) getActivity().findViewById(R.id.et_isbn);
        mCover = (EditText) getActivity().findViewById(R.id.et_cover);
        mGenre = (EditText) getActivity().findViewById(R.id.et_genre);
        mPublisher = (EditText) getActivity().findViewById(R.id.et_publisher);
        mDatePub = (EditText) getActivity().findViewById(R.id.et_pubdate);
        mRating = (EditText) getActivity().findViewById(R.id.et_rating);
        mTotalPages = (EditText) getActivity().findViewById(R.id.et_totalpages);
        mCurrentPage = (EditText) getActivity().findViewById(R.id.et_currentpage);
        mCoverThumbnail = (ImageView) getActivity().findViewById(R.id.iv_coverThumbnail);

        //LISTENER for ADDBOOK button
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
                int rating = Integer.parseInt(mRating.getText().toString());
                int totalpages = Integer.parseInt(mTotalPages.getText().toString());
                int currentpage = Integer.parseInt(mCurrentPage.getText().toString());

                //TODO Fix author in the fragment and here, Forgot to add author field into the fragment
                //Temp author to test input
                String[] author = {"Jim", "Jeff"};

                mydb.insertBook(title, isbn, cover, genre, author, publisher, datepub, rating, totalpages, currentpage);
                //Test print
                //Log.d("myTag", mydb.getAllBooks().toString());
                Log.d("myTag", title+" "+isbn+" "+cover+" "+genre+" "+author+" "+publisher+" "+datepub+" "+" "+rating+" "+totalpages+" "+currentpage);
            }
        });

        //LISTENER for TAKEPHOTO button
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
    }

    //Method to open the camera
    public void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.bmp");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivityForResult(takePictureIntent, 100);
    }

    //For the taking photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        mCoverThumbnail.setImageBitmap(bitmap);
                        Toast.makeText(getActivity(), selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }

    private void refresh() {

    }

}
