package com.danilloyal.todolist.model;

public class TaskNotFoundException extends RunTimeException{
    
    public TaskNotFoundException (String message){
        super(message);
    }
}