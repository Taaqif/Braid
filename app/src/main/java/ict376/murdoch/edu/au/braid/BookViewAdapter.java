package ict376.murdoch.edu.au.braid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public BookViewAdapter(List<Book> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mAuthorView.setText(mValues.get(position).getTitle());
        holder.mDateView.setText(mValues.get(position).getAddedDate());

        int totalpages = mValues.get(position).getTotalPages();
        int currentpage = mValues.get(position).getCurrentPages();
        holder.mPageView.setText("Page: " + currentpage + " of " + totalpages);

        Bitmap coverPath = BitmapFactory.decodeFile(mValues.get(position).getCover());
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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.Title);
            mAuthorView = (TextView) view.findViewById(R.id.author);
            mCoverView = (ImageView) view.findViewById(R.id.cover);
            mDateView = (TextView) view.findViewById(R.id.dateAdded);
            mPageView = (TextView) view.findViewById(R.id.pageRange);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAuthorView.getText() + "'";
        }
    }
}
