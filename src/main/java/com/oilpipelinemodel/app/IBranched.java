package com.oilpipelinemodel.app;

/*
* Интерфейс разветвляемого объекта
* К ним относятся объекты которые могут соединяться снизу и сверху
* Это интерфейс могут реализовывать не все объекты нефтепровода
* */

interface IBranched extends IConnectedPipeObject {
    IBranched getTopObject(); // получить объект сверху
    IBranched getDownObject(); // получить объект снизу
    void setTopObject(IBranched tO); // установить объект сверху
    void setDownObject(IBranched dO); // установить объект снизу
}
