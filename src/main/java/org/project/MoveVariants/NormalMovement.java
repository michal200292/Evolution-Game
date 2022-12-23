package org.project.MoveVariants;

public class NormalMovement implements MoveType{
    public int getNext(int pointer, int n){
        return (pointer + 1) % n;
    }
}
