package my.edu.utar.assignment2.createGame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.assignment2.HomePage;
import my.edu.utar.assignment2.LearningPage.LearningPage;
import my.edu.utar.assignment2.ProfilePage.Profile;
import my.edu.utar.assignment2.R;

public class GameList extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView gamesListView;
    private List<String> gamesList;
    private List<String> profileImageUrls;
    private List<String> gamesID;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        gamesListView = findViewById(R.id.gamesListView);
        gamesList = new ArrayList<>();
        gamesID = new ArrayList<>();
        profileImageUrls = new ArrayList<>();

        // Initialize adapter
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_game, R.id.gameDetailsTextView, gamesList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Get views
                TextView gameDetailsTextView = view.findViewById(R.id.gameDetailsTextView);
                ImageView profileImageView = view.findViewById(R.id.profileImageView);

                // Set game details text
                gameDetailsTextView.setText(gamesList.get(position));

                // Load profile image
                String profileImageUrl = profileImageUrls.get(position);
                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                    Picasso.get().load(profileImageUrl).into(profileImageView);
                }

                return view; // Return the view

            }
        };
        // Set adapter after initializing
        gamesListView.setAdapter(adapter);
        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gameId = gamesID.get(position); // Assuming gamesList contains document IDs
                Intent intent = new Intent(GameList.this, GameDetails.class);
                intent.putExtra("gameId", gameId);
                startActivity(intent);
            }
        });
        fetchGamesList();

        // Home button
        ImageView homePageBtn = findViewById(R.id.homePageBtn);
        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameList.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Chat button
        ImageView chatBtn = findViewById(R.id.Home_CommunityBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameList.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        // Book button
        ImageView learnBtn = findViewById(R.id.learnBtn);
        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameList.this, LearningPage.class);
                startActivity(intent);
            }
        });

        // Profile button
        ImageView ProfileBtn = findViewById(R.id.ProfileBtn);
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameList.this, Profile.class);
                startActivity(intent);
            }
        });

    }


    private void fetchGamesList() {
        db.collection("createGame")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        gamesList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String gameId = document.getId();
                            String sportType = document.getString("sportType");
                            String location = document.getString("location");
                            String address = document.getString("address");
                            String date = document.getString("date");
                            String startTime = document.getString("startTime");
                            String endTime = document.getString("endTime");
                            String gameSkill = document.getString("gameSkill");
                            String userId = document.getString("userId");
                            String maxPlayers = String.valueOf(document.getLong("maxPlayers"));

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

                                                profileImageUrls.add(profileImageUrl);

                                                String gameDetails = username + "\n" + "\n" +
                                                        sportType + "\n" +
                                                        initCapLocation + "\n" +
                                                        date + ", " + startTime + " - " + endTime + "\n" +
                                                        gameSkill + "\n" +
                                                        "Max Players: " + maxPlayers;

                                                gamesList.add(gameDetails);
                                                gamesID.add(gameId);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Log.d("fetchGamesList", "User document does not exist");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("fetchGamesList", "Error getting user document", e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GameList.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

}
