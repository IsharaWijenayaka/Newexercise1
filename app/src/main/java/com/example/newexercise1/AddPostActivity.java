package com.example.newexercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class AddPostActivity extends AppCompatActivity {
    TextView textUserId,textId,textTitle,textBody,addPost;
    EditText usrId,title,body;
    Button add;
    MainActivity.JsonPlaceHolder jsonPlaceHolder;
    String Title4,Body4;
    String userid;

    Retrofit retrofit = new Retrofit.Builder()                          // retrofit object
            .baseUrl(" https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        textUserId = findViewById(R.id.textUserId);
        textTitle = findViewById(R.id.textTitle);
        textBody = findViewById(R.id.textbody);
        usrId = findViewById(R.id.userId);
        title= findViewById(R.id.title);
        body= findViewById(R.id.body);
        add = findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userid  = usrId.getText().toString();
                Title4  =   title.getText().toString();
                Body4   =   body.getText().toString();

                SharedPreferences prf = getSharedPreferences("Dtails",MODE_PRIVATE);
                SharedPreferences.Editor editor = prf.edit();
                editor.putString("UserId", String.valueOf(userid));

                editor.putString("title",Title4);
                editor.putString("body", Body4);
                editor.commit();

                Log.i("post data", String.valueOf(userid));
                Log.i("post data", Title4);
                Log.i("post data", Body4);

                createPost();

            }
        });
    }

   public void createPost(){
       MainActivity.Post post = new MainActivity.Post(userid,Title4,Body4);

       MainActivity.JsonPlaceHolder jsonPlaceHolder = retrofit.create(MainActivity.JsonPlaceHolder.class);

       Call<MainActivity.Post> call = jsonPlaceHolder.createPost(post);
       call.enqueue(new Callback<MainActivity.Post>() {
           @Override
           public void onResponse(Call<MainActivity.Post> call, Response<MainActivity.Post> response) {
               Log.i("new create", String.valueOf(response.code()));
               Toast.makeText(getApplicationContext(),"add new post...",Toast.LENGTH_LONG).show();
           }

           @Override
           public void onFailure(Call<MainActivity.Post> call, Throwable t) {
               Toast.makeText(getApplicationContext(),"Could not create new post......",Toast.LENGTH_LONG).show();

           }
       });

   }
}