package org.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.project.GenesCreators.FullRandom;
import org.project.GenesCreators.IMutation;
import org.project.GrassFields.AbstractGrassField;
import org.project.GrassFields.EquatorForest;
import org.project.GrassFields.ToxicBodies;
import org.project.IObserver;
import org.project.Maps.GlobeMap;
import org.project.Maps.PortalMap;
import org.project.Maps.WorldMap;
import org.project.MoveVariants.CrazyMovement;
import org.project.MoveVariants.IMoveType;
import org.project.MoveVariants.NormalMovement;
import org.project.SimulationEngine;
import org.project.Vector2d;

public class App extends Application implements IObserver{
    public SimulationEngine engine;
    public WorldMap map;

    public AbstractGrassField grassField;

    public GridPane grid;
    public Stage primaryStage;

    Label[][] labels;


    public void init(){
        int width = 60;
        int height = 60;
        int startingNumberOfPlants = 20;
        int numberOfPlantsEveryDay = 20;
        int plantEnergy = 10;
        grassField = new ToxicBodies(width, height, startingNumberOfPlants, numberOfPlantsEveryDay, plantEnergy);
        int wastedEnergy = 25;
        int numberOfAnimals = 100;
        int minimumEnergy = 35;
        int startEnergy = 100;
        int numberOfGenes = 5;
        int minMutations = 0;
        int maxMutations = 5;
        int moveDelay = 100;
        IMutation typeOfMutation = new FullRandom(minMutations, maxMutations, numberOfGenes);
        IMoveType typeOfMove = new CrazyMovement();
        this.map = new GlobeMap(width, height, wastedEnergy, numberOfAnimals, startEnergy, numberOfGenes, minimumEnergy, typeOfMove);
        this.engine = new SimulationEngine(map, typeOfMutation, moveDelay, grassField);
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
        Scene scene = new Scene(grid, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setColor(int i, int j, Color color){
        labels[i][j].setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void update(){
        for(int i = 0; i < map.width; i++){
            for(int j = 0; j < map.height; j++){
                setColor(i, j, Color.LIGHTGRAY);
                if(grassField.grass[i][j].energy > 0){
                    setColor(i, j, Color.GREEN);
                }
                Vector2d pos = new Vector2d(i, j);
                if(map.animals.containsKey(pos)){
                    setColor(i, j, Color.RED);
                }
            }
        }
    }
}
