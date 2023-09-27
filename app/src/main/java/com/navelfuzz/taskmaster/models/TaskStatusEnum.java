package com.navelfuzz.taskmaster.models;

public enum TaskStatusEnum {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private String text;

    TaskStatusEnum(String text) {
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static TaskStatusEnum fromString(String text) {
        for (TaskStatusEnum tse : TaskStatusEnum.values()) {
            if(tse.text.equalsIgnoreCase(text)) {
                return tse;
            }
        }
        return null;
    }

}