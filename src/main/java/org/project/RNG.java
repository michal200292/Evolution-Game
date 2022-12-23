package org.project;

import java.util.Random;

public class RNG {
    private static final Random r = new Random();
    public static int randomNumber(int a, int b){
        return r.nextInt(a, b + 1);
    }
    public static MoveDirection randomEnum(){
        return MoveDirection.directions[randomNumber(0, 7)];
    }

    public static int[] generateGenes(int n){
        int[] genes = new int[n];
        for(int i = 0; i < n; i++){
            genes[i] = randomNumber(0, 7);
        }
        return genes;
    }

    public static void shuffle(int[] tab){
        int n = tab.length;
        for(int i = 0; i < n; ++i){
            int index1 = RNG.randomNumber(0, n - 1);
            int index2 = RNG.randomNumber(0, n - 1);
            int temp = tab[index1];
            tab[index1] = tab[index2];
            tab[index2] = temp;
        }
    }
}
