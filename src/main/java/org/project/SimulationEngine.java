package org.project;

import javafx.application.Platform;
import org.project.GenesCreators.GenesCreator;
import org.project.GenesCreators.IMutation;
import org.project.GrassFields.AbstractGrassField;
import org.project.Maps.WorldMap;

import java.util.*;

public class SimulationEngine implements Runnable{
    List<IObserver> observers;
    WorldMap map;
    AbstractGrassField grassField;
    GenesCreator genesCreator;

    List<Animal> animalsToRemove;
    List<Vector2d> arraysToRemove;
    public int moveDelay;
    public SimulationEngine(WorldMap map, IMutation mutation, int moveDelay, AbstractGrassField grassField){
        this.map = map;
        this.genesCreator = new GenesCreator(mutation);
        this.moveDelay = moveDelay;
        observers = new LinkedList<>();
        this.grassField = grassField;
    }

    public void moveAnimals(){
        for(Animal x : map.animalList){
            x.move();
        }
    }

    public void removeDeadAnimals() {
        arraysToRemove = new LinkedList<>();
        for (var entry : map.animals.entrySet()) {
            Vector2d key = entry.getKey();
            animalsToRemove = new LinkedList<>();
            for (Animal x : map.animals.get(key)) {
                if (x.energy <= 0) {
                    animalsToRemove.add(x);
                }
            }
            for (Animal x : animalsToRemove){
                map.animalList.remove(x);
                map.animals.get(key).remove(x);
            }
            if(map.animals.get(key).size() == 0){
                arraysToRemove.add(key);
            }
        }
        for(Vector2d key : arraysToRemove){
            map.animals.remove(key);
        }
    }

    public void grassConsumption(){

    }
    public void run(){
        for(int i = 0; i < 1000; i++){
            moveAnimals();
            removeDeadAnimals();
            grassConsumption();
            grassField.drawGrass(grassField.noOfPlantsDaily);
            Platform.runLater(this::informObservers);
            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void informObservers(){
        for(IObserver x: observers){
            x.update();
        }
    }
    public void addObserver(IObserver observer){
        observers.add(observer);
    }
    public void removeObservers(IObserver observer){
        observers.remove(observer);
    }
}
