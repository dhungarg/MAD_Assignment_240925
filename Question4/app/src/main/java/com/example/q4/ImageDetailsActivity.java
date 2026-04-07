package com.example.q4;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView detailImageView = findViewById(R.id.detailImageView);
        TextView tvImageName = findViewById(R.id.tvImageName);
        TextView tvImagePath = findViewById(R.id.tvImagePath);
        TextView tvImageSize = findViewById(R.id.tvImageSize);
        TextView tvImageDate = findViewById(R.id.tvImageDate);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Grab the image path that MainActivity sent to us
        String imagePath = getIntent().getStringExtra("IMAGE_PATH");

        if (imagePath != null) {
            File imageFile = new File(imagePath);

            // 1. Display the Full-Size Image
            detailImageView.setImageURI(Uri.fromFile(imageFile));

            // 2. Calculate and Display the Details
            tvImageName.setText("Name: " + imageFile.getName());
            tvImagePath.setText("Path: " + imageFile.getAbsolutePath());
            tvImageSize.setText("Size: " + (imageFile.length() / 1024) + " KB");

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            tvImageDate.setText("Date Taken: " + sdf.format(new Date(imageFile.lastModified())));

            // 3. The Delete Button Logic
            btnDelete.setOnClickListener(v -> {
                // Show the required Confirmation Dialog
                new AlertDialog.Builder(this)
                        .setTitle("Delete Image")
                        .setMessage("Are you sure you want to permanently delete this photo?")
                        .setPositiveButton("DELETE", (dialog, which) -> {
                            if (imageFile.delete()) {
                                Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK); // Tell the Grid to refresh
                                finish(); // Close this details screen
                            } else {
                                Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
            });
        }
    }
}
