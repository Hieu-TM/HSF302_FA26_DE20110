package com.hsf302.hieutm.demologin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c;");
            lblMessage.setText("Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            return;
        }

        if (username.equals("admin") && password.equals("123456")) {
            lblMessage.setStyle("-fx-text-fill: #27ae60;");
            lblMessage.setText("Đăng nhập thành công!");
        } else {
            lblMessage.setStyle("-fx-text-fill: #e74c3c;");
            lblMessage.setText("Tài khoản hoặc mật khẩu không chính xác!");
        }
    }
}