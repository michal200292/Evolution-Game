package org.project.Maps;

import org.project.MapObjects.Animal;
import org.project.MapObjects.AnimalComparator;
import org.project.MoveVariants.IMoveType;
import org.project.RNG;
import org.project.Vector2d;

import java.util.*;

abstract public class WorldMap{
    public int width;
    public int height;
    public int wastedEnergy;

    public int minimumEnergy;

    public Map<Vector2d, List<Animal>> animals;

    public List<Animal> animalList;


    public WorldMap(int width, int height, int wastedEnergy, int startingNoAnimals, int startingEnergy, int numberOfGenes, int minimumEnergy, IMoveType moveType){
        this.width = width;
        this.height = height;
        this.wastedEnergy= wastedEnergy;
        this.animals = new HashMap<>();
        this.animalList = new ArrayList<>();
        this.minimumEnergy = minimumEnergy;
        for(int i = 0; i < startingNoAnimals; i++){
            Animal newAnimal = new Animal(RNG.randomVector(width, height), startingEnergy, numberOfGenes, moveType, minimumEnergy, this);
            animalList.add(newAnimal);
        }
    }
    abstract public Vector2d nextPosition(Animal animal);

    public void addAnimal(Vector2d pos, Animal animal){
        if(!animals.containsKey(pos)){
            animals.put(pos, new ArrayList<>());
        }
        animals.get(pos).add(animal);
    }

    public void removeAnimal(Vector2d pos, Animal animal){
        animals.get(pos).remove(animal);
        if(animals.get(pos).size() == 0){
            animals.remove(pos);
        }
    }

    public void sortAnimals(List<Animal> listOfAnimals) {
        listOfAnimals.sort(new AnimalComparator());
    }
}
