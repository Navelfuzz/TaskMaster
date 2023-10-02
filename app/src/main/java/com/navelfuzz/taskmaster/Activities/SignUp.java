package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.navelfuzz.taskmaster.R;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.navelfuzz.taskmaster.activities.Verification;

public class SignUp extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";

    Button submitButton;
    EditText username;
    EditText nickname;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        submitButton = findViewById(R.id.SignUpActivitySubmitButton);
        username = findViewById(R.id.signUpActivityEmailInput);
        nickname = findViewById(R.id.SignUpActivityUserNameInput);
        password = findViewById(R.id.SignUpActivityPasswordTextInput);

        setupSubmitButton();
    }

    void setupSubmitButton(){
        submitButton.setOnClickListener(view -> {
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(),username.getText().toString())
                    .userAttribute(AuthUserAttributeKey.nickname(),nickname.getText().toString())
                    .build();

            Amplify.Auth.signUp(username.getText().toString(),password.getText().toString(), options,
                    successResponse -> {
                        Log.i(TAG, "Signup succeeded: " + successResponse.toString());
                        Intent goToVerifyActivity = new Intent(SignUp.this, Verification.class);
                        goToVerifyActivity.putExtra("email", username.getText().toString());
                        startActivity(goToVerifyActivity);
                    },
                    failureResponse -> Log.e(TAG, "signup failed with message: ", failureResponse)
            );
        });
    }
}