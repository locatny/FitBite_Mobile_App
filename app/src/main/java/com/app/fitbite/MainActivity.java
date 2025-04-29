package com.app.fitbite;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ImageView imageProfile;
    private Button buttonAddFood;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private BottomNavigationView bottomNavigationView;

    private DatabaseReference foodsRef;
    private String userId;
    private String todayKey; // format yyyy-MM-dd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase & get the key for today
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        foodsRef = FirebaseDatabase.getInstance().getReference("foods").child(userId).child(todayKey);

        // Bind views
        imageProfile = findViewById(R.id.imageProfile);
        buttonAddFood = findViewById(R.id.buttonAddFood);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Setup bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home); // Set Home sebagai default yang aktif

        // Setup RecyclerView
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodAdapter);

        // Click profile image → open ProfileActivity
        imageProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        // Click "Add Food" button → open AddFoodActivity
        buttonAddFood.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddFoodActivity.class)));

        // Load today's food data from Firebase
        loadTodayFoods();
    }

    private void loadTodayFoods() {
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Food food = child.getValue(Food.class);
                    if (food != null) {
                        food.setId(child.getKey());
                        foodList.add(food);
                    }
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error (for example, show a Toast)
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        // Periksa item yang dipilih dan navigasikan ke Activity yang sesuai
        if (itemId == R.id.nav_home) {
            // Kita sudah berada di MainActivity, tidak perlu melakukan apa-apa
            return true;
        } else if (itemId == R.id.nav_report) {
            startActivity(new Intent(MainActivity.this, ReportActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return true;
        } else if (itemId == R.id.nav_journal) {
            startActivity(new Intent(MainActivity.this, JournalActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return true;
        } else if (itemId == R.id.nav_menu) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return true;
        }

        return false;
    }
}