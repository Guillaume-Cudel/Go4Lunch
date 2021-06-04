package com.example.go4lunch;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.ActivityNavigationBinding;
import com.example.go4lunch.ui.BaseActivity;
import com.example.go4lunch.ui.map.MapFragment;
import com.example.go4lunch.ui.restaurants_list.RestaurantsListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NavigationActivity extends BaseActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private ActivityNavigationBinding binding;
    BottomNavigationView mBottomNavigationView;

    //-----------------

    private Fragment fragmentMap;
    private Fragment fragmentRestaurantsList;
    private Fragment fragmentWorkmates;

    private static final int FRAGMENT_MAP = 0;
    private static final int FRAGMENT_RESTAURANT =1;
    private static final int FRAGMENT_WORKMATES = 2;

    //------------

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


        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_bottom_nav_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        configureNavigation();
        updateUIWhenCreating();
        onClickItemsDrawer();

    }

    // CONFIGURATION

    private void configureNavigation(){
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

    // UI

    private void updateUIWhenCreating(){
       // NavigationView navigationView = (NavigationView) findViewById(R.id.your_nav_view_id);
        View header = binding.navigationDrawerNavView.getHeaderView(0);
        ImageView profilImage = (ImageView)  header.findViewById(R.id.profilImage);
        TextView profilUsername = (TextView) header.findViewById(R.id.profil_name);
        TextView profilUsermail = (TextView) header.findViewById(R.id.profil_mail);

        FirebaseUser currentUser = this.getCurrentUser();
        if (currentUser != null){

            //Get picture URL from Firebase
            Uri photoUrl = currentUser.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(this)
                        .load(photoUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profilImage);
            }

            //Get email & username from Firebase
            String email = TextUtils.isEmpty(currentUser.getEmail()) ? getString(R.string.info_no_email_found) : currentUser.getEmail();
            String username = TextUtils.isEmpty(currentUser.getDisplayName()) ? getString(R.string.info_no_username_found) : currentUser.getDisplayName();

            //Update views with data
            profilUsername.setText(username);
            profilUsermail.setText(email);
        }
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

    // ACTION

    private void onClickItemsDrawer(){
        NavigationView navView = binding.navigationDrawerNavView;
        if( navView != null){
            setupDrawerContent(navView);
            setUpBottomContent(mBottomNavigationView);
        }
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpBottomContent( BottomNavigationView bottomContent){
        bottomContent.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //  Handle Navigation Item Click
        int id = item.getItemId();
        item.setChecked(true);
        binding.drawerLayout.closeDrawers();

        switch (id){
            case R.id.nav_your_lunch :
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_log_out:
                //AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                finish();
                break;

            case R.id.navigation_map:
                this.showFragment(FRAGMENT_MAP);
                break;

            case R.id.navigation_restaurants_list:
                this.showFragment(FRAGMENT_RESTAURANT);
                break;

            case R.id.navigation_workmates:
                this.showFragment(FRAGMENT_WORKMATES);
                break;

            default:
                break;
        }

        this.binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // -------------

    private void showFragment(int fragmentID){
        switch (fragmentID){
            case FRAGMENT_MAP:
                this.showMapFragment();
                break;

            case FRAGMENT_RESTAURANT:
                this.showRestaurantsListFragment();
                break;

            case FRAGMENT_WORKMATES:
                this.showWorkmatesFragment();
                break;

            default:
                break;

        }
    }

    private void showMapFragment(){
        if (this.fragmentMap == null){
            this.fragmentMap = MapFragment.newInstance();
            this.startTransactionFragment(this.fragmentMap);
        }
    }

    private void showRestaurantsListFragment(){
        if (this.fragmentRestaurantsList == null) {
            this.fragmentRestaurantsList = RestaurantsListFragment.newInstance();
            this.startTransactionFragment(this.fragmentRestaurantsList);
        }
    }

    private void showWorkmatesFragment(){
        if (this.fragmentWorkmates == null){
            this.fragmentWorkmates = MapFragment.newInstance();
            this.startTransactionFragment(this.fragmentWorkmates);
        }
    }

    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment).commit();
        }
    }


}