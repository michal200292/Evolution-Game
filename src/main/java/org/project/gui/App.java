package org.project.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.project.GenesCreators.FullRandom;
import org.project.GenesCreators.IMutation;
import org.project.GenesCreators.LittleChange;
import org.project.GrassFields.AbstractGrassField;
import org.project.GrassFields.EquatorForest;
import org.project.GrassFields.ToxicBodies;
import org.project.Maps.GlobeMap;
import org.project.Maps.AbstractWorldMap;
import org.project.Maps.PortalMap;
import org.project.MoveVariants.CrazyMovement;
import org.project.MoveVariants.IMoveType;
import org.project.MoveVariants.NormalMovement;

public class App extends Application{

    public Stage primaryStage;

    public GridPane layout;

    private int pos;

    public Slider createSlider(int minVal, int maxVal, int initVal){
        Slider slider = new Slider();
        slider.setMin(minVal);
        slider.setMax(maxVal);
        slider.setValue(initVal);
        slider.setMajorTickUnit(Math.floor((maxVal - minVal) / 10.0));
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(1);
        layout.add(slider, 2, pos, 3, 1);
        pos++;
        return slider;
    }

    public void addLabel(String text){
        Label t = new Label(text);
        layout.add(t, 0, pos, 2, 1);
    }

    public ChoiceBox<String> createChoiceBox(String[] choices){
        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(choices));
        choiceBox.setValue(choices[0]);
        choiceBox.setMinWidth(150);
        layout.add(choiceBox, 2, pos);
        pos++;
        return choiceBox;
    }

    public void start(Stage pStage){
        layout = new GridPane();
        for(int i = 0; i < 2; i++)  layout.getColumnConstraints().add(new ColumnConstraints(120));
        for(int i = 0; i < 3; i++)  layout.getColumnConstraints().add(new ColumnConstraints(90));

        layout.setPadding(new Insets(10, 45, 10, 45));

        Label upper = new Label("Simulation parameters");
        upper.setMinHeight(50);
        upper.setFont(new Font(20));
        GridPane.setHalignment(upper, HPos.CENTER);
        layout.add(upper, 0, 0, 5, 1);
        pos = 1;
        addLabel("Width of a World");
        Slider widthConf = createSlider(10, 60, 35);
        addLabel("Height of a World");
        Slider heightConf = createSlider(10, 60, 35);
        addLabel("Starting number of Plants");
        Slider startPlants = createSlider(0, 50, 25);
        addLabel("Plants drawn every day");
        Slider plantsDaily = createSlider(0, 20, 10);
        addLabel("Energy gained from eating plant");
        Slider plantEnergy = createSlider(0, 20, 10);
        addLabel("Starting number of animals");
        Slider numberOfAnimals = createSlider(5, 100, 52);
        addLabel("Starting energy for animals");
        Slider animalsStartEnergy = createSlider(5, 100, 52);
        addLabel("Minimum energy to reproduce");
        Slider minimumReproducingEnergy = createSlider(3, 40, 21);
        addLabel("Energy wasted on reproducing");
        Slider reproducingEnergy = createSlider(2, 30, 16);
        addLabel("Length of animal's genome");
        Slider numberOfGenes = createSlider(4, 40, 22);
        addLabel("Min number of changes during mutation");
        Slider minimumChange = createSlider(0, 40, 20);
        addLabel("Max number of changes during mutation");
        Slider maximumChange = createSlider(0, 40, 20);
        addLabel("Move Delay(milliseconds)");
        Slider moveDelay = createSlider(10, 300, 150);

        Label upper2 = new Label("Simulation variants");
        upper2.setMinHeight(50);
        upper2.setFont(new Font(20));
        GridPane.setHalignment(upper2, HPos.CENTER);
        layout.add(upper2, 0, pos, 5, 1);
        pos++;

        addLabel("Map variant");
        ChoiceBox<String> mapChoice = createChoiceBox(new String[]{"Globe Map", "Portal Map"});
        addLabel("Plant growth variant");
        ChoiceBox<String> grassFieldChoice = createChoiceBox(new String[]{"Equator Forest", "Toxic Bodies"});
        addLabel("Mutation variant");
        ChoiceBox<String> mutationTypeChoice = createChoiceBox(new String[]{"Full Random", "Little Change"});
        addLabel("Animal movement variant");
        ChoiceBox<String> animalMovementChoice = createChoiceBox(new String[]{"Normal Movement", "Crazy Movement"});

        Label upper3 = new Label();
        upper2.setMinHeight(50);
        GridPane.setHalignment(upper2, HPos.CENTER);
        layout.add(upper3, 0, pos, 5, 1);
        pos++;
        Button start = new Button("Start simulation");
        start.setFont(new Font(15));
        GridPane.setHalignment(start, HPos.CENTER);
        start.minWidth(60);
        start.minHeight(80);
        layout.add(start, 0, pos, 5, 1);

        Scene scene = new Scene(layout, 600, 800);
        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxWidth(600);
        primaryStage.setMinHeight(800);
        primaryStage.setMaxHeight(800);
        primaryStage.show();

        start.setOnAction(x -> {
            int width = (int)widthConf.getValue();
            int height = (int)heightConf.getValue();
            int startingNumberOfPlants = (int)startPlants.getValue();
            int numberOfPlantsEveryDay = (int)plantsDaily.getValue();
            int eatingEnergy = (int)plantEnergy.getValue();
            int noOfAnimals = (int)numberOfAnimals.getValue();
            int startEnergy = (int)animalsStartEnergy.getValue();
            int minimumEnergy = (int)minimumReproducingEnergy.getValue();
            int wastedEnergy = (int)reproducingEnergy.getValue();
            int noOfGenes = (int)numberOfGenes.getValue();
            int minMut = (int)minimumChange.getValue();
            int maxMut = (int)maximumChange.getValue();
            int delay = (int)moveDelay.getValue();
            AbstractGrassField grassField;
            AbstractWorldMap map;
            IMutation typeOfMutation;
            IMoveType typeOfMove;
            if(delay < 150) {
                return;
            }
            if(grassFieldChoice.getValue().equals("Equator Forest")){
                grassField = new EquatorForest(width, height, startingNumberOfPlants, numberOfPlantsEveryDay, eatingEnergy);
            }
            else{
                grassField = new ToxicBodies(width, height, startingNumberOfPlants, numberOfPlantsEveryDay, eatingEnergy);
            }
            if(mutationTypeChoice.getValue().equals("Full Random")){
                typeOfMutation = new FullRandom(minMut, maxMut, noOfGenes);
            }
            else{
                typeOfMutation = new LittleChange(minMut, maxMut, noOfGenes);
            }
            if(animalMovementChoice.getValue().equals("Normal Movement")){
                typeOfMove = new NormalMovement();
            }
            else{
                typeOfMove = new CrazyMovement();
            }
            if(mapChoice.getValue().equals("Globe Map")){
                map = new GlobeMap(width, height, wastedEnergy, noOfAnimals, startEnergy, noOfGenes, minimumEnergy, typeOfMove);
            }
            else{
                map = new PortalMap(width, height, wastedEnergy, noOfAnimals, startEnergy, noOfGenes, minimumEnergy, typeOfMove);
            }
            SimulationVisualizer S1 = new SimulationVisualizer(grassField, map, typeOfMutation, delay);

        });
    }
}
