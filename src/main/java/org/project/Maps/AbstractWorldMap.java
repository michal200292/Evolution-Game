package org.project.Maps;

import org.project.MapObjects.Animal;
import org.project.MapObjects.AnimalComparator;
import org.project.MoveVariants.IMoveType;
import org.project.RNG;
import org.project.Vector2d;

import java.util.*;

abstract public class AbstractWorldMap {
    public int width;
    public int height;
    public int wastedEnergy;

    public int minimumEnergy;

    public List<List<List<Animal>>> animals;

    public List<Animal> animalList;


    public AbstractWorldMap(int width, int height, int wastedEnergy, int startingNoAnimals, int startingEnergy, int numberOfGenes, int minimumEnergy, IMoveType moveType){
        this.width = width;
        this.height = height;
        this.wastedEnergy= wastedEnergy;
        this.animals = new ArrayList<>(width);
        for(int i = 0; i < width; i++){
            this.animals.add(new ArrayList<>(height));
            for(int j = 0; j < height; j++){
                this.animals.get(i).add(new LinkedList<>());
            }
        }
        this.animalList = new LinkedList<>();
        this.minimumEnergy = minimumEnergy;
        for(int i = 0; i < startingNoAnimals; i++){
            Animal newAnimal = new Animal(RNG.randomVector(width, height), startingEnergy, numberOfGenes, moveType, minimumEnergy, this);
            animalList.add(newAnimal);
        }
    }
    abstract public Vector2d nextPosition(Animal animal);

    public void addAnimal(Vector2d pos, Animal animal){
        animals.get(pos.x).get(pos.y).add(animal);
    }

    public void removeAnimal(Vector2d pos, Animal animal){
        animals.get(pos.x).get(pos.y).remove(animal);
    }

    public void sortAnimals(List<Animal> listOfAnimals) {
        listOfAnimals.sort(new AnimalComparator());
    }
}
