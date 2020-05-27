package com.oilpipelinemodel.app.Interfaces.Primitives;

public interface CalculatedPoint {
    double getPressure();
    double getVelocity();
    double getPreviousPressure();
    double getPreviousVelocity();
    double getPositivePressure();
    double getNegativePressure();
    double getPrevPositivePressure();
    double getPrevNegativePressure();
    double getPositiveVelocity();
    double getNegativeVelocity();
    double getPrevPositiveVelocity();
    double getPrevNegativeVelocity();
}
