package my.edu.utar.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import my.edu.utar.assignment2.RegisterSignIn.Login;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.buddylogo).into(iv);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            navigateToHomePage();
        }
        else{
            navigateToLoginPage();
        }

        // Delay for 3 seconds before navigating to the main activity
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this, Login.class);
//                startActivity(intent);
//                finish(); // Finish the splash activity so it can't be returned to
//            }
//        }, 2000); // 1500 milliseconds = 1.5 seconds delay
    }
    private void navigateToHomePage() {
        // Delay for 3 seconds before navigating to the home activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                finish(); // Finish the splash activity so it can't be returned to
            }
        }, 2000); // 2000 milliseconds = 2 seconds delay
    }
    
    private void navigateToLoginPage() {
        // Delay for 3 seconds before navigating to the login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish(); // Finish the splash activity so it can't be returned to
            }
        }, 2000); // 2000 milliseconds = 2 seconds delay
    }
}