package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import com.navelfuzz.taskmaster.MainActivity;
import com.navelfuzz.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        setupTaskNameTextView();
    }

    void setupTaskNameTextView(){
        Intent callingIntent = getIntent();
        String taskNameStr = null;
        String taskDescStr = null;
        String taskStatusStr = null;
        if(callingIntent != null) {
            taskNameStr = callingIntent.getStringExtra(MainActivity.TASK_NAME_TAG);
            taskDescStr = callingIntent.getStringExtra(MainActivity.TASK_DESC_TAG);
            taskStatusStr = callingIntent.getStringExtra(MainActivity.TASK_STATUS_TAG);
        }

        TextView taskNameTextView = (TextView) findViewById(R.id.TaskDetailActivityLabelTextView);
        TextView taskDescTextView = (TextView) findViewById(R.id.TaskDetailActivityTaskDescription);
        TextView taskStatusTextView = (TextView) findViewById(R.id.TaskDetailActivityTaskStatus);
        if(taskNameStr != null && !taskNameStr.equals("")){
            taskNameTextView.setText(taskNameStr);
        } else {
            taskNameTextView.setText("No Task Name");
        }
        if(taskDescStr != null && !taskDescStr.equals("")){
            taskDescTextView.setText(taskDescStr);
        } else {
            taskDescTextView.setText("No Task Description");
        }
        if(taskStatusStr != null && !taskStatusStr.equals("")){
            taskStatusTextView.setText(taskStatusStr);
        } else {
            taskStatusTextView.setText("No Task Status");
        }
    }
}


