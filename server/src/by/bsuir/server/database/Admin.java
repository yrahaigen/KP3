package by.bsuir.server.database;

public class Admin {
    private int id;
    private String userName;
    private String password;

    Admin(int id, String userName, String password){
        this.id=id;
        this.userName=userName;
        this.password=password;
    }

    Admin(){}

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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
