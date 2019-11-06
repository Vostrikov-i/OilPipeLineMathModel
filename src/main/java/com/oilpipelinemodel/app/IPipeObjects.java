package com.oilpipelinemodel.app;

import java.util.HashMap;

/*
* Интерфейс для отображаемых объектов нефтепровода
* MagistralPipeLine возвращает массив из IDrawed для того чтобы знать какая конфигурация у нефтепровода
*
* */
public interface IPipeObjects {
    String getTypeObject(); // вернуть строковое описание объекта, для насоса будет Pump, для заслонки Damper и т.д
    /* получить настроечные значения (для участка трубы наприме это длина, количество сегментов, номер нитки, диаметр ) значения отдаем в String потому что их все равно нельзя править и они только для отображения
     */
    HashMap<String, String> getProperty();
}
