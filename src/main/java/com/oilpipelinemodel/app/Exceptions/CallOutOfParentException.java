package com.oilpipelinemodel.app.Exceptions;

/*
* Исключение выбрасывается в момент когда пытаемся вызвать методы вычисления у PipePoint, при этом работая с ее копией
* нужно для того, чтобы обратить внимание разработчика на то, что у копии не надо вызывать метод calc
* */
public class CallOutOfParentException extends Exception{
    private Integer id;
    private String Message;

    public CallOutOfParentException(String Message){
        super(Message);
    }
}
