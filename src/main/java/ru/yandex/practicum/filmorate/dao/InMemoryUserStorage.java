package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {

    HashMap<Integer, User> userMap = new HashMap<>();

    @Override
    public Collection<Integer> getUserMap() {
        return userMap.keySet();
    }

    @Override
    public User get(int userId){
        return userMap.get(userId);
    }

    @Override
    public User save(User user){
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
    public HashMap getFriends(int userId){
        User user = get(userId);
        HashMap<Integer, User> allFriends = new HashMap<>();
        Set<Integer> friendsId = user.getFriendIds();
        for(Integer id: friendsId){
            User friend = get(id);
            allFriends.put(id, friend);
        }
        return allFriends;
    }

    @Override
    public HashMap getMutualFriends(int userId, int otherId){
        User user = get(userId);
        User otherUser = get(otherId);
        HashMap<Integer, User> mutualFriends = new HashMap<>();
        Set<Integer> friendsIdForUser = user.getFriendIds();
        Set<Integer> friendsIdForOtherUser = otherUser.getFriendIds();
        for(Integer userFriendId: friendsIdForUser){
            for(Integer otherUserFriendId: friendsIdForOtherUser){
                if (userFriendId == otherUserFriendId){
                    mutualFriends.put(userFriendId, get(userFriendId));
                }
            }
        }
        return mutualFriends;
    }
}
