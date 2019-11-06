package com.oilpipelinemodel.app;

import java.util.ArrayList;

/*
* Интерфейс для объектов у которых можно рассчитать давление
*
*
* */
interface ICalculatedPipeObject
{
     ArrayList<Double> getCurrVelocity();
     ArrayList<Double> getCurrPressure();
}
