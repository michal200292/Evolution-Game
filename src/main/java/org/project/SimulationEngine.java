package org.project;

import javafx.application.Platform;
import org.project.GenesCreators.GenesCreator;
import org.project.GenesCreators.IMutation;
import org.project.Maps.WorldMap;
import org.project.MoveVariants.IMoveType;

import java.util.*;

public class SimulationEngine implements Runnable{
    List<IObserver> observers;
    List<Animal> animalList;
    WorldMap map;
    GenesCreator genesCreator;

    List<Animal> animalsToRemove;
    List<Vector2d> arraysToRemove;
    public int moveDelay;
    public SimulationEngine(WorldMap map, int startingNoAnimals, int startingEnergy, int numberOfGenes, IMoveType moveType, IMutation mutation, int moveDelay){
        this.map = map;
        this.genesCreator = new GenesCreator(mutation);
        this.animalList = Collections.synchronizedList(new ArrayList<>());
        this.moveDelay = moveDelay;
        observers = new LinkedList<>();
        for(int i = 0; i < startingNoAnimals; i++){
            Animal newAnimal = new Animal(RNG.randomVector(map.width, map.height), startingEnergy, numberOfGenes, moveType, map);
            animalList.add(newAnimal);
        }
    }

    public void moveAnimals(){
        for(Animal x : animalList){
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
                animalList.remove(x);
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

    public void run(){
        for(int i = 0; i < 20; i++){
            moveAnimals();
            removeDeadAnimals();
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
