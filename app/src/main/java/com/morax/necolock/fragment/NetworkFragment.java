package com.morax.necolock.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.morax.necolock.R;
import com.morax.necolock.WifiSettingsActivity;

import java.text.DecimalFormat;
import java.util.Random;

public class NetworkFragment extends Fragment {

    private TextView tvDownload, tvUpload, tvUploadSpeedUnit, tvDownloadSpeedUnit;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateNetworkSpeed();
            handler.postDelayed(this, 1000); // Update every 1 second
        }
    };

    public void updateNetworkSpeed() {
        long beforeTime = System.currentTimeMillis();
        long totalTxBeforeTest = TrafficStats.getTotalTxBytes();
        long totalRxBeforeTest = TrafficStats.getTotalRxBytes();

        /* DO WHATEVER NETWORK STUFF YOU NEED TO DO */

        long totalTxAfterTest = TrafficStats.getTotalTxBytes();
        long totalRxAfterTest = TrafficStats.getTotalRxBytes();
        long afterTime = System.currentTimeMillis();

        double timeDifference = afterTime - beforeTime;

        double rxDiff = totalRxAfterTest - totalRxBeforeTest;
        double txDiff = totalTxAfterTest - totalTxBeforeTest;
        double rxBps = (rxDiff / (timeDifference / 1000.0)); // total rx bytes per second
        double txBps = (txDiff / (timeDifference / 1000.0)); // total tx bytes per second

        double uploadSpeed;
        double downloadSpeed;
        String uploadSpeedUnit;
        String downloadSpeedUnit;

        if (txBps < 1000) {
            // Convert upload speed to Mbps
            uploadSpeed = txBps;
            uploadSpeedUnit = "Kbps";
        } else {
            // Keep upload speed in Kbps
            uploadSpeed = txBps / 1000.0;
            uploadSpeedUnit = "Mbps";
        }

        if (rxBps < 1000) {
            // Convert download speed to Mbps
            downloadSpeed = rxBps;
            downloadSpeedUnit = "Kbps";
        } else {
            // Keep download speed in Kbps
            downloadSpeed = rxBps / 1000.0;
            downloadSpeedUnit = "Mbps";
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedUploadSpeed = decimalFormat.format(uploadSpeed);
        String formattedDownloadSpeed = decimalFormat.format(downloadSpeed);

        tvUpload.setText(formattedUploadSpeed);
        tvDownload.setText(formattedDownloadSpeed);
        tvUploadSpeedUnit.setText(uploadSpeedUnit);
        tvDownloadSpeedUnit.setText(downloadSpeedUnit);

    }

    public NetworkFragment() {
    }

    public static NetworkFragment newInstance(String param1) {
        NetworkFragment fragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.slide_left));
        setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDownload = view.findViewById(R.id.tv_download_speed);
        tvUpload = view.findViewById(R.id.tv_upload_speed);
        tvDownloadSpeedUnit = view.findViewById(R.id.tv_download_speed_unit);
        tvUploadSpeedUnit = view.findViewById(R.id.tv_upload_speed_unit);

        TextView tvWifiName = view.findViewById(R.id.tv_wifi_name);
        if (getArguments() != null) {
            String param1 = getArguments().getString("param1");

            tvWifiName.setText(param1);
        }
        handler.postDelayed(runnable, 1000);
    }

    private void updateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100); // Generate a random number between 0 and 99
        tvDownload.setText(String.valueOf(randomNumber));
        randomNumber = random.nextInt(100); // Generate a random number between 0 and 99
        tvUpload.setText(String.valueOf(randomNumber));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}