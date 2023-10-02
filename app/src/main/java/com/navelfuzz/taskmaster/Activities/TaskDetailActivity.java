package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.navelfuzz.taskmaster.MainActivity;
import com.navelfuzz.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {
    private static String TAG = "TaskDetailActivity";

    Intent callingIntent;
    Task currentTask;
    String s3ImageKey;

    ImageView taskImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        callingIntent = getIntent();
        taskImageView = findViewById(R.id.TaskDetailActivityImageView);

        setupTaskNameTextView();
        setupTaskImageView();
    }

    void setupTaskNameTextView(){
//        Intent callingIntent = getIntent();
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

    void setupTaskImageView(){
        String taskId = "";
        if(callingIntent != null) {
            taskId = callingIntent.getStringExtra(MainActivity.TASK_ID_EXTRA_TAG);
        }

        if(!taskId.equals("")) {
            Amplify.API.query(
                ModelQuery.get(Task.class, taskId),
                success -> {
                    Log.i(TAG, "successfully found task with id: " + success.getData().getId());
                    currentTask = success.getData();
                    populateImageView();
                },
                failure -> {
                    Log.i(TAG,"Failed to query task from DB: " + failure.getMessage());
                }
            );
        }

    }

    void populateImageView() {
        // truncate folder name from product's s3key
        if(currentTask.getTaskImageS3Key() != null) {
            int cut = currentTask.getTaskImageS3Key().lastIndexOf('/');
            if(cut != -1) {
                s3ImageKey = currentTask.getTaskImageS3Key().substring(cut + 1);
            }
        }

        if(s3ImageKey != null && !s3ImageKey.isEmpty()) {
            Amplify.Storage.downloadFile(
                s3ImageKey,
                new File(getApplication().getFilesDir(), s3ImageKey),
                success -> {
                    taskImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                },
                failure -> {
                    Log.e(TAG, "Unable to get image from S3 for the task for S3 key: " + s3ImageKey + " for reason: " + failure.getMessage());
                }
            );
        }
    }
}


