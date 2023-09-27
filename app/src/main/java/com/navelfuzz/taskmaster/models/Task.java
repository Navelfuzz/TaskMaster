package com.navelfuzz.taskmaster.models;



import java.util.Date;


public class Task {
    String title;
    String body;
    java.util.Date dateCreated;
    TaskStatusEnum status;

    public Task(String title, String body, Date dateCreated, TaskStatusEnum status){
        this.title = title;
        this.body = body;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getBody(){
        return this.body;
    }

    public void setBody(String body){
        this.body = body;
    }


    public Date getDateCreated(){
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated){
        this.dateCreated = dateCreated;
    }

    public TaskStatusEnum getStatus(){
        return status;
    }
    public void setStatus(TaskStatusEnum status){
        this.status = status;
    }
}
