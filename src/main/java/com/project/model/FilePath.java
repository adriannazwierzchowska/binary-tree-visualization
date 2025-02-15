package com.project.model;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;

public class FilePath {
    private final FileChooser fileChooser;
    private final DirectoryChooser directoryChooser;
    private String inputPath;
    private String outputPath;
    private String infoPath;
    private boolean compressionMode;
    private ImplementationStrategy implementationStrategy;


    public FilePath() {
        this.inputPath = "";
        this.outputPath = "";
        this.infoPath = "";
        this.compressionMode = true;
        this.fileChooser = new FileChooser();
        this.directoryChooser = new DirectoryChooser();
    }

    public String getInputPath() {
        return this.inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    public void setOutputPath(String outputPath) {
        File file = new File(outputPath);
        if (file.isDirectory()) {
            this.outputPath = outputPath + File.separator +
                    (this.compressionMode ? "compressed_" : "decompressed_") +
                    new File(inputPath).getName().split("\\.")[0] +
                    (this.compressionMode ? ".bin" : ".txt");
        } else {
            this.outputPath = outputPath;
        }
    }

    public File chooseFile() {
        return fileChooser.showOpenDialog(null);
    }

    public File chooseDirectory() {
        return directoryChooser.showDialog(null);
    }

    public boolean getCompressionMode() {
        return this.compressionMode;
    }

    public void setCompressionMode(boolean compressionMode) {
        this.compressionMode = compressionMode;
    }

    public ImplementationStrategy getImplementationStrategy() {
        return this.implementationStrategy;
    }

    public void setImplementationStrategy(ImplementationStrategy implementationStrategy) {
        this.implementationStrategy = implementationStrategy;
    }

    public void runCompressionOrDecompression(ImplementationStrategy implementationStrategy) {
        if (compressionMode) {
            implementationStrategy.compress(inputPath, outputPath, infoPath);
        } else {
            implementationStrategy.decompress(inputPath, outputPath, infoPath);
        }
    }

    public String getTextFilePath() {
        return compressionMode ? inputPath : outputPath;
    }

    public String getBinaryFilePath() {
        return compressionMode ? outputPath : inputPath;
    }

    public String getDictionaryFilePath() {
        return getBinaryFilePath();
    }

    public boolean isCompressionMode() {
        return compressionMode;
    }

}
