package by.bsuir.client.sample.controllers;

import by.bsuir.client.sample.connectoin.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    @FXML
    private Button authorization;

    @FXML
    private Button registration;

    @FXML
    private Button exit;

    @FXML
    void initialize() {
        registration.setOnAction(actionEvent -> {

            try {
                registration.getScene().getWindow().hide();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/by/bsuir/client/sample/view/registration.fxml"));

                    loader.load();

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Регистрация");


                Client.getInstance().send("registration");
                stage.show();
            } catch (IOException e) {
            e.printStackTrace();
        }

        });

        authorization.setOnAction(actionEvent -> {

            try {
               authorization.getScene().getWindow().hide();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/by/bsuir/client/sample/view/authorization.fxml"));

                loader.load();

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Авторизация");

         //       Client.getInstance().send("authorization");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        exit.setOnAction(actionEvent -> {
            Client.getInstance().send("exit");
            Client.getInstance().close();
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });

    }
}



