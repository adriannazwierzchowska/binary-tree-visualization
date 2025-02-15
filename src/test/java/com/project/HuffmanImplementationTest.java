package com.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.project.model.HuffmanImplementation;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanImplementationTest {

    private HuffmanImplementation huffmanImplementation;

    @BeforeEach
    public void setUp() {
        huffmanImplementation = new HuffmanImplementation();
    }

    @Test
    public void should_ReturnArrayListOfBits_When_InputFileIsValid() throws IOException {
        // given
        URL url = getClass().getClassLoader().getResource("SI_BinaryTestFile.bin");
        String binaryFilePath = url.getFile();

        // when
        boolean[] resultBitsArray = huffmanImplementation.getBits(binaryFilePath);

        // then
        boolean[] expectedBitsArray = {false, false, true, false, false, true, false, true,
                true, true, true, true, false, true, true};

        assertArrayEquals(expectedBitsArray, resultBitsArray);
    }

    @Test
    public void should_ThrowException_When_InputFileIsInvalid_getBits() {
        // given
        String invalidFilePath = null;

        // when & then
        assertThrows(NullPointerException.class, () -> huffmanImplementation.getBits(invalidFilePath));
    }

    @Test
    public void should_CreateAndReturnCorrectDictionary_When_FileIsValid() throws IOException {
        // given
        URL url = getClass().getClassLoader().getResource("FI_DictionaryTestFile.txt");
        String filePath = url.getPath();

        // when
        HashMap<Integer, String> resultDictionary = huffmanImplementation.createHuffmanDictionary(filePath);

        // then
        HashMap<Integer, String> expectedDictionary = new HashMap<>();
        expectedDictionary.put(101, "00");
        expectedDictionary.put(102, "01");
        expectedDictionary.put(103, "10");

        assertEquals(expectedDictionary, resultDictionary);
    }

    @Test
    public void should_ThrowException_When_FileIsInvalid_hfdictionary() {
        // given
        String filePath = null;

        // when & then
        assertThrows(NullPointerException.class, () -> huffmanImplementation.createHuffmanDictionary(filePath));
    }
}
