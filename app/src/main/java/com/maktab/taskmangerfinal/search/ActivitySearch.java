package com.maktab.taskmangerfinal.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.maktab.taskmangerfinal.R;
import com.maktab.taskmangerfinal.controller.MainActivity;
import com.maktab.taskmangerfinal.controller.PagerActivity;
import com.maktab.taskmangerfinal.fragment.LoginFragment;


public class ActivitySearch extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView hambur;
    AlertDialog.Builder alertBuilder;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_search);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container1);


        if (fragment == null) {
            AllFragment allFragment = new AllFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container1, allFragment)
                    .commit();
        }

        findViews();
        setListener();

    }

    private void findViews() {
        drawerLayout = findViewById(R.id.navigation_drawer);
        navigationView = findViewById(R.id.navigation_view);
        hambur = findViewById(R.id.ham);
    }


    private void setListener() {

        hambur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.setting) {
                    Log.i("mylog", "hello!");
                }

                if (id == R.id.search) {

                    Intent intent = new Intent(ActivitySearch.this, MainActivity.class);
                    startActivity(intent);

                }

                if (id == R.id.exit) {

                    alertBuilder = new AlertDialog.Builder(ActivitySearch.this);
                    alertBuilder.setTitle("خروج");
                    alertBuilder.setMessage("آیا می خواهید خارج شوید؟");

                    alertBuilder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ActivitySearch.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });

                    alertBuilder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });


                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }


                return true;
            }
        });
    }


}