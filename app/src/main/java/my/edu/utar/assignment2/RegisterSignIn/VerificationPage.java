package my.edu.utar.assignment2.RegisterSignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import my.edu.utar.assignment2.MainActivity;
import my.edu.utar.assignment2.R;

public class VerificationPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button verifyButton;
    ProgressBar progressBar;
    TextView textViewVerificationLink , resendLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        mAuth = FirebaseAuth.getInstance();

        verifyButton = findViewById(R.id.verifyButton);
        progressBar = findViewById(R.id.progressBar);
        resendLinkTextView = findViewById(R.id.resendLinkTextView);
        TextView textViewVerificationLink = findViewById(R.id.textViewVerificationLink);
        String verificationLink = "Verification link was sent to " + mAuth.getCurrentUser().getEmail();
        textViewVerificationLink.setText(verificationLink);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Check if the user is already verified
            if (user.isEmailVerified()) {
                // User is already verified, navigate to login activity
                startActivity(new Intent(VerificationPage.this, Login.class));
                finish();
            }
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (user.isEmailVerified()) {
                                    // User is verified, navigate to main activity
                                    startActivity(new Intent(VerificationPage.this, MainActivity.class));
                                    finish();
                                } else {
                                    // User is not verified
                                    Toast.makeText(VerificationPage.this, "Email not verified yet.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Failed to reload user
                                Toast.makeText(VerificationPage.this, "Failed to reload user.", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        resendLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(VerificationPage.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(VerificationPage.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}