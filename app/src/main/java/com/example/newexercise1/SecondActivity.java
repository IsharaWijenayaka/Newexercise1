package com.example.newexercise1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.IdentityHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Path;

import android.os.StrictMode;


public class SecondActivity extends AppCompatActivity {

    TextView textView2, textView4, textView5;
    Button button1, button2;
    ListView listView;
    MainActivity.JsonPlaceHolder jsonPlaceHolder;

    Retrofit retrofit = new Retrofit.Builder()                          // retrofit object
            .baseUrl(" https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView2 = findViewById(R.id.textView2);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        listView = findViewById(R.id.listView);

        Intent intent = getIntent();            // get intent

        final String data = getIntent().getStringExtra("ListViewClickValue");  // get intend passing data
        String body = getIntent().getStringExtra("body1");
        final String id3 = getIntent().getStringExtra("id2");

        textView2.setText(data);                                // set value to textViw
        textView4.setText(body);
        textView5.setText(id3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(id3);
                Log.i("delete post", String.valueOf(id3));
            }
        });


    }
    public void deletePost(String id3) {

        MainActivity.JsonPlaceHolder jsonPlaceHolder = retrofit.create(MainActivity.JsonPlaceHolder.class);

        Call<Void> call = jsonPlaceHolder.deletePost(id3);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(), "delete your post", Toast.LENGTH_LONG).show();
                Log.i("delete Post", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_example, menu);
        return true;
    }
}






