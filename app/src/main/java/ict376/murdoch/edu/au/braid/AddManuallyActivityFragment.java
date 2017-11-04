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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddManuallyActivityFragment extends Fragment  {
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
    RatingBar mRating;
    EditText mTotalPages;
    EditText mCurrentPage;
    ImageView mCoverThumbnail;

    //Button for adding book
    Button mAddButton;
    //Button for taking image
    Button mTakePhotoButton;
    //Button for autofilling details
    Button mAutoFill;

    //Variable for the taking photo
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    Uri imageUri;
    File output = null;

    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.

    public static AddManuallyActivityFragment getInstance(String ISBN) {
        Bundle bundle = new Bundle();
        bundle.putString("ISBN", ISBN);

        AddManuallyActivityFragment fragment = new AddManuallyActivityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.fragment_add_book, null);
        requestQueue = Volley.newRequestQueue(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.d("BUNDLE", bundle.toString());
            if(bundle.getString("ISBN").length()>0){
                autofill(bundle.getString("ISBN"));
            }
        }
        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mydb = new DatabaseHelper(getActivity());

        View detailsFrame = getActivity().findViewById(R.id.menu_fragment_container);

        // set the onclick listeners for the buttons
        mAddButton = (Button) getActivity().findViewById(R.id.button_addBook);
        mTakePhotoButton = (Button) getActivity().findViewById(R.id.button_takeCoverPhoto);
        mAutoFill = (Button) getActivity().findViewById(R.id.button_autofill);

        //Assign name to each edit text
        mTitle = (EditText) getActivity().findViewById(R.id.et_title);
        mIsbn = (EditText) getActivity().findViewById(R.id.et_isbn);
        mGenre = (EditText) getActivity().findViewById(R.id.et_genre);
        mAuthor = (EditText) getActivity().findViewById(R.id.et_author);
        mPublisher = (EditText) getActivity().findViewById(R.id.et_publisher);
        mDatePub = (EditText) getActivity().findViewById(R.id.et_pubdate);
        mRating = (RatingBar) getActivity().findViewById(R.id.et_rating);
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
                int rating = (int) mRating.getRating();
                
                //If pages are null set to 0
                int totalpages, currentpage;
                if (mTotalPages.getText().toString().isEmpty()){
                    totalpages = 0;
                }
                else{
                    totalpages = Integer.parseInt(mTotalPages.getText().toString());
                }
                if (mCurrentPage.getText().toString().isEmpty()){
                    currentpage = 0;
                }
                else{
                    currentpage = Integer.parseInt(mCurrentPage.getText().toString());
                }

                //TODO Check if this is right not sure how the insert for author works
                String authorArray[] = author.split(",");

                mydb.insertBook(title, isbn, cover, genre, authorArray, publisher, datepub, rating, totalpages, currentpage);
                getActivity().finish();
                //Test print
                //Log.d("myTag", mydb.getAllBooks().toString());
                //Log.d("myTag", title+" "+isbn+" "+cover+" "+genre+" "+author+" "+publisher+" "+datepub+" "+" "+rating+" "+totalpages+" "+currentpage);
            }
        });

        //LISTENER for TAKEPHOTO button
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        mAutoFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autofill("9783161484100");
            }
        });
    }

    private void autofill(String isbn) {
        final BookAPIHelper bh = new BookAPIHelper(getActivity());
        bh.getBookFromISBN(isbn, new VolleyCallback(){

            @Override
            public void VolleyResponse(JSONObject books) {
                try {
                    JSONArray resBookArray = books.getJSONArray("items");
                    if(resBookArray.length()>0){

                        JSONObject bookInfo = resBookArray.getJSONObject(0).getJSONObject("volumeInfo");
                        Log.d("Volley", bookInfo.toString());
                        mTitle.setText(bookInfo.getString("title"));
                        mIsbn.setText(bookInfo.getString("title"));
                        mAuthor.setText(bookInfo.getJSONArray("authors").join(", "));
//                        mGenre.setText();
//                        mPublisher.setText();
                        mDatePub.setText(bookInfo.getString("publishedDate"));

                        new DownloadImageTask(mCoverThumbnail)
                                .execute(bookInfo.getJSONObject("imageLinks").getString("thumbnail"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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


    private void refresh() {

    }


}
