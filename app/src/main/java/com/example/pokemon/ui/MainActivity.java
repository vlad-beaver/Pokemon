package com.example.pokemon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.pokemon.DataBase.DbHelper;
import com.example.pokemon.R;
import com.example.pokemon.databinding.ActivityMainBinding;
import com.example.pokemon.ui.fragments.Favorites;
import com.example.pokemon.ui.fragments.Home;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isFavoriteListVisible = false;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Home())
                .commit();

        binding.changeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavoriteListVisible){
                  isFavoriteListVisible = false;
                  binding.changeFragment.setText("Favorites");
                  getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Home())
                          .commit();
                }
                else {
                    isFavoriteListVisible = true;
                    binding.changeFragment.setText("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Favorites())
                            .commit();
                }
            }
        });

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        // Unccommit when you start project for the first time
//        dbHelper.createViews(db);
//        dbHelper.initDatabase(db);
    }

    public void DataBaseActivity(View view)
    {
        Intent intent = new Intent(this, DataBase.class);
        startActivity(intent);
    }
}
