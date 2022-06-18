package com.puradox.practicalbarrels.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.puradox.practicalbarrels.PracticalBarrelsClient;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Configuration {
    private static final ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getName().startsWith("localizedText");
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    };
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().addSerializationExclusionStrategy(strategy).create();

    public boolean requireNBT = false; //If auto-deposit requires items to contain the same NBT data (unused).
    public boolean acceptDamageableItems = false; //If empty barrels will quick-deposit melee-damaging items, such as many tools.
    public boolean acceptUnstackableItems = true; //If empty barrels will accept non-stacking items.
    public boolean emptyBarrelDepositing = true; //If empty barrels will auto-deposit the items from your mainhand.
    public boolean autoDeposit = true; //If auto-deposit is disabled entirely.

    public static Configuration loadConfig(File file) {
        Configuration config;

        if (file.exists() && file.isFile()) {
            try (
                    FileInputStream fileInputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                config = GSON.fromJson(bufferedReader, Configuration.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config", e);
            }
        } else {
            config = new Configuration();
        }

        config.saveConfig(file);

        return config;
    }

    public void saveConfig(File config) {
        try (
                FileOutputStream stream = new FileOutputStream(config);
                Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)
        ) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            PracticalBarrelsClient.LOGGER.error("Failed to save config");
        }
    }
}