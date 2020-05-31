package by.bsuir.server.actions;

import by.bsuir.server.database.DataBaseHandler;
import by.bsuir.server.database.IdGenerator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class Action implements Runnable {
    protected Socket clientSocket = null;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static InputStream input;
    private static OutputStream output;

    public Action(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();
            in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
            out = new BufferedWriter( new OutputStreamWriter( clientSocket.getOutputStream() ) );

            String exit = "ok";
            while (!exit.equals( "exit" )) {
                exit = "ok";

                String str = in.readLine();

                if(in!=null) {
                    System.out.println("я получил: " + str);
                    String who = null;

                    switch (str) {
                        case "authorization": {
                            who = authorization();
                            break;
                        }
                        case "registration": {
                            registration();
                            who = "ok";
                            break;
                        }
                        case "back": {
                            exit = "ok";
                            break;
                        }
                        case "exit": {
                            exit = "exit";
                            output.close();
                            input.close();
                            break;
                        }

                        default:
                            break;
                    }

                    switch (who) {
                        case "admin":
                            exit = menuAdmin();
                            break;

                        case "user":
                            exit = menuUser();
                            break;

                        default : break;
                    }
                }


            }

            out.close();
            in.close();
        }
        catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private String menuAdmin() throws SQLException, ClassNotFoundException {
        DataBaseHandler handler = new DataBaseHandler();
        String users = handler.getUsers();
        String projects = handler.getDetails();
        String menu = "work";
        try {
            out.write( users + '\n' );
            out.flush();
            System.out.println( "я отправил: " + users );

            out.write(projects+'\n');
            out.flush();
            System.out.println("я оправил проекты: " + projects);

            while (!menu.equals( "back" )) {
                menu = in.readLine();
                System.out.println( "я получил: " + menu );
                switch (menu) {
                    case "deleteUser":{
                        int userId = in.read();
                        System.out.println( "я получил: " + userId );
                        handler.deleteUser(userId);
                        break;
                    }
                    case "deleteProject":{
                        int detailsId = in.read();
                        System.out.println( "я получил: " + detailsId );
                        handler.deleteDetails(detailsId);
                        break;
                    }
                    case "searchLogin":{
                        String userLogin = in.readLine();
                        String search = handler.checkUser(userLogin);
                        out.write( search + '\n' );
                        out.flush();
                        System.out.println("я отправил: " + search);
                        break;
                    }
                    case "getProject":{
                        getProject();
                        break;
                    }
                    case "getSinglSolution":{
                        getSinglSolution();
                        break;
                    }
                    case "back": {
                        menu = "back";
                        break;
                    }
                    default:break;
                }
            }
        }
        catch (IOException | JSONException e) {
           e.printStackTrace();
        }
        return menu;

    }

    protected void registration() {
        try {
            String user;

            user = in.readLine();
            if (user != null) {
                System.out.println("я получил: " + user);
                if (!user.equals("back")) {
                    JSONObject userJson = new JSONObject(user);
                    int id = IdGenerator.getInstance("user").getNextId();

                    String firstName = userJson.getString("firstName");
                    String lastName = userJson.getString("lastName");
                    String email = userJson.getString("email");
                    String userName = userJson.getString("userName");
                    String password = userJson.getString("password");

                    DataBaseHandler handler = new DataBaseHandler();

                    String signIn = handler.checkUser(userName);
                    if(signIn.equals(userName)){
                        out.write("exist" + '\n');
                        out.flush();
                        System.out.println("я отправил: " + "exist");
                    }
                    else {
                      String sign = handler.signUpUser(firstName, lastName, email, userName, password);
                      out.write(sign + '\n');
                      out.flush();
                      System.out.println("я отправил: " + sign);
                    }
                }
            }
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    protected String authorization() {
        String who = null;
        try {
            String user = in.readLine();
            JSONObject userJson = new JSONObject( user );

            String userName = userJson.getString( "userName" );
            String password = userJson.getString( "password" );

            DataBaseHandler handler = new DataBaseHandler();
            String sign = handler.signInAdmin( userName, password );
            if(!sign.equals("nobody")){
                who = "admin";
                output.write( "admin\n".getBytes() );
                out.flush();
                System.out.println( "я отправил: " + "admin" );

            }


            else {

                 sign = handler.signInUser( userName, password );

                if(sign.equals( "nobody" )){
                    who = "nobody";
                    output.write( "nobody\n".getBytes() );
                    out.flush();
                    System.out.println( "я отправил: " + "nobody" );

                }
                else {
                    who = "user";
                    output.write( "user\n".getBytes() );
                    out.flush();
                    System.out.println( "я отправил: " + "user" );

                }

            }


        }
        catch (IOException | JSONException | SQLException e) {
            e.printStackTrace();
        }
        return who;
    }

    public String menuUser() throws IOException {
        DataBaseHandler handler = new DataBaseHandler();

        String projects = handler.getDetails();
        String menu2 = "work";
        try {
            out.write(projects+'\n');
            out.flush();
            System.out.println("я оправил проекты: " + projects);


            while (!menu2.equals( "back" )) {
                menu2 = in.readLine();
                System.out.println( "я получил: " + menu2 );
                switch (menu2) {

                    case "getProject":{
                        getProject();
                        break;
                    }
                    case "getSinglSolution":{
                        getSinglSolution();
                        break;
                    }
                    case "back": {
                        menu2 = "back";
                        break;
                    }
                    default:break;
                }
            }
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return menu2;
    }

    protected void getProject(){
        try {
            String project;
            project = in.readLine();
            if(!project.equals( "back" )) {
                JSONObject projectJson = new JSONObject( project );
                int id = IdGenerator.getInstance( "project" ).getNextId();

                String constructor = projectJson.getString( "constructor" );
                String manager = projectJson.getString( "manager" );
                String engineer = projectJson.getString( "engineer" );
                String name = projectJson.getString( "name" );
                int number = projectJson.getInt( "number" );
                String format = projectJson.getString( "format" );
                int machine = projectJson.getInt( "machine" );

                DataBaseHandler handler = new DataBaseHandler();
                String input = handler.detailsInput( id, constructor, manager, engineer, name, number, format, machine );
                out.write( input + '\n' );
                out.flush();
                System.out.println( "я отправил: " + input );
                out.write( input + '\n' );
                out.flush();
                System.out.println( "я отправил новую деталь: " + input );
            }
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    protected void getSinglSolution(){
        try {
            String task;
            task = in.readLine();
            if(!task.equals( "back" )) {
                JSONObject taskJson = new JSONObject( task );

                int mark11 = taskJson.getInt( "mark11" );
                int mark12 = taskJson.getInt( "mark12" );
                int mark13 = taskJson.getInt( "mark13" );
                int mark21 = taskJson.getInt( "mark21" );
                int mark22 = taskJson.getInt( "mark22" );
                int mark23 = taskJson.getInt( "mark23" );


                int[][] array = {{mark11, mark12, mark13}, {mark21, mark22, mark23}};

                for(int i=0; i<2; i++){
                    for(int j=0; j<3; j++){
                        System.out.print(array[i][j]+ " ");
                    }
                    System.out.print("\n");
                }

                float[] array_yd = new float[3];
                for(int i=0; i<3; i++){
                    array_yd[i]=((float)(3*array[0][i]+2*array[1][i]))/5;
                }

                for(int i=0; i<3; i++){
                    System.out.print(array_yd[i]+ " ");
                }
                System.out.print("\n");


                float[] array_av = new float[3];
                for(int i=0; i<3; i++){
                    array_av[i]=((float)(array[1][i]-array[0][i]))/5;
                }


                for(int i=0; i<3; i++){
                    System.out.print(array_av[i] + " ");
                }
                System.out.print("\n");


                float[] array_ans = new float[3];
                for(int i=0; i<3; i++){
                    array_ans[i] = (array_av[i]/array_yd[i]);
                }

                for(int i=0; i<3; i++){
                    System.out.print(array_ans[i]+ " ");
                }
                System.out.print("\n");


                float x = array_ans[0];
                float y = array_ans[0];
                int a = -1;
                int b = -1;


                for(int i=0; i<3;i++){
                    if (array_ans[i] <= x){
                        x = array_ans[i];
                        a = i;
                    }
                    if (array_ans[i] >= y){
                        y = array_ans[i];
                        b = i;
                    }
                }
                b=b+1;
                a=a+1;
                System.out.println("Минимальная оценка риска: " +x +"\nМаксимальная оценка риска: " +y);

                String ans_pr1 = Float.toString(array_ans[0]);
                String ans_pr2 = Float.toString(array_ans[1]);
                String ans_pr3 = Float.toString(array_ans[2]);
                String x_ans = Float.toString(x);
                String y_ans = Float.toString(y);
                String a_ans = Integer.toString(a);
                String b_ans = Integer.toString(b);




                out.write( a_ans + '\n' );
                out.flush();
                System.out.println( "Оптимально выбрать деталь под № " + a_ans );

                out.write( b_ans + '\n' );
                out.flush();
                System.out.println( "Нерационально выбирать деталь под № " + b_ans );

                out.write( ans_pr1 + '\n' );
                out.flush();
                System.out.println( "я отправил риски Пр1: " + ans_pr1 );

                out.write( ans_pr2 + '\n' );
                out.flush();
                System.out.println( "я отправил риски Пр2: " + ans_pr2 );
                out.write( ans_pr3 + '\n' );
                out.flush();
                System.out.println( "я отправил риски Пр3: " + ans_pr3 );
            }
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }



}