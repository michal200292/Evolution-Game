package org.project.GrassFields;

import org.project.RNG;
import org.project.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class EquatorForest extends AbstractGrassField{
    public int equatorBegin;
    public int equatorEnd;
    public List<Vector2d> freePlacesOnEquator;
    public List<Vector2d> freePlacesOutsideEquator;

    public EquatorForest(int width, int height, int startNoOfPlants, int noOfPlantsDaily, int plantEnergy){
        super(width, height, startNoOfPlants, noOfPlantsDaily, plantEnergy);
        this.equatorBegin = (height / 2) - (height / 10);
        this.equatorEnd = (height / 2) + (height / 10);
        this.freePlacesOnEquator = new ArrayList<>();
        this.freePlacesOutsideEquator = new ArrayList<>();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < equatorBegin; j++){
                freePlacesOutsideEquator.add(grass[i][j].position);
            }
            for(int j = equatorBegin; j < equatorEnd; j++){
                freePlacesOnEquator.add(grass[i][j].position);
            }
            for(int j = equatorEnd; j < height; j++){
                freePlacesOutsideEquator.add(grass[i][j].position);
            }
        }
        drawGrass(startNoOfPlants);
    }

    public void findPlace(List<Vector2d> freePlaces){
        int index = RNG.randomNumber(0, freePlaces.size() - 1);
        Vector2d pos = freePlaces.get(index);
        grass[pos.x][pos.y].energy = plantEnergy;
        freePlaces.remove(pos);
    }

    public void drawGrass(int n){
        for(int i = 0; i < n; i++){
            if(freePlacesOnEquator.size() ==0 && freePlacesOutsideEquator.size()==0){
                break;
            }
            if(RNG.randomNumber(1, 5) <= 4){
                if(freePlacesOnEquator.size() == 0){
                    findPlace(freePlacesOutsideEquator);
                }
                else {
                    findPlace(freePlacesOnEquator);
                }
            }
            else{
                if(freePlacesOutsideEquator.size() == 0){
                    findPlace(freePlacesOnEquator);
                }
                else{
                    findPlace(freePlacesOutsideEquator);
                }
            }
        }
    }

    public void removeGrass(int x, int y){
        grass[x][y].energy = 0;
        if(y < equatorBegin || y >= equatorEnd){
            freePlacesOutsideEquator.add(grass[x][y].position);
        }
        else{
            freePlacesOnEquator.add(grass[x][y].position);
        }
    }
}
