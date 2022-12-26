package org.project.Maps;

import org.project.Animal;
import org.project.RNG;
import org.project.Vector2d;

public class PortalMap extends WorldMap{
    public PortalMap(int width, int height, int wastedEnergy) {
        super(width, height, wastedEnergy);
    }

    public Vector2d nextPosition(Animal animal){
        Vector2d pos = animal.position.add(animal.orientation.toVector());
        if(0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height){
            animal.energy -= 1;
            return pos;
        }
        else{
            animal.energy -= this.wastedEnergy;
            return RNG.randomVector(width, height);
        }
    }
}
