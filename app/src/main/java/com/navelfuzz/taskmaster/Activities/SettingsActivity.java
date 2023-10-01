package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.navelfuzz.taskmaster.R;

public class SettingsActivity extends AppCompatActivity {
    public static final String USERNAME_TAG = "userName";
    public static final String TEAM_NAME_TAG = "teamName";
    SharedPreferences preferences;
    private String selectedTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setupUserNameEditText();
        setupSaveButton();
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
}
