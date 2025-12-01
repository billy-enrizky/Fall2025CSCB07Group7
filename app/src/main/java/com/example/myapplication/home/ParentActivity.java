package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.home.DashboardFragment;
import com.example.myapplication.home.ChildrenFragment;
import com.example.myapplication.home.ProvidersFragment;
import com.example.myapplication.userdata.ParentAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParentActivity extends AppCompatActivity {
    private static final String TAG = "ParentActivity";
    
    private TextView textViewNotificationBadge;
    private ParentAccount parentAccount;
    private DatabaseReference triageSessionsRef;
    private ChildEventListener triageListener;
    private DatabaseReference notificationsRef;
    private ChildEventListener notificationsListener;
    private ValueEventListener notificationsCountListener;
    private Map<String, String> lastSeenSessions = new HashMap<>();
    private Map<String, String> lastSeenWorseningIds = new HashMap<>();
    private Set<String> seenNotificationIds = new HashSet<>();
    private SharedPreferences dismissedAlertsPrefs;
    private static final String PREFS_NAME = "ParentActivityDismissedAlerts";
    
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        if (!(UserManager.currentUser instanceof ParentAccount)) {
            Log.e(TAG, "Current user is not a ParentAccount");
            finish();
            return;
        }
        
        parentAccount = (ParentAccount) UserManager.currentUser;
        
        dismissedAlertsPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        textViewNotificationBadge = findViewById(R.id.textViewNotificationBadge);
        
        Button notificationButton = findViewById(R.id.buttonNotificationButton);
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                Intent intent = new Intent(ParentActivity.this, com.example.myapplication.notifications.NotificationActivity.class);
                startActivity(intent);
            });
        }
        
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_children) {
                selectedFragment = new ChildrenFragment();
            } else if (itemId == R.id.nav_providers) {
                selectedFragment = new ProvidersFragment();
            }
            
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                return true;
            }
            
            return false;
        });
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new DashboardFragment())
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachTriageListener();
        loadExistingNotificationIds();
        attachNotificationsListener();
    }
    
    private void loadExistingNotificationIds() {
        if (parentAccount == null) {
            return;
        }
        if (notificationsRef == null) {
            notificationsRef = UserManager.mDatabase
                    .child("users")
                    .child(parentAccount.getID())
                    .child("notifications");
        }
        
        notificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                seenNotificationIds.clear();
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getKey() != null) {
                            seenNotificationIds.add(child.getKey());
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachTriageListener();
        detachNotificationsListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachTriageListener();
        detachNotificationsListener();
    }

    private void attachTriageListener() {
        if (parentAccount == null) {
            return;
        }
        if (triageSessionsRef == null) {
            triageSessionsRef = UserManager.mDatabase
                    .child("triageSessions")
                    .child(parentAccount.getID());
        }
        if (triageListener != null) {
            return;
        }

        triageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                handleTriageSnapshot(snapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                handleTriageSnapshot(snapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Triage listener cancelled", error.toException());
            }
        };

        triageSessionsRef.addChildEventListener(triageListener);
    }

    private void detachTriageListener() {
        if (triageSessionsRef != null && triageListener != null) {
            triageSessionsRef.removeEventListener(triageListener);
            triageListener = null;
        }
    }

    private void attachNotificationsListener() {
        if (parentAccount == null) {
            return;
        }
        if (notificationsRef == null) {
            notificationsRef = UserManager.mDatabase
                    .child("users")
                    .child(parentAccount.getID())
                    .child("notifications");
        }
        
        // Use ChildEventListener to detect new notifications in real-time
        if (notificationsListener == null) {
            notificationsListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                    handleNewNotification(snapshot);
                    updateNotificationBadgeCount();
                }

                @Override
                public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                    updateNotificationBadgeCount();
                }

                @Override
                public void onChildRemoved(DataSnapshot snapshot) {
                    if (snapshot.getKey() != null) {
                        seenNotificationIds.remove(snapshot.getKey());
                    }
                    updateNotificationBadgeCount();
                }

                @Override
                public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "Notifications listener cancelled", error.toException());
                }
            };
            notificationsRef.addChildEventListener(notificationsListener);
        }
        
        // Use ValueEventListener to maintain badge count
        if (notificationsCountListener == null) {
            notificationsCountListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    updateNotificationBadgeCount();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "Notifications count listener cancelled", error.toException());
                }
            };
            notificationsRef.addValueEventListener(notificationsCountListener);
        }
    }

    private void detachNotificationsListener() {
        if (notificationsRef != null) {
            if (notificationsListener != null) {
                notificationsRef.removeEventListener(notificationsListener);
                notificationsListener = null;
            }
            if (notificationsCountListener != null) {
                notificationsRef.removeEventListener(notificationsCountListener);
                notificationsCountListener = null;
            }
        }
    }
    
    private void handleNewNotification(DataSnapshot snapshot) {
        if (snapshot == null || snapshot.getKey() == null) {
            return;
        }
        
        String notificationId = snapshot.getKey();
        
        // Skip if we've already seen this notification
        if (seenNotificationIds.contains(notificationId)) {
            return;
        }
        
        // Mark as seen
        seenNotificationIds.add(notificationId);
        
        // Get notification data
        com.example.myapplication.notifications.NotificationItem notification = 
            snapshot.getValue(com.example.myapplication.notifications.NotificationItem.class);
        
        if (notification == null || notification.isRead()) {
            return;
        }
        
        // Check if this alert has already been dismissed
        String alertKey = "notification_" + notificationId;
        boolean isDismissed = dismissedAlertsPrefs.getBoolean(alertKey, false);
        
        if (isDismissed) {
            return;
        }
        
        // Show alert dialog for critical notifications
        String childName = notification.getChildName() != null ? notification.getChildName() : "Your child";
        String title = getNotificationTitle(notification.getType());
        String message = notification.getMessage() != null ? notification.getMessage() : "New alert for " + childName;
        
        runOnUiThread(() -> {
            new androidx.appcompat.app.AlertDialog.Builder(ParentActivity.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Mark this alert as dismissed
                        dismissedAlertsPrefs.edit().putBoolean(alertKey, true).apply();
                    })
                    .show();
            
            android.widget.Toast.makeText(
                    ParentActivity.this,
                    message,
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        });
    }
    
    private String getNotificationTitle(com.example.myapplication.notifications.NotificationItem.NotificationType type) {
        if (type == null) {
            return "New Alert";
        }
        switch (type) {
            case RED_ZONE_DAY:
                return "Red Zone Alert";
            case RAPID_RESCUE:
                return "Rapid Rescue Alert";
            case WORSE_AFTER_DOSE:
                return "Medication Effectiveness Alert";
            case TRIAGE_ESCALATION:
                return "Emergency Guidance Alert";
            case INVENTORY_LOW:
                return "Inventory Low Alert";
            case INVENTORY_EXPIRED:
                return "Inventory Expired Alert";
            default:
                return "New Alert";
        }
    }
    
    private void updateNotificationBadgeCount() {
        if (notificationsRef == null || parentAccount == null) {
            return;
        }
        
        notificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                int unreadCount = 0;
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        com.example.myapplication.notifications.NotificationItem notification = 
                            child.getValue(com.example.myapplication.notifications.NotificationItem.class);
                        if (notification != null && !notification.isRead()) {
                            unreadCount++;
                        }
                    }
                }
                updateNotificationBadge(unreadCount);
            }
        });
    }

    private void updateNotificationBadge(int count) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        runOnUiThread(() -> {
            if (textViewNotificationBadge != null) {
                if (count > 0) {
                    textViewNotificationBadge.setText(String.valueOf(count));
                    textViewNotificationBadge.setVisibility(View.VISIBLE);
                } else {
                    textViewNotificationBadge.setVisibility(View.GONE);
                }
            }
        });
    }

    private void handleTriageSnapshot(DataSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        String childId = snapshot.getKey();
        if (childId == null) {
            return;
        }

        String childName = snapshot.child("childName").getValue(String.class);
        if (childName == null && parentAccount != null && parentAccount.getChildren() != null) {
            com.example.myapplication.userdata.ChildAccount childAccount = parentAccount.getChildren().get(childId);
            if (childAccount != null && childAccount.getName() != null) {
                childName = childAccount.getName();
            }
        }
        final String finalChildName = childName != null ? childName : "Your child";

        // Handle triage start notification (sessionId changes)
        String sessionId = snapshot.child("sessionId").getValue(String.class);
        if (sessionId != null && !sessionId.isEmpty()) {
            String lastSeen = lastSeenSessions.get(childId);
            if (!sessionId.equals(lastSeen)) {
                lastSeenSessions.put(childId, sessionId);
                
                // Check if this alert has already been dismissed
                String alertKey = "triage_start_" + childId + "_" + sessionId;
                boolean isDismissed = dismissedAlertsPrefs.getBoolean(alertKey, false);
                
                if (!isDismissed) {
                    runOnUiThread(() -> {
                        new androidx.appcompat.app.AlertDialog.Builder(ParentActivity.this)
                                .setTitle("Breathing Assessment Started")
                                .setMessage(finalChildName + " started a breathing assessment.")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    // Mark this alert as dismissed
                                    dismissedAlertsPrefs.edit().putBoolean(alertKey, true).apply();
                                })
                                .show();

                        android.widget.Toast.makeText(
                                ParentActivity.this,
                                finalChildName + " started a breathing assessment.",
                                android.widget.Toast.LENGTH_SHORT
                        ).show();
                    });
                }
            }
        }

        // Handle worsening notification after 10-minute re-check
        String worseningId = snapshot.child("worseningId").getValue(String.class);
        Boolean worseningHasRedFlag = snapshot.child("worseningHasRedFlag").getValue(Boolean.class);

        if (worseningId != null && !worseningId.isEmpty() && Boolean.TRUE.equals(worseningHasRedFlag)) {
            String lastWorsening = lastSeenWorseningIds.get(childId);
            if (!worseningId.equals(lastWorsening)) {
                lastSeenWorseningIds.put(childId, worseningId);
                
                // Check if this alert has already been dismissed
                String alertKey = "worsening_" + childId + "_" + worseningId;
                boolean isDismissed = dismissedAlertsPrefs.getBoolean(alertKey, false);
                
                if (!isDismissed) {
                    runOnUiThread(() -> {
                        new androidx.appcompat.app.AlertDialog.Builder(ParentActivity.this)
                                .setTitle("Breathing Symptoms Still Present")
                                .setMessage(finalChildName + " still has breathing symptoms after the 10-minute re-check.")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    // Mark this alert as dismissed
                                    dismissedAlertsPrefs.edit().putBoolean(alertKey, true).apply();
                                })
                                .show();

                        android.widget.Toast.makeText(
                                ParentActivity.this,
                                finalChildName + " still has breathing symptoms after the 10-minute re-check.",
                                android.widget.Toast.LENGTH_LONG
                        ).show();
                    });
                }
            }
        }
    }

    public void Signout(android.view.View view) {
        UserManager.currentUser = null;
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, com.example.myapplication.SignIn.SignInView.class);
        startActivity(intent);
        finish();
    }

}
