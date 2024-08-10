package com.example.expensemanager.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemanager.R;
import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Expense;
import com.example.expensemanager.viewmodels.ExpenseViewModel;
import com.example.expensemanager.viewmodels.CategoryViewModel;

public class AddEditExpenseActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextNote;
    private Spinner spinnerCategory;
    private Button buttonSaveExpense;
    private String expenseId = null;

    private ExpenseViewModel expenseViewModel;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);  // Ensure this layout file exists

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextNote = findViewById(R.id.editTextNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSaveExpense = findViewById(R.id.buttonSaveExpense);

        // Fetch categories and populate the spinner
        categoryViewModel.fetchCategories(this);
        categoryViewModel.getCategories().observe(this, categories -> {
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);
        });

        // Check if editing an existing expense
        expenseId = getIntent().getStringExtra("expense_id");
        if (expenseId != null) {
            expenseViewModel.fetchExpenseById(this, expenseId);
            expenseViewModel.getExpense().observe(this, this::populateExpenseDetails);
        }

        // Save button click listener
        buttonSaveExpense.setOnClickListener(v -> saveExpense());
    }

    private void populateExpenseDetails(Expense expense) {
        editTextAmount.setText(String.valueOf(expense.getAmount()));
        editTextNote.setText(expense.getNote());

        // Set the selected category
        ArrayAdapter<Category> adapter = (ArrayAdapter<Category>) spinnerCategory.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getId().equals(expense.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    private void saveExpense() {
        double amount = Double.parseDouble(editTextAmount.getText().toString());
        String note = editTextNote.getText().toString();
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();

        if (expenseId == null) {
            // Add new expense
            expenseViewModel.addExpense(this, amount, selectedCategory, note);  // Pass the Category object
        } else {
            // Update existing expense
            expenseViewModel.updateExpense(this, expenseId, amount, selectedCategory, note);  // Pass the Category object
        }

        finish(); // Close the activity after saving
    }

}
