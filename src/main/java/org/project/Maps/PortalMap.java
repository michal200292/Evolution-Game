package org.project.Maps;

import org.project.MapObjects.Animal;
import org.project.MoveVariants.IMoveType;
import org.project.RNG;
import org.project.Vector2d;

public class PortalMap extends WorldMap{
    public PortalMap(int width, int height, int wastedEnergy, int startingNoAnimals, int startingEnergy, int numberOfGenes, int minimumEnergy, IMoveType moveType) {
        super(width, height, wastedEnergy, startingNoAnimals, startingEnergy, numberOfGenes, minimumEnergy, moveType);
    }

    public Vector2d nextPosition(Animal animal){
        Vector2d pos = animal.position.add(animal.orientation.toVector());
        if(0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height){
            animal.energy -= 1;
            return pos;
        }
        else{
            animal.orientation = animal.orientation.opposite(animal.orientation);
            animal.energy -= this.wastedEnergy;
            return RNG.randomVector(width, height);
        }
    }
}
