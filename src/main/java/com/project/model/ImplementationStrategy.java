package com.project.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public interface ImplementationStrategy {
    void compress(String inputPath, String outputPath, String infoPath);

    void decompress(String inputPath, String outputPath, String infoPath);

    boolean[] getBits(String binaryFilePath) throws Exception;

    HuffmanTree getHuffmanTree();

    HashMap<Integer, String> createHuffmanDictionary(String filePath) throws Exception;

    default String getExecutablePath(String resource) throws IOException {
        InputStream executableStream = getClass().getClassLoader().getResourceAsStream(resource);
        if (executableStream != null) {
            try {
                File tempExecutableForJAR = File.createTempFile("tempExecutableForJAR", null);
                tempExecutableForJAR.deleteOnExit();
                Files.copy(executableStream, tempExecutableForJAR.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return tempExecutableForJAR.getAbsolutePath();
            } catch (IOException e) {
                throw new IOException("Cannot use executable file " + resource);
            }
        } else {
            throw new IOException("Cannot find executable file " + resource);
        }
    }

    default void checkIfFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("Cannot find file " + filePath);
        }
    }

    default void checkFilePath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new NullPointerException("Wrong path for file " + filePath);
        }
    }

}
