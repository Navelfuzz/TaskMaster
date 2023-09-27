package com.navelfuzz.taskmaster;

import static com.navelfuzz.taskmaster.activities.SettingsActivity.USERNAME_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.navelfuzz.taskmaster.activities.AddTaskActivity;
import com.navelfuzz.taskmaster.activities.AllTasksActivity;
import com.navelfuzz.taskmaster.activities.SettingsActivity;
import com.navelfuzz.taskmaster.adapters.ViewAdapter;
import com.navelfuzz.taskmaster.database.TaskDatabase;
import com.navelfuzz.taskmaster.models.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public static final String TASK_NAME_TAG = "taskName";
    public static final String TASK_DESC_TAG = "taskDesc";
    public static final String TASK_STATUS_TAG = "taskStatus";
    SharedPreferences preferences;

    List<Task> tasks = new ArrayList<>();
    TaskDatabase taskDatabase;
    public static final String DATABASE_NAME = "navelfuzz_tasks";
    ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setupDatabase();
        setupSettingsButton();
        setupAddTaskButton();
        setupAllTasksButton();
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

    void setupDatabase(){
        taskDatabase = Room.databaseBuilder(
                getApplicationContext(), TaskDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

        tasks = taskDatabase.taskDao().findAll();
    }

    void updateTasksListFromDatabase(){
        tasks.clear();
        tasks.addAll(taskDatabase.taskDao().findAll());
        adapter.notifyDataSetChanged();
    }
}


// Removed from previous labs

//    void setupTaskDetailButtons(){
//        Button taskOneDetailButton = findViewById(R.id.MainActivityViewTaskOneButton);
//        taskOneDetailButton.setOnClickListener(view -> {
//            String taskName = ((Button)findViewById(R.id.MainActivityViewTaskOneButton)).getText().toString();
//
//            Intent goToTaskDetailIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
//            goToTaskDetailIntent.putExtra(TASK_NAME_TAG, taskName);
//            startActivity(goToTaskDetailIntent);
//        });
//    }


//    void createTaskInstances(){
//        tasks.add(new Task("First Task", "You need to do some stuff"));
//        tasks.add(new Task("Second Task", "You need to do some stuff"));
//        tasks.add(new Task("Third Task", "You need to do some stuff"));
//        tasks.add(new Task("Fourth Task", "You need to do some stuff"));
//        tasks.add(new Task("Fifth Task", "You need to do some stuff"));
//        tasks.add(new Task("Sixth Task", "You need to do some stuff"));
//        tasks.add(new Task("Seventh Task", "You need to do some stuff"));
//        tasks.add(new Task("Eighth Task", "You need to do some stuff"));
//        tasks.add(new Task("Ninth Task", "You need to do some stuff"));
//        tasks.add(new Task("Tenth Task", "You need to do some stuff"));
//    }