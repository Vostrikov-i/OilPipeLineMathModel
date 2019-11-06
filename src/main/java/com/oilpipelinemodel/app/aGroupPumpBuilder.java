package com.oilpipelinemodel.app;
/*
*
* Используется абстрактный класс, а не интерфейс потому что метод  addPumpObject надо сделать default видимость
* однако интфейс этого не позволит
* */
abstract class aGroupPumpBuilder {
    abstract void addPumpObject(Pump pp);
}
