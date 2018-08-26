package com.example.honeyapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.honeyapp.R;
import com.example.honeyapp.dao.UsersDao;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.UsersEntity;
import com.example.honeyapp.model.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AddStore extends AppCompatActivity {

    private TextInputLayout textInputStoreInn;
    private TextInputLayout textInputStoreAddress;
    private TextInputLayout textInputStoreTitle;
    private TextInputLayout textInputStorePhone;
    private TextInputLayout textInputStoreContactName;
    private TextInputLayout textInputStoreWorkTime;

    private TreeMap<String, String> mRequestParams;
    private final String JSON_URL = "https://agents.sbonus.ru/json/add_store.php";
    private RequestQueue requestQueue;
    private JsonArrayRequest request;

    private String response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);



        textInputStoreInn = findViewById(R.id.text_input_store_inn);
        textInputStoreAddress = findViewById(R.id.text_input_store_address);
        textInputStoreTitle = findViewById(R.id.text_input_store_title);
        textInputStorePhone = findViewById(R.id.text_input_store_phone);
        textInputStoreContactName = findViewById(R.id.text_input_store_contact_name);
        textInputStoreWorkTime = findViewById(R.id.text_input_store_worktime);
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


    private boolean validateInn(){
        String inn = textInputStoreInn.getEditText().getText().toString().trim();

        if(inn.isEmpty()){
            textInputStoreInn.setError("Поле должно быть заполнено");
            return false;
        }else if(inn.length() > 10){
            textInputStoreInn.setError("Слишком длинный ИНН");
            return false;
        }else{
            textInputStoreInn.setError(null);
            return true;
        }
    }

    private boolean validateAddress(){
        String address = textInputStoreAddress.getEditText().getText().toString().trim();

        if(address.isEmpty()){
            textInputStoreAddress.setError("Поле должно быть заполнено");
            return false;
        }else{
            textInputStoreAddress.setError(null);
            return true;
        }
    }

    private boolean validateTitle(){
        String title = textInputStoreTitle.getEditText().getText().toString().trim();

        if(title.isEmpty()){
            textInputStoreTitle.setError("Поле должно быть заполнено");
            return false;
        }else{
            textInputStoreTitle.setError(null);
            return true;
        }
    }

    private boolean validatePhone(){
        String phone = textInputStorePhone.getEditText().getText().toString().trim();

        if(phone.isEmpty()){
            textInputStorePhone.setError("Поле должно быть заполнено");
            return false;
        }else{
            textInputStorePhone.setError(null);
            return true;
        }
    }

    private boolean validateContactName(){
        String contactName = textInputStoreContactName.getEditText().getText().toString().trim();

        if(contactName.isEmpty()){
            textInputStoreContactName.setError("Поле должно быть заполнено");
            return false;
        }else{
            textInputStoreContactName.setError(null);
            return true;
        }
    }

    private boolean validateWorkTime(){
        String workTime = textInputStoreWorkTime.getEditText().getText().toString().trim();

        if(workTime.isEmpty()){
            textInputStoreWorkTime.setError("Поле должно быть заполнено");
            return false;
        }else{
            textInputStoreWorkTime.setError(null);
            return true;
        }
    }

    // onClick form button
    public void confirmInput(View v){

        if(!validateInn() | !validateAddress() | !validateTitle() | !validatePhone() | !validateContactName() | !validateWorkTime()){
            return;
        }

        String inn = textInputStoreInn.getEditText().getText().toString();



        Toast.makeText(getApplicationContext(), "Данные успешно отправлены", Toast.LENGTH_SHORT).show();

        // отправляем данные на серсер
        AppDatabase db = App.getInstance().getDatabase();
        UsersDao usersDao = db.usersDao();
        List<UsersEntity> user = usersDao.getAll();
        int id = user.get(0).id;
        String token = user.get(0).token;


        mRequestParams = new TreeMap<String, String>();
        mRequestParams.put("act", "addStore");
        mRequestParams.put("token", token);
        mRequestParams.put("agent_id", String.valueOf(id));
        mRequestParams.put("storeInn",textInputStoreInn.getEditText().getText().toString());
        mRequestParams.put("storeAddress",textInputStoreAddress.getEditText().getText().toString());
        mRequestParams.put("storeTitle",textInputStoreTitle.getEditText().getText().toString());
        mRequestParams.put("storePhone",textInputStorePhone.getEditText().getText().toString());
        mRequestParams.put("storeContactName",textInputStoreContactName.getEditText().getText().toString());
        mRequestParams.put("storeWorkTime",textInputStoreWorkTime.getEditText().getText().toString());


        this.mRequestParams = mRequestParams; // POST




        // пример отправки и получения данных
        // https://www.androidhive.info/2014/09/android-json-parsing-using-volley/



       String url = "https://agents.sbonus.ru/json/add_store.php";

       StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                   @Override
                   public void onResponse(String response) {
                       // response
                       Log.d("Response", response);
                       setResponse(response);


                   }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", response);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return mRequestParams;
            }
        };

        requestQueue = Volley.newRequestQueue(AddStore.this);
        //requestQueue.add(request);

        requestQueue.add(postRequest);













        // переход на главную
        finish();

    }

    public void setResponse(String response){
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
