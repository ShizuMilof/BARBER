package com.example.barber;

import com.example.barber.model.Radnik;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface MyApi {
    @GET("Radnici/UlogeId/1")
    Call<List<Radnik>> getYourData();


}