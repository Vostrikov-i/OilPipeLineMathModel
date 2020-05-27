package com.oilpipelinemodel.app.Interfaces.Primitives;

/*
* Все соединяемые объекты должны быть вычисляемыми
*
* */
public interface ConnectedObject extends CalculatedObject {

    void setLeftObject(ExchangedObject exchangedObject); // установить объект слева
    void setRightObject(ExchangedObject exchangedObject); // установить объект справа

}
