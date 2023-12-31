package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.navelfuzz.taskmaster.R;
import com.amplifyframework.core.Amplify;

public class Verification extends AppCompatActivity {
    public static String TAG = "VerificationActivity";

    Button submitButton;
    EditText email;
    EditText confirmationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        submitButton = findViewById(R.id.VerifyActivitySubmitButton);
        email = findViewById(R.id.VerifyActivityEmailInput);
        confirmationCode = findViewById(R.id.VerifyActivityCodeInput);

        setupEmailTextInput();
        setupSubmitButton();
    }

    void setupEmailTextInput() {
        Intent callingIntent = getIntent();
        String emailString = null;

        if (callingIntent != null) {
            emailString = callingIntent.getStringExtra("email");
        }

        if (emailString != null) {
            email.setText(emailString);
        }
    }

    void setupSubmitButton() {
        //Cognito Verification Logic
        submitButton.setOnClickListener(v -> {
            Amplify.Auth.confirmSignUp(email.getText().toString(),
                    confirmationCode.getText().toString(),
                    success -> {
                        Log.i(TAG, "Verification succeeded: " + success.toString());
                        Intent goToLoginIntent = new Intent(Verification.this, Login.class);
                        //pass the user's email to the login activity for faster login
                        //hint: can also pass the password from the signup through verify to login
                        startActivity(goToLoginIntent);
                    },
                    failure -> {
                        Log.i(TAG, "Verification failed: " + failure.toString());
                    }
            );
        });
    }
}




//        Amplify.Auth.confirmSignUp(email.getText().toString(),
//                confirmationCode.getText().toString(),
//                successResponse -> {
//                    Log.i(TAG, "Verification Succeeded.");
//                    Intent goToLoginIntent = new Intent(Verification.this, Login.class);
//                    goToLoginIntent.putExtra("email", email.getText().toString());
//                    startActivity(goToLoginIntent);
//                },
//                failure -> Log.e(TAG, "Verification Failed"));
