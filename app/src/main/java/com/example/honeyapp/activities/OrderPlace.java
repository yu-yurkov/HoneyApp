package com.example.honeyapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.honeyapp.R;
import com.example.honeyapp.adapters.OrderPlaceAdapter;
import com.example.honeyapp.dao.StoreDao;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.StoreEntity;
import com.example.honeyapp.entities.UserCartEntity;

import com.example.honeyapp.model.Products;
import com.example.honeyapp.model.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class OrderPlace extends AppCompatActivity{


    private List<UserCartEntity> userCart;
    RecyclerView recyclerView;

    public static TextView userCartSumm;
    public static Spinner spinner;

    private TreeMap<String, String> mRequestParams;
    private final String JSON_URL = "https://agents.sbonus.ru/json/add_store.php";
    private RequestQueue requestQueue;
    private JsonArrayRequest request;

    private String mResponse;
    private List<Store> shopsList = new ArrayList<>();
    private List<StoreEntity> storeList = new ArrayList<>();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TreeMap<Integer, Integer> userCartMap = new TreeMap<Integer, Integer>();

        AppDatabase db = App.getInstance().getDatabase();
        UserCartDao userCartDao = db.userCartDao();

        userCart = userCartDao.getAll();

        if(userCart.size() == 0){
            setContentView(R.layout.user_cart_empty);
            return;
        }

        setContentView(R.layout.user_cart_order_place);


        recyclerView = findViewById(R.id.cart_recyclerView);

        // сумма покупок
        userCartSumm = findViewById(R.id.user_cart_summ);
        cartRefresh(userCart);

        ///
        ///
        StringRequest postRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Store[] store = gson.fromJson(response, Store[].class);

                for (int i = 0; i < store.length; i++) {

                    Store mStore = new Store();

                    mStore.setId(store[i].getId());
                    mStore.setName(store[i].getName());
                    mStore.setAddress(store[i].getAddress());

                    // записываем в таблицу
                    storeDataInsert(mStore);
                }
            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                //return mRequestParams;

                Map<String, String>  params = new HashMap<String, String>();
                params.put("act", "getShops");
                params.put("token", "protected");
                params.put("user_id", "15");

                return params;

            }
        };

        requestQueue = Volley.newRequestQueue(OrderPlace.this);
        requestQueue.add(postRequest);

        //получаем список
        StoreDao storeDao = db.storeDao();

        this.storeList = storeDao.getAll();

        // магазины
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < storeList.size(); i++) {
            list.add(storeList.get(i).address + " ("+storeList.get(i).name + ")");
        }

        spinner = findViewById(R.id.stores);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        OrderPlaceAdapter myAdapter = new OrderPlaceAdapter(this, userCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country=   spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });



    }


    // сохраняем адреса магазинов
    public void storeDataInsert(Store store){

        AppDatabase db = App.getInstance().getDatabase();
        StoreDao storeDao = db.storeDao();

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.id = store.getId();
        storeEntity.name = store.getName();
        storeEntity.address = store.getAddress();

        storeDao.insert(storeEntity);
    }





    public void orderSend(View v){
        Log.i("TAG", "Выбрана позиция: " +spinner.getSelectedItemId());
    }

    // пересчитываем товары в корзине
    public static void cartRefresh(List<UserCartEntity> userCart){

        float summ = 0;

        for (int i = 0; i < userCart.size(); i++) {
            summ += userCart.get(i).quantity * Float.parseFloat(userCart.get(i).products.getPrice());
        }

        userCartSumm.setText(String.valueOf(summ));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
