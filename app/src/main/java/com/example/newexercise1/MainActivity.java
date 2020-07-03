package com.example.newexercise1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static android.media.CamcorderProfile.get;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {           // intent to link page from navi item
        switch (menuItem.getItemId()){
            case R.id.navi_message:
                Intent intent = new Intent(MainActivity.this,MsgActivity.class);
                startActivity(intent);
                break;
            case R.id.navi_profile:
                Intent intent1 = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent1);
                break;
            case R.id.share:
                Toast.makeText(this,"share....",Toast.LENGTH_LONG).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class Post {                             // easy to initislize every feilds

        private String userId;
        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("body")
        private String body;

        public Post(String userId, String title, String body) {
            this.userId = userId;
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public String getId() {
            return id;
        }

        public String getBody() {
            return body;

        }

        }

        interface JsonPlaceHolder {                 //  for retofit client

            @GET("posts")
            Call<List<Post>> getPost();

            @DELETE("posts/{id}")
            Call<Void> deletePost(@Path("id") String id);

            @POST("posts")
            Call<Post> createPost(@Body Post post);

        }

        ListView listView;
        TextView textView,textView2;
        ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navi_view);
        toolbar = findViewById(R.id.toolbar);

        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        imageView = findViewById(R.id.imageView2);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);                                 //
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);            // item listener og navi item

        Retrofit retrofit = new Retrofit.Builder()                          // retrofit object
                .baseUrl(" https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolder jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class); // retrofit client

        Call<List<Post>> call = jsonPlaceHolder.getPost();                          //call retrofit library
        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                List<Post> posts = response.body();                     // get post list
                Log.i("content", String.valueOf(posts));

                final String[] title = new String[posts.size()];
                final String[] id = new String[posts.size()];
                final String[] body = new String[posts.size()];

                 for (int i = 0; i < posts.size(); i++) {
                    title [i] = posts.get(i).getTitle();
                    id[i] = posts.get(i).getId();
                    body [i] = posts.get(i).getBody();
                                       ;
                }

                CustomAdaptor adaptor = new CustomAdaptor(getApplicationContext(), title,id);
                listView.setAdapter(adaptor);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // when clicked list item

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int Position, long l) {

                            String tempListView = title[Position].toString();    // get position value
                            String bodyview = body[Position].toString();
                            String idview = id[Position].toString();
                            Intent intent = new Intent(getApplicationContext(),SecondActivity.class);   // intent to secondActivity

                            intent.putExtra( "ListViewClickValue", tempListView);  // set value
                            intent.putExtra("body1",bodyview);
                            intent.putExtra("id2",idview);
                            startActivity(intent);                                       // start seconsactivity

                }
                });
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {             // navigation view new post
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

                Toast.makeText(this, "Add post...", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this, AddPostActivity.class);
                startActivity(intent1);
            return super.onOptionsItemSelected(item);
    }
}


    class CustomAdaptor extends ArrayAdapter<String>{ // create class to parse single row attributes
        Context context;
        String[] title;;
        String[] id;;

        CustomAdaptor(Context context, String[] title,String[] id){
            super(context,R.layout.single_row,R.id.textView,title);

            this.context = context;
            this.title = title;
            this.id = id;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { // use getview method for create single row and loop it

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // xml single row design connect to java code
            View row = inflater.inflate(R.layout.single_row,parent,false);

            String post = getItem(position);

            TextView titleV = row.findViewById(R.id.textView); // reference to textview
            TextView idV = row.findViewById(R.id.textView3);

            titleV.setText(title[position]);  //set arrray content to text view
            idV.setText(id[position]);

            return row;  //  return single row
        }

    }

   


