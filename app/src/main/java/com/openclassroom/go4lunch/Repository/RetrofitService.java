package com.openclassroom.go4lunch.Repository;

import com.openclassroom.go4lunch.BuildConfig;
import com.openclassroom.go4lunch.Model.AutocompleteAPI.AutocompleteResponse;
import com.openclassroom.go4lunch.Model.NearbySearchAPI.NearbySearchResponse;
import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.PlaceDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("api/place/autocomplete/json?strictbounds&key=" + BuildConfig.MAPS_API_KEY)
    Call<AutocompleteResponse> getAutocomplete(@Query("input") String input, @Query("radius") int radius, @Query("location") String location, @Query("type") String type);

    @GET("api/place/nearbysearch/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<NearbySearchResponse> getNearbyByType(@Query("radius") int radius, @Query("location") String location, @Query("type") String type);

    @GET("api/place/nearbysearch/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<NearbySearchResponse> getNearbyByKeyword(@Query("radius") int radius, @Query("location") String location, @Query("keyword") String type);

    @GET("api/place/details/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceDetailsResponse> getDetails(@Query("place_id") String placeId);
}