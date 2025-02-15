package com.project.viewmodel;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;

import com.project.model.FilePath;
import com.project.model.HuffmanTree;
import com.project.view.TreeDrawer;

import java.io.File;
import java.io.IOException;

import static com.project.GUIController.*;

public class TreeManager {
    private static final int INITIAL_Y = 15;
    private static final int INITIAL_X = 1000;
    private final FilePath filePath;

    public TreeManager(FilePath filePath) {
        this.filePath = filePath;
    }

    public Tab createTreeTab(HuffmanTree tree) {
        Tab treeTab = new Tab("HUFFMAN TREE");
        VBox mainLayout = setupMainLayout();
        Pane treeGraph = new Pane();

        TreeDrawer treeDrawer = new TreeDrawer();
        treeDrawer.drawTree(tree.getRoot(), INITIAL_X, INITIAL_Y, treeGraph);

        mainLayout.getChildren().add(createScroll(treeGraph));
        addSaveButtonBox(mainLayout, treeGraph);

        treeTab.setContent(mainLayout);
        return treeTab;
    }

    private VBox setupMainLayout() {
        VBox mainLayout = new VBox(SPACING);
        mainLayout.setPadding(new Insets(SPACING, SPACING, SPACING, SPACING));
        mainLayout.getChildren().add(createHeader("Huffman Tree"));
        mainLayout.setAlignment(Pos.CENTER);
        return mainLayout;
    }

    private void addSaveButtonBox(VBox mainLayout, Pane treePicture) {
        HBox saveTreeButtonBox = new HBox(createSaveButton(treePicture));
        saveTreeButtonBox.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(saveTreeButtonBox);
    }

    public HuffmanTree initializeHuffmanTree() {
        try {
            if (filePath.getCompressionMode()) {
                return filePath.getImplementationStrategy().getHuffmanTree();
            } else {
                return filePath.getImplementationStrategy().getHuffmanTree();
            }
        } catch (Exception e) {
            showAlert("Error during huffman tree initialization.", e.getMessage());
            return new HuffmanTree();
        }
    }

    private ScrollPane createScroll(Pane pane) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);
        scrollPane.setPadding(new Insets(SPACING));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    private VBox createSaveButton(Pane pane) {
        Button saveTreeButton = createButton();

        TextField imagePathField = new TextField();
        imagePathField.setPromptText("Choose a file path...");

        saveTreeButton.setOnAction(event -> {
            String imagePath = imagePathField.getText();
            if (!imagePath.isEmpty()) {
                try {
                    saveImage(pane, imagePath);
                } catch (IOException e) {
                    showAlert("Error during saving the file " + imagePath, e.getMessage());
                }
            }
        });

        VBox vbox = new VBox(imagePathField, saveTreeButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(SPACING);

        return vbox;
    }

    private void saveImage(Pane pane, String imagePath) throws IOException {
        WritableImage writableImage = pane.snapshot(new SnapshotParameters(), null);
        File image = new File(imagePath);
        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", image);
    }

    private Button createButton() {
        Button saveTreeButton = new Button("SAVE TREE TO PNG");

        saveTreeButton.setStyle(PINK_BACKGROUND);
        saveTreeButton.setMinWidth(START_BUTTON_WIDTH);
        saveTreeButton.setMinHeight(START_BUTTON_HEIGHT);
        saveTreeButton.setAlignment(Pos.CENTER);
        HBox.setMargin(saveTreeButton, new Insets(3 * SPACING));

        return saveTreeButton;
    }
}