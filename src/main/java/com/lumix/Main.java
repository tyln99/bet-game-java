package com.lumix;

import com.lumix.dto.ResponseDto;
import com.lumix.service.ConfigService;
import com.lumix.service.GameService;

import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        Option configFileOption = new Option("c", "config", true, "Configuration file");
        configFileOption.setRequired(true);
        options.addOption(configFileOption);

        Option bettingAmountOption = new Option("b", "betting-amount", true, "Betting amount");
        bettingAmountOption.setRequired(true);
        options.addOption(bettingAmountOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {

            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

        String configFilePath = cmd.getOptionValue("config");
        int bettingAmount = Integer.parseInt(cmd.getOptionValue("betting-amount"));

        ConfigService configService = new ConfigService(configFilePath);
        GameService game = new GameService(configService);
        ResponseDto rsp = game.start(bettingAmount);
        System.out.println(rsp);
    }


}
