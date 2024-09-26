package com.example.barber;

import com.example.barber.model.Radnik;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyApi3 {
    @GET("Radnici/UlogeId/3")
    Call<List<Radnik>> getYourData();

    @POST("Radnici/VerifyUser")
    Call<Void> verifyUser(@Body Radnik radnik);
}
