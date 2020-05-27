package com.oilpipelinemodel.app.Interfaces.Primitives;
/*
* Все объекты которые поддерживают ветвление должны быть соединяемыми и вычисляемыми
*
* */

public interface ForkedObject extends ConnectedObject{
    double getFiPositive();
    double getFiNegative();
    double getJPositive();
    double getJNegative();
    CalculatedPoint getCutPoint();
}
