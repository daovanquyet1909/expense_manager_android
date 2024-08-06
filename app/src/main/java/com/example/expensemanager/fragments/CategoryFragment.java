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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensemanager.adapters.CategoryAdapter;
import com.example.expensemanager.databinding.FragmentCategoryBinding;
import com.example.expensemanager.models.Category;
import com.example.expensemanager.viewmodels.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        binding.setViewModel(categoryViewModel);
        binding.setLifecycleOwner(this);

        // Setup RecyclerView
        categoryAdapter = new CategoryAdapter();
        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCategories.setAdapter(categoryAdapter);

        // Fetch categories when the fragment is created
        categoryViewModel.fetchCategories(getContext());

        // Observe changes to the categories list
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategories(categories);
        });

        // Set the onClickListener for the add button
        binding.buttonAddCategory.setOnClickListener(v -> {
            String categoryName = binding.editTextCategoryName.getText().toString();
            if (!categoryName.isEmpty()) {
                categoryViewModel.addCategory(getContext(), categoryName);
                binding.editTextCategoryName.setText(""); // Clear the input field
            } else {
                Toast.makeText(getContext(), "Please enter a category name", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}
