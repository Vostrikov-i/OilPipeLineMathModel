import com.oilpipelinemodel.app.Primitives.EndTank;
import com.oilpipelinemodel.app.Primitives.Pipeline;
import com.oilpipelinemodel.app.Primitives.StartTank;
import com.oilpipelinemodel.app.Primitives.NotReturnValve;

public class Main {

    public static void main(String[] args) {
        StartTank startTank=new StartTank(20,1,20);
        Pipeline pipeLine=new Pipeline(1,100);
        NotReturnValve notReturnValve=new NotReturnValve(1,1);
        EndTank endTank=new EndTank(15,1,20);

        int j=0;
        startTank.setRightObject(pipeLine);
        pipeLine.setLeftObject(startTank);
        pipeLine.setRightObject(endTank);
        pipeLine.addCutPoint();
        pipeLine.addCutPoint();
        pipeLine.addCutPoint();
        notReturnValve.setLeftObject(pipeLine);
        notReturnValve.setRightObject(endTank);
        endTank.setLeftObject(notReturnValve);

        while (true){
            startTank.calcParameters();
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println(startTank);

            pipeLine.calcParameters();
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println(pipeLine);
            notReturnValve.calcParameters();;
            endTank.calcParameters();
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println(endTank);

        }
    }

}
