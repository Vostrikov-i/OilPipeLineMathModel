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
    @BeforeAll
    static void beforeAll(){
        System.out.println("Starting Test");
    }

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
        mPl=mB.build();
        ArrayList<ArrayList<IConnectedPipeObject>> tmpArr= mPl.getConnectionMap();
        int i=0;
        assertEquals(MaxBranch, tmpArr.size());
        //check Size all Branch. All branch must be equal lenght
        while (i<(tmpArr.size()-1)){
            assertEquals(tmpArr.get(i).size(), tmpArr.get(i+1).size());
        }

        // Check correct BranchPosition
        for(PumpProt currProt: apProt){
            assertEquals(currProt.getBranchPosition(), SearchElementInPipelineMap(currProt.getLinkedObject(),tmpArr));
        }

    }



    // метод поиска добавленного элемента в карте нефтепровода возвращает индекс элемента в ветке или -1 если не нашел
   int SearchElementInPipelineMap(IConnectedPipeObject sO, ArrayList<ArrayList<IConnectedPipeObject>> tmpArr){
        int countbranches=tmpArr.size(); // количество веток
        for (int i=0; i<countbranches; i++){
            for (int j=0; j<tmpArr.get(i).size();j++){
               if(sO.equals(tmpArr.get(i).get(j))){
                   return j;
               }
            }
        }
        return -1;
    }

}
