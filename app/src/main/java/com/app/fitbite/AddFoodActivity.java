package com.app.fitbite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddFoodActivity extends AppCompatActivity {

    private EditText editTextFoodName, editTextCalories;
    private Button buttonSaveFood;
    private ImageView imageBack;

    private DatabaseReference foodsRef;
    private String userId;
    private String todayKey; // format yyyy-MM-dd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Inisialisasi Firebase dan mendapatkan key untuk hari ini
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        foodsRef = FirebaseDatabase.getInstance().getReference("foods").child(userId).child(todayKey);

        // Bind Views
        editTextFoodName = findViewById(R.id.editTextFoodName);
        editTextCalories = findViewById(R.id.editTextCalories);
        buttonSaveFood = findViewById(R.id.buttonSaveFood);
        imageBack = findViewById(R.id.imageBack);

        // Kembali ke halaman Home
        imageBack.setOnClickListener(v -> {
            finish(); // Menutup AddFoodActivity dan kembali ke MainActivity
        });

        // Menyimpan Makanan
        buttonSaveFood.setOnClickListener(v -> {
            String foodName = editTextFoodName.getText().toString().trim();
            String calories = editTextCalories.getText().toString().trim();

            if (foodName.isEmpty() || calories.isEmpty()) {
                // Tampilkan pesan error jika ada field yang kosong
                Toast.makeText(AddFoodActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int calorieValue = Integer.parseInt(calories);  // Cek apakah calories adalah angka
            } catch (NumberFormatException e) {
                Toast.makeText(AddFoodActivity.this, "Please enter a valid number for calories.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data ke Firebase dengan struktur yang sama seperti MainActivity
            String foodId = foodsRef.push().getKey();
            if (foodId != null) {
                Food food = new Food(foodName, calories);
                foodsRef.child(foodId).setValue(food)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddFoodActivity.this, "Makanan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                            // Kembali ke halaman utama setelah simpan
                            startActivity(new Intent(AddFoodActivity.this, MainActivity.class));
                            finish(); // Menutup halaman ini agar tidak kembali ke AddFoodActivity
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddFoodActivity.this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}