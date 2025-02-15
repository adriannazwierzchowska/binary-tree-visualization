package com.project;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.project.model.Statistics;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsTest {

    private Statistics statistics;

    @BeforeEach
    public void setUp() {
        statistics = new Statistics();
    }
    @Test
    public void should_ReturnCorrectValues_WhenBitsArrayNotEmpty() {
        // given
        boolean[] bitsArray = new boolean[]{true, false, true};

        // when
        int[] zerosAndOnes = Statistics.countBits(bitsArray);

        // then
        int[] expected = {1, 2};
        assertArrayEquals(expected, zerosAndOnes);
    }

    @Test
    public void should_ThrowException_When_BitsArrayIsNull() {
        // given
        boolean[] bitsArray = null;

        // when & then
        assertThrows(NullPointerException.class, () -> Statistics.countBits(bitsArray));
    }

    @Test
    public void should_ThrowException_When_BitsArrayIsEmpty() {
        // given
        boolean[] bitsArray = new boolean[]{};

        // when & then
        assertThrows(NullPointerException.class, () -> Statistics.countBits(bitsArray));
    }

    @Test
    public void should_ReturnCorrectFileSize_When_FileIsNotNull() throws IOException {
        // given
        URL url = getClass().getClassLoader().getResource("Stats_TestFileSize.txt");
        assert url != null;
        String filePath = url.getPath();

        // when
        int result = Statistics.getFileSize(filePath);

        // then
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        int expected = (int) file.length();
        file.close();
        assertEquals(expected, result);
    }

    @Test
    public void should_ThrowException_When_FileIsInvalid_FileSize() {
        // given
        String invalidFilePath = null;

        // when & then
        assertThrows(NullPointerException.class, () -> Statistics.getFileSize(invalidFilePath));

    }

    @Test
    public void should_ReturnCorrectFrequency_When_FileIsValid() throws IOException {
        // given
        URL url = getClass().getClassLoader().getResource("Stats_FrequencyTestFile.txt");
        String filePath = url.getPath();

        // when
        HashMap<Integer, Integer> resultFrequency = Statistics.getFrequency(filePath);

        // then
        HashMap<Integer, Integer> expectedFrequency = new HashMap<>();
        expectedFrequency.put((int) 'a', 1);
        expectedFrequency.put((int) ' ', 2);
        expectedFrequency.put((int) 'b', 3);
        expectedFrequency.put((int) 'c', 4);


        assertEquals(expectedFrequency, resultFrequency);

    }

    @Test
    public void should_ThrowException_When_FileIsInvalid_GetFrequency() {
        // given
        String invalidFilePath = null;

        // when & then
        assertThrows(NullPointerException.class, () -> Statistics.getFrequency(invalidFilePath));
    }

}