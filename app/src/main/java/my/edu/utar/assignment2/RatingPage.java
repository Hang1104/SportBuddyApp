package my.edu.utar.assignment2;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import my.edu.utar.assignment2.R;

public class RatingPage extends AppCompatActivity {

    private int rating = 0;
    private FirebaseAuth auth;
    private DatabaseReference ratingRef;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_to_rating_page);

        LinearLayout starLayout = findViewById(R.id.starLayout);
        EditText reviewEditText = findViewById(R.id.reviewEditText);
        Button submitButton = findViewById(R.id.submitButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ratingRef = FirebaseDatabase.getInstance().getReference("rating");




        // Add star icons dynamically
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5, 0, 5, 0);
            star.setLayoutParams(params);
            star.setImageResource(android.R.drawable.btn_star);
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRating(starLayout.indexOfChild(star) + 1);
                }
            });
            starLayout.addView(star);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = reviewEditText.getText().toString();
                // Process the review (e.g., submit to server)
                saveRatingAndReview(rating, review);
                showToast("Review submitted: " + review);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateRating(int newRating) {
        rating = newRating;
        LinearLayout starLayout = findViewById(R.id.starLayout);
        for (int i = 0; i < starLayout.getChildCount(); i++) {
            ImageView star = (ImageView) starLayout.getChildAt(i);
            if (i < rating) {
                star.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                star.setImageResource(android.R.drawable.btn_star);
            }
        }
    }

    private void saveRatingAndReview(int rating, String review) {
        // Save the rating and review to Firestore under a "ratings" collection
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("ratings")
                .add(new Rating(userId, rating, review))
                .addOnSuccessListener(documentReference -> {
                    // Data saved successfully
                    Log.d("Firestore", "Rating and review saved with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Firestore", "Error saving data: " + e.getMessage());
                });
    }



    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
