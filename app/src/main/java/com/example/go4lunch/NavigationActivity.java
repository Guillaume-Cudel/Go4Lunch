package com.example.go4lunch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.example.go4lunch.databinding.ActivityNavigationBinding;
import com.example.go4lunch.ui.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends BaseActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private ActivityNavigationBinding binding;

    BottomNavigationView mBottomNavigationView;
    Toolbar toolbar;

    //AppBarConfiguration mAppBarConfiguration;


    @Override
    public int getFragmentLayout() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_bottom_nav_view);

        configureBottomNavigation();

    }

    @Override
    public void onBackPressed() {
        //  Handle back click to close menu
        if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //  Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.nav_your_lunch :
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_log_out:
                break;
            default:
                break;
        }

        this.binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    // CONFIGURATION

    private void configureBottomNavigation(){
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_restaurants_list, R.id.navigation_workmates)
                .build();

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        final NavController navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mBottomNavigationView, navController);
        NavigationUI.setupWithNavController(binding.navigationDrawerNavView, navController);
    }

}