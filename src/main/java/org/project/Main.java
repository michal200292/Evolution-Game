package org.project;

// 3 stycznia

import org.project.GenesCreators.FullRandom;
import org.project.GenesCreators.GenesCreator;
import org.project.GenesCreators.IMutation;
import org.project.GenesCreators.LittleChange;
import org.project.Maps.GlobeMap;
import org.project.Maps.PortalMap;
import org.project.Maps.WorldMap;
import org.project.MoveVariants.CrazyMovement;
import org.project.MoveVariants.IMoveType;
import org.project.MoveVariants.NormalMovement;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        int wastedEnergy = 5;
        int numberOfAnimals = 6;
        int startEnergy = 6;
        int numberOfGenes = 5;
        int minMutations = 0;
        int maxMutations = 10;
        IMutation typeOfMutation = new FullRandom(minMutations, maxMutations, numberOfGenes);
        IMoveType typeOfMove = new NormalMovement();
        WorldMap map = new PortalMap(width, height, wastedEnergy);
        SimulationEngine SE = new SimulationEngine(map, numberOfAnimals, startEnergy, numberOfGenes, typeOfMove, typeOfMutation);
        SE.run();

    }
}
