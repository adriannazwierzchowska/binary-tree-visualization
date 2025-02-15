package com.project.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class HuffmanTree {

    public static Node root;
    public static int maxTreeDepth;

    public HuffmanTree() {
        root = null;
        maxTreeDepth = 0;
    }

    public Node getRoot() {
        return root;
    }

    public void buildTreeFromDictionary(HashMap<Integer, String> dictionary) {
        if (dictionary == null || dictionary.isEmpty()) {
            throw new NullPointerException("Dictionary cannot be empty!");
        }
        root = new Node(null, null);

        for (Integer key : dictionary.keySet()) {
            String code = dictionary.get(key);
            Node currentNode = root;

            for (int i = 0; i < code.length(); i++) {
                char bit = code.charAt(i);
                if (bit == '1') {
                    if (currentNode.getRight() == null) {
                        currentNode.setRight(new Node(null, null));
                    }
                    currentNode = currentNode.getRight();
                } else if (bit == '0') {
                    if (currentNode.getLeft() == null) {
                        currentNode.setLeft(new Node(null, null));
                    }
                    currentNode = currentNode.getLeft();
                } else {
                    throw new IllegalArgumentException("Illegal argument in huffman code!");
                }
            }

            currentNode.setCharacter(key);
        }
    }

    public static class Node {
        private Integer character;
        private Node left;
        private Node right;

        public Node(Integer character, Node left, Node right) {
            this.character = character;
            this.left = left;
            this.right = right;
        }

        public Node(Node left, Node right) {
            this(null, left, right);
        }

        public Integer getCharacter() {
            return character;
        }

        public void setCharacter(Integer character) {
            this.character = character;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    public static class Reader {

        public static void readBitsAndPrintCharacters(HuffmanTree huffmanTree, boolean[] bitsArray, File binaryFile) {
            if (bitsArray.length == 0) {
                throw new NullPointerException("Bit array cannot be empty!");
            }

            try (PrintWriter printWriter = new PrintWriter(binaryFile)) {
                Node currentNode = huffmanTree.getRoot();
                for (boolean bit : bitsArray) {
                    if (bit) {
                        currentNode = currentNode.getRight();
                    } else {
                        currentNode = currentNode.getLeft();
                    }
                    if (currentNode.getCharacter() != null) {
                        printWriter.print((char) currentNode.getCharacter().intValue());
                        currentNode = huffmanTree.getRoot();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error during reading the binary file!");
            }
        }
    }
}
