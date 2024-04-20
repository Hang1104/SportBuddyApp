package my.edu.utar.assignment2.CommunityPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.utar.assignment2.CommunityPage.DatabaseHelper;
import my.edu.utar.assignment2.HomePage;
import my.edu.utar.assignment2.R;


public class CommunityActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        dbHelper = new DatabaseHelper(this);

        // Set onClickListener for the "Community" TextView
        TextView communityTextView = findViewById(R.id.C_textView2);
        communityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from the database
                String postData = retrieveDataFromDatabase();

                // Display retrieved data
                Toast.makeText(CommunityActivity.this, postData, Toast.LENGTH_SHORT).show();
            }
        });

        //back button
        ImageView BackBtn = findViewById(R.id.C_backBtn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Create post
        ConstraintLayout createPostLayout = findViewById(R.id.CreatePost);
        createPostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, CreateNewPost.class);
                startActivity(intent);
            }
        });
    }

    private String retrieveDataFromDatabase() {
        String result = "";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(dbHelper.getTableName(), null, null, null, null, null, DatabaseHelper.getColumnTimestamp() + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.getColumnUsername()));
                @SuppressLint("Range") String datetime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.getColumnTimestamp()));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.getColumnContent()));

                result += "Username: " + username + "\n"
                        + "Date & Time: " + datetime + "\n"
                        + "Content: " + content + "\n\n";
            }
            cursor.close();
        }
        db.close();

        return result;
    }
}
