package org.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.project.GenesCreators.IMutation;
import org.project.GrassFields.AbstractGrassField;
import org.project.Maps.AbstractWorldMap;
import org.project.SimulationEngine;
import org.project.Vector2d;


public class SimulationVisualizer extends Application implements IObserver {

    public SimulationEngine engine;
    public AbstractWorldMap map;

    Button pauseButton;
    public boolean isPaused;

    public AbstractGrassField grassField;

    public GridPane grid;

    Scene scene;
    Label[][] labels;

    Stage primaryStage;

    public SimulationVisualizer(AbstractGrassField grassField, AbstractWorldMap map, IMutation typeOfMutation, int delay){
        this.isPaused = false;
        this.grassField = grassField;
        this.map = map;
        this.engine = new SimulationEngine(map, typeOfMutation, delay, grassField);
    }

    public void start(Stage pStage){
        engine.addObserver(this);
        Thread engineThread = new Thread(engine);
        engineThread.start();
        grid = new GridPane();
        float w = map.width;
        float h = map.height;
        float boxWidth = 800 / w;
        float boxHeight = 800 / h;
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

        pauseButton = new Button();
        pauseButton.setText("\u23F8");
        grid.add(pauseButton, map.width, 0, 1, 2);
        update();

        pauseButton.setOnAction(event ->{
            pauseOrResumeSimulation();
        });

        scene = new Scene(grid, 850, 800);
        primaryStage = pStage;
        primaryStage.setScene(scene);
        primaryStage.setTitle("Evolution Game");
        primaryStage.setOnCloseRequest(event ->{
            engineThread.interrupt();
        });
        primaryStage.show();
    }

    public void setColor(int i, int j, Color color){
        labels[i][j].setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void pauseOrResumeSimulation(){
        if(isPaused){
            pauseButton.setText("\u23F8");
        }
        else{
            pauseButton.setText("\u25B6");
        }
        isPaused = !isPaused;
        engine.isPaused = !engine.isPaused;
    }
    public void update() {
        if(!isPaused) {
            for (int i = 0; i < map.width; i++) {
                for (int j = 0; j < map.height; j++) {
                    setColor(i, j, Color.LIGHTGRAY);
                    if (grassField.grass[i][j].energy > 0) {
                        setColor(i, j, Color.GREEN);
                    }
                    Vector2d pos = new Vector2d(i, j);
                    if (map.animals.containsKey(pos)) {
                        setColor(i, j, Color.RED);
                    }
                }
            }
        }
    }
}
