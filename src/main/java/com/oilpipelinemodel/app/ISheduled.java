package com.oilpipelinemodel.app;

// интерфейс для определения порядка выполнения вычислений у объектов и запуска и остановки вычислений

 interface ISheduled extends IConnectedPipeObject {
     void CalсPressure();
     boolean isRun(); //  получить состояние, запущены ли вычисления или нет
}
