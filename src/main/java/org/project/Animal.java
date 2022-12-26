package org.project;

import org.project.Maps.WorldMap;
import org.project.MoveVariants.IMoveType;

import java.util.Arrays;

public class Animal {
    public int numberOfGenes;
    public MoveDirection orientation;
    public Vector2d position;
    public int energy;
    public int[] genes;
    public int activeGene;
    public int numberOfChildren;

    public IMoveType nextMove;

    public WorldMap map;

    public Animal(){}

    public Animal(Vector2d position, int energy, int numberOfGenes, IMoveType nextMove, WorldMap map){
        this.position = position;
        this.energy = energy;
        this.numberOfGenes = numberOfGenes;
        this.genes = RNG.generateGenes(numberOfGenes);
        this.activeGene = RNG.randomNumber(0, numberOfGenes - 1);
        this.numberOfChildren = 0;
        this.nextMove = nextMove;
        this.map = map;
        this.map.addAnimal(position, this);
    }

    public Animal(Vector2d position, int energy, int numberOfGenes, int[] genes, IMoveType nextMove, WorldMap map){
        this.position = position;
        this.energy = energy;
        this.numberOfGenes = numberOfGenes;
        this.genes = genes;
        this.activeGene = RNG.randomNumber(0, numberOfGenes - 1);
        this.numberOfChildren = 0;
        this.nextMove = nextMove;
        this.map = map;
        this.map.addAnimal(position, this);
    }

    public void move(){
        orientation = MoveDirection.directions[activeGene];
        Vector2d nextPos = map.nextPosition(this);
        map.addAnimal(nextPos, this);
        map.removeAnimal(position, this);
        this.position = nextPos;
        activeGene = nextMove.getNext(activeGene, numberOfGenes);
    }

    @Override
    public String toString(){
        return "Zwierze na pozycji " + position + " o genach " + Arrays.toString(genes);
    }
}
