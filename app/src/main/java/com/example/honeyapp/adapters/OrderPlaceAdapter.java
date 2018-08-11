package com.example.honeyapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.honeyapp.R;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.dao.UserCartDao_Impl;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.UserCartEntity;
import com.example.honeyapp.model.UserCart;

import java.util.List;

import static com.example.honeyapp.activities.OrderPlace.cartRefresh;


public class OrderPlaceAdapter extends RecyclerView.Adapter<OrderPlaceAdapter.MyViewHoder> {

    Context mContext;
    List<UserCartEntity> userCart;
    RequestOptions option;

    public OrderPlaceAdapter(Context mContext, List<UserCartEntity> userCart) {
        this.mContext = mContext;
        this.userCart = userCart;

        // Options for Glide
        this.option = new RequestOptions().centerCrop().placeholder(R.drawable.add_cart_bg).error(R.drawable.add_cart_bg);
    }

    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.user_cart_item, parent,false);


        final OrderPlaceAdapter.MyViewHoder myViewHoder = new OrderPlaceAdapter.MyViewHoder(view);

        return myViewHoder;
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {

        holder.tv_name.setText(userCart.get(position).products.getName());
        holder.tv_price.setText(userCart.get(position).products.getPrice());
        holder.tv_quantity.setText(String.valueOf(userCart.get(position).quantity));

        // загрузка изображения
        Glide.with(mContext).load(userCart.get(position).products.getPhoto()).apply(option).into(holder.img_photo);

    }

    @Override
    public int getItemCount() {
        return userCart.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        TextView tv_price;
        TextView tv_quantity;
        ImageView img_photo;
        Button plusBtn;
        Button minusBtn;

        public AppDatabase db;
        public UserCartDao userCartDao;
        public UserCartEntity userCartEntity;



        public MyViewHoder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.name);
            tv_price = itemView.findViewById(R.id.price);
            img_photo = itemView.findViewById(R.id.photo);
            tv_quantity = itemView.findViewById(R.id.item_quantity);


            plusBtn = itemView.findViewById(R.id.plusButton);
            minusBtn = itemView.findViewById(R.id.minusButton);

            plusBtn.setOnClickListener(this);
            minusBtn.setOnClickListener(this);

            // бд
            db = App.getInstance().getDatabase();
            userCartDao = db.userCartDao();

        }

        @Override
        public void onClick(View v) {

            // получаем текущее количесво
            int item_quantity = userCart.get(getAdapterPosition()).quantity;


            switch (v.getId()){

                case R.id.plusButton:
                    item_quantity++;
                    break;

                case R.id.minusButton:
                    if(item_quantity > 1) item_quantity--;
                    break;

            }


            // обновляем
            userCart.get(getAdapterPosition()).quantity = item_quantity; // обновляем массив
            userCartDao.update(userCart.get(getAdapterPosition())); // записываем в корзину

            notifyDataSetChanged();

            // показываем рассчеты
            cartRefresh(userCart);

        }
    }
}
