package com.oilpipelinemodel.app.Exceptions;

public class AddObjectException  extends Exception{
    private Integer id;
    private String Message;

    public AddObjectException(String Message){
        super(Message);
    }
}