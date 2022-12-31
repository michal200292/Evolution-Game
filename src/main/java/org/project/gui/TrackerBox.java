package org.project.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.project.MapObjects.Animal;

import java.nio.Buffer;
import java.util.Arrays;

public class TrackerBox {

    VBox trackingBox;
    Label l1;
    Label l2;
    Label l3;
    Label l4;
    Label l5;
    Label l6;
    Label l7;

    Button stopTracking;
    public TrackerBox(SimulationVisualizer visualizer){
        Label mainText = new Label("Tracked animal");
        mainText.setFont(new Font(20));

        l1 = new Label();
        l2 = new Label();
        l3 = new Label();
        l4 = new Label();
        l5 = new Label();
        l6 = new Label();
        l7 = new Label();
        stopTracking = new Button("Stop Tracking");

        trackingBox = new VBox();
        trackingBox.setVisible(false);
        trackingBox.setPadding(new Insets(0, 0, 10, 60));
        trackingBox.getChildren().addAll(mainText, l1, l2, l3, l4, l5, l6, l7, stopTracking);

        stopTracking.setOnAction(event -> {
            trackingBox.setVisible(false);
            visualizer.isTracked = false;
        });
    }

    public void updateBox(Animal animal){
        if(animal.energy <= 0){
            l1.setText("Genes: ------");
            l2.setText("Activated Gene: ------");
            l3.setText("Energy: ------");
            l4.setText("Plants eaten: " + animal.plantsEaten);
            l5.setText("Number of children: " + animal.numberOfChildren);
            l6.setText("Days lived: " + animal.age);
            l7.setText("Day of death: " + animal.dayOfDeath);
        }
        else {
            l1.setText("Genes: " + Arrays.toString(animal.genes));
            l2.setText("Activated Gene: " + animal.activeGene);
            l3.setText("Energy: " + animal.energy);
            l4.setText("Plants eaten: " + animal.plantsEaten);
            l5.setText("Number of children: " + animal.numberOfChildren);
            l6.setText("Age: " + animal.age);
            l7.setText("");
        }

    }
}
