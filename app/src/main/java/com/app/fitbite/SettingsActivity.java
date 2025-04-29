package com.app.fitbite;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword;
    private Button buttonSaveSettings;
    private ImageButton buttonBack;

    private DatabaseReference userRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Bind views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        buttonBack = findViewById(R.id.buttonBack);

        // Populate fields with current user data if available
        loadUserData();

        // Handle the back button click to return to the previous activity
        buttonBack.setOnClickListener(v -> {
            finish();  // This will take the user back to the previous activity
        });

        // Handle saving updated settings
        buttonSaveSettings.setOnClickListener(v -> {
            saveUserSettings();
        });
    }

    private void loadUserData() {
        // Populate the EditText fields with current data from Firebase
        // Assuming user data is already available in Firebase, you could load it here
    }

    private void saveUserSettings() {
        // Get the updated data
        String updatedName = editTextName.getText().toString();
        String updatedEmail = editTextEmail.getText().toString();
        String updatedPassword = editTextPassword.getText().toString();

        // Validation checks
        if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update data in Firebase
        userRef.child("name").setValue(updatedName);
        userRef.child("email").setValue(updatedEmail);

        // Optionally, update the password via Firebase Authentication
        if (!updatedPassword.isEmpty()) {
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(updatedPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingsActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Optionally, save the updated email in Firebase Authentication (this is for email change only)
        FirebaseAuth.getInstance().getCurrentUser().updateEmail(updatedEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                    }
                });

        // Notify user
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}
