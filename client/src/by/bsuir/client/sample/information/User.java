package by.bsuir.client.sample.information;

import by.bsuir.client.sample.connectoin.Client;
import javafx.scene.control.Alert;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public final class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;

    private static User instance;

    public static synchronized User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }


    public User() {
        try {

            String str = Client.getInstance().get();

                JSONObject json = new JSONObject(str);
                if(!str.equals("exist")) {
                    id = json.getInt("id");
                    firstName = json.getString("firstName");
                    lastName = json.getString("lastName");
                    email = json.getString("email");
                    userName = json.getString("userName");
                    password = json.getString("password");
                }
                 else {
                     Alert alert = new Alert(Alert.AlertType.WARNING);
                     alert.setTitle("Ошибка");
                     alert.setHeaderText("Такой логин уже существует");
                     alert.showAndWait();
        }

        } catch(JSONException | IOException e){
            System.err.println(e);
        }

    }


    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

