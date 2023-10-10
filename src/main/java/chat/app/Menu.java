package chat.app;

import chat.model.Chatroom;
import chat.model.Message;
import chat.model.User;
import chat.repositories.MessagesRepositoryJdbcImpl;
import chat.repositories.NotSavedSubEntityException;
import chat.repositories.NotUpdateSubEntityException;
import chat.repositories.UsersRepositoryJdbcImpl;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private final MessagesRepositoryJdbcImpl messagesRepository;
    private final UsersRepositoryJdbcImpl usersRepository;

    private final String split = "---------------------------------------------------------";

    public Menu(DataSource dataSource) {
        messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);
        usersRepository = new UsersRepositoryJdbcImpl(dataSource);
    }

    public void menuItem() {
        long item;
        int end = 0;
        Scanner in = new Scanner(System.in);
        while (end == 0) {
            System.out.println("1. View a message by Id");
            System.out.println("2. Add new message");
            System.out.println("3. Update message");
            System.out.println("4. View list Users");
            System.out.println("5. View count of Users");
            System.out.println("6. View count of Chatroom's");
            System.out.println("7. View count of Messages");
            System.out.println("8. Finish execution");
            System.out.print("-> ");
            item = userIn(in);
            if (item == 1) {
                menuViewMessage();
            } else if (item == 2) {
                menuAddNewMessage();
            } else if (item == 3) {
                menuUpdateMessage();
            } else if (item == 4) {
                menuViewUserList();
            } else if (item == 5) {
                System.out.printf("%s\nUsers ID between 1 and %d\n", split, messagesRepository.getMaxId("User"));
            } else if (item == 6) {
                System.out.printf("%s\nChatroom's ID between 1 and %d\n", split, messagesRepository.getMaxId("Chatroom"));
            } else if (item == 7) {
                System.out.printf("%s\nMessage ID between 1 and %d\n", split, messagesRepository.getMaxId("Message"));
            } else if (item == 8) {
                end = 1;
            }
            if (in.hasNextLine()) {
                in.nextLine();
            }
            System.out.println(split);
        }
        in.close();
    }

    public static long userIn(Scanner in) {
        long result = 0;
        if (in.hasNext() && in.hasNextInt()) {
            result = in.nextInt();
            if ((result < 1) || (result > 8)) {
                result = 0;
            }
        }
        return result;
    }

    public void menuViewMessage() {
        System.out.println("Enter a message ID");
        System.out.print("-> ");
        Scanner in = new Scanner(System.in);
        if (in.hasNext() && in.hasNextInt()) {
            System.out.println(split);
            printMessage(in.nextInt());
        } else {
            System.out.println("Incorrect symbol");
        }
    }

    public void menuAddNewMessage() {
        long userId, chatroomId;
        String messageText;
        System.out.println("Enter a User ID");
        System.out.print("-> ");
        Scanner in = new Scanner(System.in);
        if (in.hasNext() && in.hasNextInt()) {
            userId = in.nextInt();
            System.out.println("Enter a Chatroom ID");
            System.out.print("-> ");
            if (in.hasNext() && in.hasNextInt()) {
                chatroomId = in.nextInt();
                System.out.println("Enter a Message Text");
                System.out.print("-> ");
                if (in.hasNextLine()) {
                    messageText = in.next();
                    if (!messageText.equals("")) {
                        System.out.println(split);
                        saveNewMessage(userId, chatroomId, messageText);
                    } else {
                        System.out.println("Message is empty");
                    }
                } else {
                    System.out.println("Incorrect symbol");
                }
            } else {
                System.out.println("Incorrect symbol");
            }
        } else {
            System.out.println("Incorrect symbol");
        }
    }

    public void menuUpdateMessage() {
        long messageId, userId, chatroomId;
        String temp;
        User user;
        Chatroom chatroom;
        boolean err = false;
        System.out.println("Enter a message ID");
        System.out.print("-> ");
        Scanner in = new Scanner(System.in);
        if (in.hasNext() && in.hasNextInt()) {
            messageId = in.nextInt();
            in.nextLine();
            Optional<Message> messageOptional = messagesRepository.findById(messageId);
            if (messageOptional.isPresent()) {
                Message message = messageOptional.get();
                printMessage(messageId);
                System.out.println("Enter a new author ID");
                System.out.print("-> ");
                temp = in.nextLine();
                if (temp.isEmpty()) {
                    userId = message.getMessageAuthor().getUserId();
                } else {
                    try {
                        userId = Integer.parseInt(temp);
                    } catch (NumberFormatException e) {
                        userId = 0;
                    }
                }
                user = messagesRepository.findUserById(userId);
                if (user != null) {
                    message.setMessageAuthor(user);
                    System.out.println("Enter a new chatroom ID");
                    System.out.print("-> ");
                    temp = in.nextLine();
                    if (temp.isEmpty()) {
                        chatroomId = message.getMessageRoom().getChatroomId();
                    } else {
                        try {
                            chatroomId = Integer.parseInt(temp);
                        } catch (NumberFormatException e) {
                            chatroomId = 0;
                        }
                    }
                    chatroom = messagesRepository.findChatroomById(chatroomId);
                    if (chatroom != null) {
                        message.setMessageRoom(chatroom);
                        System.out.println("Enter a new text message");
                        System.out.print("-> ");
                        temp = in.nextLine();
                        message.setText(temp);
                        System.out.println("Enter a new time (yyyy-MM-dd HH:mm:ss)");
                        System.out.print("-> ");
                        temp = in.nextLine();
                        if (temp.isEmpty()) {
                            message.setDate(null);
                        } else {
                            try {
                                message.setDate(LocalDateTime.parse(temp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                            } catch (DateTimeParseException e) {
                                System.out.println("Wrong time format");
                                err = true;
                            }
                        }
                        if (!err) {
                            try {
                                messagesRepository.update(message);
                                System.out.printf("Message ID = %d success update\n", messageId);
                            } catch (NotUpdateSubEntityException e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("Chatroom not found!");
                    }
                } else {
                    System.out.println("User not found!");
                }
            } else {
                System.out.printf("%s\nMessage with ID %d not found\n", split, messageId);
            }
        } else {
            System.out.println("Incorrect symbol");
        }
    }

    public void menuViewUserList() {
        int page, size;
        System.out.println("Enter a count of Users on page");
        System.out.print("-> ");
        Scanner in = new Scanner(System.in);
        if (in.hasNext() && in.hasNextInt()) {
            size = in.nextInt();
            if (size > 0) {
                System.out.println("Enter a number of page");
                System.out.print("-> ");
                if (in.hasNext() && in.hasNextInt()) {
                    page = in.nextInt();
                    if (page >= 0) {
                        System.out.println(split);
                        printListUsers(page, size);
                    } else {
                        System.out.println("The value must be positive");
                    }
                } else {
                    System.out.println("Incorrect symbol");
                }
            } else {
                System.out.println("The value must be greater than 0");
            }
        } else {
            System.out.println("Incorrect symbol");
        }
    }

    public void printMessage(long messageId) {
        Optional<Message> message = messagesRepository.findById(messageId);
        if (message.isPresent()) {
            System.out.println("Message : " + message.get());
        } else {
            System.out.printf("Message with ID %d not found\n", messageId);
            System.out.printf("Message ID between 1 and %d\n", messagesRepository.getMaxId("Message"));
        }
    }

    public void saveNewMessage(long userId, long chatroomId, String text) {
        User user = messagesRepository.findUserById(userId);
        Chatroom chatroom = messagesRepository.findChatroomById(chatroomId);
        if (user != null && chatroom != null) {
            Message message = new Message(null, messagesRepository.findUserById(userId),
                    messagesRepository.findChatroomById(chatroomId), text, LocalDateTime.now());
            try {
                messagesRepository.save(message);
                System.out.println("Message success save with ID " + message.getId());
            } catch (NotSavedSubEntityException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("User or Chatroom not found!");
        }
    }

    public void printListUsers(int page, int size) {
        List<User> arrayList = usersRepository.findAll(page, size);
        for (User u : arrayList) {
            System.out.println("User : {\n  id=" + u.getUserId() + ",\n  login='" + u.getLogin() +
                    "',\n  password='" + u.getPassword() + "',");
            if (!u.getListCreatedRooms().isEmpty()) {
                System.out.println("  Created Chatroom's : {");
                for (Chatroom c : u.getListCreatedRooms()) {
                    System.out.println("    " + c);
                }
                System.out.println("  },");
            } else {
                System.out.println("  Created Chatroom's : {},");
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
    }
}
