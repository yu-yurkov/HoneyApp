package com.example.honeyapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.honeyapp.R;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.dao.UsersDao;
import com.example.honeyapp.database.App;
import com.example.honeyapp.database.AppDatabase;
import com.example.honeyapp.entities.UserCartEntity;
import com.example.honeyapp.entities.UsersEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class LoginActivity extends AppCompatActivity {


    Button signInButton;
    EditText phone;
    EditText password;

    private final String JSON_URL = "https://agents.sbonus.ru/api/auth/";
    private RequestQueue requestQueue;
    private JsonElement obj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.sign_in_button);
    }

    // клик по кнопке "Войти"
    public void login(View v){

        validate(phone.getText().toString(), password.getText().toString());
    }

    public void validate(String phone, String password){

        final String mPhone = phone;
        final String mPass = password;

        if(!phoneValidate() | !passwordValidate()){
            return;
        }

        signInButton.setClickable(false);

        // отправляем данные на сервер и получаем ответ
        ///
        StringRequest postRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // response
                // обрабатываем ответ

                //Log.d("Response", response);
                responseProcess(response);
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
                params.put("login", mPhone);
                params.put("pass", mPass);

                return params;

            }
        };

        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(postRequest);

    }

    private void responseProcess(String response) {
        //Log.i("TAG", "responseProcess: " + response);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response);
        JsonObject rootObject = jsonElement.getAsJsonObject();

        // если авторизовались
        if(rootObject.get("result").getAsInt() > 0 ){

            JsonObject dataObject = rootObject.getAsJsonObject("data");

            // подключаемся к базе
            AppDatabase db = App.getInstance().getDatabase();
            UsersDao usersDao = db.usersDao();

            UsersEntity usersEntity = new UsersEntity();
            usersEntity.id = dataObject.get("id").getAsInt();
            usersEntity.token = dataObject.get("token").getAsString();

            usersDao.insert(usersEntity);


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }

        // ошибка авторизации
        else{
            //JsonObject dataObject = rootObject.getAsJsonObject("error");
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Error: "+rootObject.get("message").getAsString(), Toast.LENGTH_SHORT);
            toast.show();

            signInButton.setClickable(true);
        }
        
    }



    private boolean phoneValidate() {
        String phoneNumber = phone.getText().toString().trim();


        char[] chArray = phoneNumber.toCharArray();


        if(phoneNumber.isEmpty()) {
            phone.setError("Поле должно быть заполнено");
            return false;
        }

        if(Character.digit(chArray[0], 10) != 7){
            phone.setError("Номер телефона должен начинаться с цифры 7");
            return false;
        }

        if(chArray.length < 10){
            phone.setError("Слишком короткий номер телефона");
            return false;
        }

        return true;
    }

    private boolean passwordValidate(){
        String pass = password.getText().toString().trim();
        if(pass.isEmpty()) {
            password.setError("Поле должно быть заполнено");
            return false;
        }

        return true;
    }

}