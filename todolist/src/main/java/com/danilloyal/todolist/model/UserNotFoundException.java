package com.danilloyal.todolist.model;

public class UserNotFoundException extends RunTimeException{
    
    public UserNotFoundException (String message){
        super(message);
    }
}