package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.navelfuzz.taskmaster.R;

public class SettingsActivity extends AppCompatActivity {
    public static final String USERNAME_TAG = "userName";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setupUserNameEditText();
        setupSaveButton();
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

            preferencesEditor.putString(USERNAME_TAG, userNameString);
            preferencesEditor.apply();

            Toast.makeText(SettingsActivity.this, "Username Saved!", Toast.LENGTH_LONG).show();
        });
    }
}
