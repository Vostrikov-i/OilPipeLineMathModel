package com.oilpipelinemodel.app;
import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ExchangedObject;
import com.oilpipelinemodel.app.Interfaces.Repositoryes.MediatorModelRepo;
import com.oilpipelinemodel.app.Interfaces.Repositoryes.SimpleRepository;
import com.oilpipelinemodel.app.Interfaces.Wrappers.Wrappers;
import com.oilpipelinemodel.app.Repository.Manager.ManagerRepository;
import com.oilpipelinemodel.app.Repository.Manager.PipeObjectsRepoManager;
import com.oilpipelinemodel.app.Repository.PipelineRepository;
import com.oilpipelinemodel.app.Utils.PairPosition;
import com.oilpipelinemodel.app.Wrappers.PipelineWrapper;
import com.oilpipelinemodel.app.Wrappers.WrapperComparator;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Repository")
public class TestCase1 {

    @BeforeAll
    static void beforeAll(){
        System.out.println("Starting Test");
    }



    @Test
    @DisplayName("Test set link without repository")
    @Order(1)
    public void checkSetLinkObjects(){
        Wrappers wrappersOne = new PipelineWrapper(PairPosition.newInstance(0, 1), 2.0, 1); // первый объект
        Wrappers wrappersTwo = new PipelineWrapper(PairPosition.newInstance(0, 1), 2.0, 1); // второй объект
        Wrappers wrappersLeft = new PipelineWrapper(PairPosition.newInstance(1, 1), 2.0, 1); // объект для присоединения

        //назначаем одинаковые объекты
        wrappersOne.setLeftObject(wrappersLeft.getExchangedInstance());
        wrappersTwo.setLeftObject(wrappersLeft.getExchangedInstance());
        //получаем назначенные объекты
        ExchangedObject oneLeftObject=wrappersOne.getLeftObject();
        ExchangedObject twoLeftObject=wrappersTwo.getLeftObject();

        assertNotNull(oneLeftObject, "First left object is null");
        assertNotNull(twoLeftObject, "Second left object is null");


    }


    @Test
    @DisplayName("Test Equals methods in exchanged object")
    @Order(2)
    public void checkEquals(){
        Wrappers wrappersOne = new PipelineWrapper(PairPosition.newInstance(0, 1), 2.0, 1); // первый объект
        Wrappers wrappersTwo = new PipelineWrapper(PairPosition.newInstance(0, 1), 2.0, 1); // второй объект
        Wrappers wrappersLeft = new PipelineWrapper(PairPosition.newInstance(1, 1), 2.0, 1); // объект для присоединения
        Wrappers wrappersLeftTwo = new PipelineWrapper(PairPosition.newInstance(1, 1), 2.0, 2); // второй объект для присоединения

        //назначаем одинаковые объекты
        wrappersOne.setLeftObject(wrappersLeft.getExchangedInstance());
        wrappersTwo.setLeftObject(wrappersLeft.getExchangedInstance());
      //получаем назначенные объекты
        ExchangedObject oneLeftObject=wrappersOne.getLeftObject();
        ExchangedObject twoLeftObject=wrappersTwo.getLeftObject();


        assertTrue(oneLeftObject.equals(twoLeftObject), "Equals objects is not equal");
        wrappersTwo.setLeftObject(wrappersLeftTwo.getExchangedInstance()); //меняем левый объект
        //обновляем значения для левого и правого объектов
         oneLeftObject=wrappersOne.getLeftObject();
         twoLeftObject=wrappersTwo.getLeftObject();
       assertFalse(oneLeftObject.equals(twoLeftObject), "Not Equals objects is equal ");

    }

    @Test
    @DisplayName("Test add objects in repository")
    @Order(3)
    public void testAddToRepository(){
        ManagerRepository managerRepository=new PipeObjectsRepoManager();
        SimpleRepository<PipelineWrapper> testRepository=new PipelineRepository(managerRepository);

        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 0), 2.0, 2));
        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 1), 2.0, 2));
        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 2), 2.0, 2));
        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 3), 2.0, 2));
        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 3), 2.0, 2));

        //проверяем добавление объектов
        MediatorModelRepo<PairPosition, CalculatedObject> repo= (MediatorModelRepo<PairPosition, CalculatedObject>) managerRepository;
        Map<PairPosition,CalculatedObject> repoMap=repo.getObjectsMap();
        assertTrue(repoMap.size()==4, "No correct add object in repo");

    }



    @Test
    @DisplayName("Check link object after add in repository")
    @Order(4)
    public void testLinkObject(){
        ManagerRepository managerRepository=new PipeObjectsRepoManager();
        SimpleRepository<PipelineWrapper> testRepository=new PipelineRepository(managerRepository);

        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 0), 2.0, 2));
        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 1), 2.0, 2));
        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 2), 2.0, 2));
        testRepository.add(new PipelineWrapper(PairPosition.newInstance(1, 3), 2.0, 2));

        MediatorModelRepo<PairPosition, CalculatedObject> repo= (MediatorModelRepo<PairPosition, CalculatedObject>) managerRepository;
        Map<PairPosition,CalculatedObject> repoMap=repo.getObjectsMap();

        List<CalculatedObject> list=repoMap.values().stream().collect(Collectors.toList());
        List<Wrappers> wrappersList=new ArrayList<>();
        for(CalculatedObject calculatedObject: list){
            wrappersList.add((Wrappers) calculatedObject); //приводим к Wrappers
        }
        wrappersList.sort(new WrapperComparator()); // отсортировали по Branch
        //проверяем начальные и конечные точки
        //начальные точки
        assertNull(wrappersList.get(0).getLeftObject(), "Uncorrected left link fo object 0");
        assertTrue(wrappersList.get(0).getRightObject()==wrappersList.get(1).getExchangedInstance(), "Uncorrected right link fo object 0");
        //конечную точку
        assertNull(wrappersList.get(wrappersList.size()-1).getRightObject(), "Uncorrected right link fo object "+wrappersList.size());
        assertTrue(wrappersList.get(wrappersList.size()-1).getLeftObject()==wrappersList.get(wrappersList.size()-2).getExchangedInstance(), "Uncorrected right link fo object "+wrappersList.size());

        //проверяем точки между первой и последней
        for (int i=1; i<wrappersList.size()-1; i++){
            assertTrue(wrappersList.get(i).getLeftObject()==wrappersList.get(i-1).getExchangedInstance(), "Left Object: "+wrappersList.get(i).getLeftObject()+"not equal set left object "+wrappersList.get(i-1).getExchangedInstance()+"Index "+i);
            assertTrue(wrappersList.get(i).getRightObject()==wrappersList.get(i+1).getExchangedInstance(),"Right Object: "+wrappersList.get(i).getLeftObject()+"not equal set right object "+wrappersList.get(i+1).getExchangedInstance()+"Index "+i);
        }


    }

}
