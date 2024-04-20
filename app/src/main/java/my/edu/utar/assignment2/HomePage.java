package my.edu.utar.assignment2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import my.edu.utar.assignment2.LearningPage.LearningPage;
import my.edu.utar.assignment2.ProfilePage.Profile;

import my.edu.utar.assignment2.createGame.CreateGame;
import my.edu.utar.assignment2.createGame.Game;
import my.edu.utar.assignment2.createGame.GameDetails;
import my.edu.utar.assignment2.createGame.GameList;

public class HomePage extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private LocationManager locationManager;
    private String currentLocationName;
    private TextView currentLocationTextView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String latestGameId;
    private ShapeableImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //location
        currentLocationTextView = findViewById(R.id.Home_currentLocationTextView);

        //location button


        //check location permission
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationText(lastKnownLocation);
            }
        }

        //location textView
        TextView currentLocationTextView = findViewById(R.id.Home_currentLocationTextView);
        currentLocationTextView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                editLocation();
            }
        });

        //profile image
        profileImageView= findViewById(R.id.profileimageView);
        loadProfileImage();

        //create game button
        ImageView createGameButton = findViewById(R.id.Home_creategameBtn);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, CreateGame.class);
                startActivity(intent);
            }
        });

        //show all game nearby
        TextView showAllBtn = findViewById(R.id.Home_showallBtn);

        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, GameList.class);
                startActivity(intent);
            }
        });

        //game detail
        Button detailBtn = findViewById(R.id.Home_detailBtn);

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, GameDetails.class);
                intent.putExtra("gameId", latestGameId);
                startActivity(intent);
            }
        });

        //home button
        ImageView homePageBtn = findViewById(R.id.homePageBtn);

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, HomePage.class);
                startActivity(intent);
            }
        });

        //chat button
        ImageView chatBtn = findViewById(R.id.chatBtn);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, CreateGameActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //book button
        ImageView learnBtn = findViewById(R.id.learnBtn);

        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, LearningPage.class);
                startActivity(intent);
            }
        });

        //profile button below
        ImageView ProfileBtn = findViewById(R.id.ProfileBtn);

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Profile.class);
                startActivity(intent);
            }
        });

        //Community page
        Button CommunityBtn = findViewById(R.id.Home_CommunityBtn);

        CommunityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, CreateGameActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        fetchLatestGame();
    }


    // Check location permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Handle location change
        // Display latitude and longitude in a Toast message
        Toast.makeText(this, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                currentLocationName = addresses.get(0).getAddressLine(0);
                updateLocationUI(currentLocationName);
                saveLocationToFirestore(currentLocationName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disabled
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changed
    }

    private void updateLocationUI(String locationName) {
        currentLocationTextView.setText(getString(R.string.current_location, locationName));
        Log.d("Location", "Current location: " + locationName);
    }

    private void fetchLatestGame() {
        db.collection("createGame")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .limit(1) // Limit the query to retrieve only one document
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot latestGameSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                            // Retrieve details of the latest game
                            String gameId = latestGameSnapshot.getId();
                            String sportType = latestGameSnapshot.getString("sportType");
                            String location = latestGameSnapshot.getString("location");
                            String address = latestGameSnapshot.getString("address");
                            String date = latestGameSnapshot.getString("date");
                            String startTime = latestGameSnapshot.getString("startTime");
                            String endTime = latestGameSnapshot.getString("endTime");
                            String gameSkill = latestGameSnapshot.getString("gameSkill");
                            String userId = latestGameSnapshot.getString("userId");

                            // Fetch username from 'users' collection
                            db.collection("users").document(userId)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String username = documentSnapshot.getString("username");
                                                String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                                                String initCapLocation = toInitCap(location);

                                                // Set the game details in a TextView
                                                TextView sporttype = findViewById(R.id.sporttype);
                                                sporttype.setText(sportType);

                                                TextView Date = findViewById(R.id.Date);
                                                Date.setText(date);

                                                TextView Location = findViewById(R.id.Location);
                                                Location.setText(initCapLocation);

                                                TextView gameskill = findViewById(R.id.gameskill);
                                                gameskill.setText(gameSkill);

                                                ShapeableImageView Home_GamePic = findViewById(R.id.Home_GamePic);
                                                Picasso.get().load(profileImageUrl).into(Home_GamePic);


                                                latestGameId = gameId;


                                            } else {
                                                Log.d("fetchLatestGame", "User document does not exist");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("fetchLatestGame", "Error getting user document", e);
                                        }
                                    });
                        } else {
                            Log.d("fetchLatestGame", "No games found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomePage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String toInitCap(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Split the text by space and capitalize each word
        String[] words = text.toLowerCase().split("\\s");
        StringBuilder initCapText = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                initCapText.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return initCapText.toString().trim();
    }



    private void updateLocationText(Location location) {
        Geocoder geocoder = new Geocoder(HomePage.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                currentLocationTextView.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Location");

        // Set up the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_location, null);
        builder.setView(dialogView);

        // Get references to the EditText fields
        EditText streetEditText = dialogView.findViewById(R.id.streetEditText);
        EditText stateEditText = dialogView.findViewById(R.id.stateEditText);
        EditText countryEditText = dialogView.findViewById(R.id.countryEditText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the text from the EditText fields
                String street = streetEditText.getText().toString().trim();
                String state = stateEditText.getText().toString().trim();
                String country = countryEditText.getText().toString().trim();

                // Create the full location string
                String newLocation = street + ", " + state + ", " + country;

                // Update the location text view
                currentLocationTextView.setText(newLocation);

                // Save the new location to Firestore
                saveLocationToFirestore(newLocation);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    //save Location to Firestore
    private void saveLocationToFirestore(String location) {
        db.collection("users").document(auth.getCurrentUser().getUid())
                .update("location", location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        currentLocationName = location;
                        updateLocationUI(currentLocationName); // Update the UI to display the newly saved location
                        Log.d("saveLocationToFirestore", "Location updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("saveLocationToFirestore", "Error updating location", e);
                    }
                });
    }

    //load location of user from firestore
    private void loadUserLocation() {
        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String location = documentSnapshot.getString("location");
                            if (location != null && !location.isEmpty()) {
                                currentLocationName = location;
                                updateLocationUI(currentLocationName);
                            }
                        } else {
                            Log.d("HomePage", "User document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("HomePage", "Error getting user document", e);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserLocation(); // Load user's location when the activity is resumed
    }


    //load profile image
    private void loadProfileImage() {

        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                Glide.with(HomePage.this).load(profileImageUrl).into(profileImageView);
                            } else {
                                Glide.with(HomePage.this).load(R.drawable.profile).into(profileImageView);
                            }
                        } else {
                            Log.d("HomeActivity", "No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("HomeActivity", "Error getting document", e);
                    }
                });
    }
}