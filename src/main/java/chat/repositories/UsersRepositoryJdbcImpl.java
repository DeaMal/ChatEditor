package chat.repositories;

import chat.model.Chatroom;
import chat.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final DataSource data;

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        data = dataSource;
    }

    @Override
    public List<User> findAll(int page, int size) {
        List<User> userList = new ArrayList<>();
        List<Chatroom> chatroomList = new ArrayList<>();
        User newUser = null;
        Chatroom newChatroom = null;
        int currentUser = -1, currentCreated = 0,  currentSocializes = 0;
        String queryString ="WITH l2 AS (SELECT u.id as user_id, m.room as id, c.name as name,\n" +
                "                   c.owner as owner, u2.login as owner_login, u2.password as owner_pw\n" +
                "            FROM chat.user u\n" +
                "                LEFT JOIN chat.message m on u.id = m.author\n" +
                "                LEFT JOIN chat.chatroom c on m.room = c.id\n" +
                "                LEFT JOIN chat.\"user\" u2 on c.owner = u2.id\n" +
                "            GROUP BY 1, 2, 3, 4, 5, 6\n" +
                "            ORDER BY 1, 2)\n" +
                "SELECT u.id, u.login, u.password, l1.id, l1.name,\n" +
                "       l2.id, l2.name, l2.owner, l2.owner_login, l2.owner_pw\n" +
                "FROM (SELECT * FROM chat.user LIMIT ? OFFSET ?) AS u\n" +
                "    LEFT JOIN chat.chatroom l1 on u.id = l1.owner\n" +
                "    LEFT JOIN l2 on l2.user_id = u.id\n" +
                "ORDER BY u.id, l1.id, l2.id;";
        try {
            Connection connection = data.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, page * size);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (currentUser + 1 == rs.getInt(1)) {
                    for (User u : userList) {
                        if (u.getUserId() == currentUser + 1) {
                            newUser = u;
                            break;
                        }
                    }
                } else {
                    newUser = null;
                    for (User u : userList) {
                        if (u.getUserId() == rs.getInt(1) - 1) {
                            newUser = u;
                            break;
                        }
                    }
                    if (newUser == null) {
                        currentCreated = 0;
                        currentSocializes = 0;
                        newUser = new User(rs.getLong(1) - 1, rs.getString(2),
                                rs.getString(3), new ArrayList<>(), new ArrayList<>());
                        userList.add(newUser);
                        currentUser = rs.getInt(1) - 1;
                    }
                }
                if(rs.getInt(4) != currentCreated) {
                    currentCreated = rs.getInt(4);
                    for (Chatroom c : chatroomList) {
                        if (c.getChatroomId() == currentCreated) {
                            newChatroom = c;
                            break;
                        }
                    }
                    if (newChatroom == null || newChatroom.getChatroomId() != rs.getLong(4)) {
                        newChatroom = new Chatroom(rs.getLong(4), rs.getString(5), newUser, null);
                        chatroomList.add(newChatroom);
                    }
                    if (newUser != null) {
                        newUser.getListCreatedRooms().add(newChatroom);
                    }
                }
                if(rs.getInt(6) != currentSocializes) {
                    currentSocializes = rs.getInt(6);
                    for (Chatroom c : chatroomList) {
                        if (c.getChatroomId() == currentCreated) {
                            newChatroom = c;
                            break;
                        }
                    }
                    if (newChatroom == null || newChatroom.getChatroomId() != rs.getLong(6)) {
                        User findUser = null;
                        for (User u : userList) {
                            if (u.getUserId() == rs.getLong(8) - 1) {
                                findUser = u;
                                break;
                            }
                        }
                        if (findUser == null) {
                            findUser = new User(rs.getLong(8) - 1, rs.getString(9),
                                    rs.getString(10), new ArrayList<>(), new ArrayList<>());
                        }
                        newChatroom = new Chatroom(rs.getLong(6), rs.getString(7), findUser, null);
                    }
                    if (newUser != null) {
                        boolean was = false;
                        for (Chatroom c : newUser.getListSocializes()) {
                            if (c.equals(newChatroom)) {
                                was = true;
                                break;
                            }
                        }
                        if (!was) {
                            newUser.getListSocializes().add(newChatroom);
                        }
                    }
                }
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

}
