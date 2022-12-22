package org.project;

import java.util.Arrays;

public class Animal {
    public int numberOfGenes;
    public MoveDirection orientation;
    public Vector2d position;
    public int energy;
    public int[] genes;
    public int activeGene;
    public int numberOfChildren;

    public Animal(){}

    public Animal(Vector2d position, int energy, int numberOfGenes){
        this.orientation = RNG.randomEnum();
        this.position = position;
        this.energy = energy;
        this.numberOfGenes = numberOfGenes;
        this.genes = RNG.generateGenes(numberOfGenes);
        this.activeGene = RNG.randomNumber(0, numberOfGenes - 1);
        this.numberOfChildren = 0;
    }

    public Animal(Vector2d position, int energy, int numberOfGenes, int[] genes){
        this.orientation = RNG.randomEnum();
        this.position = position;
        this.energy = energy;
        this.numberOfGenes = numberOfGenes;
        this.genes = genes;
        this.activeGene = RNG.randomNumber(0, numberOfGenes - 1);
        this.numberOfChildren = 0;
    }

    @Override
    public String toString(){
        return "Zwierze na pozycji " + position + " o genach " + Arrays.toString(genes);
    }
}
