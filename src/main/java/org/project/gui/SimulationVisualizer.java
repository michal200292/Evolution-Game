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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class SimulationVisualizer extends Application implements IObserver {

    private LinePlot plot1;
    private LinePlot plot2;
    private LinePlot plot3;
    private final SimulationEngine engine;
    protected AbstractWorldMap map;
    public int dayNumber;

    boolean saveStats;
    Button pauseButton;
    protected boolean isPaused;

    protected boolean isTracked;

    protected Animal trackedAnimal;

    protected TrackerBox tracker;

    protected AbstractGrassField grassField;

    Scene scene;

    GuiElementBox[][] board;

    Stage primaryStage;

    List<int[]> stats;

    public SimulationVisualizer(AbstractGrassField grassField, AbstractWorldMap map, IMutation typeOfMutation, int delay, boolean saveStats){
        tracker = new TrackerBox(this);
        this.isTracked = false;
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
        GridPane grid = new GridPane();
        float w = map.width;
        float h = map.height;
        float boxWidth = 850 / w;
        float boxHeight = 850 / h;
        if(w > h){
            boxHeight *= h / w;
        }
        else{
            boxWidth *= w / h;
        }
        board = new GuiElementBox[map.width][map.height];
        for(int i = 0; i < map.width; i++){
            for(int j = 0; j < map.height; j++){
                board[i][j] = new GuiElementBox(i, j, boxWidth, boxHeight, this, tracker);
                grid.add(board[i][j].button, i, j, 1, 1);
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

        HBox container = new HBox();
        container.getChildren().addAll(legendBox, tracker.trackingBox);

        VBox charts = new VBox();
        charts.setAlignment(Pos.CENTER);
        charts.getChildren().addAll(plot1.lineChart, plot2.lineChart, plot3.lineChart, container);
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

    public void update() {
        if(!isPaused) {
            int[] genotypeCount = new int[8];
            for(int i = 0; i < 8; i++){
                genotypeCount[i] = 0;
            }
            int noOfAnimals = map.animalList.size();
            int sumEnergy = 0;
            for(Animal x : map.animalList) {
                sumEnergy += x.energy;
                genotypeCount[x.genes[x.activeGene]]++;
            }
            int noOfPlants = 0;
            int freePlaces = 0;
            for (int i = 0; i < map.width; i++) {
                for (int j = 0; j < map.height; j++) {
                    if (map.animals.get(i).get(j).size() > 0) {
                        if(map.animals.get(i).get(j).get(0).energy >= map.minimumEnergy) {
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
            if(isTracked){
                setColor(trackedAnimal.position.x, trackedAnimal.position.y, Color.BLUE);
                tracker.updateBox(trackedAnimal);
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
            int popularGenotype = 0;
            for(int i = 1; i < 8; i++){
                if(genotypeCount[popularGenotype] < genotypeCount[i]) popularGenotype = i;
            }
            if(saveStats){
                stats.add(new int[]{dayNumber, noOfAnimals, freePlaces, noOfPlants, averageEnergyLevel, averageLifeLength, popularGenotype});
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
    public void setColor(int i, int j, Color color){
        board[i][j].button.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
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
}
