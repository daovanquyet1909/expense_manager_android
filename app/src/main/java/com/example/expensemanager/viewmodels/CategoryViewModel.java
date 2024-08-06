package com.example.expensemanager.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemanager.models.Category;
import com.example.expensemanager.services.ApiClient;
import com.example.expensemanager.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<List<Category>> categories;

    public CategoryViewModel() {
        categories = new MutableLiveData<>();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void fetchCategories(Context context) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.setValue(response.body());
                } else {
                    Toast.makeText(context, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(context, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCategory(Context context, String categoryName) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Category newCategory = new Category();
        newCategory.setName(categoryName);

        Call<Category> call = apiService.addCategory(newCategory);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> currentCategories = categories.getValue();
                    if (currentCategories != null) {
                        currentCategories.add(response.body());
                        categories.setValue(currentCategories);
                    }
                    Toast.makeText(context, "Category added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(context, "Failed to add category", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
