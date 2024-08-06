package com.example.expensemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemanager.models.Expense;

import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private final MutableLiveData<List<Expense>> expenses;

    public ExpenseViewModel() {
        expenses = new MutableLiveData<>();
    }

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenseList) {
        expenses.setValue(expenseList);
    }
}
