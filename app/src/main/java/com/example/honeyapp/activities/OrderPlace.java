package com.example.honeyapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.honeyapp.R;
import com.example.honeyapp.adapters.OrderPlaceAdapter;
import com.example.honeyapp.adapters.RecyclerViewAdapter;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.UserCartEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class OrderPlace extends AppCompatActivity{


    private List<UserCartEntity> userCart;
    RecyclerView recyclerView;

    public static TextView userCartSumm;
    public static Spinner spinner;



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

        // магазины
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i)+" магазин");
        }


        spinner = findViewById(R.id.stores);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        OrderPlaceAdapter myAdapter = new OrderPlaceAdapter(this, userCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

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
