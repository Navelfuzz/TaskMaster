package com.navelfuzz.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import com.navelfuzz.taskmaster.Activities.AddTaskActivity;
import com.navelfuzz.taskmaster.Activities.AllTasksActivity;
import com.navelfuzz.taskmaster.Activities.SettingsActivity;
import com.navelfuzz.taskmaster.Activities.TaskDetailActivity;

import static com.navelfuzz.taskmaster.Activities.SettingsActivity.USERNAME_TAG;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public static final String TASK_NAME_TAG = "taskName";
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setupAddTaskButton();
        setupAllTasksButton();
        setupTaskDetailButtons();
        setupSettingsButton();

    }

    @Override
    protected void onResume(){
        super.onResume();
        setupUsernameTextView();
    }

    void setupAddTaskButton(){
        Button addTaskButton = findViewById(R.id.MainActivityAddTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTasksIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(goToAddTasksIntent);
        });
    }

    void setupAllTasksButton(){
        Button allTasksButton = findViewById(R.id.MainActivityAllTasksButton);
        allTasksButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }

    void setupTaskDetailButtons(){
        Button taskOneDetailButton = findViewById(R.id.MainActivityViewTaskOneButton);
        taskOneDetailButton.setOnClickListener(view -> {
            String taskName = ((Button)findViewById(R.id.MainActivityViewTaskOneButton)).getText().toString();

            Intent goToTaskDetailIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
            goToTaskDetailIntent.putExtra(TASK_NAME_TAG, taskName);
            startActivity(goToTaskDetailIntent);
        });
    }

    void setupSettingsButton(){
        ((ImageView)findViewById(R.id.MainActivitySettingsButton)).setOnClickListener(view -> {
            Intent gotToSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(gotToSettingsIntent);
        });
    }

    void setupUsernameTextView(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString(USERNAME_TAG, "No Username");

        TextView usernameTextView = findViewById(R.id.MainActivityTextView);
        usernameTextView.setText(userName + "'s Tasks");
    }

}