package com.morax.necolock.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.morax.necolock.R;
import com.morax.necolock.adapter.ProfileAdapter;
import com.morax.necolock.database.AppDatabase;
import com.morax.necolock.database.dao.ProfileDao;
import com.morax.necolock.database.entity.Profile;

import java.util.ArrayList;
import java.util.List;

public class ParentalControlFragment extends Fragment {

    private List<Profile> profileList;
    private ProfileDao profileDao;

    public ParentalControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parental_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileDao = AppDatabase.getInstance(requireContext()).profileDao();
        initData();

        RecyclerView rvProfile = view.findViewById(R.id.rv_profile);
        ProfileAdapter profileAdapter = new ProfileAdapter(requireContext(), profileList);
        rvProfile.setAdapter(profileAdapter);
    }

    private void initData() {
        profileList = new ArrayList<>();
        try {
            profileList.addAll(profileDao.getProfileList());
        } catch (NullPointerException ignored) {
        }
    }
}