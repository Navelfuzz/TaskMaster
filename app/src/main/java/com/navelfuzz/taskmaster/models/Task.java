package com.navelfuzz.taskmaster.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import java.util.Date;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    String title;
    String body;
    java.util.Date dateCreated;
    TaskStatusEnum status;

    @Ignore
    public Task(String title){
        this.title = title;
        this.status = TaskStatusEnum.NEW;
    }
    @Ignore
    public Task(String title, String body){
        this.title = title;
        this.body = body;
        this.status = TaskStatusEnum.NEW;
    }

    public Task(String title, String body, Date dateCreated, TaskStatusEnum status){
        this.title = title;
        this.body = body;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    // Status status;
//    public enum Status {
//        NEW,
//        ASSIGNED,
//        IN_PROGRESS,
//        COMPLETE
//    }

//    public Task( String title, String body){
//        this.title = title;
//        this.body = body;
//        this.status = Status.NEW;
//    }

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

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
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

//    public Status getStatus(){
//        return this.status;
//    }
//
//    public void setStatus(Status status){
//        this.status = status;
//    }

}
