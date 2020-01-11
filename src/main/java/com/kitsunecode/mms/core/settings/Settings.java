package com.kitsunecode.mms.core.settings;

import com.kitsunecode.mms.core.Main;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Function;

public class Settings {

    public static final String configFolder = "config";
    public static final String configPath = "config/config.properties";

    private static Properties properties = null;

    private static void load(InputStream is) throws IOException {
        properties.load(is);
    }

    private static Properties get() {
        if (properties != null) {
            return properties;
        }

        try {
            properties = new Properties();

            InputStream s;

            System.out.println("Loading default config first");
            s = Main.class.getClassLoader().getResourceAsStream(configPath);
            if (s != null) {
                load(s);
            }

            Path config = Paths.get(configPath);
            if (config.toFile().exists()) {
                System.out.println("Found custom config file, loading it...");
                load(new ByteArrayInputStream(Files.readAllBytes(config)));
                return properties;
            }

            File rf = Paths.get(configFolder).toFile();
            if (!rf.exists() && !rf.mkdir()) {
                return properties;
            }

            s = Main.class.getClassLoader().getResourceAsStream(configPath);
            if (s != null) {
                FileUtils.copyInputStreamToFile(s, config.toFile());
            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void reload() {
        properties = null;
    }

    private static <T> T getProperty(String property, T defaultValue, Function<String, T> converter) {
        String stringValue = get().getProperty(property);
        if (stringValue == null) {
            if (defaultValue == null) {
                throw new RuntimeException("Cannot find property '" + property + "' in config");
            }
            return defaultValue;
        }
        T value = converter.apply(stringValue);
        if (value == null) return defaultValue;
        return value;
    }

    private static Color get(String property, Color defVal) {
        return getProperty(property, defVal, (data) -> {
            String[] p = data.split(",");
            if (p.length != 4) {
                System.out.println("Invalid color: " + data);
                return null;
            }
            int[] c = Arrays.stream(p).map(String::trim).mapToInt(Integer::parseInt).toArray();
            return new Color(c[0], c[1], c[2], c[3]);
        });
    }

    private static int get(String property, int defVal) {
        return getProperty(property, defVal, Integer::parseInt);
    }

    private static long get(String property, long defVal) {
        return getProperty(property, defVal, Long::parseLong);
    }

    private static boolean get(String property, boolean defVal) {
        return getProperty(property, defVal, Boolean::parseBoolean);
    }

    private static float get(String property, float defVal) {
        return getProperty(property, defVal, Float::parseFloat);
    }

    private static String get(String property, String defVal) {
        return getProperty(property, defVal, (data) -> data);
    }

    private static String[] get(String property, String[] defVal, String divider) {
        return getProperty(property, defVal, (data) -> Arrays.stream(data.split(divider)).map(String::trim).toArray(String[]::new));
    }

    private static int[] get(String property, int[] defVal, String divider) {
        return getProperty(property, defVal, (data) -> Arrays.stream(data.split(divider)).mapToInt(Integer::parseInt).toArray());
    }

    private static double[] get(String property, double[] defVal, String divider) {
        return getProperty(property, defVal, (data) -> Arrays.stream(data.split(divider)).mapToDouble(Double::parseDouble).toArray());
    }

    public static String get(String defaultValue) {
        return get("baloon.highQuality", defaultValue);
    }

    public static String getAdapter() {
        return get("adapter", "Ship");
    }

    public static String getWaifuName() {
        return get("waifu.name", (String) null);
    }

    public static boolean isBaloonHighQualityText() {
        return get("baloon.highQualityText", true);
    }

    public static int getBaloonYOffset() {
        return get("baloon.yOffset", 300);
    }

    public static int getBaloonXOffset() {
        return get("baloon.xOffset", 0);
    }

    public static int getBaloonWidth() {
        return get("baloon.width", 400);
    }

    public static int getBaloonHeight() {
        return get("baloon.height", 100);
    }

    public static int getBaloonFontSize() {
        return get("baloon.fontSize", 15);
    }

    public static String getBaloonFont(String defaultValue) {
        return get("baloon.font", defaultValue);
    }

    public static Color getBaloonBackground(Color defaultValue) {
        return get("baloon.background", defaultValue);
    }

    public static Color getBaloonForeground() {
        return get("baloon.foreground", Color.WHITE);
    }

    public static int getWaifuHeight() {
        return get("waifu.height", 800);
    }

    public static boolean isWaifuMirrored() {
        return get("waifu.mirrored", false);
    }

    public static boolean isWaifuWelcomeEnabled() {
        return get("waifu.welcome.enabled", true);
    }

    public static int getWaifuWelcomeDelay() {
        return get("waifu.welcome.delay", 5000);
    }

    public static boolean isVoiceEnabled() {
        return get("voice.enabled", true);
    }

    public static int getVoiceVolume() {
        return get("voice.volume", 50);
    }

    public static boolean isDialogsEnabled() {
        return get("dialogs.enabled", true);
    }

    public static boolean isDialogsOnClick() {
        return get("dialogs.onClick", true);
    }

    public static boolean isDialogsOnIdle() {
        return get("dialogs.onIdle", true);
    }

    public static int getDialogsBaloonNoVoiceDuration() {
        return get("dialogs.baloon.noVoiceDuration", 3);
    }

    public static boolean isWaifuAlwaysOnTop() {
        return get("waifu.alwaysOnTop", true);
    }

    public static String getBaloonFormatString() {
        return get("baloon.formatString", "[[text]]");
    }

    public static String getWaifuStartY() {
        return get("waifu.startY", "auto");
    }

    public static int getWaifuSkinIndex() {
        return get("waifu.skinIndex", 0);
    }

    public static String isWaifuHighQuality() {
        return get("waifu.highQuality", "");
    }

    public static boolean isFloatingEnabled() {
        return get("floating.enabled", true);
    }

    public static int getFloatingPixelPerStep() {
        return get("floating.pixelPerStep", 1);
    }

    public static int getFloatingPixelRange() {
        return get("floating.pixelRange", 300);
    }

    public static int getFloatingStepSleep() {
        return get("floating.stepSleep", 16);
    }

    public static int getFloatingSwapSleep() {
        return get("floating.swapSleep", 100);
    }

    public static int getDialogsIdleFrequency() {
        return get("dialogs.idle.frequency", 60);
    }

    public static boolean isJumpOnClick() {
        return get("jump.onClick", true);
    }

    public static int getJumpCount() {
        return get("jump.count", 2);
    }

    public static int getJumpPixelPerStep() {
        return get("jump.pixelPerStep", 5);
    }

    public static int getJumpSleep() {
        return get("jump.stepSleep", 15);
    }

    public static int getJumpPixelRange() {
        return get("jump.pixelRange", 40);
    }

    public static int getFloatingSwitchSleep() {
        return get("floating.switchSleep", 10);
    }

    public static String getWaifuLanguage() {
        return get("waifu.language", "Chinese");
    }

    public static boolean useWaifuNativeLanguage() {
        return get("waifu.language.useNative", false);
    }

}
