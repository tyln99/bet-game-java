package com.lumix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Probabilities {
    @JsonProperty("standard_symbols")
    private List<Probability> standardSymbols;
    @JsonProperty("bonus_symbols")
    private BonusSymbols bonusSymbols;

    // Getters and setters
    public List<Probability> getStandardSymbols() {
        return standardSymbols;
    }

    public void setStandardSymbols(List<Probability> standardSymbols) {
        this.standardSymbols = standardSymbols;
    }

    public BonusSymbols getBonusSymbols() {
        return bonusSymbols;
    }

    public void setBonusSymbols(BonusSymbols bonusSymbols) {
        this.bonusSymbols = bonusSymbols;
    }

    @Override
    public String toString() {
        return "Probabilities{" +
                "standardSymbols=" + standardSymbols +
                ", bonusSymbols=" + bonusSymbols +
                '}';
    }
}
