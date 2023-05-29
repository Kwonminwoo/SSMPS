package com.example.ssmps_android.guest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuestStoreSelectActivity extends AppCompatActivity {
    SharedPreferenceUtil sharedPreferenceUtil;
    TokenInterceptor tokenInterceptor;

    Retrofit retrofit;
    RetrofitAPI service;
    Gson gson;
    List<Store> storeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_select);
        initData();
        setStoreList();

        findViewById(R.id.storeSelect_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);
        gson = new GsonBuilder().create();
    }
    private void setRecyclerviewData(){
        // 리사이클러뷰 데이터 추가
    }
    private void setStoreList(){
        Call<List<Store>> findAllStore = service.findAllStore();
        findAllStore.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if(!response.isSuccessful()){
                    Log.e("find store list error", response.errorBody().toString());
                    Toast.makeText(GuestStoreSelectActivity.this, "매장 불러오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                storeList = response.body();
                Log.e("store size", storeList.size() + "");
                setRecyclerviewData();
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.e("find store list fail", t.getMessage());
                Toast.makeText(GuestStoreSelectActivity.this, "매장 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
}