package com.dga.services.waa;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.View;


import androidx.navigation.ui.AppBarConfiguration;


import com.dga.services.waa.Utils.Configurations;
import com.dga.services.waa.Utils.UrlController;
import com.dga.services.waa.Utils.connexion.RestService;
import com.dga.services.waa.adapters.RestaurantsAdapters;
import com.dga.services.waa.databinding.ActivityMainBinding;
import com.dga.services.waa.models.Restaurants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private View search_bar;
    private static final String TAG = "Activity";

    private List<Restaurants> restaurantList;
    private RestaurantsAdapters restoAdapter;

    private RecyclerView recycle_resto;

    List<String> img = null;

    JSONObject jsonObjectSetting;
    JSONArray jsonArrayHydraMember;
    JSONArray jsonArrayImages;
    Configurations settingsMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        settingsMain = new Configurations(this);

        initComponent();

       restaurantData();

     /*   binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void restaurantData() {
        RestService restService = UrlController.createService(RestService.class);
        JsonObject params = new JsonObject();
        try {
            Call<ResponseBody> myCall = restService.getRestaurants(UrlController.AddHeaders(this));
            Log.d(TAG, " restaurant " + myCall.request().url());
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        Log.d(TAG, "" + responseObj.message() + " " + myCall.request().url());
                        if (responseObj.isSuccessful()) {
                            Log.d(TAG, "onResponse: " + responseObj.toString());
                            JSONObject response = new JSONObject(responseObj.body().string());
                            jsonArrayHydraMember = response.getJSONArray("hydra:member");

                            Log.d(TAG, "onResponseHydraMember: " + jsonArrayHydraMember.toString());
                            restaurantList = new ArrayList<>();

                            for (int i = 0; i <  jsonArrayHydraMember.length(); i++) {
                                Restaurants restaurantsModel = new Restaurants();
                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = jsonArrayHydraMember.getJSONObject(i);
                                    restaurantsModel.setNom(jsonObject.getString("nom"));
                                   img = new ArrayList<>();

                                   jsonArrayImages = jsonObject.getJSONArray("images");
                                    Log.d(TAG, "ImagesHydraMember: " + jsonArrayImages.toString());
                                    for(int j=0; j  < jsonArrayImages.length(); j++)
                                    {
                                        JSONObject jsonObject1 = null;
                                        jsonObject1 = jsonArrayImages.getJSONObject(j);
                                        img.add(jsonObject1.getString("url"));
                                    }
                                    restaurantsModel.setImages(img);
                                    restaurantList.add(restaurantsModel);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            restoAdapter = new RestaurantsAdapters (getApplicationContext(),restaurantList);
                            recycle_resto.setAdapter( restoAdapter);


                        }
                        else {
                            Log.d(TAG, "onResponse: notsuccesIOOOO " + responseObj.raw());}
                    }catch (
                            JSONException e) {
                        Log.d(TAG, "onResponse failure: Erreur chargement settings ");

                        e.printStackTrace();
                    } catch (
                            IOException e) {
                        Log.d(e.toString(), "onResponse otherfailure: Erreur chargement settings ");
                        //Timber.e(e.toString(), "onResponse otherfailure: Erreur chargement settings ");
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(t.toString(), "onResponse otherfailure: Erreur chargement settings ");
                }
            });

        }
        catch (
                ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "SettingsTR: ", e);
            e.printStackTrace();
        }


    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    private void initComponent() {
        search_bar = (View) findViewById(R.id.search_bar);
        mTextMessage = (TextView) findViewById(R.id.search_text);

        recycle_resto = findViewById(R.id.recyclerview_restaurant);
        GridLayoutManager MyLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recycle_resto.setLayoutManager(MyLayoutManager);

//        navigation = (BottomNavigationView) findViewById(R.id.navigation);



    }
}