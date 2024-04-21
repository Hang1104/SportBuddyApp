package my.edu.utar.assignment2;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

import my.edu.utar.assignment2.LearningPage.LearningPage;
import my.edu.utar.assignment2.ProfilePage.Profile;

public class ViewRatingActivity extends AppCompatActivity {

    private static final String TAG = "ViewRatingActivity";

    private String gameId;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RatingReviewAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rating);

        gameId = getIntent().getStringExtra("gameId");
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RatingReviewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        retrieveRatingAndReview(gameId);


//        // Handle item click
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get the class of the activity to start
//                Class<?> activityClass = (Class<?>) sports.get(position).get("activity");
//
//                // Start the activity
//                Intent intent = new Intent(LearningPage.this, activityClass);
//                startActivity(intent);
//            }
//        });

        ImageView homePageBtn = findViewById(R.id.homePageBtn);
        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewRatingActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Chat button
        ImageView chatBtn = findViewById(R.id.Home_CommunityBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ViewRatingActivity.this, CommunityActivity.class);
//                startActivity(intent);
            }
        });

        // Book button
        ImageView learnBtn = findViewById(R.id.learnBtn);
        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewRatingActivity.this, LearningPage.class);
                startActivity(intent);
            }
        });

        // Profile button
        ImageView ProfileBtn = findViewById(R.id.ProfileBtn);
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewRatingActivity.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveRatingAndReview(String gameId) {
        db.collection("createGame").document(gameId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Long> ratings = (List<Long>) document.get("ratings");
                            List<String> reviews = (List<String>) document.get("reviews");

                            if (ratings != null && reviews != null && !ratings.isEmpty()) {
                                List<RatingReviewItem> items = new ArrayList<>();
                                for (int i = 0; i < Math.min(ratings.size(), reviews.size()); i++) {
                                    Long rating = ratings.get(i);
                                    String review = reviews.get(i);
                                    items.add(new RatingReviewItem(String.valueOf(rating), review));
                                }
                                adapter.setItems(items);
                            } else {
                                // No ratings found
                                adapter.setItems(new ArrayList<>());
                                TextView textView = findViewById(R.id.noRatingTextView);
                                textView.setText("No rating yet");
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }


}
