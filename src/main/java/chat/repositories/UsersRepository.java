package chat.repositories;

import chat.model.User;

import java.util.List;

public interface UsersRepository {
    List<User> findAll(int page, int size);
}
