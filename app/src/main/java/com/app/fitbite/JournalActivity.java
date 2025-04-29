package com.app.fitbite;

import static com.app.fitbite.R.id.textWeightGoal;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class JournalActivity extends AppCompatActivity {

    private TextView textWeightGoal, textCurrentWeight, textWeightChange, textWeightHistory;
    private RecyclerView recyclerViewWeightHistory;
    private WeightHistoryAdapter weightHistoryAdapter;
    private List<WeightData> weightHistoryList;

    private DatabaseReference weightRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        // Inisialisasi Firebase
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        weightRef = FirebaseDatabase.getInstance().getReference("weight").child(userId);

        // Inisialisasi view
        textWeightGoal = findViewById(R.id.textWeightGoal);
        textCurrentWeight = findViewById(R.id.textCurrentWeight);
        textWeightChange = findViewById(R.id.textWeightChange);
        textWeightHistory = findViewById(R.id.textWeightHistory);
        recyclerViewWeightHistory = findViewById(R.id.recyclerViewWeightHistory);

        // Setup RecyclerView
        weightHistoryList = new ArrayList<>();
        weightHistoryAdapter = new WeightHistoryAdapter(weightHistoryList);
        recyclerViewWeightHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewWeightHistory.setAdapter(weightHistoryAdapter);

        // Load data dari Firebase
        loadUserWeightData();
    }

    private void loadUserWeightData() {
        weightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                weightHistoryList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    WeightData weightData = snapshot.getValue(WeightData.class);
                    if (weightData != null) {
                        weightHistoryList.add(weightData);
                    }
                }

                // Tampilkan data terbaru di atas
                if (!weightHistoryList.isEmpty()) {
                    WeightData latestData = weightHistoryList.get(weightHistoryList.size() - 1);

                    textWeightGoal.setText("Target Berat: " + latestData.getTargetWeight() + " kg");
                    textCurrentWeight.setText("Berat Saat Ini: " + latestData.getCurrentWeight() + " kg");

                    double weightChange = latestData.getInitialWeight() - latestData.getCurrentWeight();
                    textWeightChange.setText("Sejauh Ini Berkurang: " + weightChange + " kg");
                } else {
                    // Jika belum ada data
                    textWeightGoal.setText("Target Berat: -");
                    textCurrentWeight.setText("Berat Saat Ini: -");
                    textWeightChange.setText("Sejauh Ini Berkurang: -");
                }

                // Update RecyclerView
                weightHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textWeightGoal.setText("Gagal mengambil data.");
            }
        });
    }
}
