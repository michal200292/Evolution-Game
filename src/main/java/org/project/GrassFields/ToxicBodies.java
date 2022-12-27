package org.project.GrassFields;

import org.project.RNG;
import org.project.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class ToxicBodies extends AbstractGrassField{

    public List<Vector2d> freePlaces;

    public ToxicBodies(int width, int height, int startNoOfPlants, int noOfPlantsDaily, int plantEnergy){
        super(width, height, startNoOfPlants, noOfPlantsDaily, plantEnergy);
        freePlaces = new ArrayList<>();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                freePlaces.add(grass[i][j].position);
            }
        }
    }

    public void drawGrass(int n){
        freePlaces.sort((o1, o2) -> grass[o1.x][o1.y].deadAnimals - grass[o2.x][o2.y].deadAnimals);
        for(int i = 0; i < n; i++) {
            if (freePlaces.size() == 0) {
                break;
            }
            int k = freePlaces.size();
            int index;
            if (RNG.randomNumber(1, 5) <= 4) {
                index = RNG.randomNumber(0, k / 5);
            }
            else{
                index = RNG.randomNumber(k / 5, k - 1);
            }
            Vector2d pos = freePlaces.get(index);
            grass[pos.x][pos.y].energy = plantEnergy;
            freePlaces.remove(pos);
        }
    }
    public void removeGrass(int x, int y){
        grass[x][y].energy = 0;
        freePlaces.add(grass[x][y].position);
    }
}
