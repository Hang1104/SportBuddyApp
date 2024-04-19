package my.edu.utar.assignment2.RegisterSignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import my.edu.utar.assignment2.GameDetails;
import my.edu.utar.assignment2.Home;
import my.edu.utar.assignment2.MainActivity;
import my.edu.utar.assignment2.ProfilePage.Profile;
import my.edu.utar.assignment2.R;
import my.edu.utar.assignment2.createGame.CreateGame;

public class Login extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLog;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView registerbutton, resetbutton;

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
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLog = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        registerbutton = findViewById(R.id.RegisterTextView);
        resetbutton = findViewById(R.id.resetpass);

        //Navigate to Login Page Listener
        registerbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Navigate to the register page
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Login.this, ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthInvalidUserException) {
                                        // User not found
                                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        // Incorrect password
                                        Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_LONG).show();
                                    } else if (!isValidEmail(email)) {
                                        // Validate email format
                                            Toast.makeText(Login.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                                    } else if (TextUtils.isEmpty(email)) {
                                        Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                                    } else if (TextUtils.isEmpty(password)) {
                                        Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(Login.this, "Authentication failed." + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        });
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void onRegisterClicked(View view) {
        // Navigate to the register page
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}