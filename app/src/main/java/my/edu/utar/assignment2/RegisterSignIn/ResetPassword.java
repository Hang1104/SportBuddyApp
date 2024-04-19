package my.edu.utar.assignment2.RegisterSignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import my.edu.utar.assignment2.R;

public class ResetPassword extends AppCompatActivity {

    EditText resetemail;
    Button resetbutton;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        resetemail = findViewById(R.id.resetemail);
        resetbutton = findViewById(R.id.reset);

        resetbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = resetemail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPassword.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPassword.this, "Reset email sent", Toast.LENGTH_SHORT).show();
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(ResetPassword.this, Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 2000);
                                } else {
                                    Toast.makeText(ResetPassword.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}