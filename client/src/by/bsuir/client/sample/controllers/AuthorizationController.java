package by.bsuir.client.sample.controllers;

import by.bsuir.client.sample.connectoin.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationController {

    @FXML
    private PasswordField password_field;

    @FXML
    private Button authSigInButton;

    @FXML
    private TextField login_field;

    @FXML
    private Button back;

    @FXML
    void initialize() {
        back.setOnAction(event -> {
            back.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("/by/bsuir/client/sample/view/sample.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        });

        authSigInButton.setOnAction(event -> {

            try {
                Client.getInstance().send("authorization");

                JSONObject userJson = new JSONObject();
                userJson.put("userName", login_field.getText().trim());
                userJson.put("password", password_field.getText().trim());

                Client.getInstance().send(userJson.toString());

                String status = Client.getInstance().get();
                if (!status.equals("nobody")) {

                    openMenu(status);
                } else {
                    System.out.println("Повторите ввод");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Такой пользователь не зарегистрирован!");
                    alert.showAndWait();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        });
    }

        public void openMenu(String status) {
            if (status.equals("admin")) {
                try {
                    authSigInButton.getScene().getWindow().hide();

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/by/bsuir/client/sample/view/menuAdmin.fxml"));

                    loader.load();

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                    stage.setTitle("Меню администратора");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (status.equals("user")) {

                try {
                    authSigInButton.getScene().getWindow().hide();

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/by/bsuir/client/sample/view/menuUser.fxml"));

                    loader.load();

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                    stage.setTitle("Меню пользователя");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Повторите ввод");
            }
        }
    }

