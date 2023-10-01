package com.navelfuzz.taskmaster.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.core.Amplify;
import com.navelfuzz.taskmaster.MainActivity;
import com.navelfuzz.taskmaster.R;

public class SettingsActivity extends AppCompatActivity {
    public static final String USERNAME_TAG = "userName";
    public static final String TEAM_NAME_TAG = "teamName";
    SharedPreferences preferences;
    AuthUser authUser;
    private String selectedTeam;
    Button loginButton;
    Button logoutButton;
    Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        loginButton = findViewById(R.id.SettingsLoginButton);
        logoutButton = findViewById(R.id.SettingsLogoutButton);
        signUpButton = findViewById(R.id.SettingsSignUpButton);

        setupUserNameEditText();
        setupSaveButton();
        setupSignUpButton();
        setupLoginButton();
        setupLogoutButton();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();
        if (id == R.id.radio_user_alpha) {
            if (checked) {
                selectedTeam = "Alpha Team";
            }
        } else if (id == R.id.radio_user_bravo) {
            if (checked) {
                selectedTeam = "Bravo Team";
            }
        } else if (id == R.id.radio_user_delta) {
            if (checked) {
                selectedTeam = "Delta Team";
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        Amplify.Auth.getCurrentUser(
                success -> {
                    Log.i(TAG, "User authenticated with username: " + success.getUsername());
                    authUser = success;
                    runOnUiThread(this::renderButtons);
                },
                failure -> {
                    Log.i(TAG, "There is no current authenticated user");
                    authUser = null;
                    runOnUiThread(this::renderButtons);
                }
        );
    }

    void setupUserNameEditText(){
        String userName = preferences.getString(USERNAME_TAG, null);
        ((EditText)findViewById(R.id.SettingsActivityUsernameInputEditText)).setText(userName);
    }

    void setupSaveButton(){
        ((Button)findViewById(R.id.SettingsActivitySaveButton)).setOnClickListener(view -> {
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            EditText userNameEditText = (EditText) findViewById(R.id.SettingsActivityUsernameInputEditText);
            String userNameString = userNameEditText.getText().toString();
            preferencesEditor.putString(TEAM_NAME_TAG, selectedTeam);
            preferencesEditor.putString(USERNAME_TAG, userNameString);
            preferencesEditor.apply();

            Toast.makeText(SettingsActivity.this, "Username Saved!", Toast.LENGTH_LONG).show();
        });
    }

    void setupSignUpButton() {
        signUpButton.setOnClickListener(v -> {
            Intent goToSignUpActivityIntent = new Intent(SettingsActivity.this, SignUp.class);
            startActivity(goToSignUpActivityIntent);
        });
    }

    void setupLoginButton() {
        loginButton.setOnClickListener(v -> {
            Intent goToLoginActivityIntent = new Intent(SettingsActivity.this, Login.class);
            startActivity(goToLoginActivityIntent);
        });
    }

    void setupLogoutButton() {
        logoutButton.setOnClickListener(v -> {
            //Cognito Logout Logic
            AuthSignOutOptions signOutOptions = AuthSignOutOptions.builder()
                    .globalSignOut(true)
                    .build();

            Amplify.Auth.signOut(signOutOptions,
                    signOutResult -> {
                        if(signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                            Log.i(TAG, "Global sign out successful!");
                            // Lecture36 Followup: Send user back to MainActivity on Logout
                            Intent goToMainActivity = new Intent(SettingsActivity.this, MainActivity.class);
                            startActivity(goToMainActivity);
                        } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
                            Log.i(TAG, "Partial sign out successful!");
                        } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                            Log.i(TAG, "Logout failed: " + signOutResult.toString());
                        }
                    }
            );
        });
    }


    void renderButtons() {
        if(authUser == null) {
            logoutButton.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
        } else {
            logoutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            signUpButton.setVisibility(View.INVISIBLE);
        }
    }
}
