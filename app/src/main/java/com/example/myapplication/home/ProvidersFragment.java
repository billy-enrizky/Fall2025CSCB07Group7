package com.example.myapplication.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.providermanaging.ProviderManagerActivity;

public class ProvidersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_providers, container, false);
        
        Button buttonOpenProviderManager = view.findViewById(R.id.buttonOpenProviderManager);
        if (buttonOpenProviderManager != null) {
            buttonOpenProviderManager.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProviderManagerActivity.class);
                startActivity(intent);
            });
        }
        
        return view;
    }
}

