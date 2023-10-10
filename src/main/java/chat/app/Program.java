package chat.app;

import chat.model.Chatroom;
import chat.model.User;
import chat.repositories.DataBase;
import chat.repositories.UsersRepositoryJdbcImpl;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        String baseURL = "jdbc:postgresql://localhost:5433/postgres";
        String baseUser = "postgres";
        String basePassword = "postgres";
        DataBase dataBase = new DataBase(baseURL, baseUser, basePassword);
        dataBase.fillData("schema.sql");
        dataBase.fillData("data.sql");

        UsersRepositoryJdbcImpl usersRepository = new UsersRepositoryJdbcImpl(dataBase.getDataSource());
        List<User> arrayList = usersRepository.findAll(3, 4);
        for (User u : arrayList) {
            System.out.println("User : {\n  id=" + u.getUserId() + ",\n  login='" + u.getLogin() +
                    "',\n  password='" + u.getPassword() + "',");
            if (!u.getListCreatedRooms().isEmpty()) {
                System.out.println("  Created Chatrooms : {");
                for (Chatroom c : u.getListCreatedRooms()) {
                    System.out.println("    " + c);
                }
                System.out.println("  },");
            } else {
                System.out.println("  Created Chatrooms : {},");
            }
            if (!u.getListSocializes().isEmpty()) {
                System.out.println("  Socializes : {");
                for (Chatroom c : u.getListSocializes()) {
                    System.out.println("    " + c);
                }
                System.out.println("  }");
            } else {
                System.out.println("  Socializes : {}");
            }
        }
        System.exit(0);
    }
}
