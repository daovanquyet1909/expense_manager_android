package com.example.expensemanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemanager.R;
import com.example.expensemanager.activities.LoginActivity;
import com.example.expensemanager.databinding.FragmentAccountBinding;
import com.example.expensemanager.utils.SharedPrefManager;
import com.example.expensemanager.viewmodels.AccountViewModel;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private AccountViewModel accountViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        binding.setViewModel(accountViewModel);
        binding.setLifecycleOwner(this);

        // Fetch user profile when the fragment is created
        accountViewModel.fetchUserProfile(getContext());

        // Observe user profile changes and update the UI
        accountViewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.textViewUsername.setText("Hello, " + user.getUsername() + "!");
                binding.textViewEmail.setText("Email: " + user.getEmail());
                // Update profile picture if available
                // For example, you might load the image with Glide or Picasso
                // Glide.with(this).load(user.getProfilePictureUrl()).into(binding.imageViewProfilePicture);

                binding.imageViewProfilePicture.setVisibility(View.VISIBLE);
                binding.buttonLogin.setVisibility(View.GONE);
                binding.buttonLogout.setVisibility(View.VISIBLE);
            } else {
                binding.textViewUsername.setText("Hello, Guest!");
                binding.textViewEmail.setText("Email: N/A");
                binding.imageViewProfilePicture.setVisibility(View.GONE);
                binding.buttonLogin.setVisibility(View.VISIBLE);
                binding.buttonLogout.setVisibility(View.GONE);
            }
        });

        binding.buttonLogout.setOnClickListener(v -> {
            SharedPrefManager.getInstance(getContext()).clear();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        binding.buttonLogin.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        return binding.getRoot();
    }
}
