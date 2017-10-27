package ict376.murdoch.edu.au.braid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;


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
    EditText mAuthor;
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
    File output = null;


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
        mGenre = (EditText) getActivity().findViewById(R.id.et_genre);
        mAuthor = (EditText) getActivity().findViewById(R.id.et_author);
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
                String cover = mCurrentPhotoPath;
                String author = mAuthor.getText().toString();
                String genre = mGenre.getText().toString();
                String publisher = mPublisher.getText().toString();
                String datepub = mDatePub.getText().toString();
                int rating = Integer.parseInt(mRating.getText().toString());
                int totalpages = Integer.parseInt(mTotalPages.getText().toString());
                int currentpage = Integer.parseInt(mCurrentPage.getText().toString());

                //TODO Check if this is right not sure how the insert for author works
                String authorArray[] = author.split("\\,");

                mydb.insertBook(title, isbn, cover, genre, authorArray, publisher, datepub, rating, totalpages, currentpage);
                Toast.makeText(getActivity(), "You added a book!",
                        Toast.LENGTH_LONG).show();
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

    //For the taking photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            mCoverThumbnail.setImageBitmap(myBitmap);
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "ict376.murdoch.edu.au.braid",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   /*prefix*/
                ".jpg",          /*suffix*/
                storageDir       /*directory*/
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void refresh() {

    }

}
