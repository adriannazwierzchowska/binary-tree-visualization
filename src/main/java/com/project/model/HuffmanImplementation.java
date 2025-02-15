package com.project.model;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;


public class HuffmanImplementation implements ImplementationStrategy {
    private HuffmanTree huffmanTree;

    @Override
    public void compress(String inputPath, String outputPath, String infoPath) {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");
        String executableName = isWindows ? "huffman.exe" : "huffman";
        String COMPRESS = " -c ";

        String EXECUTE_PERMISSION = "chmod +x ";

        try {
            checkIfFileExists(inputPath);
            checkFilePath(outputPath);

            String firstExecutablePath = getExecutablePath(executableName);
            if (!isWindows) {
                Runtime.getRuntime().exec(EXECUTE_PERMISSION + firstExecutablePath);
            }
            String runCommand = firstExecutablePath + COMPRESS + "-f " + inputPath + " -t " + outputPath;
            Process process = Runtime.getRuntime().exec(runCommand);
            process.waitFor();
            try (InputStream is = new FileInputStream(outputPath)) {
                this.huffmanTree = new HuffmanTree();
                huffmanTree.root = readTree(is);
            }
        } catch (IOException | InterruptedException | NullPointerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Override
    public void decompress(String inputPath, String outputPath, String infoPath) {
        try (InputStream is = new FileInputStream(inputPath)) {
            HuffmanTree.Node root = readTree(is);
            this.huffmanTree = new HuffmanTree();
            huffmanTree.root = root;

            byte[] sizeBytes = new byte[8];
            is.read(sizeBytes);

            ByteArrayOutputStream data = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                data.write(buffer, 0, bytesRead);
            }

            byte[] allBytes = data.toByteArray();
            int padding = allBytes[allBytes.length - 1];
            byte[] encodedData = Arrays.copyOf(allBytes, allBytes.length - 1);

            boolean[] bits = convertBytesToBits(encodedData, padding);
            HuffmanTree.Reader.readBitsAndPrintCharacters(huffmanTree, bits, new File(outputPath));
        } catch (Exception e) {
            throw new RuntimeException("Decompression error: " + e.getMessage(), e);
        }
    }

    private HuffmanTree.Node readTree(InputStream is) throws IOException {
        int marker = is.read();
        if (marker == -1) throw new IOException("Unexpected end of file");
        
        if (marker == 49) {
            int character = is.read();
            if (character == -1) throw new IOException("Unexpected end of file");
            return new HuffmanTree.Node(character, null, null);
        } else if (marker == 48) { 
            HuffmanTree.Node left = readTree(is);
            HuffmanTree.Node right = readTree(is);
            return new HuffmanTree.Node(null, left, right);
        } else {
            throw new IOException("Wrong file marker: " + marker);
        }
    }

    private boolean[] convertBytesToBits(byte[] data, int padding) {
        if (data == null || data.length == 0) {
            return new boolean[0];
        }
        
        if (padding < 0 || padding > 7) {
            padding = 0;
        }
        
        int totalBits = data.length * 8 - padding;
        boolean[] bits = new boolean[totalBits];
        
        int index = 0;
        for (byte b : data) {
            for (int i = 7; i >= 0; i--) {
                if (index < totalBits) {
                    bits[index++] = ((b >> i) & 1) == 1;
                }
            }
        }
        return bits;
    }

    @Override
    public boolean[] getBits(String binaryFilePath) throws IOException {
        try (InputStream is = new FileInputStream(binaryFilePath)) {
            skipTree(is); 
            
            byte[] sizeBytes = new byte[8];
            is.read(sizeBytes);
            
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                data.write(buffer, 0, bytesRead);
            }
            
            byte[] allBytes = data.toByteArray();
            int padding = allBytes[allBytes.length - 1];
            byte[] encodedData = Arrays.copyOf(allBytes, allBytes.length - 1);
            
            return convertBytesToBits(encodedData, padding);
        }
    }

    private void skipTree(InputStream is) throws IOException {
        int marker = is.read();
        if (marker == 49) { 
            is.read();
        } else if (marker == 48) { 
            skipTree(is);
            skipTree(is);
        } else {
            throw new IOException("Wrong tree marker");
        }
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
