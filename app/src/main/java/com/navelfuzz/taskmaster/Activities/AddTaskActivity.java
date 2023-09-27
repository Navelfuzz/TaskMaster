package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.navelfuzz.taskmaster.MainActivity;
import com.navelfuzz.taskmaster.R;
import com.navelfuzz.taskmaster.database.TaskDatabase;
import com.navelfuzz.taskmaster.models.TaskStatusEnum;
import com.navelfuzz.taskmaster.models.Task;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    TaskDatabase tasksDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        tasksDatabase = Room.databaseBuilder(
                getApplicationContext(), TaskDatabase.class, MainActivity.DATABASE_NAME)
                .allowMainThreadQueries().build();
        setupAddTaskButton();
    }

    void setupAddTaskButton(){
        Button addTaskButton = findViewById(R.id.AddTaskInputButton);
        addTaskButton.setOnClickListener(view -> {

            EditText taskInputEditText = findViewById(R.id.AddTaskInputField);
            EditText descriptionInputEditText = findViewById(R.id.AddTaskDescriptionInputField);
            String taskTitle = taskInputEditText.getText().toString();
            String taskDescription = descriptionInputEditText.getText().toString();

            Task taskToSave = new Task(taskTitle, taskDescription, new Date(), TaskStatusEnum.NEW);

            tasksDatabase.taskDao().insertATask(taskToSave);

            taskInputEditText.setText("");
            descriptionInputEditText.setText("");

            Snackbar.make(findViewById(android.R.id.content), "Task Saved!", Snackbar.LENGTH_SHORT).show();
        });
    }
}
