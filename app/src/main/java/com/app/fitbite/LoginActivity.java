package com.app.fitbite;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsernameEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textForgotPassword, textRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        editTextUsernameEmail = findViewById(R.id.editTextUsernameEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textForgotPassword = findViewById(R.id.textForgotPassword);
        textRegister = findViewById(R.id.textRegister);

        // Set up button clicks
        buttonLogin.setOnClickListener(v -> loginUser());
        textForgotPassword.setOnClickListener(v -> openForgotPasswordActivity());
        textRegister.setOnClickListener(v -> openRegisterActivity());
    }

    private void loginUser() {
        String usernameEmail = editTextUsernameEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validation checks
        if (TextUtils.isEmpty(usernameEmail) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Masukkan Email dan Kata Sandi Anda", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the input is email or username
        if (isEmail(usernameEmail)) {
            // Login using email
            loginWithEmail(usernameEmail, password);
        } else {
            // Login using username (fetch the email from Firestore)
            loginWithUsername(usernameEmail, password);
        }
    }

    // Check if the input is an email
    private boolean isEmail(String usernameEmail) {
        return usernameEmail.contains("@");
    }

    // Login with email
    private void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Login with username (fetch the email from Firestore)
    private void loginWithUsername(String username, String password) {
        firestore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Fetch email from Firestore document
                        String email = task.getResult().getDocuments().get(0).getString("email");

                        if (email != null) {
                            // Login using the email
                            loginWithEmail(email, password);
                        } else {
                            Toast.makeText(LoginActivity.this, "Username not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Username not found.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openForgotPasswordActivity() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    private void openRegisterActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}
