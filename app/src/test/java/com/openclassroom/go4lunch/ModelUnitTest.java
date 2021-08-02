package com.openclassroom.go4lunch;

import com.openclassroom.go4lunch.models.User;
import com.openclassroom.go4lunch.models.api.autocomplete.MatchedSubstring;
import com.openclassroom.go4lunch.models.api.autocomplete.Prediction;
import com.openclassroom.go4lunch.models.api.autocomplete.StructuredFormatting;
import com.openclassroom.go4lunch.models.api.autocomplete.Term;
import com.openclassroom.go4lunch.models.api.nearbysearch.Geometry;
import com.openclassroom.go4lunch.models.api.nearbysearch.NearbySearchResult;
import com.openclassroom.go4lunch.models.api.nearbysearch.OpeningHours;
import com.openclassroom.go4lunch.models.api.nearbysearch.Photo;
import com.openclassroom.go4lunch.models.api.nearbysearch.PlusCode;
import com.openclassroom.go4lunch.models.api.placedetails.PlaceDetailsResult;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import android.graphics.Path;

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

    @Test
    public void PredictionCreationAndUpdate() {
        List<Term> termList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        List<MatchedSubstring> matchedSubstringsList = new ArrayList<>();
        StructuredFormatting structuredFormatting = new StructuredFormatting();

        Prediction prediction = new Prediction();
        prediction.setDescription("Une Description");
        prediction.setMatchedSubstrings(matchedSubstringsList);
        prediction.setPlaceId("2f532132s1f10d2d22f22f11r2");
        prediction.setReference("000050222222");
        prediction.setTerms(termList);
        prediction.setStructuredFormatting(structuredFormatting);
        prediction.setTypes(typeList);

        assertEquals("Une Description", prediction.getDescription());
        assertEquals(matchedSubstringsList, prediction.getMatchedSubstrings());
        assertEquals("2f532132s1f10d2d22f22f11r2", prediction.getPlaceId());
        assertEquals("000050222222", prediction.getReference());
        assertEquals(termList, prediction.getTerms());
        assertEquals(structuredFormatting, prediction.getStructuredFormatting());
        assertEquals(typeList, prediction.getTypes());
    }

    @Test
    public void NearbySearchResultCreationAndUpdate() {
        Geometry geometry = new Geometry();
        OpeningHours openingHours = new OpeningHours();
        List<Photo> photoList = new ArrayList<>();
        PlusCode plusCode = new PlusCode();
        List<String> typeList = new ArrayList<>();

        NearbySearchResult prediction = new NearbySearchResult();
        prediction.setBusinessStatus("Business a lot a lot");
        prediction.setGeometry(geometry);
        prediction.setIcon("Icon a lot 5050");
        prediction.setName("Hello5000");
        prediction.setOpeningHours(openingHours);
        prediction.setPhotos(photoList);
        prediction.setPlaceId("ed5g4z6gf44hg5h5");
        prediction.setPlusCode(plusCode);
        prediction.setPriceLevel(3.3545611123);
        prediction.setRating(1.2365887);
        prediction.setReference("544515132354");
        prediction.setScope("thisIsAScope");
        prediction.setTypes(typeList);
        prediction.setUserRatingTotal(135435132121L);
        prediction.setVicinity("Rue des chats");

        assertEquals("Business a lot a lot", prediction.getBusinessStatus());
        assertEquals(geometry, prediction.getGeometry());
        assertEquals("Icon a lot 5050", prediction.getIcon());
        assertEquals("Hello5000", prediction.getName());
        assertEquals(openingHours, prediction.getOpeningHours());
        assertEquals(photoList, prediction.getPhotos());
        assertEquals("ed5g4z6gf44hg5h5", prediction.getPlaceId());
        assertEquals(plusCode, prediction.getPlusCode());
        assertEquals((Double) 3.3545611123, prediction.getPriceLevel());
        assertEquals((Double) 1.2365887, prediction.getRating());
        assertEquals("544515132354", prediction.getReference());
        assertEquals("thisIsAScope", prediction.getScope());
        assertEquals(typeList, prediction.getTypes());
        assertEquals((Long) 135435132121L, prediction.getUserRatingTotal());
        assertEquals("Rue des chats", prediction.getVicinity());
    }

    @Test
    public void PlaceDetailResultCreationAndUpdate() {
        com.openclassroom.go4lunch.models.api.placedetails.Geometry geometry = new com.openclassroom.go4lunch.models.api.placedetails.Geometry();
        com.openclassroom.go4lunch.models.api.placedetails.OpeningHours openingHours = new com.openclassroom.go4lunch.models.api.placedetails.OpeningHours();
        List<com.openclassroom.go4lunch.models.api.placedetails.Photo> photoList = new ArrayList<>();

        PlaceDetailsResult prediction = new PlaceDetailsResult();
        prediction.setGeometry(geometry);
        prediction.setName("Hello5000");
        prediction.setFormattedAddress("Rue du chat");
        prediction.setFormattedPhoneNumber("+33630211100");
        prediction.setOpeningHours(openingHours);
        prediction.setPhotos(photoList);
        prediction.setPlaceId("ed5g4z6gf44hg5h5");
        prediction.setRating(1.2365887);
        prediction.setReference("544515132354");
        prediction.setVicinity("Rue des chats");
        prediction.setWebsite("https://sitetropbien.fun/");

        assertEquals(geometry, prediction.getGeometry());
        assertEquals("Hello5000", prediction.getName());
        assertEquals("Rue du chat", prediction.getFormattedAddress());
        assertEquals("+33630211100", prediction.getFormattedPhoneNumber());
        assertEquals(openingHours, prediction.getOpeningHours());
        assertEquals(photoList, prediction.getPhotos());
        assertEquals("ed5g4z6gf44hg5h5", prediction.getPlaceId());
        assertEquals((Double) 1.2365887, prediction.getRating());
        assertEquals("544515132354", prediction.getReference());
        assertEquals("Rue des chats", prediction.getVicinity());
        assertEquals("https://sitetropbien.fun/", prediction.getWebsite());
    }
}