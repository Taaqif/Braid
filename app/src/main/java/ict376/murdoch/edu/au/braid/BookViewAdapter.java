package ict376.murdoch.edu.au.braid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ict376.murdoch.edu.au.braid.BookDisplayFragment.OnListFragmentInteractionListener;

import java.util.List;

//recycler view adaptor that can display a book object and implemetes listners
public class BookViewAdapter extends RecyclerView.Adapter<BookViewAdapter.ViewHolder> {

    //the list of books
    private final List<Book> mValues;
    private final OnListFragmentInteractionListener mListener;

    //fragments
    private BookDisplayFragment fragment;
    private BookViewAdapter instance;

    public BookViewAdapter(List<Book> items, OnListFragmentInteractionListener listener, BookDisplayFragment fragment) {
        mValues = items;
        mListener = listener;
        this.fragment = fragment;
        this.instance = this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book_display, parent, false);
        //inflates the view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //this is where you set the book details within the view

        holder.mTitleView.setText(holder.mItem.getTitle());
        holder.mAuthorView.setText(holder.mItem.getAuthors());
        holder.mDateView.setText(holder.mItem.getAddedDate());

        int totalpages = holder.mItem.getTotalPages();
        int currentpage = holder.mItem.getCurrentPages();
        holder.mPageView.setText("Page: " + currentpage + " of " + totalpages);

        if(holder.mItem.getCover() != null && !holder.mItem.getCover().isEmpty()){
            Bitmap myBitmap = BitmapFactory.decodeFile(holder.mItem.getCover());
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                ExifInterface exif = new ExifInterface(holder.mItem.getCover());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Matrix matrix = new Matrix();
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    matrix.postRotate(90);
                }
                else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    matrix.postRotate(180);
                }
                else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    matrix.postRotate(270);
                }
                myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
            }
            catch (Exception e) {

            }
            holder.mCoverView.setImageBitmap(myBitmap);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // return the current selected book object
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        //for the options view
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_book_popup_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                //handle edit
                                Intent intent = new Intent(view.getContext(), AddBookActivity.class);
                                intent.putExtra("ID", holder.mItem);
                                view.getContext().startActivity(intent);
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }


    //get the number of items
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //sets uo the variables
        public final View mView;
        public final TextView mTitleView;
        public final TextView mAuthorView;
        public final ImageView mCoverView;
        public final TextView mDateView;
        public final TextView mPageView;
        public Book mItem;
        public TextView buttonViewOption;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.Title);
            mAuthorView = (TextView) view.findViewById(R.id.author);
            mCoverView = (ImageView) view.findViewById(R.id.cover);
            mDateView = (TextView) view.findViewById(R.id.dateAdded);
            mPageView = (TextView) view.findViewById(R.id.pageRange);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
