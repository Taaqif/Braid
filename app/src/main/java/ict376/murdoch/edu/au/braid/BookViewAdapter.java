package ict376.murdoch.edu.au.braid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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

/**
 * {@link RecyclerView.Adapter} that can display a {@link Book} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BookViewAdapter extends RecyclerView.Adapter<BookViewAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final OnListFragmentInteractionListener mListener;
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //this is where you se the book details
        holder.mTitleView.setText(holder.mItem.getTitle());
        holder.mAuthorView.setText(holder.mItem.getAuthors());
        holder.mDateView.setText(holder.mItem.getAddedDate());

        int totalpages = holder.mItem.getTotalPages();
        int currentpage = holder.mItem.getCurrentPages();
        holder.mPageView.setText("Page: " + currentpage + " of " + totalpages);

        Bitmap coverPath = BitmapFactory.decodeFile(holder.mItem.getCover());
        holder.mCoverView.setImageBitmap(coverPath);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
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
                            case R.id.delete:
                                //handle delete click
                                showDeleteDialog(view.getContext(), holder.mItem.getID());
                                fragment.refresh();
                                instance.notifyDataSetChanged();
                                view.getContext();
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

    private void showDeleteDialog(final Context context, final int id) {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);
        // Set up the input

        builder.setTitle("Remove book?")
                .setMessage("Are you sure you wish to remove this book?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue
                        DatabaseHelper mydb = new DatabaseHelper(context);
                        Toast.makeText(context, "Book Removed", Toast.LENGTH_SHORT).show();

                        mydb.deleteBook(id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            return super.toString() + " '" + mAuthorView.getText() + "'";
        }
    }
}
