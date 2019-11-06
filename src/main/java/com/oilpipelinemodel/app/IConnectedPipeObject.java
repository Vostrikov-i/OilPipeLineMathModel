package com.oilpipelinemodel.app;

/*
* Интерфейс для объектов которые можно соединять друг с другом
* Получение давлений на входе и выходе неотъемлемая часть соединенных объектов (для этого и нужны соединения)
* поэтому данные методы описаны здесь
* Каждый объект нефтепровода обязан реализовывать данный интерфейс
* */

interface IConnectedPipeObject extends ICalculatedPipeObject {

    Double getInPressure_pos(); //получить давление на входе в участок положительное на прошлом шаге
    Double getInPressure_neg(); // получить давление на входе в участок отрицательное на прошлом шаге
    Double getInVelocity_pos(); // получить скорость на входе в участок положительную на прошлом шаге
    Double getInVelocity_neg(); // получить скорость на входе в участок отрицательную на прошлом шаге

    Double getOutPressure_pos(); //получить давление на выходе из участка положительное на прошлом шаге
    Double getOutPressure_neg(); // получить давление на выходе из участка отрицательное на прошлом шаге
    Double getOutVelocity_pos(); // получить скорость на выходе из участка положительную на прошлом шаге
    Double getOutVelocity_neg(); // получить скорость на выходе из участка отрицательную на прошлом шаге

    IConnectedPipeObject getLeftObject(); // получить объект слева
    IConnectedPipeObject getRightObject(); // получить объект справа
    void setLeftObject(IConnectedPipeObject lO); // установить объект слева
    void setRightObject(IConnectedPipeObject rO); // установить объект справа

    int getNumBranch(); // получить номер ветки нефтепровода
}
