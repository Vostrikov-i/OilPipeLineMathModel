package com.oilpipelinemodel.app;
/*
* Интерфейс работы с управляемыми объектами, такими как насосы и заслонки (им можно изменять частоту вращения или процент открытия в процессе рассчета)
*
*
* */
public interface IManaged extends IPipeObjects{

    boolean sendCommand(); // true - если команда прошла успешно

}
