package com.example.expensemanager.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemanager.models.Expense;
import com.example.expensemanager.models.Wallet;
import com.example.expensemanager.services.ApiClient;
import com.example.expensemanager.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Double> walletBalance = new MutableLiveData<>();
    private final MutableLiveData<List<Expense>> expenses = new MutableLiveData<>();

    public HomeViewModel() {
        // Initial value
        walletBalance.setValue(0.0);
    }

    public LiveData<Double> getWalletBalance() {
        return walletBalance;
    }

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    public void fetchWalletBalance(Context context) {
        if (context == null) {
            walletBalance.setValue(0.0);
            return;
        }

        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Call<List<Wallet>> call = apiService.getWallets();
        call.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    walletBalance.setValue(response.body().get(0).getBalance());
                } else {
                    walletBalance.setValue(0.0);
                }
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable t) {
                walletBalance.setValue(0.0);
            }
        });
    }

    public void fetchExpenses(Context context) {
        if (context == null) {
            return;
        }

        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Call<List<Expense>> call = apiService.getExpenses();
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    expenses.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                expenses.setValue(null);
            }
        });
    }
}
