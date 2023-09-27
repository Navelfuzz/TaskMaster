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
            callingActivity.startActivity(goToTaskDetailIntent);
        });
    }

    @Override
    public int getItemCount(){
        return tasks.size();
    }

//    private String formatDateString(Task task){
//        DateFormat dateCreatedIso8601InputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//        dateCreatedIso8601InputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        DateFormat dateCreatedOutputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateCreatedOutputFormat.SetTimeZone(TimeZone.getDefault());
//        String dateCreatedString = "";
//
//        try {
//            Date dateCreatedJavaDate = dateCreatedIso8601InputFormat.parse(task.getDateCreated().format());
//            if (dateCreatedJavaDate != null) {
//                dateCreatedString = dateCreatedOutputFormat.format(dateCreatedJavaDate);
//            }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        return dateCreatedString;
//    }




    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
