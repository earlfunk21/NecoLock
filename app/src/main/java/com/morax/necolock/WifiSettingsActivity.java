package com.morax.necolock;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class WifiSettingsActivity extends AppCompatActivity {

    private String wifiName;
    private SharedPreferences wifiSharedPrefs;
    private EditText etWifiName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_settings);

        wifiSharedPrefs = getSharedPreferences("wifiSharedPrefs", MODE_PRIVATE);

        etWifiName = findViewById(R.id.et_wifi_name);

        etWifiName.setText(wifiSharedPrefs.getString("wifiName", "Guest"));
    }

    public void goBack(View view) {
        Intent intent = new Intent(WifiSettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void connectWifi(View view) {
        wifiName = etWifiName.getText().toString();
        SharedPreferences.Editor editor = wifiSharedPrefs.edit();
        editor.putString("wifiName", wifiName);
        editor.apply();
        Intent intent = new Intent(WifiSettingsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}