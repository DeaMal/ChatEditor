package chat.repositories;

import chat.model.Chatroom;
import chat.model.Message;
import chat.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {

    private final DataSource data;

    public MessagesRepositoryJdbcImpl(DataSource dataSource) {
        data = dataSource;
    }

    @Override
    public Optional<Message> findById(Long id) {
        try {
            Connection connection = data.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chat.message WHERE id = " + id);
            Optional<Message> result = Optional.empty();
            if (resultSet.next()) {
                LocalDateTime ldt;
                try {
                    ldt = resultSet.getTimestamp(5).toLocalDateTime();
                } catch (NullPointerException e) {
                    ldt = null;
                }
                result = Optional.of(new Message(resultSet.getLong(1),
                        findUserById(resultSet.getLong(2)),
                        findChatroomById(resultSet.getLong(3)),
                        resultSet.getString(4),
                        ldt));
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Message message) {
        Long userId = findUserByName(message.getMessageAuthor().getLogin());
        Long chatroomId = findRoomByName(message.getMessageRoom().getChatroomName());
        if (userId != null && chatroomId != null) {
            String textMessage = message.getMessageText();
            String time = String.valueOf(Timestamp.valueOf(message.getDate()));
            Long messageId = makeQuery("insert into chat.message (author, room, \"Text\", \"Date\")  values (" +
                    1 + ", " + 1 + ", '" + textMessage + "', '" + time + "') RETURNING id");
            message.setMessageId(messageId);
        } else {
            throw new NotSavedSubEntityException("User or Chatroom not found!");
        }
    }

    @Override
    public void update(Message message) {
        Long id = message.getId();
        User author = message.getMessageAuthor();
        Chatroom room = message.getMessageRoom();
        String textMessage = message.getMessageText();
        LocalDateTime time = message.getDate();
        boolean comma = false;
        Optional<Message> optionalMessage = findById(id);
        if (optionalMessage.isPresent() && !message.equals(optionalMessage.get())) {
            Message oldMessage = optionalMessage.get();
            String updateString = "UPDATE chat.message SET ";
            if (author == null ) {
                throw new NotUpdateSubEntityException("Author can't be null!");
            } else if (!oldMessage.getMessageAuthor().equals(author)) {
                if (findUserByName(author.getLogin()) != null) {
                    updateString += "author = " + author.getUserId();
                    comma = true;
                } else {
                    throw new NotUpdateSubEntityException("New author not found in base!");
                }
            }
            if (room == null) {
                throw new NotUpdateSubEntityException("Chatroom can't be null!");
            } else if (!oldMessage.getMessageRoom().equals(room)) {
                if (findRoomByName(room.getChatroomName()) != null) {
                    updateString += (comma?", ":"") + "room = " + room.getChatroomId();
                    comma = true;
                } else {
                    throw new NotUpdateSubEntityException("New Chatroom not found in base!");
                }
            }
            if (textMessage == null) {
                updateString += (comma?", ":"") + "\"Text\" = null";
                comma = true;
            } else if (!oldMessage.getMessageText().equals(textMessage)) {
                updateString += (comma?", ":"") + "\"Text\" = '" + textMessage + "'";
                comma = true;
            }
            if (time == null) {
                updateString += (comma?", ":"") + "\"Date\" = null";
                comma = true;
            } else if (!oldMessage.getDate().equals(time)) {
                updateString += (comma?", ":"") + "\"Date\" = '" + Timestamp.valueOf(time) + "'";
                comma = true;
            }
            if (comma) {
                updateString += " WHERE id = ?";
                try {
                    Connection connection = data.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(updateString);
                    preparedStatement.setLong(1, id);
                    preparedStatement.executeUpdate();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new NotUpdateSubEntityException("No changes!");
            }
        } else {
            throw new NotUpdateSubEntityException("Message Id not found in base or no changes!");
        }
    }

    public User findUserById(Long id) {
        try {
            Connection connection = data.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chat.user WHERE id = " + id);
            User result = null;
            if (resultSet.next()) {
                result = new User(id, resultSet.getString(2),
                        resultSet.getString(3), null, null);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Chatroom findChatroomById(Long id) {
        try {
            Connection connection = data.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chat.chatroom WHERE id = " + id);
            Chatroom result = null;
            if (resultSet.next()) {
                result = new Chatroom(id, resultSet.getString(2),
                        new User(resultSet.getLong(3),
                                findUserById(resultSet.getLong(3)).getLogin(),
                                findUserById(resultSet.getLong(3)).getPassword(), null, null), null);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getMaxId(String table) {
        try {
            Connection connection = data.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM chat." + table);
            Long result = null;
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long findUserByName(String name) {
        try {
            Connection connection = data.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM chat.user WHERE login LIKE '" + name + "'");
            Long result = null;
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long findRoomByName(String name) {
        try {
            Connection connection = data.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM chat.chatroom WHERE name LIKE '" + name + "'");
            Long result = null;
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long makeQuery(String str) {
        try {
            Connection connection = data.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(str);
            Long result = null;
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
