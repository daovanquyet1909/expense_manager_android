package com.example.expensemanager.viewmodels;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.expensemanager.models.User;
import com.example.expensemanager.services.ApiClient;
import com.example.expensemanager.services.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountViewModel extends ViewModel {
    private final MutableLiveData<User> userProfile = new MutableLiveData<>();

    public LiveData<User> getUserProfile() {
        return userProfile;
    }

    public void fetchUserProfile(Context context) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Call<User> call = apiService.getUserProfile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(context, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
