package org.project.MoveVariants;

import org.project.RNG;

public class CrazyMovement implements MoveType{
    public int getNext(int pointer, int n){
        if(RNG.randomNumber(1, 5) <= 4){
            return (pointer + 1) % n;
        }
        else{
            return (pointer + RNG.randomNumber(1, n - 1)) % n;
        }
    }
}
