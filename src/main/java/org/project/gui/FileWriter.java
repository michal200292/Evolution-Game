package org.project.gui;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileWriter {
    public static void writeToCSV(List<int[]> stats) throws FileNotFoundException {
        int number = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/savedSimulationStatistics/numberOfFiles.txt"));
            number = Integer.parseInt(reader.readLine());
            reader.close();
            number++;
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter("src/main/resources/savedSimulationStatistics/numberOfFiles.txt"));
            writer.write(Integer.toString(number));
            writer.close();

        }
        catch(IOException e){
            e.printStackTrace();
        }

        File csvFile = new File("src/main/resources/savedSimulationStatistics/Simulation"+number+".csv");
        PrintWriter out = new PrintWriter(csvFile);
        out.println("Day Number, Animals alive, Empty spots, Number of Plants, Average energy level for living animals, Average life length for dead animals, Dominant genotype");
        for(int[] row : stats){
            String[] strArray = Arrays.stream(row)
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new);
            out.println(String.join(", ", strArray));
        }
        out.close();
    }
}
