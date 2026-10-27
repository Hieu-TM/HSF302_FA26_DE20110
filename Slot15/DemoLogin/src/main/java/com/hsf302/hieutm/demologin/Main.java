package com.hsf302.hieutm.demologin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var loginView = Main.class.getResource("login.fxml");
        if (loginView == null) {
            throw new IllegalStateException("Không tìm thấy tài nguyên login.fxml");
        }

        var loader = new FXMLLoader(loginView);
        stage.setTitle("Hệ thống quản lý - Đăng nhập");
        stage.setScene(new Scene(loader.load()));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}