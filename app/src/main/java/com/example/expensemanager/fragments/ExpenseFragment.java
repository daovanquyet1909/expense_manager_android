package com.example.expensemanager.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.ExpenseAdapter;
import com.example.expensemanager.models.Expense;
import com.example.expensemanager.viewmodels.ExpenseViewModel;

import java.util.List;

public class ExpenseFragment extends Fragment {

    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseAdapter = new ExpenseAdapter();
        recyclerView = view.findViewById(R.id.recyclerViewExpenses);
        emptyView = view.findViewById(R.id.empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expenseAdapter);

        expenseViewModel.getExpenses().observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                if (expenses != null && !expenses.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    expenseAdapter.setExpenses(expenses);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Fetch expenses from the ViewModel
        expenseViewModel.fetchExpenses(getContext());

        return view;
    }
}
