package org.project.gui;


import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class LinePlot{

    public LineChart<Number, Number> lineChart;
    private List<XYChart.Series<Number, Number>> allSeries;
    public LinePlot(){
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        allSeries = new ArrayList<>();
        xAxis.setLabel("Day number");
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setMaxWidth(600);
        lineChart.setMinWidth(600);
        lineChart.setMaxHeight(220);
        lineChart.setMinHeight(220);
        lineChart.setPadding(new Insets(10, 10, 10, 10));
    }

    public void addSeries(String seriesName){
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        allSeries.add(series);
        lineChart.getData().add(series);
    }

    public void updateData(int dayNumber, int [] values){
        for(int i = 0; i < values.length; i++) {
            allSeries.get(i).getData().add(new XYChart.Data<>(dayNumber, values[i]));
        }
    }
}
