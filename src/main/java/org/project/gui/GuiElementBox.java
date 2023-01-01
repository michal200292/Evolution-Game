package org.project.gui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.project.Vector2d;

public class GuiElementBox{
    Button button;

    TrackerBox tracker;

    int x_cord;
    int y_cord;
    SimulationVisualizer visualizer;
    public GuiElementBox(int x_cord, int y_cord, float width, float height, SimulationVisualizer visualizer, TrackerBox tracker){
        this.tracker = tracker;
        this.x_cord = x_cord;
        this.y_cord = y_cord;
        button = new Button();
        this.visualizer = visualizer;
        button.setMinHeight(height);
        button.setMinWidth(width);
        button.setOnAction(event -> {
            if(visualizer.isPaused && !visualizer.isTracked){
                if(visualizer.map.animals.get(x_cord).get(y_cord).size() > 0){
                    visualizer.trackedAnimal = visualizer.map.animals.get(x_cord).get(y_cord).get(0);
                    visualizer.isTracked = true;
                    visualizer.setColor(visualizer.trackedAnimal.position.x, visualizer.trackedAnimal.position.y, Color.BLUE);
                    tracker.updateBox(visualizer.trackedAnimal);
                    tracker.trackingBox.setVisible(true);
                }
            }
        });
    }
}
