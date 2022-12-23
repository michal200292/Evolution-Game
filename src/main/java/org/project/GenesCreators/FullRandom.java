package org.project.GenesCreators;

import org.project.RNG;

public class FullRandom extends AbstractMutationClass{
    public FullRandom(int minNo, int maxNo, int numberOfGenes){
        super(minNo, maxNo, numberOfGenes);
    }

    @Override
    public void mutate(int[] genes) {
        int n = genes.length;
        int genesToChange = RNG.randomNumber(minNoOfMutations, maxNoOfMutations);
        RNG.shuffle(randomIndexes);
        for(int i = 0; i < genesToChange; i++){
            genes[randomIndexes[i]] = RNG.randomNumber(0, 7);
        }
    }
}
