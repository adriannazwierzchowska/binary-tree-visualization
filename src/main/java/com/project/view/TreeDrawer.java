package com.project.view;

import com.project.model.HuffmanTree;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TreeDrawer {
    private static final int RADIUS = 15;
    private static final int DISTANCE_Y = 150;

    private static Label createLeaf(Circle circle, String symbol, double xMiddle, double yMiddle) {
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        if (symbol.equals("\n")) {
            symbol = "\\n";
        }

        Label label = new Label(symbol, circle);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setLayoutX(xMiddle);
        label.setLayoutY(yMiddle);

        return label;
    }

    private static void setPaneWidth(int x, int y, Pane pane) {
        double max_width = x + 2*RADIUS;
        double max_height = y + 2*RADIUS;
        if (max_width > pane.getMinWidth()) {
            pane.setPrefWidth(max_width);
            pane.setMinWidth(max_width);
        }
        if (max_height > pane.getMinHeight()) {
            pane.setPrefHeight(max_height);
            pane.setMinHeight(max_height);
        }
    }

    public void drawTree(HuffmanTree.Node node, int x, int y, Pane pane) {
        if (node == null) {
            return;
        }

        double xMiddle = x - ((double) RADIUS / 2);
        double yMiddle = y - ((double) RADIUS / 2);

        int nodeDistance = calculateNodeDistance(node);

        if (node.getLeft() != null) {
            drawChildren(x, y, -nodeDistance, y + DISTANCE_Y, pane, node.getLeft());
        }
        if (node.getRight() != null) {
            drawChildren(x, y, nodeDistance, y + DISTANCE_Y, pane, node.getRight());
        }

        Circle circle = new Circle(x, y, RADIUS);
        String symbol = node.getCharacter() != null ? String.valueOf((char) node.getCharacter().intValue()) : " ";
        pane.getChildren().add(createLeaf(circle, symbol, xMiddle, yMiddle));

        setPaneWidth(x, y, pane);
    }

    private void drawChildren(int x, int y, int nodeDistance, int newY, Pane pane, HuffmanTree.Node node) {
        int startX = x + (RADIUS / 2);
        int startY = y + (3 * RADIUS) / 4;
        int endX = x + nodeDistance + RADIUS;
        int endY = newY + RADIUS;


        Line line = new Line(startX, startY, endX, endY);
        pane.getChildren().add(line);
        drawTree(node, endX, endY, pane);
    }

    private int calculateNodeDistance(HuffmanTree.Node node) {
        int nodeCount = countNodes(node);

        return nodeCount * RADIUS;
    }

    private int countNodes(HuffmanTree.Node node) {
        if (node == null)
            return 0;
        else
            return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
    }


}
