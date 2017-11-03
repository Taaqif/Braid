package ict376.murdoch.edu.au.braid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static final String BOOK_COLUMN_ID               = "id";
    private static final String BOOK_COLUMN_TITLE            = "name";
    private static final String BOOK_COLUMN_ISBN             = "ISBN";
    private static final String BOOK_COLUMN_COVER            = "cover";
    private static final String BOOK_COLUMN_CATEGORIES       = "categories";
    private static final String BOOK_COLUMN_PUBLISHERID      = "publisher_id";
    private static final String BOOK_COLUMN_STATUS           = "status";
    private static final String BOOK_COLUMN_PUBLISHED        = "publishedDate";
    private static final String BOOK_COLUMN_ADDDATE          = "addedDate";
    private static final String BOOK_COLUMN_RATING           = "rating";
    private static final String BOOK_COLUMN_TOTALPAGES       = "totalPages";
    private static final String BOOK_COLUMN_CURRENTPAGE      = "currentPages";


    //-----------------
    private static final String AUTHOR_TABLE_NAME            = "authors";
    //-----------------
    private static final String AUTHOR_COLUMN_ID             = "id";
    private static final String AUTHOR_COLUMN_NAME           = "name";

    //-----------------
    private static final String PUBLISHER_TABLE_NAME         = "publisher";
    //-----------------
    private static final String PUBLISHER_COLUMN_ID          = "id";
    private static final String PUBLISHER_COLUMN_NAME        = "name";


    //-----------------
    private static final String BOOK_AUTHOR_TABLE_NAME       = "author_book";
    //-----------------
    private static final String BOOK_AUTHOR_COLUMN_AUTHORID  = "author_id";
    private static final String BOOK_AUTHOR_COLUMN_BOOKID    = "book_id";



    private static int ver = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, ver);
    }

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
                BOOK_COLUMN_TITLE + " text UNIQUE, " +
                BOOK_COLUMN_ISBN + " text, " +
                BOOK_COLUMN_COVER + " text, " +
                BOOK_COLUMN_CATEGORIES + " text, " +
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

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PUBLISHER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BOOK_AUTHOR_TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
//    BOOK_COLUMN_TITLE,BOOK_COLUMN_ISBNBOOK_COLUMN_COVER,BOOK_COLUMN_CATEGORIESBOOK_COLUMN_PUBLISHERID,BOOK_COLUMN_PUBLISHED,BOOK_COLUMN_ADDDATE,BOOK_COLUMN_RATINGBOOK_COLUMN_TOTALPAGESBOOK_COLUMN_CURRENTPAGE
    public boolean insertBook(String title, String ISBN, String cover, String categories, String[] author, String publisher, String publishedDate, int rating, int totalPages, int currentPage ){
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare the row to insert
        ContentValues contentValues = new ContentValues();

        contentValues.put(BOOK_COLUMN_TITLE, title);
        contentValues.put(BOOK_COLUMN_ISBN, ISBN);
        contentValues.put(BOOK_COLUMN_COVER, cover);
        contentValues.put(BOOK_COLUMN_CATEGORIES, categories);

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
        if(id > 0){
            for (String s : author) {
                contentValues = new ContentValues();
                contentValues.put(BOOK_AUTHOR_COLUMN_BOOKID, id);
                contentValues.put(BOOK_AUTHOR_COLUMN_AUTHORID, getAuthorID(s));
                db.insert(BOOK_AUTHOR_TABLE_NAME, null, contentValues);
            }


            return true;
        }
        Log.d("st", "he");
        return false;
    }

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

    public void deleteBook(int id){
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(BOOK_TABLE_NAME,
                BOOK_COLUMN_ID + " = "+ Integer.toString(id) +"" ,  null);


        // delete contact
        db.delete(BOOK_AUTHOR_TABLE_NAME,
                BOOK_AUTHOR_COLUMN_BOOKID + " = "+ Integer.toString(id) +" ", null);
    }

    public Book getBook(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME + " where "+ BOOK_COLUMN_ID +" = '" + id + "'", null );
        res.moveToFirst();
        Book tmp = new Book(
                res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)),
                res.getString(res.getColumnIndex(BOOK_COLUMN_TITLE)),
                res.getString(res.getColumnIndex(BOOK_COLUMN_ISBN)),
                res.getString(res.getColumnIndex(BOOK_COLUMN_COVER)),
                res.getString(res.getColumnIndex(BOOK_COLUMN_CATEGORIES)),
                res.getInt(res.getColumnIndex(BOOK_COLUMN_PUBLISHERID)),
                res.getString(res.getColumnIndex(BOOK_COLUMN_PUBLISHED)),
                res.getString(res.getColumnIndex(BOOK_COLUMN_ADDDATE)),
                res.getInt(res.getColumnIndex(BOOK_COLUMN_RATING)),
                res.getInt(res.getColumnIndex(BOOK_COLUMN_TOTALPAGES)),
                res.getInt(res.getColumnIndex(BOOK_COLUMN_CURRENTPAGE)));
        res.close();
        return tmp;
    }

    public List<Book> getAllBooks(){
        List<Book> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add( new Book(
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_TITLE)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ISBN)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_COVER)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_CATEGORIES)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_PUBLISHERID)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_PUBLISHED)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ADDDATE)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_RATING)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_TOTALPAGES)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_CURRENTPAGE))
            ) );
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
    public List<Book> getReadBooks(){
        List<Book> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME + " where " + BOOK_COLUMN_CURRENTPAGE + " = "+ BOOK_COLUMN_TOTALPAGES  +"", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add( new Book(
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_TITLE)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ISBN)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_COVER)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_CATEGORIES)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_PUBLISHERID)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_PUBLISHED)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ADDDATE)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_RATING)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_TOTALPAGES)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_CURRENTPAGE))
            ) );
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
    public List<Book> getUnreadBooks(){
        List<Book> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //do some joins here to add author, publisher etc
        Cursor res =  db.rawQuery( "select * from " + BOOK_TABLE_NAME + " where " + BOOK_COLUMN_CURRENTPAGE + " < "+ BOOK_COLUMN_TOTALPAGES  +"", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add( new Book(
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_ID)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_TITLE)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ISBN)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_COVER)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_CATEGORIES)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_PUBLISHERID)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_PUBLISHED)),
                    res.getString(res.getColumnIndex(BOOK_COLUMN_ADDDATE)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_RATING)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_TOTALPAGES)),
                    res.getInt(res.getColumnIndex(BOOK_COLUMN_CURRENTPAGE))
            ) );
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
