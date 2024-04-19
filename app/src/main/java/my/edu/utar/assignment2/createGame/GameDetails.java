package my.edu.utar.assignment2.createGame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.edu.utar.assignment2.R;

public class GameDetails extends AppCompatActivity {
    private FirebaseFirestore db;

    private String gameId;
    private Button joinNowButton;
    private Button editButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        db = FirebaseFirestore.getInstance();

        // Retrieve gameId from intent
        String gameId = getIntent().getStringExtra("gameId");

        String latestGameId = getIntent().getStringExtra("latestGameId");

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Find the ImageButton in the layout
        ImageButton backButton = findViewById(R.id.backButton);
        // Set onClick listener for the ImageButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate back to the GameList activity
                Intent intent = new Intent(GameDetails.this, GameList.class);

                // Start the GameList activity
                startActivity(intent);

                // Finish the current activity (GameDetails) to prevent going back to it when pressing back
                finish();
            }
        });

        Button joinNowButton = findViewById(R.id.joinNowButton);

        // Set onClick listener for the "Join Now" button
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the user ID to the list of joined players
                joinGame(gameId);
            }
        });

        // Find TextViews in the layout
        TextView sportTypeTextView = findViewById(R.id.sportTypeTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView startTimeTextView = findViewById(R.id.startTimeTextView);
        TextView endTimeTextView = findViewById(R.id.endTimeTextView);
        TextView gameSkillTextView = findViewById(R.id.gameSkillTextView);


        // Retrieve and display game details
        if (gameId != null) {
            getHostDetails(gameId);
            getGameDetails(gameId, sportTypeTextView, locationTextView, addressTextView, dateTextView, startTimeTextView, endTimeTextView, gameSkillTextView);


            displayJoinedPlayers(gameId);
        }
    }

    private void getGameDetails(String gameId, TextView sportTypeTextView, TextView locationTextView, TextView addressTextView, TextView dateTextView, TextView startTimeTextView, TextView endTimeTextView, TextView gameSkillTextView) {
        Log.d("GetGameDetails", "getGameDetails method called");
        DocumentReference docRef = db.collection("createGame").document(gameId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String sportType = documentSnapshot.getString("sportType");
                    String location = documentSnapshot.getString("location");
                    String address = documentSnapshot.getString("address");
                    String date = documentSnapshot.getString("date");
                    String startTime = documentSnapshot.getString("startTime");
                    String endTime = documentSnapshot.getString("endTime");
                    String gameSkill = documentSnapshot.getString("gameSkill");

                    String initCapLocation = toInitCap(location);

                    // Update TextViews with game details
                    sportTypeTextView.setText(sportType);
                    locationTextView.setText(initCapLocation);
                    addressTextView.setText(address);
                    dateTextView.setText(date);
                    startTimeTextView.setText(startTime);
                    endTimeTextView.setText(endTime);
                    gameSkillTextView.setText(gameSkill);

                    // Set sport type icon based on retrieved sport type
                    setSportTypeIcon(sportType);
                } else {
                    // Handle case when document does not exist
                    Log.d("GetGameDetails", "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("GetGameDetails", "Error getting game details", e);
            }
        });
    }

    private void joinGame(String gameId) {
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Add the user ID to the list of joined players in the database
        db.collection("createGame").document(gameId).collection("joinedPlayers")
                .document(userId)
                .set(new HashMap<String, Object>())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Player joined successfully
                        Toast.makeText(GameDetails.this, "Joined the game successfully!", Toast.LENGTH_SHORT).show();

                        // Refresh the UI to display the updated list of joined players
                        // Call a method to display the list of joined players
                        displayJoinedPlayers(gameId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while joining the game
                        Log.e("JoinGame", "Error joining the game", e);
                        Toast.makeText(GameDetails.this, "Failed to join the game. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayJoinedPlayers(String gameId) {
        // Retrieve the list of joined players from the database
        db.collection("createGame").document(gameId).collection("joinedPlayers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> joinedPlayersList = new ArrayList<>();
                        List<String> profileImageUrls = new ArrayList<>(); // List to store profile image URLs

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String userId = documentSnapshot.getId();
                            fetchPlayerInfo(userId, joinedPlayersList, profileImageUrls);
                        }

                        // Display the list of joined players in the UI
                        // Update UI to display usernames and profile images
                        updateJoinedPlayersUI(joinedPlayersList, profileImageUrls);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while fetching the list of joined players
                        Log.e("DisplayJoinedPlayers", "Error fetching joined players", e);
                        Toast.makeText(GameDetails.this, "Failed to fetch joined players. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
//
    private void fetchPlayerInfo(String userId, List<String> joinedPlayersList, List<String> profileImageUrls) {
        // Fetch player information from Firestore based on user ID
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Retrieve username and profile image URL
                            String username = documentSnapshot.getString("username");
                            String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                            // Add username and profile image URL to the respective lists
                            joinedPlayersList.add(username);
                            profileImageUrls.add(profileImageUrl);

                            updateJoinedPlayersUI(joinedPlayersList, profileImageUrls);
                        } else {
                            Log.d("FetchPlayerInfo", "User document does not exist for user ID: " + userId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FetchPlayerInfo", "Error fetching player information for user ID: " + userId, e);
                    }
                });
    }
    private void updateJoinedPlayersUI(List<String> joinedPlayersList, List<String> profileImageUrls) {
        // Update the UI to display the list of joined players with usernames and profile images
        // You can use RecyclerView or other UI components to display a list of players with their profile images
        // For simplicity, I'll just update a TextView here

        TextView joinedPlayersTextView = findViewById(R.id.joinedPlayersTextView);
        ImageView playerProfileImageView = findViewById(R.id.playerProfileImageView);

        for (int i = 0; i < joinedPlayersList.size(); i++) {
            String username = joinedPlayersList.get(i);
            String profileImageUrl = profileImageUrls.get(i);

            // Append username to the TextView
            joinedPlayersTextView.setText(username);

            // Load and display profile image using Picasso or any other image loading library
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                Picasso.get().load(profileImageUrl).placeholder(R.drawable.profile).into(playerProfileImageView);
                }
            }
            // Append a newline character after each username
            joinedPlayersTextView.append("\n");
        }



    // Method to get host details
    private void getHostDetails(String gameId) {
        db.collection("createGame").document(gameId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String hostId = documentSnapshot.getString("userId");
                            fetchHostInfo(hostId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GetHostDetails", "Error getting host details", e);
                    }
                });
    }

    // Method to fetch host information from Firestore
    private void fetchHostInfo(String hostId) {
        db.collection("users").document(hostId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Retrieve host's username and profile picture URL
                            String username = documentSnapshot.getString("username");
                            String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                            // Update UI with host's username and profile picture
                            updateHostInfo(username, profileImageUrl);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FetchHostInfo", "Error fetching host information", e);
                    }
                });
    }

    // Method to update UI with host's username and profile picture
    private void updateHostInfo(String username, String profileImageUrl) {
        // Find views
        TextView hostNameTextView = findViewById(R.id.hostNameTextView);
        ImageView hostProfileImageView = findViewById(R.id.hostProfileImageView);

        // Update views
        hostNameTextView.setText(username);
        // Load profile image using Picasso or any other image loading library
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Picasso.get().load(profileImageUrl).placeholder(R.drawable.profile).into(hostProfileImageView);
        }
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

    private void setSportTypeIcon(String sportType) {
        ImageView sportTypeIcon = findViewById(R.id.sportTypeIcon);

        // Set sport type icon based on sport type
        if (sportType != null) {
            switch (sportType.toLowerCase()) {
                case "badminton":
                    sportTypeIcon.setImageResource(R.drawable.ic_badminton);
                    break;
                case "swimming":
                    sportTypeIcon.setImageResource(R.drawable.ic_swimming);
                    break;
                case "futsal":
                    sportTypeIcon.setImageResource(R.drawable.ic_futsal);
                    break;
                case "ping pong":
                    sportTypeIcon.setImageResource(R.drawable.ic_pingpong);
                    break;
                case "gym":
                    sportTypeIcon.setImageResource(R.drawable.ic_gym);
                    break;
                case "bowling":
                    sportTypeIcon.setImageResource(R.drawable.ic_bowling);
                    break;
                default:
                    sportTypeIcon.setImageResource(R.drawable.ic_default_sport);
                    break;
            }
        }
    }


}
