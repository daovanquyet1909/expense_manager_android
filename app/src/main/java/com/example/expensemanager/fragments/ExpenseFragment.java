package com.example.expensemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentExpenseBinding;
import com.example.expensemanager.models.Expense;
import com.example.expensemanager.services.ApiClient;
import com.example.expensemanager.services.ApiService;
import com.example.expensemanager.viewmodels.ExpenseViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseFragment extends Fragment {

    private FragmentExpenseBinding binding;
    private ExpenseViewModel expenseViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        binding = FragmentExpenseBinding.inflate(inflater, container, false);
        binding.setViewModel(expenseViewModel);
        binding.setLifecycleOwner(this);

        // Fetch and display expenses
        fetchExpenses();

        return binding.getRoot();
    }

    private void fetchExpenses() {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        Call<List<Expense>> call = apiService.getExpenses();
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Expense> expenses = response.body();
                    expenseViewModel.setExpenses(expenses);
                } else {
                    // Handle errors here if needed
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                // Handle failure here
                Toast.makeText(getContext(), "Failed to fetch expenses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
