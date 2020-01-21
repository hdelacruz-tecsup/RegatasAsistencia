package com.hdelacruz.regatasasistencia.services;

import com.hdelacruz.regatasasistencia.models.Entrada;
import com.hdelacruz.regatasasistencia.models.Salida;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServiceSalida {

    String API_BASE_URL = "http://192.168.43.38:8080";

    @GET("/entrada")
    Call<List<Entrada>> findAll();

    @FormUrlEncoded
    @POST("/salida")
    Call<Salida> createSalida(@Field("codigo_s") String codigo_s,
                                @Field("dni_s") String dni_s,
                                @Field("fecha_s") String fecha_s,
                                @Field("hora_s") String hora_s,
                                @Field("razon_s") String razon_s);

    @Multipart
    @POST("/salida")
    Call<Salida> createSalida(@Part("codigo_s") RequestBody codigo_s,
                                @Part("dni_s")RequestBody dni_s,
                                @Part("fecha_s") RequestBody fecha_s,
                                @Part("hora_s") RequestBody hora_s,
                                @Part("razon_s") RequestBody razon_s,
                                @Part MultipartBody.Part imagen);
}
