package com.example.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.ActivityNavigationBinding;
import com.example.go4lunch.ui.BaseActivity;
import com.example.go4lunch.ui.map.MapFragment;
import com.example.go4lunch.ui.restaurants_list.RestaurantsListFragment;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityNavigationBinding binding;
    private BottomNavigationView mBottomNavigationView;
    //private final GetLocation location;

    public Fragment fragmentMap;
    public Fragment fragmentRestaurantsList;
    public Fragment fragmentWorkmates;

    private static final int FRAGMENT_MAP = 0;
    private static final int FRAGMENT_RESTAURANT = 1;
    private static final int FRAGMENT_WORKMATES = 2;

    // Location
    public FusedLocationProviderClient fusedLocation;
    public double longitude;
    public double latitude;

    // Easy location
    private static final int REQUEST_LOCATION_PERMISSION = 10;


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


        // Show the first fragment when starting activity
        fragmentMap = new MapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragmentMap);
        fragmentTransaction.commit();

        // Toolbar configuration
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        updateUIWhenCreating();
        onClickItemsDrawer();
        //location.createRequestLocation();
        createRequestLocation();

    }

    // UI

    private void updateUIWhenCreating() {
        View header = binding.navigationDrawerNavView.getHeaderView(0);
        ImageView profilImage = (ImageView) header.findViewById(R.id.profilImage);
        TextView profilUsername = (TextView) header.findViewById(R.id.profil_name);
        TextView profilUsermail = (TextView) header.findViewById(R.id.profil_mail);

        FirebaseUser currentUser = this.getCurrentUser();
        if (currentUser != null) {

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

    private void onClickItemsDrawer() {
        NavigationView navView = binding.navigationDrawerNavView;
        if (navView != null) {
            setupDrawerContent(navView);
        }
        setUpBottomContent(mBottomNavigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpBottomContent(BottomNavigationView bottomContent) {
        bottomContent.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //  Handle Navigation Item Click
        int id = item.getItemId();
        item.setChecked(true);
        binding.drawerLayout.closeDrawers();

        switch (id) {
            case R.id.nav_your_lunch:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;

            case R.id.navigation_map:
                showFragment(FRAGMENT_MAP);
                break;

            case R.id.navigation_restaurants_list:
                showFragment(FRAGMENT_RESTAURANT);
                break;

            case R.id.navigation_workmates:
                showFragment(FRAGMENT_WORKMATES);
                break;

            default:
                break;
        }

        this.binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // VIEW

    private void showFragment(int fragmentID) {
        switch (fragmentID) {
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

    private void showMapFragment() {
        if (fragmentMap == null) {
            fragmentMap = MapFragment.newInstance();
            startTransactionFragment(fragmentMap);
            return;
        }
        startTransactionFragment(fragmentMap);
    }

    private void showRestaurantsListFragment() {
        if (fragmentRestaurantsList == null) {
            fragmentRestaurantsList = RestaurantsListFragment.newInstance();
            startTransactionFragment(fragmentRestaurantsList);
            return;
        }
        startTransactionFragment(fragmentRestaurantsList);
    }

    private void showWorkmatesFragment() {
        if (fragmentWorkmates == null) {
            fragmentWorkmates = WorkmatesFragment.newInstance();
            startTransactionFragment(fragmentWorkmates);
            return;
        }
        startTransactionFragment(fragmentWorkmates);
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment).commit();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //location.createRequestLocation();
        createRequestLocation();
    }

    // Location

    // Configure EASY location request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    public void createRequestLocation() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                //super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public double getDoubleLongitude() {
        return longitude;
    }

    public double getDoubleLatitude() {
        return latitude;
    }

}