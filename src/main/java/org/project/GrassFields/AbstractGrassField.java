package org.project.GrassFields;

import org.project.MapObjects.Grass;
import org.project.Vector2d;

abstract public class AbstractGrassField {

    public int width;
    public int height;
    public int startNoOfPlants;

    public int noOfPlantsDaily;
    public int plantEnergy;
    public Grass[][] grass;


    public AbstractGrassField(int width, int height, int startNoOfPlants, int noOfPlantsDaily, int plantEnergy){
        this.width = width;
        this.height = height;
        this.startNoOfPlants = startNoOfPlants;
        this.noOfPlantsDaily = noOfPlantsDaily;
        this.plantEnergy = plantEnergy;
        grass = new Grass[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Vector2d pos = new Vector2d(i, j);
                grass[i][j] = new Grass(pos,0);
            }
        }
    }

    abstract public void drawGrass(int n);

    abstract public void removeGrass(int x, int y);
}
