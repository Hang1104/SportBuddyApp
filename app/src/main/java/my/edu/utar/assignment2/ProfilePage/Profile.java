package my.edu.utar.assignment2.ProfilePage;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import my.edu.utar.assignment2.HomePage;
import my.edu.utar.assignment2.LearningPage.LearningPage;
import my.edu.utar.assignment2.R;
import my.edu.utar.assignment2.RegisterSignIn.Login;


public class Profile extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int IMAGE_PICKER_REQUEST = 1;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ResultAdapter mAdapter;

    private RecyclerView horizontalRecyclerView;
    private HorizontalScrollAdapter adapter;
    private List<String> itemList = new ArrayList<>();

    TextView nameText, emailText, locationText, addButton;
    CircleImageView avatarImage;
    Button EditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initialize Firebase authentication and Firestore.
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Other views initialization
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        locationText = findViewById(R.id.locationText);
        avatarImage = findViewById(R.id.avatarImage);
        addButton = findViewById(R.id.addButton);

        //find game recycle view
        RecyclerView recyclerView = findViewById(R.id.gameRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mAdapter = new ResultAdapter();
        recyclerView.setAdapter(mAdapter);

        //Initialize the RecyclerView
        RecyclerView horizontalRecyclerView = findViewById(R.id.horizontalRecyclerView);
//        HorizontalScrollAdapter adapter = new HorizontalScrollAdapter(itemList, item -> {
//            // Handle item click, e.g., open edit dialog
//            Toast.makeText(this, "Edit " + item, Toast.LENGTH_SHORT).show();
//        });

        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new HorizontalScrollAdapter(itemList);
        horizontalRecyclerView.setAdapter(adapter);


        //request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }


        //select new profile image
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        //load user infomation
        loadUserProfile();

        //load game information
        loadGameInformation();

        //load sport information
        loadSportInformation();

        // Add data for sport skill level
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, AddSport.class);
                startActivity(intent);
            }
        });

        //Logout Button
        Button logoutButton = findViewById(R.id.out);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // You may also want to navigate to the login screen or perform other actions after logout
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                finish(); // Close the current activity to prevent going back to it using the back button
            }
        });

        //home button
        ImageView homePageBtn = findViewById(R.id.homePageBtn);

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, HomePage.class);
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
                Toast.makeText(Profile.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //book button
        ImageView learnBtn = findViewById(R.id.learnBtn);

        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, LearningPage.class);
                startActivity(intent);
            }
        });

        //profile button below
        ImageView ProfileBtn = findViewById(R.id.ProfileBtn);

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Profile.class);
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
                Toast.makeText(Profile.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadSportInformation() {
        // Assuming you have a 'items' collection in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("skill_levels")
                .whereEqualTo("userId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Clear the existing itemList
                    itemList.clear();

                    // Add each item from the Firestore query result to the itemList
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String selectedSkillLevel = document.getString("selectedSkillLevel");
                        String selectedSport = document.getString("selectedSport");
                        itemList.add(selectedSport);
                        itemList.add(selectedSkillLevel);
                    }

                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged();

                    // If itemList is empty, add a placeholder item
                    if (itemList.isEmpty()) {
                        itemList.add("No data");
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "Error getting documents: ", e));
    }

    //load create game information
    private void loadGameInformation() {
        db.collection("createGame")
                .whereEqualTo("userId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Map<String, Object> data = document.getData();
                            String dateString = (String) document.getData().get("date");
                            SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
                            Date date = null;
                            try {
                                date = format.parse(dateString);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            // Check if game date has passed
                            if (date.before(new Date())) {
                                mAdapter.addGamesRecord(new GameRecord(
                                        (String) data.get("sportType"),
                                        (String) data.get("gameSkill"),
                                        (String) data.get("location"),
                                        (String) data.get("address"),
                                        date,
                                        (String) data.get("startTime"),
                                        (String) data.get("endTime"),
                                        true // Indicates that the game date has passed
                                ));
                            } else {
                                mAdapter.addGamesRecord(new GameRecord(
                                        (String) data.get("sportType"),
                                        (String) data.get("gameSkill"),
                                        (String) data.get("location"),
                                        (String) data.get("address"),
                                        date,
                                        (String) data.get("startTime"),
                                        (String) data.get("endTime"),
                                        false // Indicates that the game date has not passed
                                ));
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Profile.this, "No Game", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void loadUserProfile() {
        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("username");
                            String email = documentSnapshot.getString("email");
                            String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                            String location = documentSnapshot.getString("location");

                            nameText.setText(name);
                            emailText.setText(email);

                            // Check if location exists, if not, set a default value
                            if (location == null || location.isEmpty()) {
                                location = "Unknown";
                            }
                            locationText.setText(location);

                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                Glide.with(Profile.this).load(profileImageUrl).into(avatarImage);
                            } else {
                                Glide.with(Profile.this).load(R.drawable.profile_png).into(avatarImage);
                            }
                        } else {
                            Log.d("ProfileActivity", "No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ProfileActivity", "Error getting document", e);
                    }
                });
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, IMAGE_PICKER_REQUEST);
        } else {
            Toast.makeText(this, "No app can handle image picking", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }


    //upload profile image to firebase storage
    private void uploadImageToFirebase(Uri imageUri) {
        String userId = auth.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("avatars/" + userId + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                saveImageUrlToDatabase(downloadUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ProfileActivity", "Failed to upload image", e);
                    }
                });
    }

    //save profile url to database
    private void saveImageUrlToDatabase(String downloadUrl) {
        db.collection("users").document(auth.getCurrentUser().getUid())
                .update("profileImageUrl", downloadUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ProfileActivity", "Profile image URL updated successfully");
                        loadUserProfile(); // Reload the user profile to display the new image
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ProfileActivity", "Error updating profile image URL", e);
                    }
                });
    }

    // proceed the location request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "need permission of location to save the location information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //update location
    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Convert latitude and longitude to a human-readable address
                Geocoder geocoder = new Geocoder(Profile.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        String locationName = address.getLocality() + ", " + address.getAdminArea(); // Example: Kampar, Perak
                        locationText.setText(locationName); // Display the location name in your TextView
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // doesn't have the permission of location
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}