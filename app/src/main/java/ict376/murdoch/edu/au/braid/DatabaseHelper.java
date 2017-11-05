package ict376.murdoch.edu.au.braid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.id;

/**
 * Database Helper class
 * Created by Taaqif on 23/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "Braid.db";

    //-----------------
    private static final String BOOK_TABLE_NAME              = "books";
    //-----------------
    private static final String BOOK_COLUMN_ID               = "book_id";
    private static final String BOOK_COLUMN_TITLE            = "book_name";
    private static final String BOOK_COLUMN_ISBN             = "book_ISBN";
    private static final String BOOK_COLUMN_COVER            = "book_cover";
    private static final String BOOK_COLUMN_PUBLISHERID      = "book_publisher_id";
    private static final String BOOK_COLUMN_STATUS           = "book_status";
    private static final String BOOK_COLUMN_PUBLISHED        = "book_publishedDate";
    private static final String BOOK_COLUMN_ADDDATE          = "book_addedDate";
    private static final String BOOK_COLUMN_RATING           = "book_rating";
    private static final String BOOK_COLUMN_TOTALPAGES       = "book_totalPages";
    private static final String BOOK_COLUMN_CURRENTPAGE      = "book_currentPages";


    //-----------------
    private static final String AUTHOR_TABLE_NAME            = "authors";
    //-----------------
    private static final String AUTHOR_COLUMN_ID             = "author_id";
    private static final String AUTHOR_COLUMN_NAME           = "author_name";

    //-----------------
    private static final String PUBLISHER_TABLE_NAME         = "publisher";
    //-----------------
    private static final String PUBLISHER_COLUMN_ID          = "publisher_id";
    private static final String PUBLISHER_COLUMN_NAME        = "publisher_name";


    //-----------------
    private static final String BOOK_AUTHOR_TABLE_NAME       = "book_author";
    //-----------------
    private static final String BOOK_AUTHOR_COLUMN_AUTHORID  = "book_author_author_id";
    private static final String BOOK_AUTHOR_COLUMN_BOOKID    = "book_author_book_id";



    private static int ver = 5;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, ver);
    }

    //creates the batabase tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ PUBLISHER_TABLE_NAME +" (" +
                PUBLISHER_COLUMN_ID +" integer primary key," +
                PUBLISHER_COLUMN_NAME + " text UNIQUE) ");

        sqLiteDatabase.execSQL("create table "+ AUTHOR_TABLE_NAME +" (" +
                AUTHOR_COLUMN_ID + " integer primary key, " +
                AUTHOR_COLUMN_NAME + " text UNIQUE) ");

        sqLiteDatabase.execSQL("create table "+ BOOK_TABLE_NAME +" (" +
                BOOK_COLUMN_ID +" integer primary key, " +
                BOOK_COLUMN_TITLE + " text, " +
                BOOK_COLUMN_ISBN + " text, " +
                BOOK_COLUMN_COVER + " text, " +
                BOOK_COLUMN_PUBLISHED + " text, " +
                BOOK_COLUMN_PUBLISHERID + " integer, " +
                BOOK_COLUMN_STATUS + " integer, " +
                BOOK_COLUMN_ADDDATE + " integer, " +
                BOOK_COLUMN_RATING + " integer, " +
                BOOK_COLUMN_TOTALPAGES + " integer,  " +
                BOOK_COLUMN_CURRENTPAGE + " integer, " +
                "foreign key ("+ BOOK_COLUMN_PUBLISHERID +") references "+ PUBLISHER_TABLE_NAME +"(" + PUBLISHER_COLUMN_ID + "))");




        sqLiteDatabase.execSQL("create table "+BOOK_AUTHOR_TABLE_NAME+" (" +
                BOOK_AUTHOR_COLUMN_BOOKID+ " integer," +
                BOOK_AUTHOR_COLUMN_AUTHORID+" integer, " +
                "primary key ("+ BOOK_AUTHOR_COLUMN_BOOKID +", "+ BOOK_AUTHOR_COLUMN_AUTHORID +"), " +
                "foreign key ("+ BOOK_AUTHOR_COLUMN_BOOKID +") references "+ BOOK_TABLE_NAME +"("+ BOOK_COLUMN_ID +"), " +
                "foreign key ("+ BOOK_AUTHOR_COLUMN_AUTHORID +") references "+ AUTHOR_TABLE_NAME +"("+ AUTHOR_COLUMN_ID +"))");

    }

    //removes and restarts
    //should implement a migration pattern
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PUBLISHER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BOOK_AUTHOR_TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
    //inserts a book
    public boolean insertBook(String title, String ISBN, String cover, String[] author, String publisher, String publishedDate, int rating, int totalPages, int currentPage ){
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare the row to insert
        ContentValues contentValues = new ContentValues();

        contentValues.put(BOOK_COLUMN_TITLE, title);
        contentValues.put(BOOK_COLUMN_ISBN, ISBN);
        contentValues.put(BOOK_COLUMN_COVER, cover);

        contentValues.put(BOOK_COLUMN_PUBLISHERID, getPublisherID(publisher));
        contentValues.put(BOOK_COLUMN_PUBLISHED, publishedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date = sdf.format(new Date());
        contentValues.put(BOOK_COLUMN_ADDDATE, date);

        contentValues.put(BOOK_COLUMN_RATING, rating);
        contentValues.put(BOOK_COLUMN_TOTALPAGES, totalPages);
        contentValues.put(BOOK_COLUMN_CURRENTPAGE, currentPage);

        // Insert the row
        int id = (int) db.insert(BOOK_TABLE_NAME, null, contentValues);
        //for each author
        if(id > 0){
            for (String s : author) {

                contentValues = new ContentValues();
                contentValues.put(BOOK_AUTHOR_COLUMN_BOOKID, id);
                contentValues.put(BOOK_AUTHOR_COLUMN_AUTHORID, getAuthorID(s));
                db.insert(BOOK_AUTHOR_TABLE_NAME, null, contentValues);
            }


            return true;
        }
        return false;
    }
    //update book details
    public boolean updateBook(int id, String title, String ISBN, String cover, String[] author, String publisher, String publishedDate, int rating, int totalPages, int currentPage ){
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare the row to insert
        ContentValues contentValues = new ContentValues();

        contentValues.put(BOOK_COLUMN_TITLE, title);
        contentValues.put(BOOK_COLUMN_ISBN, ISBN);
        contentValues.put(BOOK_COLUMN_COVER, cover);

        contentValues.put(BOOK_COLUMN_PUBLISHERID, getPublisherID(publisher));
        contentValues.put(BOOK_COLUMN_PUBLISHED, publishedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date = sdf.format(new Date());
        contentValues.put(BOOK_COLUMN_ADDDATE, date);

        contentValues.put(BOOK_COLUMN_RATING, rating);
        contentValues.put(BOOK_COLUMN_TOTALPAGES, totalPages);
        contentValues.put(BOOK_COLUMN_CURRENTPAGE, currentPage);

        // Insert the row
        int bookid = (int) db.update(BOOK_TABLE_NAME, contentValues, BOOK_COLUMN_ID + " = ?", new String[]{ id + ""});
        //delete all previous authors from the book
        db.delete(BOOK_AUTHOR_TABLE_NAME, BOOK_AUTHOR_COLUMN_BOOKID + " = ? ", new String[]{ Integer.toString(bookid)});
        //db.delete(BOOK_AUTHOR_TABLE_NAME, BOOK_AUTHOR_COLUMN_BOOKID + " = ? ", new String[]{ Integer.toString(bookid)});
        //reinsert them all
        if(bookid > 0){
            for (String s : author) {

                contentValues = new ContentValues();
                contentValues.put(BOOK_AUTHOR_COLUMN_BOOKID, id);
                contentValues.put(BOOK_AUTHOR_COLUMN_AUTHORID, getAuthorID(s));
                db.insert(BOOK_AUTHOR_TABLE_NAME, null, contentValues);
            }


            return true;
        }
        return false;
    }
    //gets or creates a publisher
    private int getPublisherID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id;
        Cursor c = db.rawQuery("SELECT "+ PUBLISHER_COLUMN_ID +" FROM "+ PUBLISHER_TABLE_NAME +" WHERE "+ PUBLISHER_COLUMN_NAME +" = '"+ name +"'" ,null);

        //if the id is found, return it
        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            id = c.getInt(c.getColumnIndex(PUBLISHER_COLUMN_ID));
        }else{
            //if not, add it
            ContentValues contentValues = new ContentValues();

            contentValues.put(PUBLISHER_COLUMN_NAME, name);
            id = (int) db.insert(PUBLISHER_TABLE_NAME, null, contentValues);
        }
        if(c!=null)c.close();
        return id;

    }
    //gets or creates a author
    private int getAuthorID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id;
        Cursor c = db.rawQuery("SELECT "+ AUTHOR_COLUMN_ID +" FROM "+ AUTHOR_TABLE_NAME +" WHERE "+ AUTHOR_COLUMN_NAME +" = '"+ name + "'", null);

        //if the id is found, return it
        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            id = c.getInt(c.getColumnIndex(AUTHOR_COLUMN_ID));
        }else{
            //if not, add it
            ContentValues contentValues = new ContentValues();

            contentValues.put(AUTHOR_COLUMN_NAME, name);
            id = (int) db.insert(AUTHOR_TABLE_NAME, null, contentValues);
        }
        if(c!=null)c.close();
        return id;

    }

    //deletes a book and its references
    public void deleteBook(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        //delete book
        db.delete(BOOK_TABLE_NAME,
                BOOK_COLUMN_ID + " = ?" ,  new String[]{ id + ""});


        // delete book from junction table
        db.delete(BOOK_AUTHOR_TABLE_NAME,
                BOOK_AUTHOR_COLUMN_BOOKID + " = ?" ,  new String[]{ id + ""});
    }

    //gets a book
    public Book getBook(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME +
                " INNER JOIN " + PUBLISHER_TABLE_NAME + " ON "+PUBLISHER_TABLE_NAME+"." + PUBLISHER_COLUMN_ID + " = " + BOOK_TABLE_NAME + "." + BOOK_COLUMN_PUBLISHERID +
                " where "+ BOOK_TABLE_NAME + "." + BOOK_COLUMN_ID +" = '" + id + "'", null );
        res.moveToFirst();

        //get all the authors
        //get the authors as a stirng to put into the book object
        Cursor authorres =  db.rawQuery( "select "+ AUTHOR_COLUMN_NAME +" from " + AUTHOR_TABLE_NAME +
                " INNER JOIN " + BOOK_AUTHOR_TABLE_NAME + " ON " + BOOK_AUTHOR_TABLE_NAME + "." + BOOK_AUTHOR_COLUMN_AUTHORID+ " = " + AUTHOR_TABLE_NAME + "." + AUTHOR_COLUMN_ID +
                " where "+ BOOK_AUTHOR_TABLE_NAME + "." + BOOK_AUTHOR_COLUMN_BOOKID +" = '" + res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)) + "'", null );
        //author list string seperateed by ,
        String authorString = "";
        try {
            while (authorres.moveToNext()) {

                authorString += authorres.getString(authorres.getColumnIndex(AUTHOR_COLUMN_NAME)) + "; ";

            }
        } finally {
            //author string will always have a , at the end so clean it up
            if (null != authorString && !authorString.isEmpty()) {
                authorString = authorString.substring(0, authorString.length()-2);
            }

            authorres.close();
        }

        Book tmp = null;

        if(res.getCount() > 0){
            tmp = new Book(
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_TITLE)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ISBN)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_COVER)),
                    authorString,
                    res.getString(res.getColumnIndex(PUBLISHER_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_PUBLISHED)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ADDDATE)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_RATING)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_TOTALPAGES)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_CURRENTPAGE)));
        }


        res.close();
        return tmp;
    }

    //returns all the books as a list
    public List<Book> getAllBooks(){
        List<Book> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME + " ORDER BY date("+ BOOK_COLUMN_ADDDATE +" ) DESC", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Book tmp = getBook(res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)));
            if(tmp != null){
                array_list.add(tmp);
            }

            res.moveToNext();
        }
        res.close();

        return array_list;
    }
    //gets a list of read books
    //i.e where currentpage = total page
    public List<Book> getReadBooks(){
        List<Book> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME + " where " + BOOK_COLUMN_CURRENTPAGE + " = "+ BOOK_COLUMN_TOTALPAGES  +"", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Book tmp = getBook(res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)));
            if(tmp != null){
                array_list.add(tmp);
            }

            res.moveToNext();
        }
        res.close();
        Collections.sort(array_list, new Comparator<Book>() {

            DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            public int compare(Book o1, Book o2) {
                try {
                    return f.parse(o1.getAddedDate()).compareTo(f.parse(o2.getAddedDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return array_list;
    }
    //gets all the unread books
    //i.e where currentpage < total pages
    public List<Book> getUnreadBooks(){
        List<Book> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME + " where " + BOOK_COLUMN_CURRENTPAGE + " < "+ BOOK_COLUMN_TOTALPAGES  +"", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Book tmp = getBook(res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)));
            if(tmp != null){
                array_list.add(tmp);
            }

            res.moveToNext();
        }
        res.close();
        Collections.sort(array_list, new Comparator<Book>() {

            DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            public int compare(Book o1, Book o2) {
                try {
                    return f.parse(o1.getAddedDate()).compareTo(f.parse(o2.getAddedDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return array_list;
    }
}
