package com.example.honeyapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.honeyapp.R;
import com.example.honeyapp.adapters.RecyclerViewAdapter;
import com.example.honeyapp.dao.StoreDao;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.dao.UsersDao;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.UserCartEntity;
import com.example.honeyapp.entities.UsersEntity;
import com.example.honeyapp.model.Products;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "TAG-";
    private final String JSON_URL = "https://agents.sbonus.ru/api/products/";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Products> listProducts;
    private RecyclerView recyclerView;
    private TreeMap<Integer, Integer> userCartMap;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // переход на главную
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));

        // подключаемся к базе
        AppDatabase db = App.getInstance().getDatabase();
        UsersDao usersDao = db.usersDao();

        //usersDao.deleteAll();

        List<UsersEntity> user = usersDao.getAll();

        if(user.size()>0){
            setContentView(R.layout.activity_main);
            setToken(user.get(0).token);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // загружаем список
        listProducts = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view_id);
        jsonrequest();


        // добавить магазни


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            startActivity(new Intent(MainActivity.this, OrderPlace.class));
            return true;
        }

        if (id == R.id.add_store) {
            startActivity(new Intent(MainActivity.this, AddStore.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_exit) {
            // logout
            AppDatabase db = App.getInstance().getDatabase();
            UsersDao usersDao = db.usersDao();
            usersDao.deleteAll();

            StoreDao storeDao = db.storeDao();
            storeDao.deleteAll();

            UserCartDao userCartDao = db.userCartDao();
            userCartDao.deleteAll();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /////////////////////////////
    private void jsonrequest() {


        // отправляем данные на сервер и получаем ответ
        ///
        StringRequest postRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // response

                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(response);
                JsonObject rootObject = jsonElement.getAsJsonObject();



                if(rootObject.get("result").getAsInt() > 0 ){

                    JsonObject data = rootObject.getAsJsonObject("data");
                    JsonArray entries = data.getAsJsonArray("entries");

                    JsonObject jsonObject = null;

                    for (int i = 0; i < entries.size(); i++) {



                        jsonObject = entries.get(i).getAsJsonObject();
                        Products products = new Products();



                        products.setId(jsonObject.get("id").getAsInt());
                        products.setName(jsonObject.get("name").getAsString());
                        products.setPrice(jsonObject.get("price").getAsString());
                        products.setPhoto(jsonObject.get("photo").getAsString());


                        listProducts.add(products);

                    }

                    setuprecycleview(listProducts);


                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error: "+rootObject.get("message").getAsString(), Toast.LENGTH_SHORT);
                    toast.show();
                }


                Log.i(TAG, "onResponse: " + rootObject.toString());



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

                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);
                return params;

            }
        };


//        request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                Log.i(TAG, "onResponse: ");
//
//                JSONObject jsonObject = null;
//                        for (int i = 0; i < response.length(); i++) {
//
//                            try{
//                                jsonObject = response.getJSONObject(i);
//                                Products products = new Products();
//
//                                products.setId(jsonObject.getInt("id"));
//                                products.setName(jsonObject.getString("name"));
//                                products.setPrice(jsonObject.getString("price"));
//                                products.setPhoto(jsonObject.getString("photo"));
//
//
//                                listProducts.add(products);
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                setuprecycleview(listProducts);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        //requestQueue.add(request);
        requestQueue.add(postRequest);

    }

    private void setuprecycleview(List<Products> listProducts) {


        // передаём в адаптер товары которые лежат в корзине

        List<UserCartEntity> userCart;
        userCartMap = new TreeMap<Integer, Integer>();

        AppDatabase db = App.getInstance().getDatabase();
        UserCartDao userCartDao = db.userCartDao();

        userCart = userCartDao.getAll();

        // упаковываем в TreeMap
        for (UserCartEntity element : userCart) {
            userCartMap.put(element.id, element.quantity);
        }


        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,listProducts, userCartMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);

    }

    public void setToken(String token) {
        this.token = token;
    }
}
