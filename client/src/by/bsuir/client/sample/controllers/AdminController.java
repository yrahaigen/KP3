package by.bsuir.client.sample.controllers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.bsuir.client.sample.connectoin.Client;
import by.bsuir.client.sample.information.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.json.JSONObject;

public class AdminController {


    @FXML
    private Button usersButton;

    @FXML
    private Button projectButton;

    @FXML
    private Button tasksButton;

    @FXML
    private Button backButton;

    @FXML
    private Pane userPane;

    @FXML
    private TableView<UserInf> userTable;

    @FXML
    private TableColumn<UserInf, Integer> id;

    @FXML
    private TableColumn<UserInf, String> firstName;

    @FXML
    private TableColumn<UserInf, String> lastName;

    @FXML
    private TableColumn<UserInf, String> email;

    @FXML
    private TableColumn<UserInf, String> userName;

    @FXML
    private TableColumn<UserInf, String> password;

    @FXML
    private Button search;

    @FXML
    private Button delete;



    @FXML
    private Pane projectPane;

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
    private TextField constructorField;

    @FXML
    private TextField managerField;

    @FXML
    private TextField engineerField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField formatName;

    @FXML
    private TextField machineField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Pane taskPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Details> selectedPrTable;

    @FXML
    private TableColumn<Details, String> selectedProject;

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
    private TextField searchPrField;


