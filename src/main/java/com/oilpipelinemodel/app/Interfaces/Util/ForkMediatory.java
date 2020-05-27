package com.oilpipelinemodel.app.Interfaces.Util;

import com.oilpipelinemodel.app.Interfaces.Primitives.ForkedObject;

public interface ForkMediatory {
    void appendFork(ForkedObject forkedObject); //добавление объекта к посреднику
    ForkedObject[] getLinkedForkData(ForkedObject forkedObject); // вернуть данные от связанных с forkedObject объектов
}
