package com.example.honeyapp.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.honeyapp.R;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.UserCartEntity;
import com.example.honeyapp.model.Products;


import java.util.List;
import java.util.TreeMap;


public class RecyclerViewAdapter extends SelectableAdapter<RecyclerViewAdapter.MyViewHolder>{

    private static final String TAG = "TAG";
    private Context mContext;
    private List<Products> mData;
    public TreeMap<Integer, Integer> userCartMap;
    RequestOptions option;

    SparseBooleanArray sparseBooleanArray; // for identifying: in list which items are selected


    public RecyclerViewAdapter(Context mContext, List<Products> mData, TreeMap<Integer, Integer> userCartMap) {
        this.mContext = mContext;
        this.mData = mData;
        this.userCartMap = userCartMap;

        // Options for Glide
        this.option = new RequestOptions().centerCrop().placeholder(R.drawable.add_cart_bg).error(R.drawable.add_cart_bg);


        sparseBooleanArray = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardview_item, parent,false);


        final MyViewHolder myViewHolder = new MyViewHolder(view);


        //
        Log.i(TAG, "onCreateViewHolder: " + myViewHolder.getAdapterPosition());



        // кликаем добавить в корзину
//        myViewHolder.add_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                //Log.i(TAG, "onClick: " + v.getId());

                AppDatabase db = App.getInstance().getDatabase();
                UserCartDao userCartDao = db.userCartDao();
//
//                // определяем id товара
//                int p_id = mData.get(myViewHolder.getAdapterPosition()).getId();
//
//                // формируем данные
//                UserCartEntity userCartEntity = new UserCartEntity();
//                userCartEntity.id = p_id;
//                userCartEntity.quantity = 1;
//                userCartEntity.products = mData.get(myViewHolder.getAdapterPosition());
//
//                // проверяем наличие товара в корзине
//                UserCartEntity userCartId = userCartDao.getById(p_id);
//
//
////                if(userCartId == null){
////
////                    Toast toast = Toast.makeText(mContext,"Товар добален в корзину", Toast.LENGTH_SHORT);
////                    toast.show();
////
////
////
////
////                    v.setBackgroundResource(R.drawable.added_cart_bg);
////
////                    // закидываем в корзину
////                    userCartDao.insert(userCartEntity);
////
////                }else{
////                    v.setBackgroundResource(R.drawable.listing_cart_bg);
////
////                    // удаляем из корзины
////                    userCartDao.delete(userCartEntity);
////                    Toast toast = Toast.makeText(mContext,"Товар удален из корзины", Toast.LENGTH_SHORT);
////                    toast.show();
////
////                }
//

        List<UserCartEntity> userCartsEntities = userCartDao.getAll();

        for (UserCartEntity element : userCartsEntities) {
            Log.i(TAG, "id: " + element.id
                    +", quantity: "+ element.quantity
                    +", p_id: "+ element.products.getId()
                    + ", p_name: " + element.products.getName()
                    + ", p_price: " + element.products.getPrice()
                    + ", p_img: " + element.products.getPhoto());
        }

//
//
//
//            }
//        });


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

        if (sparseBooleanArray.get(position))
        {
            holder.add_cart.setBackgroundColor(Color.GREEN);
        }
        else
        {
            holder.add_cart.setBackgroundColor(Color.WHITE);
        }




        Log.d(TAG, "onBindViewHolder: " + mData.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // MyViewHolder (обрабатываем клики)
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        TextView tv_price;
        ImageView img_photo;
        LinearLayout view_container;

        // кнопка
        ImageView add_cart;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.name);
            tv_price = itemView.findViewById(R.id.price);
            img_photo = itemView.findViewById(R.id.photo);

            // Блок добавить в корзину
            add_cart = itemView.findViewById(R.id.add_cart);

            add_cart.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            if (!sparseBooleanArray.get(getAdapterPosition()))
            {
                sparseBooleanArray.put(getAdapterPosition(),true);
                notifyItemChanged(getAdapterPosition());
            }
            else // if clicked item is already selected
            {
                sparseBooleanArray.put(getAdapterPosition(),false);
                notifyItemChanged(getAdapterPosition());
            }

        }
    }
}
