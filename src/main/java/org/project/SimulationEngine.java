package org.project;

import javafx.application.Platform;
import org.project.GenesCreators.GenesCreator;
import org.project.GenesCreators.IMutation;
import org.project.GrassFields.AbstractGrassField;
import org.project.MapObjects.Animal;
import org.project.Maps.AbstractWorldMap;
import org.project.gui.IObserver;

import java.util.*;

public class SimulationEngine implements Runnable{
    List<IObserver> observers;
    public boolean isPaused;

    public int dayNumber;
    public int noOfDeadAnimals;
    public int sumOfLifeLengthOfDeadAnimals;

    AbstractWorldMap map;
    AbstractGrassField grassField;
    GenesCreator genesCreator;

    List<Animal> animalsToRemove;
    List<Vector2d> arraysToRemove;
    public int moveDelay;
    public SimulationEngine(AbstractWorldMap map, IMutation mutation, int moveDelay, AbstractGrassField grassField){
        this.dayNumber = 0;
        this.isPaused = false;
        this.map = map;
        this.genesCreator = new GenesCreator(mutation);
        this.moveDelay = moveDelay;
        observers = new LinkedList<>();
        this.grassField = grassField;
        this.noOfDeadAnimals = 0;
        this.sumOfLifeLengthOfDeadAnimals = 0;
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
                    noOfDeadAnimals++;
                    sumOfLifeLengthOfDeadAnimals+=x.age;
                }
            }
            for (Animal x : animalsToRemove){
                x.dayOfDeath = dayNumber;
                map.animalList.remove(x);
                map.animals.get(key).remove(x);
                grassField.grass[x.position.x][x.position.y].deadAnimals++;
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
        for (var entry : map.animals.entrySet()){
            Vector2d key = entry.getKey();
            map.sortAnimals(map.animals.get(key));
            if(grassField.grass[key.x][key.y].energy > 0) {
                map.animals.get(key).get(0).consume(grassField.plantEnergy);
                grassField.removeGrass(key.x, key.y);
            }
        }
    }

    public void animalReproducing(){
        for (var entry : map.animals.entrySet()){
            Vector2d key = entry.getKey();
            if(map.animals.get(key).size() >= 2){
                Animal p1 = map.animals.get(key).get(0);
                Animal p2 = map.animals.get(key).get(1);
                p1.reproduce(p2, genesCreator);
            }
        }
    }
    public void run(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(;;){
            if(!isPaused) {
                moveAnimals();
                removeDeadAnimals();
                grassConsumption();
                animalReproducing();
                grassField.drawGrass(grassField.noOfPlantsDaily);
                dayNumber++;
            }
            Platform.runLater(this::informObservers);
            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
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
