package com.project.model;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream implements AutoCloseable {
    private InputStream input;
    private int currentByte;
    private int bitPosition;

    public BitInputStream(InputStream in) {
        this.input = in;
        this.bitPosition = 8;
    }

    public int readBit() throws IOException {
        if (bitPosition == 8) {
            currentByte = input.read();
            if (currentByte == -1) return -1;
            bitPosition = 0;
        }
        int bit = (currentByte >> (7 - bitPosition)) & 1;
        bitPosition++;
        return bit;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    public int getBitPosition() {
        return bitPosition;
    }

}