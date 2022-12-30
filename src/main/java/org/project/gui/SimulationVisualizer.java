package org.project.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.project.GenesCreators.IMutation;
import org.project.GrassFields.AbstractGrassField;
import org.project.MapObjects.Animal;
import org.project.Maps.AbstractWorldMap;
import org.project.SimulationEngine;
import org.project.Vector2d;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class SimulationVisualizer extends Application implements IObserver {

    public LinePlot plot1;
    public LinePlot plot2;
    public LinePlot plot3;
    public SimulationEngine engine;
    public AbstractWorldMap map;

    public int dayNumber;

    boolean saveStats;
    Button pauseButton;
    public boolean isPaused;

    public AbstractGrassField grassField;

    public GridPane grid;

    Scene scene;
    Label[][] labels;

    Stage primaryStage;

    List<int[]> stats;

    public SimulationVisualizer(AbstractGrassField grassField, AbstractWorldMap map, IMutation typeOfMutation, int delay, boolean saveStats){
        this.isPaused = false;
        this.grassField = grassField;
        this.map = map;
        this.engine = new SimulationEngine(map, typeOfMutation, delay, grassField);
        this.dayNumber = 0;
        this.saveStats = saveStats;
        stats = new ArrayList<>();
    }

    @Override
    public void start(Stage pStage){
        engine.addObserver(this);
        Thread engineThread = new Thread(engine);
        engineThread.start();
        grid = new GridPane();
        float w = map.width;
        float h = map.height;
        float boxWidth = 900 / w;
        float boxHeight = 900 / h;
        if(w > h){
            boxHeight *= h / w;
        }
        else{
            boxWidth *= w / h;
        }
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
        pauseButton.setOnAction((event) ->{
            pauseOrResumeSimulation();
        });
        pauseButton.setFont(new Font(15));
        pauseButton.setMaxWidth(40);
        pauseButton.setMinWidth(40);
        pauseButton.setMaxHeight(40);
        pauseButton.setMinHeight(40);

        plot1 = new LinePlot();
        plot1.addSeries("Animals alive");
        plot1.addSeries("Empty spot");
        plot1.addSeries("Number of plants");


        plot2 = new LinePlot();
        plot2.addSeries("Average energy level for living animals");

        plot3 = new LinePlot();
        plot3.addSeries("Average life length for dead animals");

        Label legend = new Label("Legend");
        legend.setFont(new Font(20));
        legend.setPadding(new Insets(0, 0, 10, 0));

        HBox d1 = makeLegend(Color.RED, " - Healthy animal");
        HBox d2 = makeLegend(Color.SALMON, " - Unhealthy animal");
        HBox d3 = makeLegend(Color.GREEN, " - Grass");
        HBox d4 = makeLegend(Color.LIGHTGRAY, " - Empty spot");

        VBox legendBox = new VBox();
        legendBox.getChildren().addAll(legend, d1, d2, d3, d4);
        legendBox.setMaxWidth(250);
        legendBox.setAlignment(Pos.CENTER);

        VBox charts = new VBox();
        charts.setAlignment(Pos.CENTER);
        charts.getChildren().addAll(plot1.lineChart, plot2.lineChart, plot3.lineChart, legendBox);
        HBox visualisation = new HBox();
        visualisation.getChildren().addAll(grid, pauseButton, charts);

        update();
        scene = new Scene(visualisation, 1600, 900);
        primaryStage = pStage;
        primaryStage.setScene(scene);
        primaryStage.setTitle("Evolution Game");
        primaryStage.setOnCloseRequest(event ->{
            engineThread.interrupt();
            if(saveStats){
                try {
                    FileWriter.writeToCSV(stats);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
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
            int noOfAnimals = map.animalList.size();
            int sumEnergy = 0;
            for(Animal x : map.animalList) sumEnergy += x.energy;
            int noOfPlants = 0;
            int freePlaces = 0;
            for (int i = 0; i < map.width; i++) {
                for (int j = 0; j < map.height; j++) {
                    Vector2d pos = new Vector2d(i, j);
                    if (map.animals.containsKey(pos)) {
                        if(map.animals.get(pos).get(0).energy >= map.minimumEnergy) {
                            setColor(i, j, Color.RED);
                        }
                        else{
                            setColor(i, j, Color.SALMON);
                        }
                    }
                    else if (grassField.grass[i][j].energy > 0) {
                        setColor(i, j, Color.GREEN);
                        noOfPlants++;
                    }
                    else{
                        setColor(i, j, Color.LIGHTGRAY);
                        freePlaces++;
                    }
                }
            }
            dayNumber++;
            int averageEnergyLevel;
            if(noOfAnimals == 0) averageEnergyLevel = 0;
            else averageEnergyLevel = sumEnergy / noOfAnimals;
            int averageLifeLength;
            if(engine.noOfDeadAnimals == 0) averageLifeLength = 0;
            else averageLifeLength = engine.sumOfLifeLengthOfDeadAnimals / engine.noOfDeadAnimals;
            plot1.updateData(dayNumber, new int[]{noOfAnimals, freePlaces, noOfPlants});
            plot2.updateData(dayNumber, new int[]{averageEnergyLevel});
            plot3.updateData(dayNumber, new int[]{averageLifeLength});
            if(saveStats){
                stats.add(new int[]{dayNumber, noOfAnimals, freePlaces, noOfPlants, averageEnergyLevel, averageLifeLength});
            }
        }
    }

    public HBox makeLegend(Color color, String description){
        HBox hbox = new HBox();
        Label coloredLabel = new Label();
        coloredLabel.setMinHeight(20);
        coloredLabel.setMinWidth(20);
        coloredLabel.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        Label desc = new Label(description);
        desc.setPadding(new Insets(0, 0, 15, 10));
        desc.setFont(new Font(12));
        hbox.getChildren().addAll(coloredLabel, desc);
        return hbox;
    }
}
