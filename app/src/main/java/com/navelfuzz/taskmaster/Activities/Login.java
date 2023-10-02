package com.navelfuzz.taskmaster.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.navelfuzz.taskmaster.MainActivity;
import com.navelfuzz.taskmaster.R;
import com.amplifyframework.core.Amplify;

public class Login extends AppCompatActivity {
    public static String TAG = "LoginActivity";

    Button submitButton;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submitButton = findViewById(R.id.LoginActivitySubmitButton);
        email = findViewById(R.id.LoginActivityEmailInput);
        password = findViewById(R.id.LoginActivityPasswordInput);

        setupSubmitButton();
    }

    void setupSubmitButton(){
        submitButton.setOnClickListener(view -> {
            Amplify.Auth.signIn(email.getText().toString(),password.getText().toString(),
                    success -> {
                        Log.i(TAG, "Sign in succeeded.");
                        Intent goToMainActivity = new Intent(Login.this, MainActivity.class);
                        startActivity(goToMainActivity);
                    },
                    failure -> Log.e(TAG, "Sign in failed."));
        });

    }
}