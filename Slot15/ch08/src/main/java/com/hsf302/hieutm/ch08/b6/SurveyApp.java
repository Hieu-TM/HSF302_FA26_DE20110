package com.hsf302.hieutm.ch08.b6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SurveyApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SurveyApp.class.getResource("survey-view.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("B6 - Phiếu khảo sát sinh viên HSF302");
        stage.setMinWidth(550);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
