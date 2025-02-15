package com.project;

import com.project.model.FilePath;
import com.project.model.HuffmanTree;
import com.project.viewmodel.ConfigViewModel;
import com.project.viewmodel.StatisticsViewModel;
import com.project.viewmodel.TreeManager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUIController extends Application {

    public static final int SPACING = 10;
    public static final int START_BUTTON_WIDTH = 100;
    public static final int START_BUTTON_HEIGHT = 50;
    public static final String PINK_BACKGROUND = "-fx-background-color: #FF69B4;";
    public static final String ENTER = "\n";
    private static final int WINDOW_HEIGHT = 450;
    private static final int WINDOW_WIDTH = 500;

    public static HBox createHeader(String headerText) {
        HBox header = new HBox();
        header.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label headerLabel = new Label(headerText);

        header.getChildren().add(headerLabel);
        header.setAlignment(Pos.CENTER);

        return header;
    }

    public static void showAlert(String alertTitle, String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaCompressor");

        FilePath filePath = new FilePath();
        TabPane tabPane = new TabPane();

        ConfigViewModel configManager = new ConfigViewModel(filePath);
        tabPane.getTabs().add(configManager.createConfigTab());

        configManager.configurationCompleteProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                StatisticsViewModel statisticsManager = new StatisticsViewModel(filePath);
                tabPane.getTabs().add(statisticsManager.createStatisticsTab());

                TreeManager treeManager = new TreeManager(filePath);
                HuffmanTree tree = treeManager.initializeHuffmanTree();
                tabPane.getTabs().add(treeManager.createTreeTab(tree));
            }
        });

        Scene scene = new Scene(tabPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
