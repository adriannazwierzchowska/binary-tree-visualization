package com.project.viewmodel;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.HashMap;

import com.project.model.FilePath;
import com.project.model.Statistics;

import static com.project.GUIController.*;

public class StatisticsViewModel {

    private static final double FULL_EXTENT = 0.0;
    private final FilePath filePath;

    public StatisticsViewModel(FilePath filePath) {
        this.filePath = filePath;
    }

    public Tab createStatisticsTab() {
        Tab statisticsTab = new Tab("STATISTICS");

        SplitPane horizontalSplitPane = createSplitPane(Orientation.HORIZONTAL);
        AnchorPane symbolPane = createSymbolSection(filePath.getTextFilePath());

        SplitPane rightSplitPane = createSplitPane(Orientation.VERTICAL);
        AnchorPane filePane = createSection("File size", getFileSizeText());
        AnchorPane bitPane = createSection("Bits", getBitText(filePath.getBinaryFilePath()));

        rightSplitPane.getItems().addAll(filePane, bitPane);
        horizontalSplitPane.getItems().addAll(symbolPane, rightSplitPane);
        statisticsTab.setContent(horizontalSplitPane);

        return statisticsTab;
    }

    private SplitPane createSplitPane(Orientation orientation) {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(orientation);

        return splitPane;
    }

    private AnchorPane createSymbolSection(String textFilePath) {
        HBox sectionHeader = createHeader("Symbols in file");

        TextFlow frequencyStatistics = null;
        try {
            HashMap<Integer, Integer> characterFrequency = Statistics.getFrequency(textFilePath);
            frequencyStatistics = Statistics.getFrequencyStatistics(characterFrequency);
        } catch (IOException e) {
            showAlert("Error during counting statistics!", e.getMessage());
        }

        HBox frequencyBox = new HBox(frequencyStatistics);
        frequencyBox.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = createScroll(frequencyBox);

        VBox vbox = new VBox(sectionHeader, scrollPane);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(SPACING);

        return createAnchorPane(vbox);
    }

    private ScrollPane createScroll(HBox text) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(text);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(SPACING));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scrollPane;
    }

    private AnchorPane createSection(String header, String label) {
        HBox sectionHeader = createHeader(header);

        Label sectionLabel = new Label(label);
        sectionLabel.setAlignment(Pos.CENTER);
        sectionLabel.setTextAlignment(TextAlignment.CENTER);

        VBox vbox = new VBox(sectionHeader, sectionLabel);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(SPACING);

        return createAnchorPane(vbox);
    }

    private AnchorPane createAnchorPane(VBox vbox) {
        AnchorPane sectionPane = new AnchorPane(vbox);
        AnchorPane.setTopAnchor(vbox, FULL_EXTENT);
        AnchorPane.setBottomAnchor(vbox, FULL_EXTENT);
        AnchorPane.setLeftAnchor(vbox, FULL_EXTENT);
        AnchorPane.setRightAnchor(vbox, FULL_EXTENT);

        return sectionPane;
    }

    private String getFileSizeText() {
        int inputFileSize = 0;
        int outputFileSize = 0;
        try {
            inputFileSize = Statistics.getFileSize(filePath.getInputPath());
            outputFileSize = Statistics.getFileSize(filePath.getOutputPath());
        } catch (IOException e) {
            showAlert("Error during counting the file statistics!", e.getMessage());
        }

        String inputFileText = "Input file: " + ENTER + inputFileSize + "B";
        String outputFileText = ENTER + ENTER + "Output file: " + ENTER + outputFileSize + "B";

        return inputFileText + outputFileText;
    }

    private String getBitText(String binaryFilePath) {
        boolean[] bitsArray = new boolean[0];
        int[] zerosAndOnes = new int[]{0, 0};

        try {
            bitsArray = filePath.getImplementationStrategy().getBits(binaryFilePath);
            if (bitsArray != null) {
                zerosAndOnes = Statistics.countBits(bitsArray);
            }
        } catch (Exception e) {
            showAlert("Error during counting the bit statistics!", e.getMessage());
        }

        String ones = "Ones: " + ENTER + zerosAndOnes[1];
        String zeros = ENTER + ENTER + "Zeros: " + ENTER + zerosAndOnes[0];

        return ones + zeros;
    }
}
