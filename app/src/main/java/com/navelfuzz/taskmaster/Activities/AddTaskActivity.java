package com.navelfuzz.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.navelfuzz.taskmaster.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    private final String TAG = "AddTaskActivity";
    private String selectedTeam;
    CompletableFuture<List<Team>> teamsFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        teamsFuture = new CompletableFuture<>();
        setupTeamsRadioButtons();
        setupAddTaskButton();
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
}
