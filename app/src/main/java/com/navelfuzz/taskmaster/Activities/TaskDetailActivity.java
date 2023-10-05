package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.widget.Button;
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
import com.amplifyframework.predictions.PredictionsException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskDetailActivity extends AppCompatActivity {
    private static String TAG = "TaskDetailActivity";
    private final MediaPlayer mp = new MediaPlayer();
    Intent callingIntent;
    Task currentTask;
    String s3ImageKey;
    TextView taskNameTextView;
    TextView taskDescTextView;
    TextView taskStatusTextView;
    TextView taskLatitudeTextview;
    TextView taskLongitudeTextview;
    TextView taskAddressTextview;

    ImageView taskImageView;
    Button announceButton;
    Button translateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        callingIntent = getIntent();
        taskImageView = findViewById(R.id.TaskDetailActivityImageView);
        announceButton = findViewById(R.id.TaskDetailActivityAnnounceTaskNameButton);
        translateButton = findViewById(R.id.TaskDetailActivityTranslateButton);

        taskNameTextView = (TextView) findViewById(R.id.TaskDetailActivityLabelTextView);
        taskDescTextView = (TextView) findViewById(R.id.TaskDetailActivityTaskDescription);
        taskStatusTextView = (TextView) findViewById(R.id.TaskDetailActivityTaskStatus);
        taskLatitudeTextview = (TextView) findViewById(R.id.TaskDetailActivityLatitude);
        taskLongitudeTextview = (TextView) findViewById(R.id.TaskDetailActivityLongitude);
        taskAddressTextview = (TextView) findViewById(R.id.TaskDetailActivityAddress);

        setupTaskNameTextView();
        setupTaskImageView();
        setupAnnounceButton();
        setupTranslateButton();
    }

    void setupTaskNameTextView() {
//        Intent callingIntent = getIntent();
        String taskNameStr = null;
        String taskDescStr = null;
        String taskStatusStr = null;
        String taskLatitude = null;
        String taskLongitude = null;
        String taskAddress = null;
        if (callingIntent != null) {
            taskNameStr = callingIntent.getStringExtra(MainActivity.TASK_NAME_TAG);
            taskDescStr = callingIntent.getStringExtra(MainActivity.TASK_DESC_TAG);
            taskStatusStr = callingIntent.getStringExtra(MainActivity.TASK_STATUS_TAG);
            taskLatitude = callingIntent.getStringExtra(MainActivity.TASK_LATITUDE_EXTRA_TAG);
            taskLongitude = callingIntent.getStringExtra(MainActivity.TASK_LONGITUDE_EXTRA_TAG);
            taskAddress = callingIntent.getStringExtra(MainActivity.TASK_ADDRESS_EXTRA_TAG);
        }

//        TextView taskNameTextView = (TextView) findViewById(R.id.TaskDetailActivityLabelTextView);
//        TextView taskDescTextView = (TextView) findViewById(R.id.TaskDetailActivityTaskDescription);
//        TextView taskStatusTextView = (TextView) findViewById(R.id.TaskDetailActivityTaskStatus);
//        TextView taskLatitudeTextview = (TextView) findViewById(R.id.TaskDetailActivityLatitude);
//        TextView taskLongitudeTextview = (TextView) findViewById(R.id.TaskDetailActivityLongitude);
//        TextView taskAddressTextview = (TextView) findViewById(R.id.TaskDetailActivityAddress);
        if (taskNameStr != null && !taskNameStr.equals("")) {
            taskNameTextView.setText(taskNameStr);
        } else {
            taskNameTextView.setText("No Task Name");
        }
        if (taskDescStr != null && !taskDescStr.equals("")) {
            taskDescTextView.setText(taskDescStr);
        } else {
            taskDescTextView.setText("No Task Description");
        }
        if (taskStatusStr != null && !taskStatusStr.equals("")) {
            taskStatusTextView.setText(taskStatusStr);
        } else {
            taskStatusTextView.setText("No Task Status");
        }
        if (taskLatitude != null && !taskLatitude.equals("")) {
            taskLatitudeTextview.setText("Latitude: " + taskLatitude);
        } else {
            taskLatitudeTextview.setText("No Latitude Provided.");
        }
        if (taskLongitude != null && !taskLongitude.equals("")) {
            taskLongitudeTextview.setText("Longitude: " + taskLongitude);
        } else {
            taskLongitudeTextview.setText("No Longitude Provided.");
        }
        if (taskAddress != null && !taskAddress.equals("")) {
            taskAddressTextview.setText("Address: " + taskAddress);
        } else {
            taskAddressTextview.setText("No Address Provided.");
        }
    }

    void setupTaskImageView() {
        String taskId = "";
        if (callingIntent != null) {
            taskId = callingIntent.getStringExtra(MainActivity.TASK_ID_EXTRA_TAG);
        }

        if (!taskId.equals("")) {
            Amplify.API.query(
                ModelQuery.get(Task.class, taskId),
                success -> {
                    Log.i(TAG, "successfully found task with id: " + success.getData().getId());
                    currentTask = success.getData();
                    populateImageView();
                },
                failure -> {
                    Log.i(TAG, "Failed to query task from DB: " + failure.getMessage());
                }
            );
        }

    }

    void populateImageView() {
        // truncate folder name from product's s3key
        if (currentTask.getTaskImageS3Key() != null) {
            int cut = currentTask.getTaskImageS3Key().lastIndexOf('/');
            if (cut != -1) {
                s3ImageKey = currentTask.getTaskImageS3Key().substring(cut + 1);
            }
        }

        if (s3ImageKey != null && !s3ImageKey.isEmpty()) {
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

    void setupAnnounceButton() {
        announceButton.setOnClickListener(view -> {
            TextView taskTextView = findViewById(R.id.TaskDetailActivityLabelTextView);
            String taskName = taskTextView.getText().toString();
            Amplify.Predictions.convertTextToSpeech(
                taskName,
                result -> playAudio(result.getAudioData()),
                error -> Log.e(TAG, "Audio conversion of product failed")
            );
        });
    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;

            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            Log.i(TAG, "audio file finished reading");

            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());

            Log.i(TAG, "Audio played successfully");
        } catch (IOException ioe) {
            Log.e(TAG, "Error writing audio file", ioe);
        }
    }

    private void setupTranslateButton(){

        translateButton.setOnClickListener(view -> {
            String taskName = taskNameTextView.getText().toString();
            String taskDesc = taskDescTextView.getText().toString();
            String taskStatus = taskStatusTextView.getText().toString();

            Amplify.Predictions.translateText(taskName,
                result -> {
                    runOnUiThread(() -> {
                        Log.i(TAG, "Text translated: " + result.getTranslatedText());
                        taskNameTextView.setText(result.getTranslatedText());
                    });
                },
                error -> {
                    runOnUiThread(() -> {
                        Log.e(TAG, "Error translating text.", error);
                        if (error instanceof PredictionsException) {
                            PredictionsException predictionsException = (PredictionsException) error;
                            Log.e(TAG, "PredictionsException details: " +
                                predictionsException.getRecoverySuggestion());
                        }
                    });
                }
            );

            Amplify.Predictions.translateText(taskDesc,
                result -> {
                    runOnUiThread(() -> {
                        Log.i(TAG, "Text translated: " + result.getTranslatedText());
                        taskDescTextView.setText(result.getTranslatedText());
                    });
                },
                error -> {
                    runOnUiThread(() -> {
                        Log.e(TAG, "Error translating text.", error);
                        if (error instanceof PredictionsException) {
                            PredictionsException predictionsException = (PredictionsException) error;
                            Log.e(TAG, "PredictionsException details: " +
                                predictionsException.getRecoverySuggestion());
                        }
                    });
                }
            );

            Amplify.Predictions.translateText(taskStatus,
                result -> {
                    runOnUiThread(() -> {
                        Log.i(TAG, "Text translated: " + result.getTranslatedText());
                        taskStatusTextView.setText(result.getTranslatedText());
                    });
                },
                error -> {
                    runOnUiThread(() -> {
                        Log.e(TAG, "Error translating text.", error);
                        if (error instanceof PredictionsException) {
                            PredictionsException predictionsException = (PredictionsException) error;
                            Log.e(TAG, "PredictionsException details: " +
                                predictionsException.getRecoverySuggestion());
                        }
                    });
                }
            );
        });


    }

}

