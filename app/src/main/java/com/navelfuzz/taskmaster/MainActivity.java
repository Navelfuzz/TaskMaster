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

        // Cognito Sign Up Logic
//        Amplify.Auth.signUp("jcstillson13@gmail.com", "Password*",
//                AuthSignUpOptions.builder()
//                .userAttribute(AuthUserAttributeKey.email(), "jcstillson13@gmail.com")
//                .userAttribute(AuthUserAttributeKey.nickname(), "Jon")
//                .build(),
//                successResponse -> Log.i(TAG, "Sign up succeeded: " + successResponse.toString()),
//                failureResponse -> Log.i(TAG, "Sign up failed with username: " + "jcstillson13@gmail.com" + "with this message: " + failureResponse.toString())
//        );

        //Cognito Verification Logic
//        Amplify.Auth.confirmSignUp("jcstillson13@gmail.com",
//                "364496",
//            success -> {
//                Log.i(TAG, "Verification succeeded: " + success.toString());
//            },
//            failure -> {
//                Log.i(TAG, "Verification failed: " + failure.toString());
//            }
//        );


        //Cognito Sign In Logic
//        Amplify.Auth.signIn("jcstillson13@gmail.com", "Password*",
//                success -> Log.i(TAG, "Login Succeeded: " + success.toString()),
//                failure -> Log.i(TAG, "Login Failed: " + failure.toString())
//        );


        //// Set the user's username based on their nickname saved in the Cognito user pool
        //Amplify.Auth.fetchUserAttributes(
        //  success -> {
        //    for(AuthUserAttribute userAttribute : success) {
        //      if(userAttribute.getKey().getKeyString().equals("nickname")) {
        //        String userNickname = userAttribute.getValue();
        //        runOnUiThread(() -> {
        //          // update a textView with the nickname
        //        });
        //      }
        //    }
        //  },
        //  failure -> {
        //
        //  }
        //);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //createTeamInstances();
        setupSettingsButton();
        setupAddTaskButton();
        setupAllTasksButton();
        updateTasksListFromDatabase();
        setupRecyclerView();
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

}
