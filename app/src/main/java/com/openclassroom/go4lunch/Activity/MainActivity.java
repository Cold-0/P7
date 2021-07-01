package com.openclassroom.go4lunch.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.Activity.Abstract.BaseActivity;
import com.openclassroom.go4lunch.BuildConfig;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.databinding.ActivityMainBinding;
import com.openclassroom.go4lunch.databinding.HeaderNavViewBinding;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = SettingsActivity.class.getName();

    private ActivityMainBinding mBinding;
    private HeaderNavViewBinding mHeaderNavViewBinding;
    private ActivityResultLauncher<Intent> mSignInLauncher;

    static final private int RC_FINE_LOCATION = 950001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        configureToolBar();
        configureBottomNavBar();
        configureDrawerLayout();
        configureNavigationView();
        configureAuth();
        configurePlaces();

        getLocationPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_search, menu);

        return true;
    }

    private void configurePlaces() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CheckIfSignIn(); // To make sure we are still connected when going back to the main Activity
    }

    // --------------------
    // Auth
    // --------------------
    private void configureAuth() {

        mSignInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                this::onSignInResult

        );

        CheckIfSignIn();
    }

    private void CheckIfSignIn() {
        if (isCurrentUserLogged()) {
            updateProfile();
        } else {
            SignIn();
        }
    }

    private void updateProfile() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            mHeaderNavViewBinding.headerUserName.setText(user.getDisplayName());
            mHeaderNavViewBinding.headerUserEmail.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mHeaderNavViewBinding.headerUserAvatar);
            }
        }
    }

    private void SignIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false, true)
                .setLogo(R.drawable.go4lunch)
                .setTheme(R.style.AuthTheme)
                .build();

        mSignInLauncher.launch(signInIntent);
    }

    private void SignOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    Snackbar.make(mBinding.getRoot(), R.string.signed_out, Snackbar.LENGTH_SHORT).show();
                    updateProfile();
                });
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (response == null) {
            // Canceled Signed In
            SignIn();
            Snackbar.make(mBinding.getRoot(), R.string.canceled_sign_in, Snackbar.LENGTH_SHORT).show();
        } else if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            Snackbar.make(mBinding.getRoot(), getString(R.string.signed_in_as_user, Objects.requireNonNull(getCurrentUser()).getDisplayName()), Snackbar.LENGTH_SHORT).show();
        } else {
            // Sign in failed
            Snackbar.make(mBinding.getRoot(), R.string.sign_in_failed, Snackbar.LENGTH_SHORT).show();
        }

        updateProfile();
    }

    // --------------------
    // Bottom Nav Bar
    // --------------------
    private void configureBottomNavBar() {

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_call, R.id.nav_like, R.id.nav_website)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, navController);
    }

    // ---------------------
    // ToolBar
    // ---------------------
    private void configureToolBar() {
        setSupportActionBar(mBinding.toolbar);
    }

    // --------------------
    // Navigation View
    // --------------------
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.getRoot(), mBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.getRoot().addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        mHeaderNavViewBinding = HeaderNavViewBinding.bind(mBinding.leftMenuNav.getHeaderView(0));
        //HeaderNavViewBinding.inflate(getLayoutInflater(), );
        //mBinding.activityMainNavView.addHeaderView(mHeaderNavViewBinding.getRoot());
        mBinding.leftMenuNav.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_nav_your_lunch:
                Intent restaurantIntent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                startActivity(restaurantIntent);
                break;
            case R.id.menu_nav_settings:
                Intent settingIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.menu_nav_sign_out:
                SignOut();
                SignIn();
                break;
            default:
                break;
        }

        mBinding.getRoot().closeDrawer(GravityCompat.START);

        return true;
    }

    private void getLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location_permission),
                    RC_FINE_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull @NotNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {

    }
}
