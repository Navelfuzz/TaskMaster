package com.navelfuzz.taskmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.navelfuzz.taskmaster.R;
import com.navelfuzz.taskmaster.MainActivity;
import com.navelfuzz.taskmaster.activities.TaskDetailActivity;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;
import java.util.Locale;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.TaskListViewHolder>{
    List<Task> tasks;
    Context callingActivity;

    public ViewAdapter(List<Task> tasks, Context callingActivity){
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);
        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position){
        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.taskFragmentTextView);
        String taskFragmentText = (position+1) + ". " + tasks.get(position).getTitle();
        taskFragmentTextView.setText(taskFragmentText);

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetailIntent = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetailIntent.putExtra(MainActivity.TASK_NAME_TAG, tasks.get(position).getTitle());
            goToTaskDetailIntent.putExtra(MainActivity.TASK_DESC_TAG, tasks.get(position).getBody());
            goToTaskDetailIntent.putExtra(MainActivity.TASK_STATUS_TAG, tasks.get(position).getStatus().toString());
            goToTaskDetailIntent.putExtra(MainActivity.TASK_ID_EXTRA_TAG, tasks.get(position).getId());
            goToTaskDetailIntent.putExtra(MainActivity.TASK_LATITUDE_EXTRA_TAG, tasks.get(position).getLatitude());
            goToTaskDetailIntent.putExtra(MainActivity.TASK_LONGITUDE_EXTRA_TAG, tasks.get(position).getLongitude());
            goToTaskDetailIntent.putExtra(MainActivity.TASK_ADDRESS_EXTRA_TAG, tasks.get(position).getAddress());
            callingActivity.startActivity(goToTaskDetailIntent);
        });
    }

    @Override
    public int getItemCount(){
        return tasks.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
