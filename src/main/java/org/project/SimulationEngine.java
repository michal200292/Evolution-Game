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
        List<Animal> allStillAlive = new LinkedList<>();
        for(int i = 0; i < map.width; i++){
            for(int j = 0; j < map.height; j++){
                List<Animal> stillAlive = new LinkedList<>();
                for(Animal x: map.animals.get(i).get(j)){
                    if(x.energy > 0){
                        stillAlive.add(x);
                        allStillAlive.add(x);
                    }
                    else{
                        x.dayOfDeath = dayNumber;
                        noOfDeadAnimals++;
                        sumOfLifeLengthOfDeadAnimals+=x.age;
                    }
                }
                map.animals.get(i).set(j, stillAlive);
            }
        }
        map.animalList = allStillAlive;

    }

    public void grassConsumption(){
        for(int i = 0; i < map.width; i++){
            for(int j = 0; j < map.height; j++){
                if(map.animals.get(i).get(j).size() == 0){
                    continue;
                }
                map.sortAnimals(map.animals.get(i).get(j));
                if(grassField.grass[i][j].energy > 0 ){
                    map.animals.get(i).get(j).get(0).consume(grassField.plantEnergy);
                    grassField.removeGrass(i, j);
                }
            }
        }
    }

    public void animalReproducing(){
        for(int i = 0; i < map.width; i++){
            for(int j = 0; j < map.height; j++){
                if(map.animals.get(i).get(j).size() >= 2){
                    Animal p1 = map.animals.get(i).get(j).get(0);
                    Animal p2 = map.animals.get(i).get(j).get(1);
                    p1.reproduce(p2, genesCreator);
                }
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
