package com.uddernetworks.mspaint.code.gui;

import com.jfoenix.controls.JFXTextField;
import com.uddernetworks.mspaint.code.LangGUIOptionRequirement;
import com.uddernetworks.mspaint.code.languages.LanguageSettings;
import com.uddernetworks.mspaint.code.languages.Option;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.layout.GridPane;

/**
 * Provides a simple String with a given name and option prompt text, with no change button.
 */
public class StringLangGUIOption implements LangGUIOption {

    String name;
    StringProperty text = new SimpleStringProperty();
    String promptText;
    int index;
    LangGUIOptionRequirement requirement;

    public StringLangGUIOption(String name) {
        this(name, null);
    }

    public StringLangGUIOption(String name, String promptText) {
        this.name = name;
        this.promptText = promptText;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Control getDisplay() {
        var textField = new JFXTextField();
        textField.textProperty().bindBidirectional(this.text);
        textField.setPromptText(this.promptText);
        textField.getStyleClass().add("theme-text");
        GridPane.setColumnIndex(textField, 1);
        return textField;
    }

    @Override
    public void setSetting(Object setting) {
        if (setting instanceof String) text.set((String) setting);
    }

    @Override
    public Object getSetting() {
        return this.text.getValueSafe();
    }

    @Override
    public StringProperty getProperty() {
        return this.text;
    }

    @Override
    public void bindValue(Option option, LanguageSettings languageSettings) {
        this.text.addListener((observable, oldValue, newValue) -> languageSettings.setSetting(option, newValue, true, false));
    }

    @Override
    public boolean hasChangeButton() {
        return false;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void setRequirement(LangGUIOptionRequirement requirement) {
        this.requirement = requirement;
    }

    @Override
    public LangGUIOptionRequirement getRequirement() {
        return this.requirement;
    }
}
