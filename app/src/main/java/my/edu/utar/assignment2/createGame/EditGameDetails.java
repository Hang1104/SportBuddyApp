package my.edu.utar.assignment2.createGame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditGameDetails extends AppCompatActivity {

    private FirebaseFirestore db;
    private String gameId;

    private EditText sportTypeEditText;
    private EditText locationEditText;
    private EditText dateEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private Spinner skillLevelSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game_details);

        db = FirebaseFirestore.getInstance();

        gameId = getIntent().getStringExtra("gameId");

        // Initialize views
        sportTypeEditText = findViewById(R.id.sportSpinner);
        locationEditText = findViewById(R.id.locationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        skillLevelSpinner = findViewById(R.id.skillLevelSpinner);
        saveButton = findViewById(R.id.saveButton);

        // Set onClick listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGameDetails();
            }
        });

        // Load existing game details
        loadGameDetails();
    }

    private void loadGameDetails() {
        // Retrieve existing game details from Firestore and populate the EditText fields
        // You need to implement this method to fetch and display existing game details based on gameId
    }

    private void saveGameDetails() {
        // Get updated game details from EditText fields
        String sportType = sportTypeEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();
        String skillLevel = skillLevelSpinner.getSelectedItem().toString();

        // Create a map to store updated game details
        Map<String, Object> gameDetails = new HashMap<>();
        gameDetails.put("sportType", sportType);
        gameDetails.put("location", location);
        gameDetails.put("date", date);
        gameDetails.put("startTime", startTime);
        gameDetails.put("endTime", endTime);
        gameDetails.put("skillLevel", skillLevel);

        // Update game details in Firestore
        db.collection("games").document(gameId)
                .update(gameDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Game details updated successfully
                        Toast.makeText(EditGameDetails.this, "Game details updated successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity after saving
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while updating game details
                        Toast.makeText(EditGameDetails.this, "Failed to update game details. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
