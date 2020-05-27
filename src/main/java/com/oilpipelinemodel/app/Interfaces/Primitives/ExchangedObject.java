package com.oilpipelinemodel.app.Interfaces.Primitives;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedPoint;

public interface ExchangedObject {
    CalculatedPoint getFirstPoint(); //получить левую точку
    CalculatedPoint getLastPoint(); // получить правую точку
    //TODO кандидат на удаление
    ExchangedObject getSafeCopy(); //получить безопасную копию объекта
}
