package org.project.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class App extends Application{

    public Stage primaryStage;

    public GridPane layout;

    private int pos;
    private Slider widthConf;
    private Slider heightConf;

    private Slider startPlants;

    private Slider plantsDaily;

    private Slider plantEnergy;

    private Slider numberOfAnimals;
    private Slider animalsStartEnergy;
    private Slider minimumReproducingEnergy;
    private Slider reproducingEnergy;
    private Slider numberOfGenes;
    private Slider minimumChange;
    private Slider maximumChange;
    private Slider moveDelay;

    private ChoiceBox<String> mapChoice;
    private ChoiceBox<String> grassFieldChoice;
    private ChoiceBox<String> mutationTypeChoice;
    private ChoiceBox<String> animalMovementChoice;

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

    public void createPopUpWindow(String message){
        Stage popUp = new Stage();
        StackPane pane = new StackPane();
        Label errorMessage = new Label(message);
        pane.getChildren().add(errorMessage);
        popUp.setScene(new Scene(pane, 300, 70));
        popUp.setMinHeight(70);
        popUp.setMaxHeight(70);
        popUp.setMinWidth(300);
        popUp.setMaxWidth(300);
        popUp.setTitle("Wrong parameters");
        popUp.show();
    }
    @Override
    public void start(Stage pStage){
        layout = new GridPane();
        for(int i = 0; i < 2; i++)  layout.getColumnConstraints().add(new ColumnConstraints(120));
        for(int i = 0; i < 3; i++)  layout.getColumnConstraints().add(new ColumnConstraints(90));

        layout.setPadding(new Insets(10, 45, 10, 45));

        Label title = new Label("Simulation parameters");
        title.setMinHeight(50);
        title.setFont(new Font(20));
        GridPane.setHalignment(title, HPos.CENTER);
        layout.add(title, 0, 0, 5, 1);
        pos = 1;
        addLabel("Width of a World");
        widthConf = createSlider(10, 80, 45);
        addLabel("Height of a World");
        heightConf = createSlider(10, 80, 45);
        addLabel("Starting number of Plants");
        startPlants = createSlider(0, 100, 50);
        addLabel("Plants drawn every day");
        plantsDaily = createSlider(0, 100, 50);
        addLabel("Energy gained from eating plant");
        plantEnergy = createSlider(0, 50, 25);
        addLabel("Starting number of animals");
        numberOfAnimals = createSlider(5, 300, 155);
        addLabel("Starting energy for animals");
        animalsStartEnergy = createSlider(5, 200, 102);
        addLabel("Minimum energy to reproduce");
        minimumReproducingEnergy = createSlider(3, 80, 41);
        addLabel("Energy wasted on reproducing");
        reproducingEnergy = createSlider(2, 60, 31);
        addLabel("Length of animal's genome");
        numberOfGenes = createSlider(4, 40, 22);
        addLabel("Min number of changes during mutation");
        minimumChange = createSlider(0, 40, 20);
        addLabel("Max number of changes during mutation");
        maximumChange = createSlider(0, 40, 20);
        addLabel("Move Delay(milliseconds)");
        moveDelay = createSlider(10, 300, 150);

        Label title2 = new Label("Simulation variants");
        title2.setMinHeight(50);
        title2.setFont(new Font(20));
        GridPane.setHalignment(title2, HPos.CENTER);
        layout.add(title2, 0, pos, 5, 1);
        pos++;

        addLabel("Map variant");
        mapChoice = createChoiceBox(new String[]{"Globe Map", "Portal Map"});
        addLabel("Plant growth variant");
        grassFieldChoice = createChoiceBox(new String[]{"Equator Forest", "Toxic Bodies"});
        addLabel("Mutation variant");
        mutationTypeChoice = createChoiceBox(new String[]{"Full Random", "Little Change"});
        addLabel("Animal movement variant");
        animalMovementChoice = createChoiceBox(new String[]{"Normal Movement", "Crazy Movement"});

        Label title3 = new Label("Ready Configurations");
        title3.setMinHeight(50);
        title3.setFont(new Font(20));
        GridPane.setHalignment(title3, HPos.CENTER);
        layout.add(title3, 0, pos, 5, 1);
        pos++;

        addLabel("Choose configurations to load");
        ChoiceBox<String> readyConfigChoice = createChoiceBox(new String[]{"Config1"});
        pos--;
        Button load = new Button("Load Config");
        GridPane.setHalignment(load, HPos.CENTER);
        layout.add(load, 4, pos, 1, 1);
        pos++;

        load.setOnAction(event -> {
            try {
                loadConfig(readyConfigChoice.getValue());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        CheckBox saveToCsv = new CheckBox("Save stats to CSV file");
        saveToCsv.setPadding(new Insets(10, 0, 0, 0));
        layout.add(saveToCsv, 0, pos, 2, 1);
        pos++;

        Label title4 = new Label();
        title4.setMinHeight(20);
        GridPane.setHalignment(title4, HPos.CENTER);
        layout.add(title4, 0, pos, 5, 1);
        pos++;
        Button start = new Button("Start simulation");
        start.setFont(new Font(15));
        GridPane.setHalignment(start, HPos.CENTER);
        start.minWidth(60);
        start.minHeight(80);
        layout.add(start, 0, pos, 5, 1);

        Scene scene = new Scene(layout, 600, 900);
        primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxWidth(600);
        primaryStage.setMinHeight(900);
        primaryStage.setMaxHeight(900);
        primaryStage.setTitle("Configuration Window");
        primaryStage.show();

        start.setOnAction(x -> {
            int width = (int)widthConf.getValue();
            int height = (int)heightConf.getValue();
            int startingNumberOfPlants = (int)startPlants.getValue();
            if(startingNumberOfPlants > height*width){
                createPopUpWindow("Too many plants for map of this size\nMaximum number of plants for this map is " + (height*width));
                return;
            }
            int numberOfPlantsEveryDay = (int)plantsDaily.getValue();
            if(startingNumberOfPlants > height*width){
                createPopUpWindow("Too many plants(Daily) for map of this size\nMaximum number of plants for this map is " + (height*width));
                return;
            }
            int eatingEnergy = (int)plantEnergy.getValue();
            int noOfAnimals = (int)numberOfAnimals.getValue();
            if(noOfAnimals > height*width){
                createPopUpWindow("Too many animals for map of this size\nMaximum number of plants for this map is " + (height*width));
                return;
            }
            int startEnergy = (int)animalsStartEnergy.getValue();
            int minimumEnergy = (int)minimumReproducingEnergy.getValue();
            int wastedEnergy = (int)reproducingEnergy.getValue();
            if(wastedEnergy >= minimumEnergy){
                createPopUpWindow("Energy wasted on reproducing should be\nstrictly smaller then minimum energy to reproduce");
                return;
            }
            int noOfGenes = (int)numberOfGenes.getValue();
            int minMut = (int)minimumChange.getValue();
            if(minMut > noOfGenes){
                createPopUpWindow("Minimum number of changes during mutation\nshould be smaller then number of genes");
                return;
            }
            int maxMut = (int)maximumChange.getValue();
            if(maxMut > noOfGenes){
                createPopUpWindow("Maximum number of changes during mutation\nshould be smaller then number of genes");
                return;
            }
            if(minMut > maxMut){
                createPopUpWindow("""
                        Maximum number of changes during mutation
                        should be greater then or equal to
                        Minimum number of changes during mutation""");
                return;
            }
            int delay = (int)moveDelay.getValue();
            AbstractGrassField grassField;
            AbstractWorldMap map;
            IMutation typeOfMutation;
            IMoveType typeOfMove;
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
            boolean saveStats = saveToCsv.isSelected();
            SimulationVisualizer simulationVisualizer = new SimulationVisualizer(grassField, map, typeOfMutation, delay, saveStats);
            simulationVisualizer.start(new Stage());

        });
    }
    public void loadConfig(String fileName) throws FileNotFoundException {
        int[] conf = new int[17];
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/configurations/" + fileName + ".txt"));
            String line;
            int i = 0;
            while((line = reader.readLine()) != null){
                conf[i] = Integer.parseInt(line);
                i++;
            }
            reader.close();
            widthConf.setValue(conf[0]);
            heightConf.setValue(conf[1]);
            startPlants.setValue(conf[2]);
            plantsDaily.setValue(conf[3]);
            plantEnergy.setValue(conf[4]);
            numberOfAnimals.setValue(conf[5]);
            animalsStartEnergy.setValue(conf[6]);
            minimumReproducingEnergy.setValue(conf[7]);
            reproducingEnergy.setValue(conf[8]);
            numberOfGenes.setValue(conf[9]);
            minimumChange.setValue(conf[10]);
            maximumChange.setValue(conf[11]);
            moveDelay.setValue(conf[12]);
            if(conf[13] == 0){
                mapChoice.setValue("Globe Map");
            }
            else{
                mapChoice.setValue("Portal Map");
            }
            if(conf[14] == 0){
                grassFieldChoice.setValue("Equator Forest");
            }
            else{
                grassFieldChoice.setValue("Toxic Bodies");
            }
            if(conf[15] == 0){
                mutationTypeChoice.setValue("Full Random");
            }
            else{
                mutationTypeChoice.setValue("Little Change");
            }
            if(conf[16] == 0){
                animalMovementChoice.setValue("Normal Movement");
            }
            else{
                animalMovementChoice.setValue("Crazy Movement");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
