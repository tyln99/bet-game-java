package com.lumix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumix.dto.GameConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigService {

    private GameConfiguration config;

    public ConfigService(String path) {
        this.config = readConfig(path);
    }

    private GameConfiguration readConfig(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), GameConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GameConfiguration getConfig() {
        return config;
    }

    public void setConfig(GameConfiguration config) {
        this.config = config;
    }
}
