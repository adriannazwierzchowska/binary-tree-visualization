package com.project.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;

import com.project.model.FilePath;
import com.project.model.HuffmanImplementation;
import com.project.model.ImplementationStrategy;

import static com.project.GUIController.*;

public class ConfigViewModel {
    private final FilePath filePath;
    private final BooleanProperty configurationComplete = new SimpleBooleanProperty(false);
    public RadioButton huffmanImplementationButton;
    public TextField inputPath;
    public TextField outputPath;
    public TextField infoPath;
    private RadioButton compressionButton;
    private RadioButton decompressionButton;
    private RadioButton infoHuffmanImplementationButton;


    public ConfigViewModel(FilePath filePath) {
        this.filePath = filePath;
        this.infoPath = new TextField();
    }

    public BooleanProperty configurationCompleteProperty() {
        return configurationComplete;
    }

    public Tab createConfigTab() {
        Tab configTab = new Tab("CONFIG");
        configTab.setClosable(false);

        VBox mainLayout = new VBox(SPACING);
        mainLayout.setPadding(new Insets(SPACING, SPACING, SPACING, SPACING));

        mainLayout.getChildren().addAll(createHeader("Choose compression or decompression"), createModeSection());
        mainLayout.getChildren().addAll(createHeader("Choose the implementation of the huffman algorithm"), createImplementationSection());
        mainLayout.getChildren().addAll(createHeader("Choose the files"), createFilesSection());

        HBox startButtonContainer = new HBox(createStartButton());
        startButtonContainer.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(startButtonContainer);

        configTab.setContent(mainLayout);
        return configTab;
    }

    private VBox createModeSection() {
        VBox modeSection = new VBox(SPACING);

        ToggleGroup modeToggleGroup = new ToggleGroup();
        compressionButton = new RadioButton("Compress");
        compressionButton.setToggleGroup(modeToggleGroup);
        decompressionButton = new RadioButton("Decompress");
        decompressionButton.setToggleGroup(modeToggleGroup);

        modeSection.getChildren().addAll(compressionButton, decompressionButton);
        return modeSection;
    }

    private VBox createImplementationSection() {
        VBox implementationSection = new VBox(SPACING);

        ToggleGroup implementationToggleGroup = new ToggleGroup();
        huffmanImplementationButton = new RadioButton("First - without info file");
        huffmanImplementationButton.setToggleGroup(implementationToggleGroup);
        infoHuffmanImplementationButton = new RadioButton("Second - with info file");
        infoHuffmanImplementationButton.setToggleGroup(implementationToggleGroup);

        implementationSection.getChildren().addAll(huffmanImplementationButton, infoHuffmanImplementationButton);

        return implementationSection;
    }

    private GridPane createFilesSection() {
        GridPane filesSection = new GridPane();
        filesSection.setHgap(SPACING);
        filesSection.setVgap(SPACING);

        inputPath = new TextField();
        outputPath = new TextField();

        createFileTextField("Input file:", inputPath, filesSection, 0);
        createFileTextField("Output file:", outputPath, filesSection, 1);
        createFileTextField("Info file:", infoPath, filesSection, 2);
        return filesSection;
    }


    private void createFileTextField(String label, TextField textField, GridPane filesSection, int row) {
        Label fileLabel = new Label(label);
        Button selectButton = new Button("Choose...");

        GridPane.setHgrow(textField, Priority.ALWAYS);

        selectButton.setOnAction(event -> {
            boolean isDirectory;

            if (compressionButton.isSelected()) {
                isDirectory = row != 0;
            } else {
                isDirectory = row != 0 && row != 2;
            }
            chooseFileOrDirectory(textField, isDirectory);
        });

        filesSection.add(fileLabel, 0, row);
        filesSection.add(textField, 1, row);
        filesSection.add(selectButton, 2, row);
    }

    private void chooseFileOrDirectory(TextField textField, boolean isDirectory) {
        File selectedFile;

        if (isDirectory) {
            selectedFile = filePath.chooseDirectory();
        } else {
            selectedFile = filePath.chooseFile();
        }

        if (selectedFile != null) {
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    private Button createStartButton() {
        Button startButton = new Button("START");

        startButton.setStyle(PINK_BACKGROUND);
        startButton.setMinWidth(START_BUTTON_WIDTH);
        startButton.setMinHeight(START_BUTTON_HEIGHT);

        HBox.setMargin(startButton, new Insets(3 * SPACING, 0, 0, 0));

        startButton.setOnAction(event -> handleStartButton());
        return startButton;
    }

    private void handleStartButton() {
        handleModeSelection();

        filePath.setInputPath(inputPath.getText());
        filePath.setOutputPath(outputPath.getText());

        try {
            ImplementationStrategy implementationStrategy = handleImplementationSelection();
            filePath.setImplementationStrategy(implementationStrategy);

            filePath.runCompressionOrDecompression(implementationStrategy);
            configurationIsComplete();
        } catch (IllegalStateException | IllegalArgumentException e) {
            showAlert("Error during the configuration.", e.getMessage());
        } catch (RuntimeException e) {
            showAlert("An error occured. ", e.getMessage());
        }
    }

    public ImplementationStrategy handleImplementationSelection() throws IllegalStateException {
        if (huffmanImplementationButton.isSelected()) {
            return new HuffmanImplementation();
        } else if (infoHuffmanImplementationButton.isSelected()) {
            return new HuffmanImplementation();
        } else {
            throw new IllegalStateException("Choose one implementation.");
        }
    }

    private void handleModeSelection() {
        if (compressionButton.isSelected()) {
            filePath.setCompressionMode(true);
        } else if (decompressionButton.isSelected()) {
            filePath.setCompressionMode(false);
        } else {
            throw new IllegalStateException("Choose compression or decompression.");
        }
    }

    public void configurationIsComplete() {
        this.configurationComplete.set(true);
    }

}