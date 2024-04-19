package my.edu.utar.assignment2.ProfilePage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.assignment2.R;

public class EditProfile extends AppCompatActivity {

    private EditText usernameEditText, locationEditText;
    private Spinner sportSpinner, skillLevelSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialization
        usernameEditText = findViewById(R.id.usernameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        sportSpinner = findViewById(R.id.sportSpinner);
        skillLevelSpinner = findViewById(R.id.skillLevelSpinner);
        saveButton = findViewById(R.id.saveButton);

        //retrieve the current info from database and show int he page
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String username = user.getDisplayName();
            String location = ""; // retrieve the location from firebase
            usernameEditText.setText(username);
            locationEditText.setText(location);
        }

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
                String newUsername = usernameEditText.getText().toString();
                String newLocation = locationEditText.getText().toString();
                String selectedSport = sportSpinner.getSelectedItem().toString();
                String selectedSkillLevel = skillLevelSpinner.getSelectedItem().toString();

                //update information of user
                updateUserInfo(newUsername, newLocation, selectedSport, selectedSkillLevel);
            }
        });
    }

    private void updateUserInfo(String newUsername, String newLocation, String selectedSport, String selectedSkillLevel) {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        if (users != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(users.getUid());

            // update username
            userRef.child("username").setValue(newUsername);

            //update location
            userRef.child("location").setValue(newLocation);

            // 更新选择的运动类型和技能级别
            userRef.child("selectedSport").setValue(selectedSport);
            userRef.child("selectedSkillLevel").setValue(selectedSkillLevel);

        } else {
            // 用户未登录，处理未登录情况
        }
    }

}