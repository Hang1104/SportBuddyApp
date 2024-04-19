package my.edu.utar.assignment2.ProfilePage;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import my.edu.utar.assignment2.R;

public class AddSport extends AppCompatActivity {

    private EditText usernameEditText, locationEditText;
    private Spinner sportSpinner, skillLevelSpinner;
    private Button saveButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);

        // Initialization
        sportSpinner = findViewById(R.id.sportSpinner);
        skillLevelSpinner = findViewById(R.id.skillLevelSpinner);
        saveButton = findViewById(R.id.saveButton);

        // 设置运动类型和技能水平的 Spinner
        ArrayAdapter<CharSequence> sportAdapter = ArrayAdapter.createFromResource(this,
                R.array.sports, android.R.layout.simple_spinner_item);
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(sportAdapter);

        ArrayAdapter<CharSequence> skillLevelAdapter = ArrayAdapter.createFromResource(this,
                R.array.skill_levels, android.R.layout.simple_spinner_item);
        skillLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skillLevelSpinner.setAdapter(skillLevelAdapter);

        // click to save the info
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedSport = sportSpinner.getSelectedItem().toString();
                String selectedSkillLevel = skillLevelSpinner.getSelectedItem().toString();

                //update information of user
                checkAndCreateSkillLevelCollection(selectedSport, selectedSkillLevel);

                // Return to profile page
                Intent intent = new Intent(AddSport.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkAndCreateSkillLevelCollection(String selectedSport, String selectedSkillLevel) {
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a reference to the "skill_levels" collection in the database
        CollectionReference skillLevelsRef = FirebaseFirestore.getInstance().collection("skill_levels");

        // Create a document with a random ID in the "skill_levels" collection
        Map<String, Object> skillLevelData = new HashMap<>();
        skillLevelData.put("userId", userId);
        skillLevelData.put("selectedSport", selectedSport);
        skillLevelData.put("selectedSkillLevel", selectedSkillLevel);

        skillLevelsRef.add(skillLevelData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Skill level data added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding skill level data", e);
                    }
                });
    }

}