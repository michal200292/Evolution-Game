package org.project.GenesCreators;

abstract class AbstractMutationClass implements Mutation{
    int minNoOfMutations;
    int maxNoOfMutations;

    int numberOfGenes;

    int[] randomIndexes;
    public AbstractMutationClass(int minNo, int maxNo, int numberOfGenes) {
        this.minNoOfMutations = minNo;
        this.maxNoOfMutations = maxNo;
        this.numberOfGenes = numberOfGenes;
        this.randomIndexes = new int[numberOfGenes];
        for(int i = 0; i < numberOfGenes; i++){
            randomIndexes[i] = i;
        }
    }
    public abstract void mutate(int[] genes);
}
