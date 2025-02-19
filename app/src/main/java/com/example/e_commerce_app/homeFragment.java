package com.example.e_commerce_app;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.e_commerce_app.R;

public class homeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up ImageButton interactions
        ImageButton btnRestaurants = view.findViewById(R.id.art);
        ImageButton btnHotels = view.findViewById(R.id.decor);
        ImageButton btnEducation = view.findViewById(R.id.music);

        btnRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RestaurantsFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ArtFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                Toast.makeText(getActivity(), "Art", Toast.LENGTH_SHORT).show();
            }
        });

        btnHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new DecoFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                Toast.makeText(getActivity(), "décor", Toast.LENGTH_SHORT).show();
            }
        });

        btnEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new MusiqueFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                Toast.makeText(getActivity(), "musique", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}