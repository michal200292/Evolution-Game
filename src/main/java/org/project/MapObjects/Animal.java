package org.project.MapObjects;

import org.project.GenesCreators.GenesCreator;
import org.project.Maps.WorldMap;
import org.project.MoveDirection;
import org.project.MoveVariants.IMoveType;
import org.project.RNG;
import org.project.Vector2d;

import java.util.Arrays;

public class Animal{
    public int numberOfGenes;
    public MoveDirection orientation;
    public Vector2d position;
    public int energy;
    public int[] genes;
    public int activeGene;
    public int numberOfChildren;

    public int minimumEnergy;

    public int age;

    public IMoveType nextMove;

    public WorldMap map;

    public Animal(){}

    public Animal(Vector2d position, int energy, int numberOfGenes, IMoveType nextMove, int minimumEnergy, WorldMap map){
        this.position = position;
        this.energy = energy;
        this.numberOfGenes = numberOfGenes;
        this.genes = RNG.generateGenes(numberOfGenes);
        this.activeGene = RNG.randomNumber(0, numberOfGenes - 1);
        this.numberOfChildren = 0;
        this.nextMove = nextMove;
        this.map = map;
        this.map.addAnimal(position, this);
        this.age = 0;
        this.minimumEnergy = minimumEnergy;
    }

    public Animal(Vector2d position, int energy, int numberOfGenes, int[] genes, IMoveType nextMove, int minimumEnergy, WorldMap map){
        this.position = position;
        this.energy = energy;
        this.numberOfGenes = numberOfGenes;
        this.genes = genes;
        this.activeGene = RNG.randomNumber(0, numberOfGenes - 1);
        this.numberOfChildren = 0;
        this.nextMove = nextMove;
        this.map = map;
        this.map.addAnimal(position, this);
        this.map.animalList.add(this);
        this.age = 0;
        this.minimumEnergy = minimumEnergy;
    }

    public void move(){
        orientation = MoveDirection.directions[genes[activeGene]];
        Vector2d nextPos = map.nextPosition(this);
        map.addAnimal(nextPos, this);
        map.removeAnimal(position, this);
        age++;
        this.position = nextPos;
        activeGene = nextMove.getNext(activeGene, numberOfGenes);
    }

    @Override
    public String toString(){
        return "Zwierze na pozycji " + position + " o genach " + Arrays.toString(genes);
    }

    public void reproduce(Animal other, GenesCreator genesCreator){
        if(energy >= minimumEnergy && other.energy >= minimumEnergy) {
            int[] newGenes = genesCreator.createNewGenes(this, other);
            this.energy -= map.wastedEnergy;
            other.energy -= map.wastedEnergy;
            new Animal(this.position, 2*map.wastedEnergy, this.numberOfGenes, newGenes, this.nextMove, this.minimumEnergy, map);
        }
    }

}
