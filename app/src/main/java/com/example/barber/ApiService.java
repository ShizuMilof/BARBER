package com.example.barber;

import com.example.barber.model.Appointment;
import com.example.barber.model.HairdresserStatistics;
import com.example.barber.model.Radnik;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/registracija")
    Call<Registracija> createRegistracija(@Body Registracija registracija);

    // Ovdje možeš dodati dodatne metode za komunikaciju s API-jem

    @POST("api/Termini")
    Call<Termini> createTermini(@Body Termini termini);


    @GET("api/Termini")
    Call<List<Appointment>> getAppointments();

    @POST("api/Termini")
    Call<ResponseBody> bookAppointment(@Body Appointment appointment);


    @GET("api/Termini/ByUser/{username}")
    Call<List<Appointment>> getAppointmentsByUser(@Path("username") String username);

    @POST("api/Termini/ApproveAppointment")
    Call<Void> approveAppointment(@Body int appointmentId);



    @GET("api/Termini/AllByFrizer/{frizerIme}")
    Call<List<Appointment>> GetAllAppointmentsByFrizer(@Path("frizerIme") String frizerIme);

    @GET("api/Termini/Statistics")
    Call<List<HairdresserStatistics>> getHairdresserStatistics();

    @GET("api/Termini/Statistics")
    Call<List<HairdresserStatistics>> getKorisnici();

    @GET("getRadniciByMonth")
    Call<List<Radnik>> getRadniciByMonth(@Query("month") int month, @Query("year") int year);

    @GET("api/Radnici/UlogeId/3")
    Call<List<Radnik>> getRadniciWithUlogeId3(); //statitsitka

    @GET("api/Radnici/UlogeId/1")
    Call<List<Radnik>> getRadniciWithUlogeId1(); //statitsitka

    @GET("api/Termini/ByFrizer/{frizerIme}")
    Call<List<Appointment>> getAppointmentsByFrizer(@Path("frizerIme") String frizerIme);

    @GET("api/Termini/PendingByFrizer/{frizerIme}")
    Call<List<Appointment>> getPendingAppointmentsByFrizer(@Path("frizerIme") String frizerIme);



    @POST("api/Termini/CancelAppointment")
    Call<Void> cancelAppointment(@Body int appointmentId);

    @GET("api/Termini")
    Call<List<Appointment>> getAllAppointments();


}
