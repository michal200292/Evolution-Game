package org.project.GenesCreators;

import org.project.RNG;

public class LittleChange extends AbstractMutationClass{
    public LittleChange(int minNo, int maxNo, int numberOfGenes){
        super(minNo, maxNo, numberOfGenes);
    }

    @Override
    public void mutate(int[] genes) {
        int n = genes.length;
        int genesToChange = RNG.randomNumber(minNoOfMutations, maxNoOfMutations);
        RNG.shuffle(randomIndexes);
        for(int i = 0; i < genesToChange; i++){
            int number = genes[randomIndexes[i]];
            int r = RNG.randomNumber(0, 1);
            if (r == 0) r = -1;
            number = (number + 8 + r) % 8;
            genes[randomIndexes[i]] = number;
        }
    }
}
