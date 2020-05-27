package com.oilpipelinemodel.app.Exceptions;

public class DeleteObjectException  extends Exception{
    private Integer id;
    private String Message;

    public DeleteObjectException(String Message){
        super(Message);
    }
}
