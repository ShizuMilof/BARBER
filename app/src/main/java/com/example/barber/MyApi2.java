package com.example.barber;

import com.example.barber.model.Usluge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface MyApi2 {
    @GET("Usluge")
    Call<List<Usluge>> getYourData();


}