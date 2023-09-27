package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.google.android.material.snackbar.Snackbar;
import com.navelfuzz.taskmaster.R;

import java.util.Date;


public class AddTaskActivity extends AppCompatActivity {
    private final String TAG = "AddTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setupAddTaskButton();
    }


    void setupAddTaskButton(){
        Button addTaskButton = findViewById(R.id.AddTaskInputButton);
        addTaskButton.setOnClickListener(view -> {
            EditText taskInputEditText = findViewById(R.id.AddTaskInputField);
            EditText descriptionInputEditText = findViewById(R.id.AddTaskDescriptionInputField);


        //TODO: Make a DynamoDB/GraphQL call
            Task taskToSave = Task.builder()
                    .title(taskInputEditText.getText().toString())
                    .body(descriptionInputEditText.getText().toString())
                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .status(TaskStatusEnum.New)
                    .build();
            Amplify.API.mutate(
                    ModelMutation.create(taskToSave), // making a GraphQL
                    successResponse -> Log.i(TAG, "AddTaskActivity.setupAddTaskButton(): made task successfully"),
                    failureResponse -> Log.i(TAG, "AddTaskActivity.setupAddTaskButton(): failed with this response" + failureResponse)
            );

            taskInputEditText.setText("");
            descriptionInputEditText.setText("");

            Snackbar.make(findViewById(android.R.id.content), "Task Saved!", Snackbar.LENGTH_SHORT).show();
        });
    }
}
