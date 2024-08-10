package com.example.expensemanager.services;

import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Expense;
import com.example.expensemanager.models.User;
import com.example.expensemanager.models.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<User> loginUser(@Body User user);

    @POST("auth/register")
    Call<User> registerUser(@Body User user);

    @GET("auth/profile")
    Call<User> getUserProfile();

    @GET("user/{id}")
    Call<User> getUserProfile(@Path("id") String id);

    @PUT("user/{id}")
    Call<User> updateUserProfile(@Path("id") String id, @Body User user);

    @GET("categories")
    Call<List<Category>> getCategories();

    @POST("categories")
    Call<Category> addCategory(@Body Category category);

    @GET("wallet")
    Call<Wallet> getWalletBalance();

    @GET("wallets")
    Call<List<Wallet>> getWallets();

    @PUT("wallets")
    Call<Wallet> updateWalletBalance(@Body Wallet wallet);

    @GET("expenses")
    Call<List<Expense>> getExpenses();

    @GET("expenses/{id}")
    Call<Expense> getExpenseById(@Path("id") String id);

    @POST("expenses")
    Call<Expense> addExpense(@Body Expense expense);

    @PUT("expenses/{id}")
    Call<Expense> updateExpense(@Path("id") String id, @Body Expense expense);

    @DELETE("expenses/{id}")
    Call<Void> deleteExpense(@Path("id") String id);
}
