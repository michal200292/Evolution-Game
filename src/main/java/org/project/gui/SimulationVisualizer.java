package org.project.gui;

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
import org.project.GenesCreators.IMutation;
import org.project.GrassFields.AbstractGrassField;
import org.project.Maps.AbstractWorldMap;
import org.project.SimulationEngine;
import org.project.Vector2d;

public class SimulationVisualizer implements IObserver {

    public SimulationEngine engine;
    public AbstractWorldMap map;

    public AbstractGrassField grassField;

    public GridPane grid;

    Scene scene;
    Label[][] labels;

    Stage primaryStage;

    public SimulationVisualizer(AbstractGrassField grassField, AbstractWorldMap map, IMutation typeOfMutation, int delay){
        this.grassField = grassField;
        this.map = map;
        this.engine = new SimulationEngine(map, typeOfMutation, delay, grassField);
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
        update();
        scene = new Scene(grid, 800, 800);
        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setColor(int i, int j, Color color){
        labels[i][j].setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void update() {
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
