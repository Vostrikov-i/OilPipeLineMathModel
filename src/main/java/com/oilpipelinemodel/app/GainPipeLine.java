package com.oilpipelinemodel.app;

import java.util.ArrayList;

public class GainPipeLine extends aPipeObject implements ISheduled, IBranched {

    private IBranched top_object;
    private IBranched down_object;
    private IConnectedPipeObject left_object;
    private IConnectedPipeObject right_object;


    private void CalcPressPoint(int numPoint) {

    }


    @Override
    public IBranched getTopObject() {
        return top_object;
    }

    @Override
    public IBranched getDownObject() {

        return down_object;
    }

    @Override
    public void setTopObject(IBranched tO) {
        top_object=tO;
    }

    @Override
    public void setDownObject(IBranched dO) {
        down_object=dO;
    }

    @Override
    public void CalсPressure() {

    }

    @Override
    public boolean isRun() {
        return false;
    }

    @Override
    public ArrayList<Double> getCurrVelocity() {
        return null;
    }

    @Override
    public ArrayList<Double> getCurrPressure() {
        return null;
    }

    @Override
    public Double getInPressure_pos() {
        return null;
    }

    @Override
    public Double getInPressure_neg() {
        return null;
    }

    @Override
    public Double getInVelocity_pos() {
        return null;
    }

    @Override
    public Double getInVelocity_neg() {
        return null;
    }

    @Override
    public Double getOutPressure_pos() {
        return null;
    }

    @Override
    public Double getOutPressure_neg() {
        return null;
    }

    @Override
    public Double getOutVelocity_pos() {
        return null;
    }

    @Override
    public Double getOutVelocity_neg() {
        return null;
    }

    @Override
    public IConnectedPipeObject getLeftObject() {
        return left_object;
    }

    @Override
    public IConnectedPipeObject getRightObject() {
        return right_object;
    }

    @Override
    public void setLeftObject(IConnectedPipeObject lO) {
        left_object=lO;
    }

    @Override
    public void setRightObject(IConnectedPipeObject rO) {
        right_object=rO;
    }

    @Override
    public int getNumBranch() {
        return this.getNumberBranch();
    }



}
