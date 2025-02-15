package com.project.model;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    public static int[] countBits(boolean[] bitsArray) {
        if (bitsArray.length == 0) {
            throw new NullPointerException("Bit array cannot be empty!");
        }

        int oneCount = 0;
        int zeroCount = 0;

        for (Boolean bit : bitsArray) {
            if (bit) {
                oneCount++;
            } else {
                zeroCount++;
            }
        }

        return new int[]{zeroCount, oneCount};
    }

    public static int getFileSize(String filePath) throws IOException {
        int fileSize = 0;

        File fileToCheck = new File(filePath);
        if (!fileToCheck.exists()) {
            return fileSize;
        }

        try (FileInputStream file = new FileInputStream(filePath)) {
            fileSize = file.available();
        } catch (IOException e) {
            throw new IOException("File " + filePath + " is not available!");
        }

        return fileSize;
    }

    public static HashMap<Integer, Integer> getFrequency(String textFilePath) throws IOException {
        HashMap<Integer, Integer> characterFrequency = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(textFilePath))) {
            int character;
            while ((character = reader.read()) != -1) {
                characterFrequency.put(character, characterFrequency.getOrDefault(character, 0) + 1);
            }
        } catch (IOException e) {
            throw new IOException("Error during counting the statistics from " + textFilePath);
        }

        return characterFrequency;
    }

    public static TextFlow getFrequencyStatistics(HashMap<Integer, Integer> characterFrequency) {
        TextFlow frequencyText = new TextFlow();
        for (Map.Entry<Integer, Integer> entry : characterFrequency.entrySet()) {
            int code = entry.getKey();
            String symbol = code == '\n' ? "\\n" : String.valueOf((char) code);
            int frequency = entry.getValue();
            Text text = new Text(symbol + " - " + frequency + "\n");
            frequencyText.getChildren().add(text);
        }

        return frequencyText;
    }

}
