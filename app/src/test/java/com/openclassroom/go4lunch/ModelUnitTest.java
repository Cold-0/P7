package com.openclassroom.go4lunch;

import com.openclassroom.go4lunch.models.User;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ModelUnitTest {
    @Test
    public void UserCreation() {
        User user = new User(
                "x1df5e4rt6g54df587e",
                "Bobby Lemon",
                new ArrayList<>(Arrays.asList("11111111", "22222222", "3333333")),
                "https://i.pravatar.cc/300",
                "65g465g4evs615q46e84z6",
                "Restaurant de la tour",
                "bobby.lemon@email.com"
        );

        assertEquals("x1df5e4rt6g54df587e", user.getUid());
        assertEquals("Bobby Lemon", user.getDisplayName());
        assertTrue(new ArrayList<>(Arrays.asList("11111111", "22222222", "3333333")).containsAll(user.getLikeList()));
        assertEquals("https://i.pravatar.cc/300", user.getPhotoUrl());
        assertEquals("65g465g4evs615q46e84z6", user.getEatingAt());
        assertEquals("Restaurant de la tour", user.getEatingAtName());
        assertEquals("bobby.lemon@email.com", user.getEmail());
    }

    @Test
    public void UserSet() {
        User user = new User(
                "x1df5e4rt6g54df587e",
                "Bobby Lemon",
                new ArrayList<>(Arrays.asList("11111111", "22222222", "3333333")),
                "https://i.pravatar.cc/300",
                "65g465g4evs615q46e84z6",
                "Restaurant de la tour",
                "bobby.lemon@email.com"
        );

        user.setUid("7a8z9e7r8t9e8r7");
        user.setDisplayName("Maya Origer");
        user.setLikeList(new ArrayList<>(Arrays.asList("55555", "66666", "77777")));
        user.setAvatarUrl("https://i.pravatar.cc/500");
        user.setEatingAt("azer7894qsdf456wxcv123");
        user.setEatingAtName("La cloche Dorée");
        user.setEmailAddress("maya.origer@email.com");

        assertEquals("7a8z9e7r8t9e8r7", user.getUid());
        assertEquals("Maya Origer", user.getDisplayName());
        assertTrue(new ArrayList<>(Arrays.asList("55555", "66666", "77777")).containsAll(user.getLikeList()));
        assertEquals("https://i.pravatar.cc/500", user.getPhotoUrl());
        assertEquals("azer7894qsdf456wxcv123", user.getEatingAt());
        assertEquals("La cloche Dorée", user.getEatingAtName());
        assertEquals("maya.origer@email.com", user.getEmail());
    }
}