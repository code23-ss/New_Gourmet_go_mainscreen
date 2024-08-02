package com.example.mainscreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class RestaurantsActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantsActivity";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // RecyclerView 찾기
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // JSON 파일에서 데이터 비동기적으로 읽기
        new LoadRestaurantsTask().execute();
    }

    private class LoadRestaurantsTask extends AsyncTask<Void, Void, List<Restaurant>> {

        @Override
        protected List<Restaurant> doInBackground(Void... voids) {
            return loadRestaurantsFromJson();
        }

        @Override
        protected void onPostExecute(List<Restaurant> restaurantList) {
            if (restaurantList != null) {
                // 어댑터 설정
                RestaurantAdapter adapter = new RestaurantAdapter(RestaurantsActivity.this, restaurantList);
                recyclerView.setAdapter(adapter);
            } else {
                // 오류 메시지 표시
                Toast.makeText(RestaurantsActivity.this, "Failed to load restaurant data from JSON.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed to load restaurant data from JSON.");
            }
        }
    }

    private List<Restaurant> loadRestaurantsFromJson() {
        String json;
        try {
            // assets 폴더에서 JSON 파일 읽기
            InputStream is = getAssets().open("restaurants_image.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        // JSON을 Restaurant 객체 리스트로 변환
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Restaurant>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}

