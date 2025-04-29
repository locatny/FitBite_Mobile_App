package com.app.fitbite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private List<Food> foodList;
    private TextView totalCaloriesTextView;
    private ImageButton buttonBack;

    private DatabaseReference foodsRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Bind views
        recyclerView = findViewById(R.id.recyclerView);
        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView);
        buttonBack = findViewById(R.id.buttonBack);

        // Set up RecyclerView
        foodList = new ArrayList<>();
        reportAdapter = new ReportAdapter(foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reportAdapter);

        // Set up Firebase
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        foodsRef = FirebaseDatabase.getInstance().getReference("foods").child(userId);

        // Load food data
        loadFoodData();

        // Handle Back Button click
        buttonBack.setOnClickListener(v -> {
            finish(); // Kembali ke MainActivity
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void loadFoodData() {
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                foodList.clear();
                int totalCalories = 0;

                for (DataSnapshot child : snapshot.getChildren()) {
                    Food food = child.getValue(Food.class);
                    if (food != null) {
                        foodList.add(food);
                        // Sum calories
                        try {
                            totalCalories += Integer.parseInt(food.getCalories());
                        } catch (NumberFormatException e) {
                            Log.e("ReportActivity", "Invalid calories value: " + food.getCalories());
                        }
                    }
                }

                reportAdapter.notifyDataSetChanged();
                totalCaloriesTextView.setText("Total Calories: " + totalCalories);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
                Log.e("ReportActivity", "Database error: " + error.getMessage());
            }
        });
    }
}
