package com.project;

import org.junit.jupiter.api.*;

import com.project.model.HuffmanTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class HuffmanTreeTest {
    private HuffmanTree huffmanTree;
    @BeforeEach
    public void setUp() {
        huffmanTree = new HuffmanTree();
    }

    @Test
    public void should_ThrowException_When_DictionaryIsNull() {
        // given
        HashMap<Integer, String> huffmanDictionary = null;

        // when & then
        assertThrows(NullPointerException.class, () -> huffmanTree.buildTreeFromDictionary(huffmanDictionary));
    }
    @Test
    public void should_ThrowException_When_DictionaryIsEmpty() {
        // given
        HashMap<Integer, String> huffmanDictionary = new HashMap<>();

        // when & then
        assertThrows(NullPointerException.class, () -> huffmanTree.buildTreeFromDictionary(huffmanDictionary));
    }
    @Test
    public void should_CreateCorrectTree_When_DictionaryIsValid() {
        // given
        HashMap<Integer, String> dictionary = new HashMap<>();
        dictionary.put(65, "0");
        dictionary.put(66, "10");
        dictionary.put(67, "11");

        // when
        HuffmanTree huffmanTree = new HuffmanTree();
        huffmanTree.buildTreeFromDictionary(dictionary);

        HuffmanTree.Node root = huffmanTree.getRoot();

        // then
        Assertions.assertNotNull(root);

        Assertions.assertNull(root.getCharacter());
        Assertions.assertNotNull(root.getLeft());
        Assertions.assertNotNull(root.getRight());

        HuffmanTree.Node leftChild = root.getLeft();
        Assertions.assertEquals(65, leftChild.getCharacter().intValue());
        Assertions.assertNull(leftChild.getLeft());
        Assertions.assertNull(leftChild.getRight());

        HuffmanTree.Node rightChild = root.getRight();
        Assertions.assertNull(rightChild.getCharacter());
        Assertions.assertNotNull(rightChild.getLeft());
        Assertions.assertNotNull(rightChild.getRight());

        HuffmanTree.Node rightLeftChild = rightChild.getLeft();
        Assertions.assertEquals(66, rightLeftChild.getCharacter().intValue());
        Assertions.assertNull(rightLeftChild.getLeft());
        Assertions.assertNull(rightLeftChild.getRight());

        HuffmanTree.Node rightRightChild = rightChild.getRight();
        Assertions.assertEquals(67, rightRightChild.getCharacter().intValue());
        Assertions.assertNull(rightRightChild.getLeft());
        Assertions.assertNull(rightRightChild.getRight());
    }

    @Test
    public void should_TraverseThroughTreeAndCreateDecompressedFile_When_AllArgumentsAreValid() throws IOException {
        // given
        HuffmanTree.Node root = new HuffmanTree.Node(null, null);
        HuffmanTree.Node nodeA = new HuffmanTree.Node(65, null, null);
        HuffmanTree.Node nodeB = new HuffmanTree.Node(66, null, null);
        root.setLeft(nodeA);
        root.setRight(nodeB);

        HuffmanTree huffmanTree = new HuffmanTree();
        HuffmanTree.root = root;

        boolean[] bitsArray = {true, false, true, true, false};

        File outputFile = new File("src//test//resources//outputFile.txt");

        // when
        HuffmanTree.Reader.readBitsAndPrintCharacters(huffmanTree, bitsArray, outputFile);
        String resultOutput = readFileToString("src//test//resources//outputFile.txt");


        // when
        String expectedOutput = "BABBA";
        Assertions.assertEquals(expectedOutput, resultOutput);
    }
    @Test
    public void should_ThrowException_When_PassedTreeIsNull() {
        // given
        HuffmanTree huffmanTree1 = null;
        boolean[] bitsArray = new boolean[]{true, false, true};
        File file = new File("src//test//resources//outputFile.txt");

        // when & then
        assertThrows(NullPointerException.class, () -> HuffmanTree.Reader.readBitsAndPrintCharacters(huffmanTree1, bitsArray, file));

    }
    @Test
    public void should_ThrowException_When_BitsArrayIsNull() {
        // given
        HuffmanTree.Node root = new HuffmanTree.Node(null, null);
        HuffmanTree.Node nodeA = new HuffmanTree.Node(65, null, null);
        HuffmanTree.Node nodeB = new HuffmanTree.Node(66, null, null);
        root.setLeft(nodeA);
        root.setRight(nodeB);

        HuffmanTree huffmanTree = new HuffmanTree();
        HuffmanTree.root = root;

        boolean[] bitsArray = null;

        File outputFile = new File("src//test//resources//outputFile.txt");

        // when & then
        assertThrows(NullPointerException.class, () -> HuffmanTree.Reader.readBitsAndPrintCharacters(huffmanTree, bitsArray, outputFile));


    }
    @Test
    public void should_ThrowException_When_BitsArrayIsEmpty() {
        // given
        HuffmanTree.Node root = new HuffmanTree.Node(null, null);
        HuffmanTree.Node nodeA = new HuffmanTree.Node(65, null, null);
        HuffmanTree.Node nodeB = new HuffmanTree.Node(66, null, null);
        root.setLeft(nodeA);
        root.setRight(nodeB);

        HuffmanTree huffmanTree = new HuffmanTree();
        HuffmanTree.root = root;

        boolean[] bitsArray = new boolean[]{};

        File outputFile = new File("src//test//resources//outputFile.txt");

        // when & then
        assertThrows(NullPointerException.class, () -> HuffmanTree.Reader.readBitsAndPrintCharacters(huffmanTree, bitsArray, outputFile));

    }
    @Test
    public void should_ThrowException_When_OutputFileIsInvalid() {
        // given
        HuffmanTree.Node root = new HuffmanTree.Node(null, null);
        HuffmanTree.Node nodeA = new HuffmanTree.Node(65, null, null);
        HuffmanTree.Node nodeB = new HuffmanTree.Node(66, null, null);
        root.setLeft(nodeA);
        root.setRight(nodeB);

        HuffmanTree huffmanTree = new HuffmanTree();
        HuffmanTree.root = root;

        boolean[] bitsArray = {true, false, true, true, false};

        File outputFile = null;

        // when & then
        assertThrows(NullPointerException.class, () -> HuffmanTree.Reader.readBitsAndPrintCharacters(huffmanTree, bitsArray, outputFile));
    }

    private static String readFileToString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

 }
