package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {

    private HashMap<Integer, User> userMap = new HashMap<>();

    @Override
    public Collection<User> getUserMap() {
        return userMap.values();
    }

    @Override
    public User getUserById(int userId) {
        return userMap.get(userId);
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user){
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
    }

    @Override
    public HashMap<Integer, User> getFriends(int userId) {
        User user = getUserById(userId);
        HashMap<Integer, User> allFriends = new HashMap<>();
        Set<Integer> friendsId = user.getFriendIds();
        for (Integer id : friendsId) {
            User friend = getUserById(id);
            allFriends.put(id, friend);
        }
        return allFriends;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        HashMap<Integer, User> userFriends = getFriends(userId);
        HashMap<Integer, User> otherUserFriends = getFriends(otherId);
        List<User> commonFriends = (List<User>)
                userFriends.values().stream()
                        .filter(otherUserFriends.values()::contains).collect(Collectors.toList());
        return commonFriends;
    }
}
