package org.project.Maps;

import org.project.MapObjects.Animal;
import org.project.MoveVariants.IMoveType;
import org.project.Vector2d;

public class GlobeMap extends AbstractWorldMap {
    public GlobeMap(int width, int height, int wastedEnergy, int startingNoAnimals, int startingEnergy, int numberOfGenes, int minimumEnergy, IMoveType moveType) {
        super(width, height, wastedEnergy, startingNoAnimals, startingEnergy, numberOfGenes, minimumEnergy, moveType);
    }

    public Vector2d nextPosition(Animal animal){
        Vector2d pos = animal.position.add(animal.orientation.toVector());
        animal.energy -= 1;
        if(0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height){
            return pos;
        }
        else if(!(0 <= pos.y && pos.y < height)){
            return new Vector2d(animal.position.x, animal.position.y);
        }
        else{
            int newX = pos.x;
            if(pos.x == -1){
                newX = width - 1;
            }
            else{
                newX = 0;
            }
            return new Vector2d(newX, pos.y);
        }
    }
}
