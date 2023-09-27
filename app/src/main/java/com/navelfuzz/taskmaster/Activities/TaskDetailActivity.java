package com.navelfuzz.taskmaster.Activities;

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
        String taskNameString = null;
        if(callingIntent != null){
            taskNameString = callingIntent.getStringExtra(MainActivity.TASK_NAME_TAG);
        }

         TextView taskNameTextView = (TextView) findViewById(R.id.TaskDetailActivityLabelTextView);
         if(taskNameString != null && !taskNameString.equals("")){
         taskNameTextView.setText(taskNameString);
         } else {
            taskNameTextView.setText("No task name provided");
         }
    }
}


