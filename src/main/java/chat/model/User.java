package chat.model;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private Long userId;
    private String login;
    private String password;
    private ArrayList<Chatroom> listCreatedRooms;
    private ArrayList<Chatroom> listSocializes;

    public User(Long id, String name, String pass, ArrayList<Chatroom> createdRooms, ArrayList<Chatroom> socializes) {
        userId = id;
        login = name;
        password = pass;
        listCreatedRooms = createdRooms;
        listSocializes = socializes;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Chatroom> getListCreatedRooms() {
        return listCreatedRooms;
    }

    public ArrayList<Chatroom> getListSocializes() {
        return listSocializes;
    }

    public void setUserId(Long id) {
        userId = id;
    }

    public void setLogin(String name) {
        login = name;
    }

    public void setPassword(String pass) {
        password = pass;
    }

    public void setListCreatedRooms(ArrayList<Chatroom> createdRooms) {
        listCreatedRooms = createdRooms;
    }

    public void setListSocializes(ArrayList<Chatroom> socializes) {
        listSocializes = socializes;
    }

    @Override
    public boolean equals(Object obj) {
        if (hashCode() != obj.hashCode()) return false;
        if (getClass() != obj.getClass()) return false;
        User that = (User) obj;
        if (userId != null && !userId.equals(that.userId)) return false;
        if (login != null && !login.equals(that.login)) return false;
        if (password != null && !password.equals(that.password)) return false;
        if (listCreatedRooms != that.listCreatedRooms) return false;
        return listSocializes == that.listSocializes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, login, password);
    }

    @Override
    public String toString() {
        return "{id:" + userId + ", login:'" + login + "', p/w:'" + password +
                "', created:" + listCreatedRooms + ", socialized:" + listSocializes + "}";
    }
}
