package com.example.honeyapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.honeyapp.R;
import com.example.honeyapp.entities.UserCartEntity;

import java.util.List;

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
        view = inflater.inflate(R.layout.cardview_item, parent,false);



        final OrderPlaceAdapter.MyViewHoder myViewHoder = new OrderPlaceAdapter.MyViewHoder(view);

        return myViewHoder;
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {

        holder.tv_name.setText(userCart.get(position).products.getName());
        holder.tv_price.setText(userCart.get(position).products.getPrice());

        // загрузка изображения
        Glide.with(mContext).load(userCart.get(position).products.getPhoto()).apply(option).into(holder.img_photo);
        //holder.tv_name.setText("Name");
        //holder.tv_price.setText("99");

    }

    @Override
    public int getItemCount() {
        return userCart.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_price;
        ImageView img_photo;

        public MyViewHoder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.name);
            tv_price = itemView.findViewById(R.id.price);
            img_photo = itemView.findViewById(R.id.photo);

        }
    }
}
