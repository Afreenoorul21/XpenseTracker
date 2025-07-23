package com.example.xpensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity {

    private EditText titleEditText, amountEditText, dateEditText, notesEditText;
    private Spinner typeSpinner;
    private Button submitTransactionButton;
    private DatabaseHelper dbHelper; // Database helper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        amountEditText = findViewById(R.id.amountEditText);
        dateEditText = findViewById(R.id.dateEditText);
        notesEditText = findViewById(R.id.notesEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        submitTransactionButton = findViewById(R.id.submitTransactionButton);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set up Spinner for Transaction Type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Set onClickListener for the submit button
        submitTransactionButton.setOnClickListener(v -> submitTransaction());

        // Set date picker for dateEditText
        dateEditText.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                    dateEditText.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void submitTransaction() {
        String title = titleEditText.getText().toString().trim();
        String amountStr = amountEditText.getText().toString().trim();
        String type = typeSpinner.getSelectedItem().toString();
        String date = dateEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();

        // Validate input fields
        if (title.isEmpty() || amountStr.isEmpty() || type.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(this, "Please enter a valid amount greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save transaction to the database
        try {
            dbHelper.addTransaction(title, amount, type, date, notes);
            Log.d("TransactionActivity", "Transaction saved: " + title + ", Amount: " + amount + ", Type: " + type); // Log the transaction
            Toast.makeText(this, "Transaction saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving transaction: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // Set result to update dashboard and finish activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATE_DASHBOARD", true);
        setResult(RESULT_OK, resultIntent);
        finish(); // Finish the current activity
    }
}
