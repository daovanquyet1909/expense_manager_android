package com.example.expensemanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensemanager.R;
import com.example.expensemanager.activities.AddEditExpenseActivity;
import com.example.expensemanager.adapters.ExpenseAdapter;
import com.example.expensemanager.models.Expense;
import com.example.expensemanager.viewmodels.HomeViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ExpenseAdapter expenseAdapter;
    private TextView textViewWalletBalance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        textViewWalletBalance = view.findViewById(R.id.textViewWalletBalance);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseAdapter = new ExpenseAdapter();
        recyclerView.setAdapter(expenseAdapter);

        // Fetch wallet balance and expenses
        homeViewModel.fetchWalletBalance(getContext());
        homeViewModel.fetchExpenses(getContext());

        homeViewModel.getWalletBalance().observe(getViewLifecycleOwner(), balance -> {
            textViewWalletBalance.setText(String.format("$%.2f", balance));
        });

        homeViewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            expenseAdapter.setExpenses(expenses);
        });

        // Set up the adapter's click listeners
        expenseAdapter.setOnExpenseClickListener(new ExpenseAdapter.OnExpenseClickListener() {
            @Override
            public void onEditClick(Expense expense) {
                Intent intent = new Intent(getContext(), AddEditExpenseActivity.class);
                intent.putExtra("expense_id", expense.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Expense expense) {
                homeViewModel.deleteExpense(getContext(), expense.getId());
            }
        });

        // Handle FAB click to add a new expense
        view.findViewById(R.id.fabAddExpense).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditExpenseActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
