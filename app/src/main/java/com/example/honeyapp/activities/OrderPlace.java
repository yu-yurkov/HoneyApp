package com.example.honeyapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.honeyapp.R;
import com.example.honeyapp.adapters.OrderPlaceAdapter;
import com.example.honeyapp.adapters.RecyclerViewAdapter;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.UserCartEntity;

import java.util.List;
import java.util.TreeMap;


public class OrderPlace extends AppCompatActivity{


    private List<UserCartEntity> userCart;
    RecyclerView recyclerView;


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


        TextView cartText = findViewById(R.id.user_cart_order_place);
        cartText.setText("Товаров в корзине: " + userCart.size());

        recyclerView = findViewById(R.id.cart_recyclerView);

        OrderPlaceAdapter myAdapter = new OrderPlaceAdapter(this, userCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);


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
