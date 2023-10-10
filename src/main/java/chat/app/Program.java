package chat.app;

import chat.repositories.DataBase;

public class Program {
    public static void main(String[] args) {
        Menu m = new Menu(dataBaseConnect().getDataSource());
        m.menuItem();
        System.exit(0);
    }

    public static DataBase dataBaseConnect() {
        String baseURL = "jdbc:postgresql://localhost:5433/postgres";
        String baseUser = "postgres";
        String basePassword = "postgres";
        DataBase dataBase = DataBase.getInstance();
        dataBase.init(baseURL, baseUser, basePassword);
        DataBase db = DataBase.getInstance();
        db.fillData("schema.sql");
        dataBase.fillData("data.sql");
        return dataBase;
    }
}
