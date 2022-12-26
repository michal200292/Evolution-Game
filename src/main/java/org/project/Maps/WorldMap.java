package org.project.Maps;

import org.project.Animal;
import org.project.Vector2d;

import java.util.*;

abstract public class WorldMap{
    public int width;
    public int height;
    public int wastedEnergy;

    public Map<Vector2d, List<Animal>> animals;


    public WorldMap(int width, int height, int wastedEnergy){
        this.width = width;
        this.height = height;
        this.wastedEnergy= wastedEnergy;
        this.animals = new HashMap<>();
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


    public void totoString() {
        for(int i = height - 1; i >= 0; i--){
            for(int j = 0; j < width; j++){
                Vector2d cur = new Vector2d(j, i);
                if(animals.containsKey(cur)){
                    System.out.print(animals.get(cur));
                }
                else{
                    System.out.print("*");
                }
            }
            System.out.println();
        }
    }
}
