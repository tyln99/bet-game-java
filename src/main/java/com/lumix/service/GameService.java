package com.lumix.service;

import com.lumix.dto.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameService {

    private final ConfigService configService;
    private static final Random random = new Random();

    private final String STANDARD = "standard";
    private final String SAME_SYMBOL_PATTERN = "same_symbol_%s_times";
    private final String SAME_SYMBOLS_VERTICALLY = "same_symbols_vertically";
    private final String SAME_SYMBOLS_L_T_RRIGHT = "same_symbols_diagonally_left_to_right";
    private final String SAME_SYMBOLS_R_T_LEFT = "same_symbols_diagonally_right_to_left";
    private final String MISS = "MISS";
    private final String EXTRA_BONUS = "extra_bonus";
    private final String MULTIPLY_REWARD = "multiply_reward";
    private final String SAME_SYMBOLS = "same_symbols";

    public GameService(ConfigService configService) {
        this.configService = configService;
    }

    public ResponseDto start(double bettingAmount) {
        GameConfiguration config = configService.getConfig();
        String[][] matrix = generateMetric(config.getColumns(), config.getRows(), config.getProbabilities());

        int ranBonusCol = random.nextInt(config.getColumns());
        int ranBonusRow = random.nextInt(config.getRows());
        String bonusName = generateCharacter(config.getProbabilities().getBonusSymbols().getSymbols());
        matrix[ranBonusCol][ranBonusRow] = bonusName;

        Map<String, Integer> repeatingSymbols = getSymbolsRepeatingMoreThan(matrix, 3);
        ResponseDto responseDto = new ResponseDto();

        if (!repeatingSymbols.isEmpty()) {
            Double reward = 0d;
            Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
            for (Map.Entry<String, Integer> repeatingSymbol : repeatingSymbols.entrySet()) {

                String symbolName = repeatingSymbol.getKey();
                Integer repeat = repeatingSymbol.getValue();
                List<String> winingCombinations = new ArrayList<>();

                Symbol symbol = config.getSymbols().get(symbolName);

                if (symbol == null) {
                    continue;
                }

                if (symbol.getType().equals(STANDARD)) {
                    reward += symbol.getRewardMultiplier();
                }

                WinCombination winCombination = config.getWinCombinations().get(String.format(SAME_SYMBOL_PATTERN, repeat));
                if (winCombination == null) {
                    continue;
                }

                if (winCombination.getWhen().equals(SAME_SYMBOLS)) {
                    winingCombinations.add(String.format("same_symbol_%s_times", repeat));
                    reward *= winCombination.getRewardMultiplier();
                }

                // special case:
                List<String> specialWinCombinationCases = Arrays.asList(SAME_SYMBOLS_VERTICALLY, SAME_SYMBOLS_L_T_RRIGHT, SAME_SYMBOLS_R_T_LEFT);
                for (String specialWinCombinationCase : specialWinCombinationCases) {
                    WinCombination sameSymbolsVertically = config.getWinCombinations().get(specialWinCombinationCase);
                    List<List<String>> coveredAreas = sameSymbolsVertically.getCoveredAreas();
                    for (List<String> coveredArea : coveredAreas) {

                        boolean satisfy = true;
                        for (String location : coveredArea) {
                            String[] tmp = location.split(":");
                            if (!Objects.equals(matrix[Integer.parseInt(tmp[0])][Integer.parseInt(tmp[1])], symbolName)) {
                                satisfy = false;
                                break;
                            }
                        }

                        if (satisfy) {
                            winingCombinations.add(specialWinCombinationCase);
                            reward *= sameSymbolsVertically.getRewardMultiplier();
                            break;
                        }
                    }
                }
                // special case:

                appliedWinningCombinations.put(symbolName, winingCombinations);
            }
            responseDto.setAppliedWinningCombinations(appliedWinningCombinations);

            // bonus:
            Symbol bonusSymbol = config.getSymbols().get(bonusName);
            if (bonusSymbol.getImpact().equals(MISS)) {
                // skip
            } else if (bonusSymbol.getImpact().equals(EXTRA_BONUS)) {
                reward += bonusSymbol.getExtra();
            } else if (bonusSymbol.getImpact().equals(MULTIPLY_REWARD)) {
                reward *= bonusSymbol.getRewardMultiplier();
            }

            responseDto.setAppliedBonusSymbol(bonusName);
            responseDto.setReward(reward * bettingAmount);
        } else {
            responseDto.setReward((double) 0);
        }

        responseDto.setMatrix(matrix);
        return responseDto;
    }

    public static Map<String, Integer> getSymbolsRepeatingMoreThan(String[][] matrix, int threshold) {
        Map<String, Integer> symbolCount = new HashMap<>();

        for (String[] row : matrix) {
            for (String symbol : row) {
                symbolCount.put(symbol, symbolCount.getOrDefault(symbol, 0) + 1);
            }
        }

        Map<String, Integer> repeatingSymbols = new HashMap<>();
        for (Map.Entry<String, Integer> entry : symbolCount.entrySet()) {
            if (entry.getValue() >= threshold) {
                repeatingSymbols.put(entry.getKey(), entry.getValue());
            }
        }

        return repeatingSymbols;
    }

    public String[][] generateMetric(int columns, int rows, Probabilities probabilities) {

        String[][] metric = new String[rows][columns];
        Map<String, Probability> standardSymbols = probabilities.getStandardSymbols().stream()
                .collect(Collectors.toMap(probability ->
                        String.format("%s:%s", probability.getColumn(), probability.getRow()), Function.identity()));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                Probability probability = standardSymbols.get(String.format("%s:%s", i, j));

                if (probability == null) {
                    probability = standardSymbols.get(String.format("%s:%s", 0, 0));
                }

                metric[i][j] = generateCharacter(probability.getSymbols());
            }
        }

        return metric;
    }

    public static String generateCharacter(Map<String, Integer> probabilities) {
        int totalProbability = probabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomNumber = random.nextInt(totalProbability) + 1; // Random number between 1 and totalProbability

        int cumulativeProbability = 0;
        for (Map.Entry<String, Integer> entry : probabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomNumber <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        // This should ideally not happen if probabilities are correctly defined
        throw new RuntimeException("Failed to generate character based on probabilities");
    }

    public static void main(String[] args) {

        Map<String, Integer> probabilities = new HashMap<>();
        probabilities.put("A", 1);
        probabilities.put("B", 2);
        probabilities.put("C", 3);
        probabilities.put("D", 4);
        probabilities.put("E", 5);
        probabilities.put("F", 6);

        // Generate characters based on probabilities
        String character = generateCharacter(probabilities);
        System.out.println("Generated character: " + character);
    }
}
