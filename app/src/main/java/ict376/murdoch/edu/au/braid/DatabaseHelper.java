package ict376.murdoch.edu.au.braid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
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



    private static int ver = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, ver);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table ? (" +
                "? integer primary key autoincrement," +
                "? text, " +
                "? text UNIQUE, " +
                "? text, " +
                "? text, " +
                "? text, " +
                "? text, " +
                "? integer, " +
                "? integer, " +
                "? integer,  " +
                "? integer) " +
                "foreign key (?) references ?(?)", new String[]{
                BOOK_TABLE_NAME,
                BOOK_COLUMN_ID,
                BOOK_COLUMN_TITLE,
                BOOK_COLUMN_ISBN,
                BOOK_COLUMN_COVER,
                BOOK_COLUMN_CATEGORIES,
                BOOK_COLUMN_PUBLISHED,
                BOOK_COLUMN_ADDDATE,
                BOOK_COLUMN_RATING,
                BOOK_COLUMN_TOTALPAGES,
                BOOK_COLUMN_CURRENTPAGE,
                BOOK_COLUMN_PUBLISHERID,
                PUBLISHER_TABLE_NAME,
                PUBLISHER_COLUMN_ID});

        sqLiteDatabase.execSQL("create table ? (" +
                "? integer primary key autoincrement," +
                "? text UNIQUE) ", new String[]{
                AUTHOR_TABLE_NAME,
                AUTHOR_COLUMN_ID,
                AUTHOR_COLUMN_NAME});

        sqLiteDatabase.execSQL("create table ? (" +
                "? integer primary key autoincrement," +
                "? text UNIQUE) ", new String[]{
                PUBLISHER_TABLE_NAME,
                PUBLISHER_COLUMN_ID,
                PUBLISHER_COLUMN_NAME});

        sqLiteDatabase.execSQL("create table ? (" +
                "? integer primary key autoincrement," +
                "? text) " +
                "primary key (?, ?)," +
                "foreign key (?) references ?(?)" +
                "foreign key (?) references ?(?)", new String[]{
                BOOK_AUTHOR_TABLE_NAME,
                BOOK_AUTHOR_COLUMN_BOOKID,
                BOOK_AUTHOR_COLUMN_AUTHORID,
                BOOK_AUTHOR_COLUMN_BOOKID,
                BOOK_AUTHOR_COLUMN_AUTHORID,
                BOOK_AUTHOR_COLUMN_BOOKID,
                BOOK_TABLE_NAME,
                BOOK_COLUMN_ID,
                BOOK_AUTHOR_COLUMN_AUTHORID,
                AUTHOR_TABLE_NAME,
                AUTHOR_COLUMN_ID});

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
    public void insertBook(String title, String ISBN, String cover, String categories, String publisher, String publishedDate, int rating, int totalPages, int currentPage ){

    }
}
