package org.project.GenesCreators;

import org.project.MapObjects.Animal;
import org.project.RNG;

public class GenesCreator {
    IMutation typeOfMutation;
    public GenesCreator(IMutation typeOfMutation){
        this.typeOfMutation = typeOfMutation;
    }

    public void assignGenes(Animal parent, int [] genes, int left, int right){
        if (right + 1 - left >= 0) System.arraycopy(parent.genes, left, genes, left, right + 1 - left);
    }
    public int[] createNewGenes(Animal parent1, Animal parent2){
        int divisionPoint = parent1.energy* parent1.numberOfGenes / (parent1.energy + parent2.energy);
        if (divisionPoint > 0) divisionPoint--;
        int [] newGenes = new int[parent1.numberOfGenes];
        int side = RNG.randomNumber(0, 1);
        if(side == 0){
            assignGenes(parent1, newGenes, 0, divisionPoint);
            assignGenes(parent2, newGenes, divisionPoint + 1, parent1.numberOfGenes - 1);
        }
        else{
            assignGenes(parent2, newGenes, 0, divisionPoint);
            assignGenes(parent1, newGenes, divisionPoint + 1, parent1.numberOfGenes - 1);
        }
        typeOfMutation.mutate(newGenes);
        return newGenes;
    }
}
