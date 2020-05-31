package by.bsuir.client.sample.information;

import by.bsuir.client.sample.connectoin.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public final class CollectionDetails {

    private ObservableList<Details> details = FXCollections.observableArrayList();

    private ObservableList<Details> selectedDetails = FXCollections.observableArrayList();



    private static CollectionDetails instance;

    public static synchronized CollectionDetails getInstance(){
        if(instance == null){
            instance = new CollectionDetails();
        }
        return instance;
    }

    public ObservableList<Details> getDetails() {
        return details;
    }

    public ObservableList<Details> getSelectedProject() {
        return selectedDetails;
    }

    public void delete(Details details){
        this.details.remove(details);
    }

    public void deletePrTask(Details details){
        selectedDetails.remove(details);
    }

    public void selectProject(Details details){
        if(selectedDetails.size()>2){
            Alert alert = new Alert(  Alert.AlertType.WARNING);
            alert.setTitle( "Ошибка" );
            alert.setHeaderText( "В методе уже выбранно 3 детали" );
            alert.showAndWait();
        }
        else {
            selectedDetails.add(details);
        }
    }

    public void fillData(){
        try {
            details.removeAll(details);
            String array = Client.getInstance().get();
            JSONArray newArray = new JSONArray( array );
            int count = newArray.length();
            for(int i = 0; i<count; i++) {
                JSONObject object = newArray.getJSONObject( i );
                int id = object.getInt("id");
                String constructor = object.getString( "constructor" );
                String manager = object.getString( "manager" );
                String engineer = object.getString( "engineer" );
                String name = object.getString( "name" );
                int number = object.getInt("number");
                String format = object.getString( "format" );
                int machine = object.getInt("machine");
                Details details = new Details(id, constructor, manager, engineer, name, number, format, machine );
                this.details.add(details);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    public void fillNewProject(){

        try {
            String array = Client.getInstance().get();
            JSONObject object = new JSONObject(array);

            int id = object.getInt("id");
            String constructor = object.getString( "constructor" );
            String manager = object.getString( "manager" );
            String engineer = object.getString( "engineer" );
            String name = object.getString( "name" );
            int number = object.getInt("number");
            String format = object.getString( "format" );
            int machine = object.getInt("machine");
            Details details = new Details(id, constructor, manager, engineer, name, number, format, machine );
            this.details.add(details);


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


    }

}





