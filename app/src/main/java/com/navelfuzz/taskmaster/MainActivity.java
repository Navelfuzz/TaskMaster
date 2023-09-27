package com.navelfuzz.taskmaster;

import static com.navelfuzz.taskmaster.activities.SettingsActivity.USERNAME_TAG;

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

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.navelfuzz.taskmaster.activities.AddTaskActivity;
import com.navelfuzz.taskmaster.activities.AllTasksActivity;
import com.navelfuzz.taskmaster.activities.SettingsActivity;
import com.navelfuzz.taskmaster.adapters.ViewAdapter;
import com.amplifyframework.datastore.generated.model.Task;

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

//    Button addTaskButton;
//    Button allTasksButton;
//    ImageView settingsButton;
//    EditText taskInputEditText;
//    RecyclerView taskListRecyclerView;
//    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

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

        TextView usernameTextView = findViewById(R.id.MainActivityTextView);
        usernameTextView.setText(userName + "'s Tasks");
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
                    tasks.clear();
                    for(Task databaseTask : success.getData()){
                        tasks.add(databaseTask);
                    }
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read tasks successfully.")
        );
    }
}
