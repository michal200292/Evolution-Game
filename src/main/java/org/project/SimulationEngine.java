package org.project;

import org.project.GenesCreators.GenesCreator;
import org.project.GenesCreators.IMutation;
import org.project.Maps.WorldMap;
import org.project.MoveVariants.IMoveType;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SimulationEngine {
    List<Animal> animalList;
    WorldMap map;

    GenesCreator genesCreator;
    public SimulationEngine(WorldMap map, int startingNoAnimals, int startingEnergy, int numberOfGenes, IMoveType moveType, IMutation mutation){
        this.map = map;
        this.genesCreator = new GenesCreator(mutation);
        this.animalList = new ArrayList<>();
        for(int i = 0; i < startingNoAnimals; i++){
            Animal newAnimal = new Animal(RNG.randomVector(map.width, map.height), startingEnergy, numberOfGenes, moveType, map);
            animalList.add(newAnimal);
        }
    }

    public void run(){
        for(int i = 0; i < 10; i++){
            for(Animal x : animalList){
                x.move();
            }
            map.totoString();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            for(var entry : map.animals.entrySet()){
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
            for(Animal x : animalList){
                System.out.println(x.energy);
            }
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }

    }
}
