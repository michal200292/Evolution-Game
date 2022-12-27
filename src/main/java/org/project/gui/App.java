package org.project.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.project.GenesCreators.FullRandom;
import org.project.GenesCreators.IMutation;
import org.project.IObserver;
import org.project.Maps.GlobeMap;
import org.project.Maps.PortalMap;
import org.project.Maps.WorldMap;
import org.project.MoveVariants.IMoveType;
import org.project.MoveVariants.NormalMovement;
import org.project.SimulationEngine;
import org.project.Vector2d;

public class App extends Application implements IObserver{
    public SimulationEngine engine;
    public WorldMap map;

    public GridPane grid;
    public Stage primaryStage;

    Label[][] labels;


    public void init(){
        int width = 10;
        int height = 10;
        int wastedEnergy = 5;
        int numberOfAnimals = 26;
        int startEnergy = 15;
        int numberOfGenes = 5;
        int minMutations = 0;
        int maxMutations = 10;
        int moveDelay = 400;
        IMutation typeOfMutation = new FullRandom(minMutations, maxMutations, numberOfGenes);
        IMoveType typeOfMove = new NormalMovement();
        this.map = new PortalMap(width, height, wastedEnergy);
        this.engine = new SimulationEngine(map, numberOfAnimals, startEnergy, numberOfGenes, typeOfMove, typeOfMutation, moveDelay);
        engine.addObserver(this);
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }

    public void start(Stage pStage){
        primaryStage = pStage;
        grid = new GridPane();
        int boxWidth = 800 / map.width;
        int boxHeight = 800 / map.height;
        labels = new Label[map.width][map.height];
        for(int i = 0; i < map.width; i++){
            for(int j = 0; j < map.height; j++){
                labels[i][j] = new Label();
                labels[i][j].setMinHeight(boxHeight);
                labels[i][j].setMinWidth(boxWidth);
                labels[i][j].setAlignment(Pos.CENTER);
                grid.add(labels[i][j], i, j, 1, 1);
            }
        }
        update();
        grid.setGridLinesVisible(true);
        Scene scene = new Scene(grid, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void update(){
        for(int i = 0; i < map.width; i++){
            for(int j = 0; j < map.height; j++){
                Vector2d pos = new Vector2d(i, j);
                if(map.animals.containsKey(pos)){
                    labels[i][j].setText(String.valueOf(map.animals.get(pos).size()));
                }
                else{
                    labels[i][j].setText("");
                }
            }
        }
    }
}
