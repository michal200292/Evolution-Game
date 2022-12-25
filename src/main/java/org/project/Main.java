package org.project;

// 3 stycznia

import org.project.GenesCreators.FullRandom;
import org.project.GenesCreators.GenesCreator;
import org.project.MoveVariants.CrazyMovement;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        Animal one = new Animal(new Vector2d(1, 1), 20, 10, new CrazyMovement());
        Animal two = new Animal(new Vector2d(1, 1), 20, 10, new CrazyMovement());
        GenesCreator tak = new GenesCreator(new FullRandom(2, 6, 10));
        System.out.println(Arrays.toString(tak.createNewGenes(one, two)));
    }
}
