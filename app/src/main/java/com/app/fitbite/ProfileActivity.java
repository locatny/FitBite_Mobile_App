package com.app.fitbite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imageProfile, imageSettings;
    private TextView textUserName, textUserEmail;
    private CardView cardAddWeight, cardFood;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Bind views
        imageProfile = findViewById(R.id.imageProfile);
        imageSettings = findViewById(R.id.imageSettings);
        textUserEmail = findViewById(R.id.textUserEmail);
        cardAddWeight = findViewById(R.id.cardAddWeight);
        cardFood = findViewById(R.id.cardFood);

        // Get current logged in user from Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Set User info in TextViews if user is logged in
        if (currentUser != null) {
            textUserEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "Email Tidak Tersedia");
        }

        // Click on settings icon to go to SettingsActivity
        imageSettings.setOnClickListener(v -> {
            // Open the settings activity
            startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
        });

        // Click on "Berat Saya" CardView to go to AddWeightActivity
        cardAddWeight.setOnClickListener(v -> {
            // Open AddWeightActivity
            startActivity(new Intent(ProfileActivity.this, AddWeightActivity.class));
        });

        // Click on "Makanan Saya" CardView to go to FoodActivity
        cardFood.setOnClickListener(v -> {
            // Open FoodActivity
            startActivity(new Intent(ProfileActivity.this, AddFoodActivity.class));
        });
    }
}
