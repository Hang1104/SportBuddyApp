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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationInputView extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION_PERMISSION= 100;
    private LocationManager locationManager;
    private String currentLocationName;
    private TextView currentLocationTextView;

    private RecyclerView recyclerView;

    private SearchView searchView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input_view);

        //back button
        ImageView BackBtn = findViewById(R.id.LIV_backBtn);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationInputView.this, HomePage.class);
                startActivity(intent);
            }
        });
        //search view
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        // map API not functioning remove first
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        //gps location view
        View useCurrentlocationView = findViewById(R.id.useCurrentlocationView);

        useCurrentlocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermission();
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
                updateToFirebase(currentLocationName);
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
        if (currentLocationTextView != null) {
            currentLocationTextView.setText(getString(R.string.current_location, locationName));
            Log.d("Location", "Current location: " + locationName);
        } else {
            Log.e("Location", "currentLocationTextView is null");
        }
    }

    private void updateToFirebase(String location) {
        db.collection("users").document(auth.getCurrentUser().getUid())
                .update("location", location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        currentLocationName = location;
                        updateLocationUI(currentLocationName); // Update the UI to display the newly saved location
                        Log.d("saveLocationToFirestore", "Location updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("saveLocationToFirestore", "Error updating location", e);
                    }
                });
    }

}