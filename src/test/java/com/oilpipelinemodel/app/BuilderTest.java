package com.oilpipelinemodel.app;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Builders Test")
public class BuilderTest {
    MagistralPipeline mPl;
    MagistralBuilder mB=new MagistralBuilder();

    double setD;
    double setCoeffA;
    double setcoeffB;
    double setMaxSpeed;
    int setNumBranch;
    final int MaxBranch=6;
    final int maxAddObject=15;
    int num1;
    int num2;
    @BeforeAll
    static void beforeAll(){
        System.out.println("Starting Test");
    }
/*
    @Test
    public void TestPumpBilder() {
        setD = 0.2;
        setCoeffA = 0.3;
        setcoeffB = 0.4;
        setMaxSpeed = 100;
       List<PumpProt> apProt=new ArrayList<>();
        int j=0;
        PumpBuilder pB = mB.CreatePump();
        for (int i = 1; i < maxAddObject*MaxBranch; i++) { // добавили по maxAddObject в каждую ветку
            if (j>MaxBranch) {j=0;} else {j++;}
            setNumBranch=j;
            PumpProt pProt = new PumpProt();
            pProt.setDiam(setD);
            pProt.setCoeffA(setCoeffA);
            pProt.setCoeffB(setcoeffB);
            pProt.setMaxSpeed(setMaxSpeed);
            pProt.setNumBranch(setNumBranch);

            pB.createByProt(pProt);
            pProt = pB.build();

            // Test Setters
            assertEquals(setD, pProt.getDiam());
            assertEquals(setCoeffA, pProt.getCoeffA());
            assertEquals(setcoeffB, pProt.getCoeffB());
            assertEquals(setMaxSpeed, pProt.getMaxSpeed());
            assertEquals(setNumBranch, pProt.getNumBranch());
            assertEquals(setNumBranch, pProt.getLinkedObject().getNumBranch());

            //Test Set linked Object and get method for Pressure & Velocity
            assertNotNull(pProt.getPressure());
            assertNotNull(pProt.getVelocity());
            assertEquals(pProt.getPressure().size(), pProt.getVelocity().size());
            //Test Set Branch Position
            apProt.add(pProt);
        }

    }

    @Test
    public void TestConfig(){
        mPl=CreateCorrectConfiguration(mB);
        // Check Map size
        ArrayList<ArrayList<IConnectedPipeObject>> tmpArr= mPl.getConnectionMap();
        int i=0;
        assertEquals(2, tmpArr.size());
        //check Size all Branch. All branch must be equal lenght
        while (i<(tmpArr.size()-1)){
            assertEquals(tmpArr.get(i).size(), tmpArr.get(i+1).size());
            i++;
        }
    }

    MagistralPipeline CreateCorrectConfiguration(MagistralBuilder mB){
        MagistralPipeline mpl;
        //Create Builders;
        STankBuilder stB=mB.CreateStartTank();
        PipeLineBuilder plB=mB.CreatePipeLine();
        DrainPipelineBuilder dpB= mB.CreateDrainPipeline();
        GainPipelineBuilder gpB= mB.CreateGainPipeline();
        PumpBuilder pmpB= mB.CreatePump();
        ETankBuilder etB= mB.CreateEndTank();
        //Temp list
        List<DrainPipeLineProt> tmpD=new ArrayList<>();
        List<GainPipeLineProt> tmpG=new ArrayList<>();

        //Create List Prototype
        List<TankProt> tankProt=new ArrayList<>();
        List<PipelineProt> pipelineProt=new ArrayList<>();
        List<DrainPipeLineProt> drainProt=new ArrayList<>();
        List<GainPipeLineProt> gainProt=new ArrayList<>();
        List<PumpProt> pumpProt=new ArrayList<>();
        //Create Prototype
        TankProt tp=new TankProt();
        PipelineProt pP=new PipelineProt();
        DrainPipeLineProt dp=new DrainPipeLineProt();
        GainPipeLineProt gp=new GainPipeLineProt();
        PumpProt pmpP=new PumpProt();

        //Start Tank
        tp.setDiam(setD);
        tp.setHeight(20);
        tp.setNumBranch(0);

        stB.createByProt(tp);
        tankProt.add(stB.build());
        //Pipeline
        pP.setDiam(setD);
        pP.setLenght(10000);
        pP.setSegmentLen(100);
        pP.setNumBranch(0);

        plB.createByProt(pP);
        pipelineProt.add(plB.build());
        //Drain
        dp.setDiam(setD);
        dp.setNumBranch(0);

        dpB.createByProt(dp);
        tmpD=dpB.build();
        for(DrainPipeLineProt iter: tmpD){
            drainProt.add(iter);
        }

        //Pumps
        pmpP.setNumBranch(0);
        pmpP.setDiam(setD);
        pmpB.createByProt(pmpP);
        pumpProt.add(pmpB.build());

        pmpP.setNumBranch(1);
        pmpP.setDiam(setD);
        pmpB.createByProt(pmpP);
        pumpProt.add(pmpB.build());

        //Gain
        gp.setDiam(setD);
        gp.setNumBranch(0);

        gpB.createByProt(gp);
        tmpG=gpB.build();
        for(GainPipeLineProt iter: tmpG){
            gainProt.add(iter);
        }

        //Pipeline
        pP.setDiam(setD);
        pP.setLenght(10000);
        pP.setSegmentLen(100);
        pP.setNumBranch(0);

        plB.createByProt(pP);
        pipelineProt.add(plB.build());

        //End Tank
        tp.setDiam(setD);
        tp.setHeight(20);
        tp.setNumBranch(0);

        etB.createByProt(tp);
        tankProt.add(etB.build());

        mpl=mB.build();

        //Check correct BranchPosition Tank
        for(TankProt currProt: tankProt){
            System.out.println("---------- Check Tank------------");
            assertEquals(currProt.getBranchPosition(), SearchElementInPipelineMap(currProt.getLinkedObject(),mpl.getConnectionMap()));
        }

        // Check correct BranchPosition Pump
        for(PumpProt currProt: pumpProt){
            System.out.println("---------- Check Pump------------");
            assertEquals(currProt.getBranchPosition(), SearchElementInPipelineMap(currProt.getLinkedObject(),mpl.getConnectionMap()));
        }
        //Check correct BranchPosition Pipeline
        for(PipelineProt currProt: pipelineProt){
            System.out.println("---------- Check Pipeline------------");
            assertEquals(currProt.getBranchPosition(), SearchElementInPipelineMap(currProt.getLinkedObject(),mpl.getConnectionMap()));
        }
        //Check correct BranchPosition Gain
        for(GainPipeLineProt currProt: gainProt){
            System.out.println("---------- Check Gain------------");
            assertEquals(currProt.getBranchPosition(), SearchElementInPipelineMap(currProt.getLinkedObject(),mpl.getConnectionMap()));
        }
        //Check correct BranchPosition Drain
        for(DrainPipeLineProt currProt: drainProt){
            System.out.println("---------- Check Drain------------");
            assertEquals(currProt.getBranchPosition(), SearchElementInPipelineMap(currProt.getLinkedObject(),mpl.getConnectionMap()));
        }


        return mpl;

}

    // метод поиска добавленного элемента в карте нефтепровода возвращает индекс элемента в ветке или -5 если не нашел
   int SearchElementInPipelineMap(IConnectedPipeObject sO, ArrayList<ArrayList<IConnectedPipeObject>> tmpArr){
        int countbranches=tmpArr.size(); // количество веток
        for (int i=0; i<countbranches; i++){
            for (int j=0; j<tmpArr.get(i).size();j++){
               if(sO.equals(tmpArr.get(i).get(j))){
                   return j;
               }
            }
        }
        return -5;
    }
    void CheckTwoNumber(int num1, int num2){
        assertEquals(num1, num2);
    }
*/
}
