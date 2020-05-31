package by.bsuir.client.sample.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/by/bsuir/client/sample/view/sample.fxml"));
        primaryStage.setTitle("Выбор рациональной структуры системы методом экспертных оценок");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {

        launch(args);
    }
}
