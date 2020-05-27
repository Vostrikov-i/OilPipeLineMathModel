package com.oilpipelinemodel.app.Command;

import com.oilpipelinemodel.app.Interfaces.Command.Command;
import com.oilpipelinemodel.app.Interfaces.Primitives.ManagedObject;
/*
* Класс команды для управления
*
* */


public class PipeObjectCommand implements Command {

    private double managedValue;
    private final ManagedObject<Double> managedObject; // объект управления

    public PipeObjectCommand(ManagedObject<Double> managedObject){
        this.managedObject=managedObject;
    }

    public void setManagedValue(double managedValue) {
        this.managedValue = managedValue;
    }

    @Override
    public void execute() {
        managedObject.receiveCommand(managedValue); // отправляем команду к управляемому объекту
    }
}
