package com.openclassroom.go4lunch.repository.retrofit;

import com.openclassroom.go4lunch.BuildConfig;
import com.openclassroom.go4lunch.model.api.autocomplete.AutocompleteResponse;
import com.openclassroom.go4lunch.model.api.nearbysearch.NearbySearchResponse;
import com.openclassroom.go4lunch.model.api.placedetails.PlaceDetailsResponse;

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