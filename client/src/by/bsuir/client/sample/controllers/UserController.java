package by.bsuir.client.sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.bsuir.client.sample.connectoin.Client;
import by.bsuir.client.sample.information.CollectionDetails;
import by.bsuir.client.sample.information.Details;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONObject;

public class UserController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button projectButton;

    @FXML
    private Button tasksButton;

    @FXML
    private Button backButton;

    @FXML
    private Pane taskPane;

    @FXML
    private TextField mark11;

    @FXML
    private TextField mark12;

    @FXML
    private TextField mark13;

    @FXML
    private TextField mark21;

    @FXML
    private TextField mark22;

    @FXML
    private TextField mark23;

    @FXML
    private TextField mProfit;

    @FXML
    private Button singlSolutionButton;

    @FXML
    private Pane projectPane;

    @FXML
    private Label statusLabel1;

    @FXML
    private TableView<Details> projectTable;

    @FXML
    private TableColumn<Details, Integer> idProject;

    @FXML
    private TableColumn<Details, String> constructor;

    @FXML
    private TableColumn<Details, String> manager;

    @FXML
    private TableColumn<Details, String> engineer;

    @FXML
    private TableColumn<Details, String> name;

    @FXML
    private TableColumn<Details, Integer> number;

    @FXML
    private TableColumn<Details, String> format;

    @FXML
    private TableColumn<Details, Integer> machine;

    @FXML
    private TextField searchPrField;

    @FXML
    private TableView<Details> selectedPrTable;

    @FXML
    private TableColumn<Details, String> selectedDetails;

    @FXML
    void handleCliks(ActionEvent event) {
        if(event.getSource()==projectButton){
            projectPane.toFront();
        }
        else if(event.getSource()==tasksButton){
            taskPane.toFront();
        }
        else if(event.getSource()==backButton){

            backButton.getScene().getWindow().hide();

            Client.getInstance().send("back");

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
        }
    }

    @FXML
    void searchProject(ActionEvent event) {
        FilteredList<Details> filterDetails;
        filterDetails = new FilteredList<>(CollectionDetails.getInstance().getDetails(), e->true);
        searchPrField.textProperty().addListener((observableValue, oldValue, newValue)->{
            filterDetails.setPredicate((Details details)->{

                String newVal = newValue.toLowerCase();
                return  details.getConstructor().toLowerCase().contains(newVal)
                        || details.getManager().toLowerCase().contains(newVal)
                        || details.getEngineer().toLowerCase().contains(newVal)
                        || details.getName().toLowerCase().contains(newVal)
                        || details.getFormat().toLowerCase().contains(newVal);

            });
            projectTable.setItems(filterDetails);
        });
    }

    @FXML
    void singlSolution(ActionEvent event) throws IOException {

        boolean key = true;
        JSONObject  taskJson = new JSONObject();

        if( mark11.getText().isEmpty() || mark11.getText()==null || !checkNum(mark11.getText()) ){key = false;}
        else {taskJson.put("mark11", mark11.getText().trim()); }
        if( mark12.getText().isEmpty() || mark12.getText()==null || !checkNum(mark12.getText())){key = false;}
        else {taskJson.put("mark12", mark12.getText().trim()); }
        if( mark13.getText().isEmpty() || mark13.getText()==null || !checkNum(mark13.getText())){key = false;}
        else {taskJson.put("mark13", mark13.getText().trim()); }
        if( mark21.getText().isEmpty() || mark21.getText()==null || !checkNum(mark21.getText())){key = false;}
        else {taskJson.put("mark21", mark21.getText().trim()); }
        if( mark22.getText().isEmpty() || mark22.getText()==null || !checkNum(mark22.getText())){key = false;}
        else {taskJson.put("mark22", mark22.getText().trim()); }
        if(mark23.getText().isEmpty() || mark23.getText()==null || !checkNum(mark23.getText())){key = false;}
        else {taskJson.put("mark23", mark23.getText().trim()); }
        if( mProfit.getText().isEmpty() || mProfit.getText()==null || !checkNum(mProfit.getText())){key = false;}
        else {taskJson.put("mProfit", mProfit.getText().trim()); }

        if(key) {
            Client.getInstance().send("getSinglSolution");
            Client.getInstance().send(taskJson.toString());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/by/bsuir/client//sample/view/task.fxml"));

            loader.load();

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            stage.setTitle("Решение");

        }
        else {
            Alert alert = new Alert(  Alert.AlertType.WARNING);
            alert.setTitle( "Ошибка" );
            alert.setHeaderText( "Заполните все поля корректно!" );
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        projectInTable();
    }

    public void projectInTable() {

        CollectionDetails.getInstance().fillData();
        idProject.setCellValueFactory(new PropertyValueFactory<Details, Integer>("id"));
        constructor.setCellValueFactory(new PropertyValueFactory<Details, String>("constructor"));
        manager.setCellValueFactory(new PropertyValueFactory<Details, String>("manager"));
        engineer.setCellValueFactory(new PropertyValueFactory<Details, String>("engineer"));
        name.setCellValueFactory(new PropertyValueFactory<Details, String>("name"));
        number.setCellValueFactory(new PropertyValueFactory<Details, Integer>("number"));
        format.setCellValueFactory(new PropertyValueFactory<Details, String>("format"));
        machine.setCellValueFactory(new PropertyValueFactory<Details, Integer>("machine"));
        projectTable.setItems(CollectionDetails.getInstance().getDetails());

    }

    @FXML
    void adPrForTask(ActionEvent event) {
        Details selectedDetails = (Details) projectTable.getSelectionModel().getSelectedItem();
        CollectionDetails.getInstance().selectProject(selectedDetails);
        selectedProjectInTable();
    }

    @FXML
    void deletePr(ActionEvent event) {
        Details selectedDetails = (Details) selectedPrTable.getSelectionModel().getSelectedItem();
        CollectionDetails.getInstance().deletePrTask(selectedDetails);
    }

    public void selectedProjectInTable(){
        selectedDetails.setCellValueFactory(new PropertyValueFactory<Details, String>("name"));
        selectedPrTable.setItems(CollectionDetails.getInstance().getSelectedProject());
    }

    private boolean checkNum(String source) {
        Pattern pattern = Pattern.compile("^([0-9]+)$");
        Matcher matcher = pattern.matcher(source);

        if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }
}

