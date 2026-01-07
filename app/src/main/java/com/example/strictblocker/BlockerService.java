package com.example.strictblocker;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

public class BlockerService extends AccessibilityService {

    // أضف هنا أسماء حزم التطبيقات التي تريد حظرها
    private final List<String> BLOCKED_PACKAGES = Arrays.asList(
            "com.facebook.katana",
            "com.instagram.android",
            "com.tiktok.android"
            // "com.android.settings" // احذر من تفعيل هذا السطر أثناء التجربة
    );

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null) {
                String currentPackage = event.getPackageName().toString();
                
                if (isBlockActive() && BLOCKED_PACKAGES.contains(currentPackage)) {
                    performGlobalAction(GLOBAL_ACTION_HOME);
                    Toast.makeText(this, "ممنوع!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isBlockActive() {
        SharedPreferences prefs = getSharedPreferences("BlockerPrefs", MODE_PRIVATE);
        long unlockTime = prefs.getLong("UNLOCK_TIME", 0);
        return System.currentTimeMillis() < unlockTime;
    }

    @Override
    public void onInterrupt() { }
}
