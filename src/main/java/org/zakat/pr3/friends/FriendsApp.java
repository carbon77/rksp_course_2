package org.zakat.pr3.friends;

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class FriendsApp {
    private static final Random random = new Random();
    private static List<UserFriend> users;

    public static void main(String[] args) throws InterruptedException {
        generateUsers(1000);

        List<Integer> userIds = Stream
                .generate(() -> random.nextInt(100))
                .limit(10)
                .toList();

        Observable
                .fromStream(userIds.stream())
                .flatMap(FriendsApp::getFriends)
                .subscribe(System.out::println);
    }

    public static Observable<UserFriend> getFriends(int userId) throws InterruptedException {
        return Observable
                .fromStream(users.stream())
                .filter(user -> user.userId() == userId);
    }

    public static void generateUsers(int count) {
        users = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            var user = new UserFriend(random.nextInt(100), random.nextInt(100));
            users.add(user);
        }
    }
}
