package com.lumix.dto;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ResponseDto {

    private String[][] matrix;
    private Double reward;
    private Map<String, List<String>> appliedWinningCombinations;
    private String appliedBonusSymbol;


    public void setMatrix(String[][] matrix) {
        this.matrix = matrix;
    }


    public void setReward(Double reward) {
        this.reward = reward;
    }


    public void setAppliedWinningCombinations(Map<String, List<String>> appliedWinningCombinations) {
        this.appliedWinningCombinations = appliedWinningCombinations;
    }


    public void setAppliedBonusSymbol(String appliedBonusSymbol) {
        this.appliedBonusSymbol = appliedBonusSymbol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"matrix\": [\n");
        for (String[] row : matrix) {
            sb.append("        [");
            StringJoiner joiner = new StringJoiner(", ");
            for (String cell : row) {
                joiner.add("\"" + cell + "\"");
            }
            sb.append(joiner.toString());
            sb.append("],\n");
        }
        sb.setLength(sb.length() - 2); // Remove the last comma and newline
        sb.append("\n    ],\n");
        sb.append("    \"reward\": ").append(reward).append(",\n");
        if (appliedWinningCombinations != null) {
            sb.append("    \"applied_winning_combinations\": {\n");
            for (Map.Entry<String, List<String>> entry : appliedWinningCombinations.entrySet()) {
                sb.append("        \"").append(entry.getKey()).append("\": [");
                StringJoiner joiner = new StringJoiner(", ");
                for (String combination : entry.getValue()) {
                    joiner.add("\"" + combination + "\"");
                }
                sb.append(joiner.toString());
                sb.append("],\n");
            }
            sb.setLength(sb.length() - 2); // Remove the last comma and newline
            sb.append("\n    },\n");
        }
        if (appliedBonusSymbol != null) {
            sb.append("    \"applied_bonus_symbol\": \"").append(appliedBonusSymbol).append("\"\n");
        }
        sb.append("}");

        return sb.toString();
    }

}
