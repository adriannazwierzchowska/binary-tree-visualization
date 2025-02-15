package com.project.model;

import java.io.*;
import java.util.HashMap;

public class InfoHuffmanImplementation implements ImplementationStrategy {
    private HuffmanTree huffmanTree;
    
    @Override
    public void compress(String inputPath, String outputPath, String infoPath) {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");
        String executableName = isWindows ? "huffman-info.exe" : "huffman-info";
        String COMPRESS = " -c ";

        String EXECUTE_PERMISSION = "chmod +x ";

        try {
            checkIfFileExists(inputPath);
            checkFilePath(outputPath);
            checkFilePath(infoPath);

            String firstExecutablePath = getExecutablePath(executableName);
            Runtime.getRuntime().exec(EXECUTE_PERMISSION + firstExecutablePath);
            String runCommand = firstExecutablePath + COMPRESS + "-f " + inputPath + " -t " + outputPath + " -i " + infoPath;
            Process process = Runtime.getRuntime().exec(runCommand);
            process.waitFor();
        } catch (IOException | InterruptedException | NullPointerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Override
    public void decompress(String inputPath, String outputPath, String infoPath) {
        try {
            checkIfFileExists(inputPath);
            checkFilePath(outputPath);
            checkIfFileExists(infoPath);

            HashMap<Integer, String> dictionary = createHuffmanDictionary(infoPath);
            HuffmanTree huffmanTree = new HuffmanTree();
            huffmanTree.buildTreeFromDictionary(dictionary);

            boolean[] bitsArray = getBits(inputPath);

            File outputFile = new File(outputPath);
            HuffmanTree.Reader.readBitsAndPrintCharacters(huffmanTree, bitsArray, outputFile);
            this.huffmanTree = huffmanTree;
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean[] getBits(String binaryFilePath) throws IOException {
        checkIfFileExists(binaryFilePath);

        int BITS_IN_BYTE = 8;
        boolean[] bitsArray;

        try (FileInputStream binaryFile = new FileInputStream(binaryFilePath)) {
            int byteNumber = binaryFile.available();
            byte[] buffer = new byte[byteNumber];

            int firstByte = binaryFile.read();
            int bitsToRead = (byteNumber - 1) * BITS_IN_BYTE - firstByte;

            bitsArray = new boolean[bitsToRead];
            int bitIndex = 0;

            int bytesRead;
            while ((bytesRead = binaryFile.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    byte b = buffer[i];

                    for (int j = BITS_IN_BYTE - 1; j >= 0; j--) {
                        bitsArray[bitIndex] = ((b >>> j) & 1) == 1;
                        bitIndex++;

                        if (bitIndex == bitsToRead) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error during reading the bits from " + binaryFilePath);
        }

        if (bitsArray.length == 0) {
            throw new NullPointerException("File " + binaryFilePath + " is empty.");
        }

        return bitsArray;
    }

    @Override
    public HashMap<Integer, String> createHuffmanDictionary(String filePath) throws IOException {
        checkIfFileExists(filePath);

        HashMap<Integer, String> dictionary = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 1) {
                    String[] tokens = line.split("\\s+");
                    int c = Integer.parseInt(tokens[0]);
                    String code = tokens[1];
                    dictionary.put(c, code);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error during creating the dictionary from " + filePath);
        }

        return dictionary;
    }

    @Override
    public HuffmanTree getHuffmanTree() {
        return huffmanTree;
    }

}
