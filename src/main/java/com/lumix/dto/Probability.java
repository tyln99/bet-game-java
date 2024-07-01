package com.lumix.dto;

import java.util.Map;

public class Probability {
    private int column;
    private int row;
    private Map<String, Integer> symbols;

    // Getters and setters
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Map<String, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Integer> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        return "Probability{" +
                "column=" + column +
                ", row=" + row +
                ", symbols=" + symbols +
                '}';
    }
}
