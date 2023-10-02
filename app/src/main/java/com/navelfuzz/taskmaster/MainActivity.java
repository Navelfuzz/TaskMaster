package com.navelfuzz.taskmaster;

import static com.navelfuzz.taskmaster.activities.SettingsActivity.USERNAME_TAG;
import static com.navelfuzz.taskmaster.activities.SettingsActivity.TEAM_NAME_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.navelfuzz.taskmaster.activities.AddTaskActivity;
import com.navelfuzz.taskmaster.activities.AllTasksActivity;
import com.navelfuzz.taskmaster.activities.SettingsActivity;
import com.navelfuzz.taskmaster.adapters.ViewAdapter;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public static final String TASK_NAME_TAG = "taskName";
    public static final String TASK_DESC_TAG = "taskDesc";
    public static final String TASK_STATUS_TAG = "taskStatus";

    List<Task> tasks = new ArrayList<>();
    ViewAdapter adapter;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //createTeamInstances();
        setupSettingsButton();
        setupAddTaskButton();
        setupAllTasksButton();
        updateTasksListFromDatabase();
        setupRecyclerView();
        //manualS3FileUpload();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupUsernameTextView();

        updateTasksListFromDatabase();

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
    void setupSettingsButton(){
        ((ImageView)findViewById(R.id.MainActivitySettingsButton)).setOnClickListener(view -> {
            Intent gotToSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(gotToSettingsIntent);
        });
    }

    void setupUsernameTextView(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString(USERNAME_TAG, "No Username");
        String teamName = preferences.getString(TEAM_NAME_TAG, "All Teams");
        TextView usernameTextView = findViewById(R.id.MainActivityTextView);
        usernameTextView.setText(userName + "'s Tasks");
        TextView teamTextView = findViewById(R.id.MainActivityTeamNameLabel);
        teamTextView.setText(teamName + " View");
    }

    void setupRecyclerView(){
        RecyclerView taskListRecyclerView = (RecyclerView) findViewById(R.id.MainActivityTaskRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(layoutManager);

        int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.task_fragment_spacing);
        taskListRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = spaceInPixels;

                if(parent.getChildAdapterPosition(view) == tasks.size()-1) {
                    outRect.bottom = 0;
                }
            }
        });

        adapter = new ViewAdapter(tasks, this);
        taskListRecyclerView.setAdapter(adapter);
    }


    void updateTasksListFromDatabase(){
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read tasks successfully.");
                    String teamName = preferences.getString(TEAM_NAME_TAG, null);
                    tasks.clear();
                    if (teamName == null) {
                        for(Task databaseTask : success.getData()){
                            tasks.add(databaseTask);
                        }
                    } else {
                        for (Task databaseTask : success.getData()){
                            if(databaseTask.getTeam().getTeamName().equals(teamName)){
                                tasks.add(databaseTask);
                            }
                        }
                    }
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read tasks successfully.")
        );
    }

    void createTeamInstances() {
        Team team1 = Team.builder()
                .teamName("Alpha Team")
                .build();

        Amplify.API.mutate(
                ModelMutation.create(team1),
                successResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): made a contact successfully"),
                failureResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): contact failed with this response" + failureResponse)
        );

        Team team2 = Team.builder()
                .teamName("Bravo Team")
                .build();

        Amplify.API.mutate(
                ModelMutation.create(team2),
                successResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): made a contact successfully"),
                failureResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): contact failed with this response" + failureResponse)
        );

        Team team3 = Team.builder()
                .teamName("Delta Team")
                .build();

        Amplify.API.mutate(
                ModelMutation.create(team3),
                successResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): made a contact successfully"),
                failureResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): contact failed with this response" + failureResponse)
        );
    }

    //This is where the Manual S3 Code begins
//    void manualS3FileUpload(){
//        // create a test file to be saved to S3
//        String testFileName = "testFileName.txt";
//        File testFile = new File(getApplicationContext().getFilesDir(), testFileName);
//
//        // write to test file with BufferedWriter
//        try {
//            BufferedWriter testFileBufferedWriter = new BufferedWriter(new FileWriter(testFile));
//            testFileBufferedWriter.append("some test text here\nAnother line of test text");
//            testFileBufferedWriter.close(); // Do this or your text may not be saved
//        } catch (IOException ioe) {
//            Log.e(TAG, "Could not write file locally with filename: " + testFileName);
//        }
//
//        // Create an S3 Key
//        String testFileS3Key = "someFileOnS3.txt";
//
//        // Call Storage.uploadFile
//        Amplify.Storage.uploadFile(
//            testFileS3Key,
//            testFile,
//            success -> {
//                Log.i(TAG, "S3 uploaded successfully! Key is: " + success.getKey());
//            },
//            failure -> {
//                Log.i(TAG, "S3 upload failed! " + failure.getMessage());
//            }
//        );
//    }
}
