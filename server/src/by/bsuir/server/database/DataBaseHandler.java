package by.bsuir.server.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public class DataBaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException{
        String connectionSting="jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName  + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection=DriverManager.getConnection(connectionSting, dbUser, dbPass);

        return dbConnection;
    }

    public String signUpUser(String firstName, String lastName, String email,
                             String userName, String password){
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" + Const.USER_FIRSTNAME + "," + Const.USER_LASTNAME + "," +
                Const.USER_EMAIL + "," + Const.USER_USERNAME + "," + Const.USER_PASSWORD + ")" +
                "VALUES(?,?,?,?,?)";



        try {
                PreparedStatement prSt = getDbConnection().prepareStatement(insert);
                prSt.setString(1, firstName);
                prSt.setString(2, lastName);
                prSt.setString(3, email);
                prSt.setString(4, userName);
                prSt.setString(5, password);
                prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        JSONObject userJson = new JSONObject();
        try {
            userJson.put("firstName", firstName);
            userJson.put("lastName", lastName);
            userJson.put("email", email);
            userJson.put("userName", userName);
            userJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userJson.toString();
    }

    public String checkUser(String login){
        JSONObject userJson = new JSONObject();
        User user = new User();
        String result =null;
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_USERNAME + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, login);
            ResultSet rs = prSt.executeQuery();

            if (!rs.next()) {
                result = "nobody";
            } else
            { do {

                user.setUserName(rs.getString(5));
                result = user.getUserName();


            } while (rs.next());

                result = user.getUserName();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String signInUser(String login, String password) throws SQLException {
        JSONObject userJson = new JSONObject();
        User user = new User();
        String result =null;
        try {
            // Statement stmt = getDbConnection().createStatement();
            String select = "SELECT * FROM "+ Const.USER_TABLE + " WHERE "+ Const.USER_USERNAME +
                    "= ?";
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, login);
            //prep.setString(2, password);
            ResultSet rs = prSt.executeQuery();
            if (!rs.next()) {
                result = "nobody";
            } else
            { do {
                user.setID(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setUserName(rs.getString(5));
                user.setPassword(rs.getString(6));

                userJson.put("id", user.getID());
                userJson.put("firstName", user.getFirstName());
                userJson.put("lastName", user.getLastName());
                userJson.put("email", user.getEmail());
                userJson.put("userName", user.getUserName());
                userJson.put("password", user.getPassword());
            } while (rs.next());
                result = userJson.toString();
            }

        } catch (ClassNotFoundException | JSONException e) {
            e.printStackTrace();
        }
        //if(user.getName().equals("null")) result = user.getName();

        return result;
    }

    public String getUsers() {
        User user;
        JSONObject userJson;
        JSONArray users = new JSONArray(  );
        try {

            String select = "SELECT * FROM "+Const.USER_TABLE;
            PreparedStatement prep1 = getDbConnection().prepareStatement( select );
            ResultSet rs = prep1.executeQuery();
            while (rs.next()){

                user = new User();
                user.setID(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setUserName(rs.getString(5));
                user.setPassword(rs.getString(6));

                userJson = new JSONObject();
                userJson.put("id", user.getID());
                userJson.put("firstName", user.getFirstName());
                userJson.put("lastName", user.getLastName());
                userJson.put("email", user.getEmail());
                userJson.put("userName", user.getUserName());
                userJson.put("password", user.getPassword());

                users.put( userJson );
            }

        } catch (SQLException | ClassNotFoundException | JSONException e) {
            e.printStackTrace();
        }

        return users.toString();
    }

    public void deleteUser(Integer userId) throws SQLException, ClassNotFoundException {
        String deletion = "DELETE FROM " + Const.USER_TABLE+ " WHERE "+ Const.USER_ID +" = ?";
        PreparedStatement prSt=getDbConnection().prepareStatement(deletion);
        prSt.setInt(1,userId);
        prSt.executeUpdate();
    }

    public void deleteDetails(Integer projectId) throws SQLException, ClassNotFoundException {
        String deletion = "DELETE FROM " + Const.DETAILS_TABLE+ " WHERE "+ Const.DETAILS_ID +" = ?";
        PreparedStatement prSt=getDbConnection().prepareStatement(deletion);
        prSt.setInt(1,projectId);
        prSt.executeUpdate();
    }

    public String signInAdmin(String login, String password) throws SQLException {
        JSONObject userJson = new JSONObject();
        Admin admin = new Admin();
        String result =null;
        try {
            // Statement stmt = getDbConnection().createStatement();
            String select = "SELECT * FROM "+ Const.ADMIN_TABLE + " WHERE "+ Const.ADMIN_USERNAME +
                    "= ?";
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, login);
            //prep.setString(2, password);
            ResultSet rs = prSt.executeQuery();
            if (!rs.next()) {

                result = "nobody";
            } else
            { do {
                admin.setID(rs.getInt(1));

                admin.setUserName(rs.getString(2));
                admin.setPassword(rs.getString(3));

                userJson.put("id", admin.getID());
                userJson.put("userName", admin.getUserName());
                userJson.put("password", admin.getPassword());
            } while (rs.next());
                result = userJson.toString();
            }

        } catch (ClassNotFoundException | JSONException e) {
            e.printStackTrace();
        }
        //if(user.getName().equals("null")) result = user.getName();

        return result;
    }

    public String detailsInput(int id, String constructor, String manager, String engineer,
                             String name, int number, String format, int machine){
        String insert = "INSERT INTO " + Const.DETAILS_TABLE + "(" + Const.DETAILS_ID + ","+ Const.DETAILS_CONSTRUCTOR+ "," + Const.DETAILS_MANAGER + "," +
                Const.DETAILS_ENGINEER + "," + Const.DETAILS_NAME + "," + Const.DETAILS_NUMBER + "," + Const.DETAILS_FORMAT
                + "," + Const.DETAILS_MACHINE+ ")" +
                "VALUES(?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setInt(1, id);
            prSt.setString(2, constructor);
            prSt.setString(3, manager);
            prSt.setString(4, engineer);
            prSt.setString(5, name);
            prSt.setInt(6, number);
            prSt.setString(7, format);
            prSt.setInt(8, machine);

            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        JSONObject userJson = new JSONObject();
        try {
            userJson.put("id", id);
            userJson.put("constructor", constructor);
            userJson.put("manager", manager);
            userJson.put("engineer", engineer);
            userJson.put("name", name);
            userJson.put("number", number);
            userJson.put("format", format);
            userJson.put("machine", machine);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userJson.toString();
    }

    public String getDetails() {
        Details details;
        JSONObject projectJson;
        JSONArray projects = new JSONArray(  );
        try {

            String select = "SELECT * FROM "+Const.DETAILS_TABLE;
            PreparedStatement prep1 = getDbConnection().prepareStatement( select );
            ResultSet rs = prep1.executeQuery();
            while (rs.next()){

                details = new Details();
                details.setId(rs.getInt(1));
                details.setConstructor(rs.getString(2));
                details.setManager(rs.getString(3));
                details.setEngineer(rs.getString(4));
                details.setName(rs.getString(5));
                details.setNumber(rs.getInt(6));
                details.setFormat(rs.getString(7));
                details.setMachine(rs.getInt(8));


                projectJson = new JSONObject();
                projectJson.put("id", details.getId());
                projectJson.put("constructor", details.getConstructor());
                projectJson.put("manager", details.getManager());
                projectJson.put("engineer", details.getEngineer());
                projectJson.put("name", details.getName());
                projectJson.put("number", details.getNumber());
                projectJson.put("format", details.getFormat());
                projectJson.put("machine", details.getMachine());

                projects.put( projectJson );
            }

        } catch (SQLException | ClassNotFoundException | JSONException e) {
            e.printStackTrace();
        }

        return projects.toString();
    }


}

