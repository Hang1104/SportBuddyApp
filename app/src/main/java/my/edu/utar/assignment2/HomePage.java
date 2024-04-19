package my.edu.utar.assignment2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomePage extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private LocationManager locationManager;
    private String currentLocationName;
    private TextView currentLocationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //location
        currentLocationTextView = findViewById(R.id.Home_currentLocationTextView);
        checkLocationPermission();

        //profile button
        ImageView profileImageView = findViewById(R.id.profileimageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, ProfileActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //create game button
        ImageView createGameButton = findViewById(R.id.Home_creategameBtn);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, CreateGameActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //show all game nearby
        TextView showAllBtn = findViewById(R.id.Home_showallBtn);

        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, ShowAllActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //game detail
        Button detailBtn = findViewById(R.id.Home_detailBtn);

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the GameDetailActivity
//                Intent intent = new Intent(HomePage.this, GameDetailActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //home button
        ImageView homePageBtn = findViewById(R.id.homePageBtn);

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, HomePage.class);
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
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //book button
        ImageView learnBtn = findViewById(R.id.learnBtn);

        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, CreateGameActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //profile button below
        ImageView ProfileBtn = findViewById(R.id.ProfileBtn);

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, CreateGameActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        //Community page
        Button CommunityBtn = findViewById(R.id.Home_CommunityBtn);

        CommunityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, CreateGameActivity.class);
//                startActivity(intent);
                Toast.makeText(HomePage.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Check location permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Handle location change

        // Display latitude and longitude in a Toast message
        Toast.makeText(this, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                currentLocationName = addresses.get(0).getAddressLine(0);
                updateLocationUI(currentLocationName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disabled
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changed
    }

    private void updateLocationUI(String locationName) {
        currentLocationTextView.setText(getString(R.string.current_location, locationName));
        Log.d("Location", "Current location: " + locationName);
    }


//    public void onCurrentLocationClicked(View view) {
//        // Redirect user to the location input page
//    }
}