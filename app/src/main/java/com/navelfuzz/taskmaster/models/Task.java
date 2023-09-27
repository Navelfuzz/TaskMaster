package com.navelfuzz.taskmaster.models;

public class Task {
    String title;
    String body;
    Status status;
    public enum Status {
        NEW,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETE
    }

    public Task( String title, String body){
        this.title = title;
        this.body = body;
        this.status = Status.NEW;
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

    public Status getStatus(){
        return this.status;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
