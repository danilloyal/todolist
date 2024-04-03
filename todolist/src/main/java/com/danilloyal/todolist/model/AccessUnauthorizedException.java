package com.danilloyal.todolist.model;

public class AccessUnauthorizedException extends RunTimeException{
    
    public AccessUnauthorizedException (String message){
        super(message);
    }
}