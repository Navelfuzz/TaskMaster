package com.navelfuzz.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ImageView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.navelfuzz.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    private final String TAG = "AddTaskActivity";
    private String s3ImageKey = "";
    private String selectedTeam;
    CompletableFuture<List<Team>> teamsFuture = null;
    ImageView taskImageView;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskImageView = findViewById(R.id.AddTaskActivityImageView);
        activityResultLauncher = getImagePickingActivityResultLauncher();

        teamsFuture = new CompletableFuture<>();
        setupTaskImageView();
        setupTeamsRadioButtons();
        setupAddTaskButton();
    }

    void setupTaskImageView(){
        taskImageView.setOnClickListener(view -> {
            launchImageSelectionIntent();
        });
    }

    void setupTeamsRadioButtons(){
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read teams Successfully");
                    ArrayList<Team> teams = new ArrayList<>();
                    for(Team team : success.getData()){
                        teams.add(team);
                    }
                    teamsFuture.complete(teams);
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Did not read teams successfully");
                }
        );
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();
        if (id == R.id.radio_alpha) {
            if (checked) {
                selectedTeam = "Alpha Team";
            }
        } else if (id == R.id.radio_bravo) {
            if (checked) {
                selectedTeam = "Bravo Team";
            }
        } else if (id == R.id.radio_delta) {
            if (checked) {
                selectedTeam = "Delta Team";
            }
        }
    }

    void setupAddTaskButton(){
        Button addTaskButton = findViewById(R.id.AddTaskInputButton);
        addTaskButton.setOnClickListener(view -> {

            if (selectedTeam == null) {

                Snackbar.make(findViewById(android.R.id.content), "Please select a team", Snackbar.LENGTH_SHORT).show();
                return;
            }
            List<Team> teams = null;
            try {
                teams = teamsFuture.get();
            } catch (InterruptedException ie) {
                Log.e(TAG, "InterruptedException while getting teams.");
                Thread.currentThread().interrupt();
            } catch (ExecutionException ee){
                Log.e(TAG, "ExecutionException while getting teams");
            }
            assert teams != null;
            Team teamToInput = teams.stream().filter(t -> t.getTeamName().equals(selectedTeam)).findAny().orElseThrow(RuntimeException::new);
            EditText taskInputEditText = findViewById(R.id.AddTaskInputField);
            EditText descriptionInputEditText = findViewById(R.id.AddTaskDescriptionInputField);
            Task taskToSave = Task.builder()
                    .title(taskInputEditText.getText().toString())
                    .body(descriptionInputEditText.getText().toString())
                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .status(TaskStatusEnum.New)
                    .team(teamToInput)
                        .taskImageS3Key(s3ImageKey)
                            .build();

            Amplify.API.mutate(
                    ModelMutation.create(taskToSave),
                    successResponse -> Log.i(TAG, "AddTaskActivity.setupAddTaskButton(): made task successfully"),
                    failureResponse -> Log.i(TAG, "AddTaskActivity.setupAddTaskButton: failed with this response" + failureResponse)
            );

            taskInputEditText.setText("");
            descriptionInputEditText.setText("");
            Snackbar.make(findViewById(android.R.id.content), "Task Saved!", Snackbar.LENGTH_SHORT).show();
        });
    }

    void launchImageSelectionIntent(){
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});

        activityResultLauncher.launch(imageFilePickingIntent);
    }

    ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher() {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
            registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Uri pickedImageFileUri = result.getData().getData();
                    try {
                        InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                        String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                        Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                        uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Could not get file from file picker!");
                    }
                }
            );
        return imagePickingActivityResultLauncher;
    }

    void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename, Uri pickedImageFileUri) {

        Amplify.Storage.uploadInputStream(
            pickedImageFilename, // S3 key
            pickedImageInputStream,
            success -> {
                Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                s3ImageKey = success.getKey();
                InputStream pickedImageInputStreamCopy = null;
                try {
                    pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Could not get file stream from URI. ");
                }
                taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
            },
            failure -> {
                Log.e(TAG, "Failure in uploading file to S3. ");
            }
        );
    }


    // Taken from class demo, sourced from StackOverflow
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
