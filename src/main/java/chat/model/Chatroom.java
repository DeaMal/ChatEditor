package chat.model;

import java.util.ArrayList;
import java.util.Objects;

public class Chatroom {
    private Long chatroomId;
    private String chatroomName;
    private User chatroomOwner;
    private ArrayList<Message> listMessagesChatroom;

    public Chatroom(Long id, String name, User owner, ArrayList<Message> listMessages) {
        chatroomId = id;
        chatroomName = name;
        chatroomOwner = owner;
        listMessagesChatroom = listMessages;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public User getChatroomOwner() {
        return chatroomOwner;
    }

    public ArrayList<Message> getListMessagesChatroom() {
        return listMessagesChatroom;
    }

    public void setChatroomId(long id) {
        chatroomId = id;
    }

    public void setChatroomName(String name) {
        chatroomName = name;
    }

    public void setChatroomOwner(User owner) {
        chatroomOwner = owner;
    }

    public void setListMessagesChatroom(ArrayList<Message> messageList) {
        listMessagesChatroom = messageList;
    }

    @Override
    public boolean equals(Object obj) {
        if (hashCode() != obj.hashCode()) return false;
        if (getClass() != obj.getClass()) return false;
        Chatroom that = (Chatroom) obj;
        if (chatroomId != null && !chatroomId.equals(that.chatroomId)) return false;
        if (chatroomName != null && !chatroomName.equals(that.chatroomName)) return false;
        if (chatroomOwner != null && !chatroomOwner.equals(that.chatroomOwner)) return false;
        return listMessagesChatroom == that.listMessagesChatroom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatroomId, chatroomName, chatroomOwner);
    }

    @Override
    public String toString() {
        return "{id:" + chatroomId + ", name:'" + chatroomName + "', " +
                "owner:{id:" + chatroomOwner.getUserId() + ", login:'" + chatroomOwner.getLogin() +
                "'}, messages:" + listMessagesChatroom + "}";
    }
}