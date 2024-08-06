package com.example.expensemanager.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentWalletBinding;
import com.example.expensemanager.viewmodels.WalletViewModel;

public class WalletFragment extends Fragment {

    private FragmentWalletBinding binding;
    private WalletViewModel walletViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        binding.setViewModel(walletViewModel);
        binding.setLifecycleOwner(this);

        // Fetch wallet balance when the fragment is created
        walletViewModel.fetchWalletBalance(getContext());

        // Set the onClickListener for the update button
        binding.buttonUpdateBalance.setOnClickListener(v -> showUpdateBalanceDialog());

        return binding.getRoot();
    }

    private void showUpdateBalanceDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_update_balance, null);

        // Create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
                .setTitle("Update Wallet Balance")
                .setCancelable(false);

        // Find views in the dialog layout
        EditText editTextNewBalance = dialogView.findViewById(R.id.editTextNewBalance);
        Button buttonConfirm = dialogView.findViewById(R.id.buttonConfirm);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set listeners for the buttons
        buttonConfirm.setOnClickListener(v -> {
            String balanceStr = editTextNewBalance.getText().toString();
            if (!balanceStr.isEmpty()) {
                double newBalance = Double.parseDouble(balanceStr);
                walletViewModel.updateWalletBalance(getContext(), newBalance);
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter a balance", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());
    }
}
