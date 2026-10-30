package com.hsf302.hieutm.ch08.b4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Đường dẫn tương đối → tìm kiếm resource trong cùng package com/hsf302/hieutm/ch08/b4/
        FXMLLoader loader = new FXMLLoader(LoginApp.class.getResource("login-view.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("B4 - Đăng nhập (FXML)");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

