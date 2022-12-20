package org.project;

import java.util.Random;

public class RNG {
    private static final Random r = new ThreadLocal<Random>().get();
    public static int randomNumber(int a, int b){
        return r.nextInt(a, b + 1);
    }
    public static MoveDirection randomEnum(){
        return MoveDirection.directions[randomNumber(0, 7)];
    }

    public static int[] generateGenes(){
        int[] genes = new int[Animal.numberOfGenes];
        for(int i = 0; i < Animal.numberOfGenes; i++){
            genes[i] = randomNumber(0, 7);
        }
        return genes;
    }
}
