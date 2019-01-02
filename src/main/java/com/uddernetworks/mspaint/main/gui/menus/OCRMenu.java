package com.uddernetworks.mspaint.main.gui.menus;

import com.uddernetworks.mspaint.main.MainGUI;
import com.uddernetworks.mspaint.main.gui.BindItem;
import com.uddernetworks.mspaint.main.gui.MenuBind;
import com.uddernetworks.mspaint.main.gui.window.SettingsWindow;
import com.uddernetworks.mspaint.main.settings.Setting;
import com.uddernetworks.mspaint.main.settings.SettingsManager;
import com.uddernetworks.mspaint.ocr.TrainGenerator;
import com.uddernetworks.newocr.OCRHandle;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class OCRMenu extends MenuBind {

    public OCRMenu(MainGUI mainGUI) {
        super(mainGUI);
    }

    @BindItem(label = "settings")
    public void onClickSettings() throws IOException {
        new SettingsWindow("OCR");
    }

    @BindItem(label = "train")
    public void onClickTrain() {
        OCRHandle ocrHandle = new OCRHandle(this.mainGUI.getMain().getDatabaseManager());
        String filePath = SettingsManager.getSetting(Setting.TRAIN_IMAGE, String.class);
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("No training file path found, can't train the OCR.");
            return;
        }

        File file = new File(filePath);

        if (!file.isFile()) {
            System.err.println("Invalid training image found, can't train the OCR.");
            return;
        }

        System.out.println("Training OCR on image " + file.getAbsolutePath());
        this.mainGUI.setStatusText("Training OCR...");
        this.mainGUI.setIndeterminate(true);

        final long start = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            try {
                ocrHandle.trainImage(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {
            System.out.println("Completed training in " + (System.currentTimeMillis() - start) + "ms");
            this.mainGUI.updateLoading(0, 1);
            this.mainGUI.setStatusText(null);
        });
    }

    @BindItem(label = "generate")
    public void onClickGenerate() {
        String filePath = SettingsManager.getSetting(Setting.TRAIN_IMAGE, String.class);
        File file = null;
        if (filePath != null && !filePath.trim().isEmpty()) file = new File(filePath);
        if (file == null) {
            System.out.println("Invalid/missing file found for the train image.");
            return;
        }

        if (file.exists()) file.delete();

        System.out.println("Generating OCR training image at " + file.getAbsolutePath());
        this.mainGUI.setStatusText("Generating train image...");
        this.mainGUI.setIndeterminate(true);

        long start = System.currentTimeMillis();
        new TrainGenerator().generate(file, () -> {
            System.out.println("Completed generation in " + (System.currentTimeMillis() - start) + "ms");
            this.mainGUI.updateLoading(0, 1);
            this.mainGUI.setStatusText(null);
        });
    }
}
