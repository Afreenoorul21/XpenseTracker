package com.example.xpensetracker; // Ensure this matches your app package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText, passwordEditText, emailEditText, phoneEditText;
    private Button submitButton;
    private ProgressBar progressBar;

    private static final int SUBMISSION_DELAY = 2000; // Delay in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this matches your layout file

        // Initialize views
        nameEditText = findViewById(R.id.editTextText4);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        // Set onClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        // Set onClickListener for the "Already have an account?" text
        TextView loginTextView = findViewById(R.id.textView3);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick(v); // Call the onLoginClick method
            }
        });
    }

    private void submitForm() {
        String name = nameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Validate input fields
        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the progress bar and disable the button
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        // Simulate form submission (replace with actual logic later)
        submitButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);

                // Navigate to DashboardActivity after form submission
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish(); // Close MainActivity to prevent going back
            }
        }, SUBMISSION_DELAY); // Use the defined constant for delay
    }

    // Method to handle login click
    public void onLoginClick(View view) {
        // Show a Toast message (or navigate to the login screen)
        Toast.makeText(this, "Login clicked!", Toast.LENGTH_SHORT).show();
        // Uncomment the following lines to navigate to the login activity
        // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        // startActivity(intent);
    }
}
