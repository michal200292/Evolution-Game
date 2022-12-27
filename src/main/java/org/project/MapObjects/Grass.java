package org.project.MapObjects;

import org.project.Vector2d;

public class Grass {
    public int energy;
    public int deadAnimals;
    public Vector2d position;
    public Grass(Vector2d position, int energy){
        this.energy = energy;
        this.position = position;
        this.deadAnimals = 0;
    }
}
