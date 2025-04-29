package com.app.fitbite;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddWeightActivity extends AppCompatActivity {

    private EditText editTextCurrentWeight, editTextInitialWeight, editTextTargetWeight;
    private Button buttonSaveWeight;
    private ImageButton buttonBack;

    private DatabaseReference weightRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);

        editTextCurrentWeight = findViewById(R.id.editTextCurrentWeight);
        editTextInitialWeight = findViewById(R.id.editTextInitialWeight);
        editTextTargetWeight = findViewById(R.id.editTextTargetWeight);
        buttonSaveWeight = findViewById(R.id.buttonSaveWeight);
        buttonBack = findViewById(R.id.buttonBack);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        weightRef = FirebaseDatabase.getInstance().getReference("weight").child(userId);

        buttonSaveWeight.setOnClickListener(v -> saveWeightData());

        buttonBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveWeightData() {
        String currentWeightStr = editTextCurrentWeight.getText().toString();
        String initialWeightStr = editTextInitialWeight.getText().toString();
        String targetWeightStr = editTextTargetWeight.getText().toString();

        if (TextUtils.isEmpty(currentWeightStr) || TextUtils.isEmpty(initialWeightStr) || TextUtils.isEmpty(targetWeightStr)) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        double currentWeight = Double.parseDouble(currentWeightStr);
        double initialWeight = Double.parseDouble(initialWeightStr);
        double targetWeight = Double.parseDouble(targetWeightStr);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        WeightData weightData = new WeightData(initialWeight, currentWeight, targetWeight, date);

        // Push data ke Firebase
        weightRef.push().setValue(weightData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddWeightActivity.this, "Data berat badan berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddWeightActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                });
    }
}
