package by.bsuir.client.sample.information;

import by.bsuir.client.sample.connectoin.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public final class CollectionUsers{

    private ObservableList<UserInf> users = FXCollections.observableArrayList();

    private static CollectionUsers instance;

    public static synchronized CollectionUsers getInstance(){
        if(instance == null){
            instance = new CollectionUsers();
        }
        return instance;
    }

    public ObservableList<UserInf> getUsers() {
        return users;
    }

    public void delete(UserInf user){
        users.remove(user);
    }

    public void fillData(){
        try {
            users.removeAll(users);
            String array = Client.getInstance().get();
            JSONArray newArray = new JSONArray( array );
            int count = newArray.length();
            for(int i = 0; i<count; i++) {
                JSONObject object = newArray.getJSONObject( i );
                Integer id = object.getInt("id");
                String firstName = object.getString( "firstName" );
                String lastName = object.getString( "lastName" );
                String email = object.getString( "email" );
                String userName = object.getString( "userName" );
                String password=object.getString("password");
                UserInf user = new UserInf(id, firstName, lastName, email, userName, password );
                users.add( user );
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

}
