package my.edu.utar.assignment2.CommunityPage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "posts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_POSTS = "posts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_POSTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    public void insertPost(String username, String content, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_IMAGE, image);
        db.insert(TABLE_POSTS, null, values);
        db.close();
    }
    public String getTableName() {
        return TABLE_POSTS;
    }
    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnUsername() {
        return COLUMN_USERNAME;
    }

    public static String getColumnContent() {
        return COLUMN_CONTENT;
    }

    public static String getColumnImage() {
        return COLUMN_IMAGE;
    }

    public static String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }
}


