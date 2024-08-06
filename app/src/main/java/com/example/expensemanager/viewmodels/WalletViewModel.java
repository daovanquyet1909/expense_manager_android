package com.example.expensemanager.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemanager.models.Wallet;
import com.example.expensemanager.services.ApiClient;
import com.example.expensemanager.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletViewModel extends ViewModel {

    private final MutableLiveData<Double> walletBalance;

    public WalletViewModel() {
        walletBalance = new MutableLiveData<>();
        walletBalance.setValue(0.0); // Default value
    }

    public LiveData<Double> getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double balance) {
        walletBalance.setValue(balance);
    }

    public void fetchWalletBalance(Context context) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Call<List<Wallet>> call = apiService.getWallets();
        call.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Wallet> wallets = response.body();
                    if (!wallets.isEmpty()) {
                        double balance = wallets.get(0).getBalance();
                        setWalletBalance(balance);
                    }
                } else {
                    // Handle errors here if needed
                    Toast.makeText(context, "Failed to fetch wallet balance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable t) {
                // Handle failure here
                Toast.makeText(context, "Failed to fetch wallet balance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateWalletBalance(Context context, double newBalance) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Wallet updatedWallet = new Wallet();
        updatedWallet.setBalance(newBalance);

        Call<Wallet> call = apiService.updateWalletBalance(updatedWallet);
        call.enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setWalletBalance(response.body().getBalance());
                    Toast.makeText(context, "Balance updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update balance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Wallet> call, Throwable t) {
                Toast.makeText(context, "Failed to update balance", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
