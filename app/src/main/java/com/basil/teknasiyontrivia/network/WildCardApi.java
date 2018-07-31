package com.basil.teknasiyontrivia.network;

import com.basil.teknasiyontrivia.model.WildCardResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface WildCardApi {
    String EndPoint = "http://localhost:9191/";
    @GET("api/fetchWildCard")
    Observable<WildCardResponse> getWildCardCount();
}
