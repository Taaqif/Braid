package com.tyeXed.braid;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddManuallyActivityFragment extends Fragment  {
    private final static String ISBN_KEY = "ISBN";
    private final static String ID_KEY = "ID";

    //View
    View mLayoutView;
    int editingID = -1;

    //Database
    DatabaseHelper mydb;

    //Fields for adding a book
    EditText mTitle;
    EditText mIsbn;
    EditText mAuthor;
    EditText mPublisher;
    EditText mDatePub;
    SeekBar mCurrentPageSeek;
    RatingBar mRating;
    EditText mTotalPages;
    EditText mCurrentPage;
    ImageView mCoverThumbnail;

    //Buttons
    Button mAddButton;
    Button mTakePhotoButton;
    Button mDeleteBookButton;

    //Variable for the taking photo
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    Uri imageUri;
    File output = null;

    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.

    public static AddManuallyActivityFragment getInstance(String ISBN) {
        Bundle bundle = new Bundle();
        bundle.putString(ISBN_KEY, ISBN);

        AddManuallyActivityFragment fragment = new AddManuallyActivityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public AddManuallyActivityFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout
        mLayoutView = inflater.inflate(R.layout.fragment_add_book, null);
        requestQueue = Volley.newRequestQueue(getActivity());


        return mLayoutView;
    }
    //populate by book object thorugh serializable
    private void populateFieldsByBook(Serializable id) {

        if(((Book) id).getID() > 0){
            editingID = ((Book) id).getID();
            mDeleteBookButton.setEnabled(true);
        }

        mTitle.setText(((Book) id).getTitle());
        mIsbn.setText(((Book) id).getISBN());
        mAuthor.setText(((Book) id).getAuthors());
        mPublisher.setText(((Book) id).getPublisher());
        mDatePub.setText(((Book) id).getPublishedDate());
        mRating.setNumStars(((Book) id).getRating());
        mTotalPages.setText(((Book) id).getTotalPages() + "");
        mCurrentPage.setText(((Book) id).getCurrentPages()+ "");


        //if there is a cover
        if(((Book) id).getCover() != null && !((Book) id).getCover().isEmpty()){
            mCurrentPhotoPath = ((Book) id).getCover();
            setCoverImage(mCurrentPhotoPath);


        }
//        mCoverThumbnail.setText();
    }
    //sets the cover image to the correct orientarttion based on photo exif
    //no scaling so would cause OOM issues
    //needs considering
    //https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a
    private void setCoverImage(String path){
        Picasso.with(getContext()).load(new File(path)).resize(256,400).centerInside().into(mCoverThumbnail);

//        Bitmap myBitmap = BitmapFactory.decodeFile(path);
//        try {
//            ExifInterface exif = new ExifInterface(path);
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//            Matrix matrix = new Matrix();
//            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//                matrix.postRotate(90);
//            }
//            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
//                matrix.postRotate(180);
//            }
//            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
//                matrix.postRotate(270);
//            }
//            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
//        }
//        catch (Exception e) {
//
//        }
//        mCoverThumbnail.setImageBitmap(myBitmap);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mydb = new DatabaseHelper(getActivity());
        //Assign name to each edit text
        mTitle = (EditText) getActivity().findViewById(R.id.et_title);
        mIsbn = (EditText) getActivity().findViewById(R.id.et_isbn);
        mAuthor = (EditText) getActivity().findViewById(R.id.et_author);
        mPublisher = (EditText) getActivity().findViewById(R.id.et_publisher);
        mDatePub = (EditText) getActivity().findViewById(R.id.et_pubdate);
        mRating = (RatingBar) getActivity().findViewById(R.id.et_rating);
        mTotalPages = (EditText) getActivity().findViewById(R.id.et_totalpages);
        mCurrentPage = (EditText) getActivity().findViewById(R.id.et_currentpage);
        mCurrentPage.setEnabled(false);
        mCurrentPageSeek = (SeekBar) getActivity().findViewById(R.id.et_currentpageSeek);
        mCurrentPageSeek.setEnabled(false);
        mCoverThumbnail = (ImageView) getActivity().findViewById(R.id.iv_coverThumbnail);

        mTakePhotoButton = (Button) getActivity().findViewById(R.id.button_takeCoverPhoto);
        mDeleteBookButton = (Button) getActivity().findViewById(R.id.delete_button);
        mDeleteBookButton.setEnabled(false);

        //total page text listner
        mTotalPages.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!s.toString().equals("null")  && s.length() != 0){
                    //check if current page is more than total page then enable the fileds for editing
                    mCurrentPage.setEnabled(true);
                    mCurrentPageSeek.setEnabled(true);
                    try{
                        if(Integer.parseInt(mCurrentPage.getText().toString()) > Integer.parseInt(s.toString())){
                            mCurrentPage.setText(s.toString());
                        }
                    }catch (NumberFormatException e){
                        //dont do anything, the current page is blank
                    }
                    //set max to the total page number
                    mCurrentPageSeek.setMax(Integer.parseInt(s.toString()));
                    mCurrentPage.setFilters(new InputFilter[]{ new InputFilterMinMax("0", s.toString())});

                }else{
                    mCurrentPage.setEnabled(false);
                    mCurrentPageSeek.setEnabled(false);
                }
            }
        });

        //current page text listner
        mCurrentPage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() != 0) {
                    try{
                        //set seekbar progress
                        mCurrentPageSeek.setProgress(Integer.parseInt(s.toString()));

                    }catch (NumberFormatException e){

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //seekbar listner
        mCurrentPageSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //set the current page based on seek bar
                mCurrentPage.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //data from previous rotation
        if (savedInstanceState != null){
            mTitle.setText(savedInstanceState.getString("mTitle_key"));
            mIsbn.setText(savedInstanceState.getString("mIsbn_key"));
            mAuthor.setText(savedInstanceState.getString("mAuthor_key"));
            mPublisher.setText(savedInstanceState.getString("mPublisher_key"));
            mDatePub.setText(savedInstanceState.getString("mDatePub_key"));
            mRating.setNumStars(savedInstanceState.getInt("mRating_key"));
            mTotalPages.setText(savedInstanceState.getString("mTotalPages_key"));
            mCurrentPage.setText(savedInstanceState.getString("mCurrentPage_key"));
            //make sure there is a book path to set
            if(savedInstanceState.getString("mCurrentPhotoPath") != null){
                setCoverImage(savedInstanceState.getString("mCurrentPhotoPath"));

            }

        }

        //LISTENER for ADDBOOK button
//        mAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SaveBook();
//            }
//        });

        //LISTENER for TAKEPHOTO button
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }else{
                    //assume perimsiion granted
                    takePhoto();
                }

            }
        });


        //listner for delete book
        mDeleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook();
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            if(bundle.getString(ISBN_KEY) != null){
                populateFieldsByISBN(bundle.getString(ISBN_KEY));
            }
            if(bundle.getSerializable(ID_KEY) != null){
                populateFieldsByBook(bundle.getSerializable(ID_KEY));
            }
        }
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
                    takePhoto();

                } else {
                    // permission denied,
                    Toast.makeText(getContext(), R.string.error_camera_permission_not_granted, Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }
    //starts the delete book dialog process
    private void deleteBook() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(getContext());
        // Set up the input

        builder.setTitle(R.string.title_remove_book)
                .setMessage(R.string.question_remove_book)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue
                        File file = new File(mCurrentPhotoPath);
                        file.delete();
                        mydb.deleteBook(editingID);
                        Toast.makeText(getContext(), R.string.success_book_removed, Toast.LENGTH_SHORT).show();

                        getActivity().finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    //save the current book
    public void SaveBook(){
        //Getting all the values from the edit texts
        String title = mTitle.getText().toString().trim();
        String isbn = mIsbn.getText().toString().trim();
        String cover = mCurrentPhotoPath;
        String author = mAuthor.getText().toString().trim();
        String publisher = mPublisher.getText().toString().trim();
        String datepub = mDatePub.getText().toString().trim();
        int rating = (int) mRating.getRating();
        //assume 0 pages
        int totalpages = 0;
        int currentpage = 0;
        try{
            totalpages = Integer.parseInt(mTotalPages.getText().toString());
            currentpage = Integer.parseInt(mCurrentPage.getText().toString());

        }catch (NumberFormatException e){
           //dont do anything, will reset to 0
        }
        //multiple authors are split by semi colon
        String authorArray[] = author.split(";");

        //get a title
        if(title.isEmpty() || title.length() <= 0){
            Toast.makeText(getContext(), R.string.error_title, Toast.LENGTH_SHORT).show();
        }
        else {
            //if there is an id loaded, update it
            if (editingID > 0) {
                mydb.updateBook(editingID, title, isbn, cover, authorArray, publisher, datepub, rating, totalpages, currentpage);
                Toast.makeText(getContext(), R.string.success_book_updated, Toast.LENGTH_SHORT).show();

            } else {
                //insert it
                mydb.insertBook(title, isbn, cover, authorArray, publisher, datepub, rating, totalpages, currentpage);
                Toast.makeText(getContext(), R.string.success_book_added, Toast.LENGTH_SHORT).show();
            }
            getActivity().finish();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the fields
        //use the variables as keys
        super.onSaveInstanceState(outState);
        outState.putString("mTitle", mTitle.getText().toString());
        outState.putString("mIsbn", mIsbn.getText().toString());
        outState.putString("mAuthor", mAuthor.getText().toString());
        outState.putString("mPublisher", mPublisher.getText().toString());
        outState.putString("mDatePub", mDatePub.getText().toString());
        outState.putInt("mRating", mRating.getNumStars());
        outState.putString("mTotalPages", mTotalPages.getText().toString());
        outState.putString("mCurrentPage", mCurrentPage.getText().toString());
        outState.putString("mCurrentPhotoPath", mCurrentPhotoPath);
    }

    //populate fields using aprovided ISBN
    private void populateFieldsByISBN(final String isbn) {
        //use the book api helper to retun a book to populate
        final BookAPIHelper bh = new BookAPIHelper(getActivity());
        bh.getBookFromISBN(isbn, new BookAPIHelper.VolleyCallback(){

            @Override
            public void VolleyBookResponse(Book book, String thumbnailURL) {
                if (book != null){
                    Toast.makeText(getActivity(), "ISBN data found", Toast.LENGTH_SHORT).show();
                    new DownloadImageTask(mCoverThumbnail)
                            .execute(thumbnailURL);
                    populateFieldsByBook(book);

                }else{
                    Toast.makeText(getActivity(), "ISBN data could not be read", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    //For the taking photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            setCoverImage(mCurrentPhotoPath);

        }
    }

    //take the image
    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            //delete old file

            File photoFile = null;
            try {
                if(mCurrentPhotoPath != null && !mCurrentPhotoPath.isEmpty()){
                    File file = new File(mCurrentPhotoPath);
                    file.delete();
                }
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "com.tyeXed.braid",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //downlaods an image and sets the corresponding image view to
    //https://stackoverflow.com/questions/3090650/android-loading-an-image-from-the-web-with-asynctask
    private class DownloadImageTask extends AsyncTask<String, Void, File> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected File doInBackground(String... urls) {
            String urldisplay = urls[0];
            File photoFile = null;
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                photoFile = createImageFile();
                mIcon11 = BitmapFactory.decodeStream(in);
                FileOutputStream out = new FileOutputStream(photoFile);
                mIcon11.compress(Bitmap.CompressFormat.JPEG, 60, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return photoFile;
        }

        protected void onPostExecute(File result) {
            Picasso.with(getContext()).load(result).into(bmImage);

//            bmImage.setImageBitmap(result);
        }
    }
    //creates a file to be written to
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


}
