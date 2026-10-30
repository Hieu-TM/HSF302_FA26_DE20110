package com.hsf302.hieutm.ch08.b5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LayoutGalleryApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LayoutGalleryApp.class.getResource("main-layout.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("B5 - Layout Components Gallery");
        stage.setMinWidth(750);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
