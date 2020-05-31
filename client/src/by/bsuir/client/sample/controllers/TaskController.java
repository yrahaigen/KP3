package by.bsuir.client.sample.controllers;

import java.io.IOException;

import by.bsuir.client.sample.connectoin.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class TaskController {


    @FXML
    private GridPane taskPane;

    @FXML
    private GridPane ansPane;

    public TaskController() throws IOException {
    }



    @FXML
    void initialize() throws IOException {

        String a_ans = Client.getInstance().get();
        String b_ans = Client.getInstance().get();

        String pr1_ans = Client.getInstance().get();
        String pr2_ans = Client.getInstance().get();
        String pr3_ans = Client.getInstance().get();

        float a1 = Float.parseFloat(pr1_ans);
        float a2 = Float.parseFloat(pr2_ans);
        float a3 = Float.parseFloat(pr3_ans);


        float[] res = {a1, a2, a3};


        Label label1 = new Label();
        label1.setText(a_ans);
        label1.setFont(new Font("Arial", 14));
        taskPane.add(label1, 1, 0);

        Label label2 = new Label();
        label2.setText(b_ans);
        label2.setFont(new Font("Arial", 14));
        taskPane.add(label2, 1, 1);

        Label label3 = new Label(" Проект 1 :  " +  String.format("%.3g%n", res[0]));
        label3.setFont(new Font("Arial", 14));
        ansPane.add(label3, 0, 0);
        Label label4 = new Label(" Проект 2:  " + String.format("%.3g%n", res[1]));
        label4.setFont(new Font("Arial", 14));
        ansPane.add(label4, 1, 0);
        Label label5 = new Label(" Проект 3:  " + String.format("%.3g%n", res[2]));
        label5.setFont(new Font("Arial", 14));
        ansPane.add(label5, 2, 0);

    }
}
