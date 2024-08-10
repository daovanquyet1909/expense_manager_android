package com.example.expensemanager.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Expense;
import com.example.expensemanager.services.ApiClient;
import com.example.expensemanager.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private final MutableLiveData<List<Expense>> expenses = new MutableLiveData<>();
    private final MutableLiveData<Expense> expense = new MutableLiveData<>();

    private static final String TAG = "ExpenseViewModel";

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    public LiveData<Expense> getExpense() {
        return expense;
    }

    public void updateExpensesList(List<Expense> newExpenses) {
        expenses.setValue(newExpenses);
    }

    public void fetchExpenseById(Context context, String id) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Call<Expense> call = apiService.getExpenseById(id);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Expense fetched successfully: " + response.body().toString());
                    expense.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch expense: " + response.message());
                    Toast.makeText(context, "Failed to fetch expense", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                Log.e(TAG, "Error fetching expense: " + t.getMessage(), t);
                Toast.makeText(context, "Failed to fetch expense", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addExpense(Context context, double amount, Category category, String note) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Expense newExpense = new Expense();
        newExpense.setAmount(amount);
        newExpense.setCategory(category);  // Set the whole Category object
        newExpense.setNote(note);

        Log.d(TAG, "Adding expense: Amount=" + amount + ", Category=" + category.getName() + ", Note=" + note);

        Call<Expense> call = apiService.addExpense(newExpense);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Expense added successfully: " + response.body().toString());
                    fetchExpenses(context);
                    Toast.makeText(context, "Expense added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to add expense: " + response.message());
                    Toast.makeText(context, "Failed to add expense", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                Log.e(TAG, "Error adding expense: " + t.getMessage(), t);
                Toast.makeText(context, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateExpense(Context context, String id, double amount, Category category, String note) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Expense updatedExpense = new Expense();
        updatedExpense.setAmount(amount);
        updatedExpense.setCategory(category);
        updatedExpense.setNote(note);

        Log.d(TAG, "Updating expense with ID: " + id);

        Call<Expense> call = apiService.updateExpense(id, updatedExpense);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Expense updated successfully: " + response.body().toString());
                    fetchExpenses(context);
                    Toast.makeText(context, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to update expense: " + response.message());
                    Toast.makeText(context, "Failed to update expense", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                Log.e(TAG, "Error updating expense: " + t.getMessage(), t);
                Toast.makeText(context, "Failed to update expense", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteExpense(Context context, String id) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);

        Log.d(TAG, "Deleting expense with ID: " + id);

        Call<Void> call = apiService.deleteExpense(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Expense deleted successfully");
                    fetchExpenses(context);
                    Toast.makeText(context, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to delete expense: " + response.message());
                    Toast.makeText(context, "Failed to delete expense", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error deleting expense: " + t.getMessage(), t);
                Toast.makeText(context, "Failed to delete expense", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchExpenses(Context context) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        Call<List<Expense>> call = apiService.getExpenses();
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Expenses fetched successfully: " + response.body().toString());
                    expenses.setValue(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch expenses: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Log.e(TAG, "Error fetching expenses: " + t.getMessage(), t);
                expenses.setValue(null);
            }
        });
    }
}
