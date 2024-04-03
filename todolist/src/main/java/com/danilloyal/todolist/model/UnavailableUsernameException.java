package com.danilloyal.todolist.model;

public class UnavailableUsernameException extends RunTimeException{
    
    public UnavailableUsernameException (String message){
        super(message);
    }
}