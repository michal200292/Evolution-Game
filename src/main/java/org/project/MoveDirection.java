package org.project;

public enum MoveDirection {
    NN,
    NE,
    EE,
    SE,
    SS,
    SW,
    WW,
    NW;

    public static MoveDirection toMoveDirection(int x) throws IllegalArgumentException{
        return switch(x){
            case 0 -> MoveDirection.NN;
            case 1 -> MoveDirection.NE;
            case 2 -> MoveDirection.EE;
            case 3 -> MoveDirection.SE;
            case 4 -> MoveDirection.SS;
            case 5 -> MoveDirection.SW;
            case 6 -> MoveDirection.WW;
            case 7 -> MoveDirection.NW;
            default -> throw new IllegalArgumentException("There is no such direction");
        };
    }
    @Override
    public String toString(){
        return switch (this){
            case NN -> "N";
            case NE -> "NE";
            case EE -> "E";
            case SE -> "SE";
            case SS -> "S";
            case SW -> "SW";
            case WW -> "W";
            case NW -> "NW";
        };
    }

    public Vector2d toVector(){
        return switch (this){
            case NN -> new Vector2d(0, 1);
            case NE -> new Vector2d(1, 1);
            case EE -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, -1);
            case SS -> new Vector2d(0, -1);
            case SW -> new Vector2d(-1, -1);
            case WW -> new Vector2d(-1, 0);
            case NW -> new Vector2d(-1, 1);
        };
    }
}