    @FXML
    void handleCliks(ActionEvent event) {

        if(event.getSource()==usersButton){
            userPane.toFront();
        }
        else if(event.getSource()==projectButton){
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
    void searchUser() {

        FilteredList<UserInf> filterUsers;
        filterUsers = new FilteredList<>(CollectionUsers.getInstance().getUsers(), e->true);
        searchField.textProperty().addListener((observableValue, oldValue, newValue)->{
            filterUsers.setPredicate((UserInf user)->{

                String newVal = newValue.toLowerCase();
                return  user.getFirstName().toLowerCase().contains(newVal)
                        || user.getLastName().toLowerCase().contains(newVal)
                        || user.getEmail().toLowerCase().contains(newVal)
                        || user.getUserName().toLowerCase().contains(newVal)
                        || user.getPassword().toLowerCase().contains(newVal);

            });
        userTable.setItems(filterUsers);
        });
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
    public void deleteProject(ActionEvent event) {
        Details selectedDetails = (Details) projectTable.getSelectionModel().getSelectedItem();
        CollectionDetails.getInstance().delete(selectedDetails);
        Client.getInstance().send( "deleteProject" );
        Client.getInstance().send( selectedDetails.getId());
    }

    @FXML
    public void deleteUser(ActionEvent actionEvent) {
        UserInf selectedUser = (UserInf) userTable.getSelectionModel().getSelectedItem();
        CollectionUsers.getInstance().delete( selectedUser );
        Client.getInstance().send( "deleteUser" );
        Client.getInstance().send( selectedUser.getId());
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

    @FXML
    void initialize() {

        projectTable.setEditable(true);
        userTable.setEditable(true);

        userInTable();

        projectInTable();

    }

    @FXML
    void addProject(ActionEvent event) {


        boolean key = true;
        JSONObject projectJson = new JSONObject();
        Details details;
        if( constructorField.getText().isEmpty() || constructorField.getText()==null || !checkWord(constructorField.getText())){key = false;}
        else {projectJson.put("constructor", constructorField.getText().trim()); }
        if( managerField.getText().isEmpty() || managerField.getText()==null || !checkWord(managerField.getText())){key = false;}
        else{projectJson.put("manager", managerField.getText().trim());}
        if( engineerField.getText().isEmpty() || engineerField.getText()==null || !checkWord(engineerField.getText())){key = false;}
        else{projectJson.put("engineer", engineerField.getText().trim());}
        if( nameField.getText().isEmpty() || nameField.getText()==null || !checkWord(nameField.getText()) ){key = false;}
        else{projectJson.put("name", nameField.getText().trim());}
        if( numberField.getText().isEmpty() || numberField.getText()==null || !checkNum(numberField.getText())){key = false;}
        else{projectJson.put("number", numberField.getText().trim());}
        if( formatName.getText().isEmpty() || formatName.getText()==null || !checkWord(formatName.getText()) ){key = false;}
        else{projectJson.put("format", formatName.getText().trim());}
        if( machineField.getText().isEmpty() || machineField.getText()==null  ||  !checkNum(machineField.getText())){key = false;}
        else{projectJson.put("machine", machineField.getText().trim());}
        if(key) {
            Client.getInstance().send("getProject");
            Client.getInstance().send( projectJson.toString() );
             details = Details.getInstance();

            CollectionDetails.getInstance().fillNewProject();
            idProject.setCellValueFactory(new PropertyValueFactory<Details, Integer>("id"));
            constructor.setCellValueFactory(new PropertyValueFactory<Details, String>("constructor"));
            manager.setCellValueFactory(new PropertyValueFactory<Details, String>("manager"));
            engineer.setCellValueFactory(new PropertyValueFactory<Details, String>("engineer"));
            name.setCellValueFactory(new PropertyValueFactory<Details, String>("name"));
            number.setCellValueFactory(new PropertyValueFactory<Details, Integer>("number"));
            format.setCellValueFactory(new PropertyValueFactory<Details, String>("format"));
            machine.setCellValueFactory(new PropertyValueFactory<Details, Integer>("machine"));
            projectTable.setItems( CollectionDetails.getInstance().getDetails());

        }
        else {
            Alert alert = new Alert(  Alert.AlertType.WARNING);
            alert.setTitle( "Ошибка" );
            alert.setHeaderText( "Поля введены не корректно!" );
            alert.showAndWait();
        }
    }


    public void userInTable(){

        CollectionUsers.getInstance().fillData();
        id.setCellValueFactory(new PropertyValueFactory<UserInf, Integer>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<UserInf, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<UserInf, String>("lastName"));
        email.setCellValueFactory(new PropertyValueFactory<UserInf, String>("email"));
        userName.setCellValueFactory(new PropertyValueFactory<UserInf, String>("userName"));
        password.setCellValueFactory(new PropertyValueFactory<UserInf, String>("password"));
        userTable.setItems( CollectionUsers.getInstance().getUsers());

        firstName.setCellFactory(TextFieldTableCell.forTableColumn());
        lastName.setCellFactory(TextFieldTableCell.forTableColumn());
        email.setCellFactory(TextFieldTableCell.forTableColumn());
        userName.setCellFactory(TextFieldTableCell.forTableColumn());
        password.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void projectInTable(){

        CollectionDetails.getInstance().fillData();
        idProject.setCellValueFactory(new PropertyValueFactory<Details, Integer>("id"));
        constructor.setCellValueFactory(new PropertyValueFactory<Details, String>("constructor"));
        manager.setCellValueFactory(new PropertyValueFactory<Details, String>("manager"));
        engineer.setCellValueFactory(new PropertyValueFactory<Details, String>("engineer"));
        name.setCellValueFactory(new PropertyValueFactory<Details, String>("name"));
        number.setCellValueFactory(new PropertyValueFactory<Details, Integer>("number"));
        format.setCellValueFactory(new PropertyValueFactory<Details, String>("format"));
        machine.setCellValueFactory(new PropertyValueFactory<Details, Integer>("machine"));
        projectTable.setItems( CollectionDetails.getInstance().getDetails());

        constructor.setCellFactory(TextFieldTableCell.forTableColumn());
        manager.setCellFactory(TextFieldTableCell.forTableColumn());
        engineer.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        number.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return ""+object;
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }
        }));
        format.setCellFactory(TextFieldTableCell.forTableColumn());
        machine.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return ""+object;
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }
        }));
    }

    public void selectedProjectInTable(){
        selectedProject.setCellValueFactory(new PropertyValueFactory<Details, String>("name"));
        selectedPrTable.setItems(CollectionDetails.getInstance().getSelectedProject());
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
    void editProject(ActionEvent event) {
        Client.getInstance().send( "changeProject" );

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

    private boolean checkWord(String source) {
        Pattern pattern = Pattern.compile("^([А-Я][а-я]+)$");
        Matcher matcher = pattern.matcher(source);

        if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

}

