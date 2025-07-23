package com.example.xpensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Import for logging
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView transactionRecyclerView;
    private TransactionAdapter transactionAdapter;
    private DatabaseHelper dbHelper;
    private ArrayList<Transaction> transactions;
    private Button addTransactionButton;
    private ActivityResultLauncher<Intent> addTransactionLauncher;

    private double totalIncome = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        dbHelper = new DatabaseHelper(this);
        transactionRecyclerView = findViewById(R.id.transactionRecyclerView);
        addTransactionButton = findViewById(R.id.addTransactionButton);


        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        addTransactionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().getBooleanExtra("UPDATE_DASHBOARD", false)) {
                            loadTransactions();
                        }
                    }
                }
        );

        addTransactionButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TransactionActivity.class);
            addTransactionLauncher.launch(intent);
        });

        handleIncomeInput();

        loadTransactions();
    }

    private void loadTransactions() {
        transactions = dbHelper.getAllTransactions();

        Log.d("DashboardActivity", "Loaded transactions: " + transactions.size());

        if (transactions.isEmpty()) {
            Toast.makeText(this, "No transactions found.", Toast.LENGTH_SHORT).show();
        }

        if (transactionAdapter == null) {
            transactionAdapter = new TransactionAdapter(transactions);
            transactionRecyclerView.setAdapter(transactionAdapter);
        } else {
            transactionAdapter.updateTransactions(transactions);
        }

        // Update income and expense summary
        updateIncomeExpenseSummary();
    }

    private void handleIncomeInput() {
        EditText incomeInput = findViewById(R.id.monthlyIncomeInput);
        TextView totalIncomeView = findViewById(R.id.totalIncomeValue);

        findViewById(R.id.saveIncomeButton).setOnClickListener(v -> {
            String incomeText = incomeInput.getText().toString();

            if (incomeText.isEmpty()) {
                Toast.makeText(this, "Please enter your monthly income.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                totalIncome = Double.parseDouble(incomeText);
                if (totalIncome <= 0) {
                    Toast.makeText(this, "Please enter a valid amount greater than zero.", Toast.LENGTH_SHORT).show();
                    return;
                }

                totalIncomeView.setText(String.format("+$%.2f", totalIncome));
                incomeInput.setText("");
                updateTotalBalance();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number for income.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalBalance() {
        double totalExpense = calculateTotalExpense();
        double balance = totalIncome - totalExpense;

        TextView totalBalanceView = findViewById(R.id.totalBalanceValue);
        totalBalanceView.setText(String.format("$%.2f", balance));
    }

    private double calculateTotalExpense() {
        double totalExpense = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("expense")) {
                totalExpense += transaction.getAmount();
            }
        }
        Log.d("DashboardActivity", "Total expense calculated: $" + totalExpense);
        return totalExpense;
    }

    // Method to update income and expense summary
    private void updateIncomeExpenseSummary() {
        double totalExpense = calculateTotalExpense();

        TextView totalIncomeView = findViewById(R.id.totalIncomeValue);
        TextView totalExpenseView = findViewById(R.id.totalExpenseValue);

        // Update UI elements for income and expense
        totalIncomeView.setText(String.format("+$%.2f", totalIncome));
        totalExpenseView.setText(String.format("-$%.2f", totalExpense));

        updateTotalBalance();
    }
}
