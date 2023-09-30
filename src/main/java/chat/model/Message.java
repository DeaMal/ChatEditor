package chat.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private Long messageId;
    private User messageAuthor;
    private Chatroom messageRoom;
    private String messageText;
    private LocalDateTime date;

    public Message(Long id, User user, Chatroom chatroom, String message, LocalDateTime time) {
        messageId = id;
        messageAuthor = user;
        messageRoom = chatroom;
        messageText = message;
        date = time;
    }

    public Long getId() {
        return messageId;
    }

    public User getMessageAuthor() {
        return messageAuthor;
    }

    public Chatroom getMessageRoom() {
        return messageRoom;
    }

    public String getMessageText() {
        return messageText;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setMessageId(Long id) {
        messageId = id;
    }

    public void setMessageAuthor(User user) {
        messageAuthor = user;
    }

    public void setMessageRoom(Chatroom chatroom) {
        messageRoom = chatroom;
    }

    public void setText(String message) {
        messageText = message;
    }

    public void setDate(LocalDateTime time) {
        date = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (hashCode() != obj.hashCode()) return false;
        if (getClass() != obj.getClass()) return false;
        Message that = (Message) obj;
        if (messageId != null && !messageId.equals(that.messageId)) return false;
        if (messageAuthor != null && !messageAuthor.equals(that.messageAuthor)) return false;
        if (messageRoom != null && !messageRoom.equals(that.messageRoom)) return false;
        if (messageText != null && !messageText.equals(that.messageText)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, messageAuthor, messageRoom, messageText, date);
    }

    @Override
    public String toString() {
        return "{\n  id:" + messageId + ",\n  author:" + messageAuthor + ",\n  chatroom:" + messageRoom +
                ",\n  text:'" + messageText + "',\n  time:" + date + "\n}\n";
    }
}
