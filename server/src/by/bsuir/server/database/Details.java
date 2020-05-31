package by.bsuir.server.database;

public class Details {
    private int id;
    private String constructor;
    private String manager;
    private String engineer;
    private String name;
    private int number;
    private String format;
    private int machine;

    public Details(){}

    public Details(int id, String constructor, String manager, String engineer, String name, int number, String format, int machine) {
        this.id = id;
        this.constructor = constructor;
        this.manager = manager;
        this.engineer= engineer;
        this.name = name;
        this.number = number;
        this.format = format;
        this.machine = machine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConstructor() {
        return constructor;
    }

    public void setConstructor(String constructor) {
        this.constructor = constructor;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getMachine() {
        return machine;
    }

    public void setMachine(int machine) {
        this.machine = machine;
    }
}
