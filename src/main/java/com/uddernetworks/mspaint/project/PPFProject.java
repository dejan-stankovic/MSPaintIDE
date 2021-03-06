package com.uddernetworks.mspaint.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PPFProject {

    private static final Logger LOGGER = LoggerFactory.getLogger(PPFProject.class);

    private transient File file;

//    private File inputLocation;         // Input file/image folder
//    private File highlightLocation;     // Highlight output file/folder
//    private File classLocation;         // Output .class file folder
//    private File jarFile;               // Output .jar file
//    private File libraryLocation;       // Library .jar/folder
//    private File otherLocation;         // File/folder for other packaged files
    private String language;            // The language used in the project
    private String name;                // The name of the project
//    private boolean compile;            // If the IDE should compile the code
//    private boolean execute;            // If the IDE should execute the code
    private String activeFont;
    private Map<String, String> fonts = new HashMap<>();
    public Map<String, Map<String, Object>> languageSettings = new HashMap<>();
    // <Lang Name,  Map<Option.getName

    private transient BiConsumer<String, String> fontUpdate;

    public PPFProject(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public PPFProject setFile(File file) {
        this.file = file;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        setLanguage(language, true);
    }

    public void setLanguage(String language, boolean override) {
        if (this.language == null || override) this.language = language;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        setName(name, true);
    }

    public void setName(String name, boolean override) {
        if (this.name == null || override) this.name = name;
    }

    public String getActiveFont() {
        return activeFont;
    }

    public String getActiveFontConfig() {
        return getFont(activeFont).getValue();
    }

    public void setActiveFont(String activeFont) {
        this.activeFont = activeFont;

        if (this.fontUpdate != null) this.fontUpdate.accept(activeFont, getFont(activeFont).getValue());
    }

    public void onFontUpdate(BiConsumer<String, String> fontUpdate) {
        this.fontUpdate = fontUpdate;
    }

    public void onFontUpdate(BiConsumer<String, String> fontUpdate, boolean initial) {
        onFontUpdate(fontUpdate);
        if (!initial) return;
        setActiveFont(getActiveFont());
    }

    public Map<String, String> getFonts() {
        return this.fonts;
    }

    public Map.Entry<String, String> getFont(String name) {
        return new AbstractMap.SimpleEntry<>(name, this.fonts.get(name));
    }

    public int getFontsAmount() {
        return this.fonts.size();
    }

    public void modifyFontName(String oldName, String name) {
        var value = this.fonts.remove(oldName);
        this.fonts.put(name, value);
    }

    public void modifyFontPath(String name, String path) {
        this.fonts.replace(name, path);
    }

    public void modifyFont(String oldName, String name, String path) {
        this.fonts.remove(oldName);
        this.fonts.put(name, path);
    }

    public void addFont(String name, String path) {
        if (this.fonts == null) this.fonts = new HashMap<>();
        this.fonts.put(name, path);
    }

    public void removeFont(String name) {
        this.fonts.remove(name);
    }

    public Map<String, Object> getLanguageSetting(String language) {
        return languageSettings.getOrDefault(language, Collections.emptyMap());
    }

    public void setLanguageSetting(String language, Map<String, Object> map) {
        setLanguageSetting(language, map, true);
    }

    public void setLanguageSetting(String language, Map<String, Object> map, boolean override) {
        if (!this.languageSettings.containsKey(language) || override) {
            this.languageSettings.put(language, map);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
