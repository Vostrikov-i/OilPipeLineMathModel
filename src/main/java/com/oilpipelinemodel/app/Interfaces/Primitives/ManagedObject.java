package com.oilpipelinemodel.app.Interfaces.Primitives;

public interface ManagedObject<T> {
    void receiveCommand(T value);

}
