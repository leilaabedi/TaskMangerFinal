package com.maktab.taskmangerfinal.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.maktab.taskmangerfinal.R;

import com.maktab.taskmangerfinal.fragment.DoingFragment;
import com.maktab.taskmangerfinal.fragment.DoneFragment;
import com.maktab.taskmangerfinal.fragment.ToDoFragment;


public class PagerActivity extends AppCompatActivity {
    public static Context context;

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView hambur;
    PageAdapter mPageAdapter;

   // PageAdapter mPageAdapter;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PagerActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity_pager);
        context = getApplicationContext();

        findViews();
      initView();
        //setTabOption();
        setListener();
    }


    private void findViews() {
        mViewPager = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.navigation_drawer);
        navigationView = findViewById(R.id.navigation_view);
        hambur=findViewById(R.id.ham);

       mTabLayout = findViewById(R.id.tabLayout);
    }


    private void initView() {
        mPageAdapter = new PageAdapter(this);
        mViewPager.setAdapter(mPageAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator
                (mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0: {
                                tab.setText("TODO");
                                break;
                            }
                            case 1: {
                                tab.setText("DOING");
                                break;
                            }
                            case 2: {
                                tab.setText("DONE");
                                break;
                            }
                        }
                    }
                });

        tabLayoutMediator.attach();
    }



    private void setListener(){

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
                return true;
            }
        });
    }




    private class PageAdapter extends FragmentStateAdapter {


        public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return ToDoFragment.newInstance();
                case 1:
                    return DoingFragment.newInstance();
                case 2:
                    return DoneFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Do You Want To Exit?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FragmentManager fragmentManager = getSupportFragmentManager();

                                LoginFragment loginFragment = new LoginFragment();
                                fragmentManager
                                        .beginTransaction()
                                        .add(R.id.fragment_container, loginFragment)
                                        .commit();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null);

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.menu_search_task:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        mPageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPageAdapter.notifyDataSetChanged();
    }



}
