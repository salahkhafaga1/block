package com.example.strictblocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnLock30Days, btnPermission;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLock30Days = findViewById(R.id.btnLock);
        btnPermission = findViewById(R.id.btnPermission);
        txtStatus = findViewById(R.id.txtStatus);

        updateStatus();

        btnPermission.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        });

        btnLock30Days.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("BlockerPrefs", MODE_PRIVATE);
            // تم ضبط الوقت لدقيقتين فقط للتجربة (2 * 60 * 1000)
            // لجعله 30 يوم غير الرقم لـ: 30L * 24 * 60 * 60 * 1000
            long durationInMillis = 2 * 60 * 1000; 
            long targetTime = System.currentTimeMillis() + durationInMillis;

            prefs.edit().putLong("UNLOCK_TIME", targetTime).apply();
            
            updateStatus();
            finish(); 
        });
    }

    private void updateStatus() {
        SharedPreferences prefs = getSharedPreferences("BlockerPrefs", MODE_PRIVATE);
        long unlockTime = prefs.getLong("UNLOCK_TIME", 0);

        if (System.currentTimeMillis() < unlockTime) {
            txtStatus.setText("⛔ الحظر مفعل!");
            btnLock30Days.setEnabled(false);
        } else {
            txtStatus.setText("✅ أنت حر حالياً.");
        }
    }
}
