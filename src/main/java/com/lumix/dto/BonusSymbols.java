package com.lumix.dto;

import java.util.Map;
import java.util.TreeMap;

public class BonusSymbols {
    private Map<String, Integer> symbols;

    // Getters and setters
    public Map<String, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Integer> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        return "BonusSymbols{" +
                "symbols=" + symbols +
                '}';
    }
}
