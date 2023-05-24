package com.morax.necolock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.morax.necolock.database.AppDatabase;
import com.morax.necolock.database.dao.ProfileDao;
import com.morax.necolock.database.entity.Profile;
import com.morax.necolock.fragment.NetworkFragment;
import com.morax.necolock.fragment.ParentalControlFragment;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_NETWORK = R.id.menu_network;
    private static final int MENU_PARENTAL_CONTROL = R.id.menu_parental_control;
    private DrawerLayout drawerLayout;
    private TextView tvWifiName;
    private SharedPreferences wifiSharedPrefs;
    private ProfileDao profileDao;
    private BottomNavigationView bottomNavigationView;

    private Handler handler;
    private Runnable runnable;
    private long seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profileDao = AppDatabase.getInstance(this).profileDao();
        wifiSharedPrefs = getSharedPreferences("wifiSharedPrefs", MODE_PRIVATE);
        String wifiName = wifiSharedPrefs.getString("wifiName", "Guest");
        tvWifiName = findViewById(R.id.tv_wifi_name_sidebar);
        tvWifiName.setText(wifiName);
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        if (item.getItemId() == MENU_NETWORK) {
                            selectedFragment = NetworkFragment.newInstance(wifiName);
                        } else if (item.getItemId() == MENU_PARENTAL_CONTROL)
                            selectedFragment = new ParentalControlFragment();

                        if (selectedFragment != null) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, selectedFragment);
                            transaction.commit();
                        }
                        return true;
                    }
                }
        );
        bottomNavigationView.setSelectedItemId(R.id.menu_network);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seconds++;
                handler.postDelayed(this, 1000); // Schedule the next update after 1 second
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileDao.updateProfileList(seconds);
    }

    public void openParentalControl(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new ParentalControlFragment());
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    public void openWifiSettings(View view) {
        Intent intent = new Intent(this, WifiSettingsActivity.class);
        startActivity(intent);
    }

    public void openSidebar(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }


    public void changeNetwork(View view) {
        Random random = new Random();
        int randomNumber = random.nextInt(999);
        SharedPreferences.Editor editor = wifiSharedPrefs.edit();
        String guestName = "Guest" + randomNumber;
        editor.putString("wifiName", guestName);
        editor.apply();
        tvWifiName.setText(guestName);
        recreate();
    }

    public void addProfile(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_profile_layout, null);
        dialogBuilder.setView(dialogView);
        EditText editText = dialogView.findViewById(R.id.et_profile_name);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText
                String enteredText = editText.getText().toString();
                Profile profile = new Profile(enteredText);
                profileDao.insert(profile);
                bottomNavigationView.setSelectedItemId(R.id.menu_network);
                recreate();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}