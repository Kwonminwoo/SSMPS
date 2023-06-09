package com.example.ssmps_android.location;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ssmps_android.R;
import com.example.ssmps_android.Recyclerview.CustomAdapter3;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.CenterItem;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Location;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.dto.CenterItemResponse;
import com.example.ssmps_android.guest.GuestStoreSelectActivity;
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

public class LocationDetailActivity extends AppCompatActivity {
    EditText searchInput;
    RecyclerView recyclerView;

    Store nowStore;

    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    Retrofit retrofit;
    RetrofitAPI service;
    TokenInterceptor tokenInterceptor;

    List<Item> itemList = new ArrayList<>();
    List<Location> locationList = new ArrayList<>();
    Location nowLocation;
    String token;

    CustomAdapter3 customAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_item_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initData();
        setItemList();
        // 검색 버튼 클릭
        findViewById(R.id.locationDetail_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tttt", "here");
                searchItem();
            }
        });

        // 등록버튼 클릭
        findViewById(R.id.location_item_register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> locationItemList = customAdapter3.getLocationItemList();
                nowLocation.setItemList(locationItemList);
                for(int i = 0;i < nowStore.getLocationList().size();i++){
                    if(nowLocation.getId() == nowStore.getLocationList().get(i).getId()){
                        nowStore.getLocationList().remove(i);
                        nowStore.getLocationList().add(nowLocation);
                    }
                }
                sharedPreferenceUtil.putData("store", gson.toJson(nowStore));
                updateItemLocation(locationItemList);
                Toast.makeText(LocationDetailActivity.this, locationItemList.size() + " ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initData(){
        searchInput = findViewById(R.id.locationDetail_search_input);
        recyclerView = findViewById(R.id.locationDetail_recyclerView);
        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        gson = new GsonBuilder().create();
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);

        nowStore = gson.fromJson(sharedPreferenceUtil.getData("store", "err"), Store.class);
        locationList = nowStore.getLocationList();
        nowLocation = (Location) (getIntent().getSerializableExtra("location"));
    }

    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }

    private void setRecyclerViewData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter3 = new CustomAdapter3(itemList, nowLocation);
        recyclerView.setAdapter(customAdapter3);
    }
    private void setItemList(){
        Call<List<Item>> findAllItem = service.findAllItem(nowStore.getId());
        findAllItem.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    Log.e("find all item error", response.errorBody().toString());
                    Toast.makeText(LocationDetailActivity.this, "매장 물건 불러오기 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                itemList = response.body();
                setRecyclerViewData();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("find all item fail", t.getMessage());
                Toast.makeText(LocationDetailActivity.this, "매장 물건 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchItem(){
        String itemName = searchInput.getText().toString();
        Call<List<Item>> searchItem = service.findItemByName(itemName, nowStore.getId());
        searchItem.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    Log.e("search item error", response.errorBody().toString());
                    Toast.makeText(LocationDetailActivity.this, "물건 검색 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                itemList = response.body();
                setRecyclerViewData();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("search item fail", t.getMessage());
                Toast.makeText(LocationDetailActivity.this, "물건 검색 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateItemLocation(List<Item> locationItemList){
        Call<Location> updateLocation = service.modifyItemLocation(locationItemList, nowLocation.getId());
        updateLocation.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if(!response.isSuccessful()){
                    Log.e("update location error", response.errorBody().toString());
                    Toast.makeText(LocationDetailActivity.this, "매대 수정 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LocationDetailActivity.this, "매대 정보가 수정되었습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Log.e("update location fail", t.getMessage());
                Toast.makeText(LocationDetailActivity.this, "매대 수정 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}