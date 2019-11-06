package com.oilpipelinemodel.app;

import java.util.ArrayList;

public interface IAdapted
{
    ArrayList<Double> getCurr_pressure();
    ArrayList<Double> getCurr_velocity();
    void setConfig(Object obj); // this method upload and set config for PipelineBuilder from Server
    byte[] reciveCommand(byte[] command); // this method recive command from Server for PipelineBuilder


}
