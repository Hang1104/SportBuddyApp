package my.edu.utar.assignment2.RegisterSignIn;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import my.edu.utar.assignment2.MainActivity;
import my.edu.utar.assignment2.R;
import my.edu.utar.assignment2.createGame.CreateGame;

public class Register extends AppCompatActivity {

    EditText editTextEmail, editUsername, editTextPassword, editTextPassword2;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView loginbutton;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editUsername = findViewById(R.id.editUsername);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPassword2 = findViewById(R.id.password2);
        buttonReg = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        loginbutton = findViewById(R.id.LoginTextView);

        //default image link
        String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/sport-buddy-14ab2.appspot.com/o/default_profile.jpg?alt=media&token=7b0131b5-8885-41f2-ac1a-fc1edf922644";


        //Navigate to Login Page Listener
        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Navigate to the register page
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        //navigate to Register Listener
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String username, email, password, confirmpass;
                username = String.valueOf(editTextEmail.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confirmpass = String.valueOf(editTextPassword2.getText());

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmpass)) {
                    Toast.makeText(Register.this, "Enter confirmed password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmpass)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // User authentication successful, navigate to verification page
                                        Intent intent = new Intent(Register.this, VerificationPage.class);
                                        startActivity(intent);
                                        finish();

                                        // Update user information in Cloud Firestore
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        String userId = user.getUid();
                                        Log.d("UUUSSER ID", userId);
                                        String username = editUsername.getText().toString();
                                        String email = editTextEmail.getText().toString();

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("username", username);
                                        userMap.put("email", email);
                                        userMap.put("profileurl", defaultImageUrl);

                                        db.collection("users")
                                                .document(userId)
                                                .set(userMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                                        Intent intent = new Intent(Register.this, CreateGame.class);
                                                        intent.putExtra("userId", mAuth.getCurrentUser().getUid());

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed." + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });
    }
}