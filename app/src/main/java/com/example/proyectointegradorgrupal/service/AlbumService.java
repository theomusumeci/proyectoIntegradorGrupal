package com.example.proyectointegradorgrupal.service;

import com.example.proyectointegradorgrupal.model.Album;
import com.example.proyectointegradorgrupal.model.AlbumContainer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlbumService {

    //Metodo para consultar 1 solo album por ID
    @GET("album/{id}")
    Call<Album> getAlbumById(@Path("id") String id);


    //Consultar un search dentro de albums
    @GET("search/album")
    Call<AlbumContainer> getAlbumPorSearch(@Query("q") String album);

}
