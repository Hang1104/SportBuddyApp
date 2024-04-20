package my.edu.utar.assignment2.CommunityPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import my.edu.utar.assignment2.HomePage;
import my.edu.utar.assignment2.R;

public class CommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        //back button
        ImageView BackBtn = findViewById(R.id.C_backBtn);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, HomePage.class);
                startActivity(intent);
            }
        });
        //Create post
        ConstraintLayout createPostLayout = findViewById(R.id.CreatePost);
        createPostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommunityActivity.this, CreateNewPost.class));
            }
        });
    }
}