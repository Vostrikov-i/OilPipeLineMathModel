package com.oilpipelinemodel.app.Factory;
import com.oilpipelinemodel.app.Interfaces.Factory.ForkFactory;
import com.oilpipelinemodel.app.Primitives.ForkPipeline;
import com.oilpipelinemodel.app.Interfaces.Primitives.ForkedObject;
import com.oilpipelinemodel.app.Interfaces.Util.ForkMediatory;
import com.oilpipelinemodel.app.Utils.ForkTwoBranchMediator;

/*
* Фабрика для создания двух ForkPipeline связанных между собой медиатором
*
*
* */

public class TwoForkPipelineFactory implements ForkFactory {

    private double[] diameters=new double[2];
    private ForkPipeline[] forkPipelines=new ForkPipeline[2];

    public TwoForkPipelineFactory(double diameterFork1, double diameterFork2){
        ForkMediatory forkTwoBranchMediator=new ForkTwoBranchMediator();
        //длину выставляем минимально возможную, это физически оправдано
        forkPipelines[0]=new ForkPipeline(diameterFork1, 1, forkTwoBranchMediator);
        forkPipelines[1]=new ForkPipeline(diameterFork2, 1, forkTwoBranchMediator);
    }

    @Override
    public ForkedObject[] getObjects() {
        return forkPipelines;
    }
}
