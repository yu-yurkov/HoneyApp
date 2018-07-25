package com.example.honeyapp.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.honeyapp.R;
import com.example.honeyapp.model.Products;


import java.util.List;
import java.util.TreeMap;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private static final String TAG = "TAG";
    private Context mContext;
    private List<Products> mData;
    private TreeMap<Integer, Integer> userCartMap;
    RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<Products> mData, TreeMap<Integer, Integer> userCartMap) {
        this.mContext = mContext;
        this.mData = mData;
        this.userCartMap = userCartMap;

        // Options for Glide
        this.option = new RequestOptions().centerCrop().placeholder(R.drawable.add_cart_bg).error(R.drawable.add_cart_bg);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardview_item, parent,false);

        final MyViewHolder myViewHolder = new MyViewHolder(view);

        myViewHolder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.cart_count_bg);
                Log.i(TAG, "onClick: ");
            }
        });


//        // enter card
//        myViewHolder.view_container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
////                Intent i = new Intent(mContext, ProductActivity.class);
////                i.putExtra("product_name", mData.get(myViewHolder.getAdapterPosition()).getName());
////                i.putExtra("product_price", mData.get(myViewHolder.getAdapterPosition()).getPrice());
////                i.putExtra("product_photo", mData.get(myViewHolder.getAdapterPosition()).getPhoto());
////                Log.i(TAG, "onClick: " + v.toString());
////
////                //v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
////
////                mContext.startActivity(i);
//
//                Log.i(TAG, "onClick: стартуем активити вход в товар");
//            }
//        });

        return myViewHolder;
    }






    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_price.setText(mData.get(position).getPrice() + " р.");

        // загрузка изображения
        Glide.with(mContext).load(mData.get(position).getPhoto()).apply(option).into(holder.img_photo);
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // MyViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_price;
        ImageView img_photo;
        LinearLayout view_container;

        // кнопка
        LinearLayout add_cart;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.name);
            tv_price = itemView.findViewById(R.id.price);
            img_photo = itemView.findViewById(R.id.photo);

            // Блок добавить в корзину
            add_cart = itemView.findViewById(R.id.add_cart);

        }



    }
}
