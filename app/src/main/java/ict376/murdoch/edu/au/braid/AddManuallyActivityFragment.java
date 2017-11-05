package ict376.murdoch.edu.au.braid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddManuallyActivityFragment extends Fragment  {
    private final static String ISBN_KEY = "ISBN";
    private final static String ID_KEY = "ID";

    //View
    boolean mDualPane;
    View mLayoutView;
    private ListView obj;
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

    //Button for adding book
    Button mAddButton;
    //Button for taking image
    Button mTakePhotoButton;

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

        mLayoutView = inflater.inflate(R.layout.fragment_add_book, null);
        requestQueue = Volley.newRequestQueue(getActivity());


        return mLayoutView;
    }

    private void populateFieldsByBook(Serializable id) {

        editingID = ((Book) id).getID();

        mTitle.setText(((Book) id).getTitle());
        mIsbn.setText(((Book) id).getISBN());
        mAuthor.setText(((Book) id).getAuthors());
        mPublisher.setText(((Book) id).getPublisher());
        mDatePub.setText(((Book) id).getPublishedDate());
        mRating.setNumStars(((Book) id).getRating());
        mTotalPages.setText(((Book) id).getTotalPages() + "");
        mCurrentPage.setText(((Book) id).getCurrentPages()+ "");
//        mCoverThumbnail.setText();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mydb = new DatabaseHelper(getActivity());

        View detailsFrame = getActivity().findViewById(R.id.menu_fragment_container);

        // set the onclick listeners for the buttons
        mAddButton = (Button) getActivity().findViewById(R.id.button_addBook);
        mTakePhotoButton = (Button) getActivity().findViewById(R.id.button_takeCoverPhoto);

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
                if(s.length() != 0){
                    //check if current page is more than total page
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

        mCurrentPage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() != 0) {
                    mCurrentPageSeek.setProgress(Integer.parseInt(s.toString()));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCurrentPageSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
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
            mTotalPages.setText(savedInstanceState.getString("mTotalPages_key") + "");
            mCurrentPage.setText(savedInstanceState.getString("mCurrentPage_key")+ "");
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
                takePhoto();
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

    public void SaveBook(){
        //Getting all the values from the edit texts
        String title = mTitle.getText().toString();
        String isbn = mIsbn.getText().toString();
        String cover = mCurrentPhotoPath;
        String author = mAuthor.getText().toString();
        String publisher = mPublisher.getText().toString();
        String datepub = mDatePub.getText().toString();
        int rating = (int) mRating.getRating();
        int totalpages = 0;
        int currentpage = 0;
        try{
            totalpages = Integer.parseInt(mTotalPages.getText().toString());
            currentpage = Integer.parseInt(mCurrentPage.getText().toString());

        }catch (NumberFormatException e){
           //dont do anything, will reset to 0
        }

        //TODO Check if this is right not sure how the insert for author works
        String authorArray[] = author.split(",");
        if(editingID > 0){
            mydb.updateBook(editingID, title, isbn, cover, authorArray, publisher, datepub, rating, totalpages, currentpage);
            Toast.makeText(getContext(), "Book Updated", Toast.LENGTH_SHORT).show();

        }else{
            mydb.insertBook(title, isbn, cover, authorArray, publisher, datepub, rating, totalpages, currentpage);
            Toast.makeText(getContext(), "Book Added", Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the fields
        super.onSaveInstanceState(outState);
        outState.putString("mTitle", mTitle.getText().toString());
        outState.putString("mIsbn", mIsbn.getText().toString());
        outState.putString("mAuthor", mAuthor.getText().toString());
        outState.putString("mPublisher", mPublisher.getText().toString());
        outState.putString("mDatePub", mDatePub.getText().toString());
        outState.putInt("mRating", mRating.getNumStars());
        outState.putString("mTotalPages", mTotalPages.getText().toString());
        outState.putString("mCurrentPage", mCurrentPage.getText().toString());
    }

    private void populateFieldsByISBN(final String isbn) {
        final BookAPIHelper bh = new BookAPIHelper(getActivity());
        bh.getBookFromISBN(isbn, new VolleyCallback(){

            @Override
            public void VolleyResponse(JSONObject books) {
                try {
                    JSONArray resBookArray = books.getJSONArray("items");
                    if(resBookArray.length()>0){

                        JSONObject bookInfo = resBookArray.getJSONObject(0).getJSONObject("volumeInfo");
                        Toast.makeText(getActivity(), "ISBN data found", Toast.LENGTH_SHORT).show();

                        Log.d("Volley", bookInfo.toString());
                        if(bookInfo.has("title")){
                            mTitle.setText(bookInfo.getString("title"));
                        }
                            mIsbn.setText(isbn);
                        if(bookInfo.has("authors")){
                            mAuthor.setText(bookInfo.getJSONArray("authors").join(", ").replace("\"",""));
                        }
                        if(bookInfo.has("pageCount")){
                            mTotalPages.setText(bookInfo.getString("pageCount"));
                        }
                        if(bookInfo.has("publisher")){
                            mPublisher.setText(bookInfo.getString("publisher"));
                        }
                        if(bookInfo.has("publishedDate")){
                            mDatePub.setText(bookInfo.getString("publishedDate"));
                        }
                        if(bookInfo.has("imageLinks")) {
                            new DownloadImageTask(mCoverThumbnail)
                                    .execute(bookInfo.getJSONObject("imageLinks").getString("thumbnail"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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


}
