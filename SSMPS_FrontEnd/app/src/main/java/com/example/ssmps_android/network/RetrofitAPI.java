package com.example.ssmps_android.network;

import com.example.ssmps_android.domain.CenterItem;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Location;
import com.example.ssmps_android.domain.Manager;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.dto.CenterItemResponse;
import com.example.ssmps_android.dto.LocationRequest;
import com.example.ssmps_android.dto.LoginRequest;
import com.example.ssmps_android.dto.LoginResponse;
import com.example.ssmps_android.dto.RegistItemRequest;
import com.example.ssmps_android.dto.StoreRequest;
import com.example.ssmps_android.dto.StoreResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @POST("api/join")
    Call<Manager> join(@Body Manager manager);

    @GET("api/manager/login")
    Call<LoginResponse> login(@Query("id") String id, @Query("password") String password);

    @POST("api/manager/test")
    Call<String> testPosting();

    @GET("api/loginFirst")
    Call<LoginResponse> loginFirst(@Query("id") String id, @Query("password") String password);

    @GET("api/storeList/{id}")
    Call<List<StoreResponse>> findStoreList(@Path("id") Long id);

    @GET("api/centerItemList")
    Call<List<CenterItemResponse>> findAllCenterItem();

    @GET("api/centerItem/{name}")
    Call<List<CenterItemResponse>> findCenterItemByName(@Path("name") String name);

    @POST("api/item")
    Call<Item> registItem(@Query("store") Long storeId, @Query("item") Long itemId);

    @GET("api/itemList")
    Call<List<Item>> findAllItem(@Query("storeId") Long storeId);

    @GET("api/storeList")
    Call<List<Store>> findAllStore();

    @GET("api/store/location/{id}")
    Call<List<Location>> findStoreLocation(@Path("id") Long storeId);

    @POST("api/store/location")
    Call<List<Location>> registStoreLocation(@Body Store store);
}