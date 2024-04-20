package my.edu.utar.assignment2.CommunityPage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import my.edu.utar.assignment2.R;


public class CreateNewPost extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;


    private ImageView uploadedImageView;
    private Button uploadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        //back button
        ImageView BackBtn = findViewById(R.id.backBtn);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewPost.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

        uploadedImageView = findViewById(R.id.uploadedImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);

        uploadImageButton.setOnClickListener(v -> {
            // Check if permission is granted
            if (ContextCompat.checkSelfPermission(CreateNewPost.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted
                openImagePicker();
            } else {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(CreateNewPost.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        });

        EditText postContentEditText = findViewById(R.id.postContentEditText);
        //post button
        Button postButton = findViewById(R.id.postButton);

        postButton.setOnClickListener(v -> {
            // Get post content, username, and image
            String postContent = postContentEditText.getText().toString();
            String username = "User";
            byte[] imageByteArray = getImageByteArray();

            // Insert post data into the database
            DatabaseHelper dbHelper = new DatabaseHelper(CreateNewPost.this);
            dbHelper.insertPost(username, postContent, imageByteArray);

            // Display toast message
            Toast.makeText(CreateNewPost.this, "Post successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the Community page
            Intent intent = new Intent(CreateNewPost.this, CommunityActivity.class);
            startActivity(intent);
        });
    }

    private byte[] getImageByteArray() {
        BitmapDrawable drawable = (BitmapDrawable) uploadedImageView.getDrawable();
        if (drawable != null && drawable.getBitmap() != null) {
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } else {
            // Handle the case when the drawable or bitmap is null
            return null;
        }
    }


    private void openImagePicker() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                uploadedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}