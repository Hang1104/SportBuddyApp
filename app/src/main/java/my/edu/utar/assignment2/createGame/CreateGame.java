package my.edu.utar.assignment2.createGame;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import my.edu.utar.assignment2.HomePage;
import my.edu.utar.assignment2.LearningPage.LearningPage;
import my.edu.utar.assignment2.MainActivity;
import my.edu.utar.assignment2.ProfilePage.Profile;
import my.edu.utar.assignment2.R;

public class CreateGame extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    private LocationManager locationManager;
    private LocationListener locationListener;

    // Declare the TextView for selected location
    private TextView selectedLocationTextView;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private TextView currentLocationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Back Button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGame.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });

        // Location EditText and Search Button
        EditText locationEditText = findViewById(R.id.locationEditText);
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = locationEditText.getText().toString();
                if (!location.isEmpty()) {
                    searchLocation(location);
                } else {
                    Toast.makeText(CreateGame.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Calendar Icon
        ImageButton calendarIcon = findViewById(R.id.calendarIcon);
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Sport Types Dropdown List
        String[] sportsTypes = {"Badminton", "Swimming", "Futsal", "Ping Pong", "Gym", "Bowling"};
        Spinner sportSpinner = findViewById(R.id.sportSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sportsTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(adapter);

        // Initialize selectedLocationTextView
        selectedLocationTextView = findViewById(R.id.selectedLocationTextView);

        // Game Skills Dropdown List
        String[] gameSkills = {"Beginner", "Intermediate", "Advanced"};
        Spinner skillLevelSpinner = findViewById(R.id.skillLevelSpinner);
        ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gameSkills);
        skillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skillLevelSpinner.setAdapter(skillAdapter);

        // Set up UI elements and event listeners
        Button createGameButton = findViewById(R.id.createGameButton);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateGame", "createGameButton.onClick: Creating game");
                createGame();
            }
        });

        // Home button
        ImageView homePageBtn = findViewById(R.id.homePageBtn);
        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGame.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Chat button
        ImageView chatBtn = findViewById(R.id.Home_CommunityBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateGame.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        // Book button
        ImageView learnBtn = findViewById(R.id.learnBtn);
        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGame.this, LearningPage.class);
                startActivity(intent);
            }
        });

        // Profile button
        ImageView ProfileBtn = findViewById(R.id.ProfileBtn);
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGame.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update selected date TextView
                updateSelectedDateTextView(dayOfMonth, monthOfYear, year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    // Method to update selected date TextView
    private void updateSelectedDateTextView(int day, int month, int year) {
        // Initialize selectedDateTextView
        TextView selectedDateTextView = findViewById(R.id.selectedDateTextView);

        // Show selected date text view
        selectedDateTextView.setVisibility(View.VISIBLE);

        // Set the selected date text
        String selectedDateText = "" + day + "/" + (month + 1) + "/" + year;
        selectedDateTextView.setText(selectedDateText);
    }

    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                Toast.makeText(this, "Location Found: " + address.getAddressLine(0), Toast.LENGTH_SHORT).show();

                // Display the selected location
                displaySelectedLocation(address);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();

                // Hide selected location text view if address is not found
                displaySelectedLocation(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error searching for location", Toast.LENGTH_SHORT).show();

            // Hide selected location text view if there's an error
            displaySelectedLocation(null);
        }
    }

    // Method to display the selected location address
    private void displaySelectedLocation(Address address) {
        if (address != null) {
            // Get the address line
            String addressLine = address.getAddressLine(0);

            // Show selected location text view
            selectedLocationTextView.setVisibility(View.VISIBLE);

            // Set the selected location text
            String selectedLocationText = "" + addressLine;
            selectedLocationTextView.setText(selectedLocationText);
        } else {
            // Hide selected location text view if address is null
            selectedLocationTextView.setVisibility(View.GONE);
        }
    }

    private void createGame() {
        // Get user input from UI elements
        Spinner sportSpinner = findViewById(R.id.sportSpinner);
        EditText locationEditText = findViewById(R.id.locationEditText);
        TextView selectedDateTextView = findViewById(R.id.selectedDateTextView);
        Spinner startHourSpinner = findViewById(R.id.startHourSpinner);
        Spinner startAmPmSpinner = findViewById(R.id.startAmPmSpinner);
        Spinner endHourSpinner = findViewById(R.id.endHourSpinner);
        Spinner endAmPmSpinner = findViewById(R.id.endAmPmSpinner);
        Spinner skillLevelSpinner = findViewById(R.id.skillLevelSpinner);
        EditText maxPlayersEditText = findViewById(R.id.maxPlayersEditText);

        String sportType = sportSpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString();
        String address = selectedLocationTextView.getText().toString();
        String date = selectedDateTextView.getText().toString();
        String startTime = startHourSpinner.getSelectedItem().toString() + " " + startAmPmSpinner.getSelectedItem().toString();
        String endTime = endHourSpinner.getSelectedItem().toString() + " " + endAmPmSpinner.getSelectedItem().toString();
        String gameSkill = skillLevelSpinner.getSelectedItem().toString();
        String maxPlayersStr = maxPlayersEditText.getText().toString();

        // Check if any field is empty
        if (sportType.isEmpty() || location.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || gameSkill.isEmpty() || maxPlayersStr.isEmpty()) {
            // Display a toast message indicating that all fields must be filled
            Toast.makeText(CreateGame.this, "Please fill in all sections to create the game", Toast.LENGTH_SHORT).show();
            return; // Stop the method execution
        }

        // Get the current user's ID
        String userId = mAuth.getUid();
        userId.toString();
        Log.d("User Id", userId);

        // Get current timestamp
        long currentTime = System.currentTimeMillis();
        int maxPlayers = Integer.parseInt(maxPlayersStr);


        // Create a new game object
        //Game game = new Game(sportType, location, address, date, startTime, endTime, gameSkill, userId);
        Game game = new Game(sportType, location, address, date, startTime, endTime, gameSkill, userId, currentTime, maxPlayers);

        // Add the game object to Firestore
        db.collection("createGame")
                .add(game)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Game successfully added to Firestore
                        Toast.makeText(CreateGame.this, "Game created successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CreateGame.this, GameList.class);
                        startActivity(intent);

                        // Close the current activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while adding the game to Firestore
                        Toast.makeText(CreateGame.this, "Error creating game: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}