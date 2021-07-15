package com.openclassroom.go4lunch.View.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.Model.AutocompleteAPI.AutocompleteResponse;
import com.openclassroom.go4lunch.Model.AutocompleteAPI.Prediction;
import com.openclassroom.go4lunch.Model.DataView.SearchValidationDataView;
import com.openclassroom.go4lunch.Repository.Repository;
import com.openclassroom.go4lunch.View.Activity.Abstract.BaseActivity;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.ViewModel.SearchViewModel;
import com.openclassroom.go4lunch.databinding.ActivityMainBinding;
import com.openclassroom.go4lunch.databinding.HeaderNavViewBinding;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {


    private static final String TAG = SettingsActivity.class.getName();

    private ActivityMainBinding mBinding;
    private HeaderNavViewBinding mHeaderNavViewBinding;
    private ActivityResultLauncher<Intent> mSignInLauncher;

    private SearchViewModel mSearchViewModel;

    private Repository mRepository;

    static final public int RC_FINE_LOCATION = 950001;

    private LocationManager locationManager;

    public enum ViewTypes {
        MAP,
        LIST,
        WORKMATES
    }

    ViewTypes currentView;

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

        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        mRepository = Repository.getRepository();

        getLocationPermission();
    }

    public Location getLastKnownCoarseLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            // Walk through each enabled location provider and return the first found, last-known location
            for (String thisLocProvider : locationManager.getProviders(true)) {
                Location lastKnown = locationManager.getLastKnownLocation(thisLocProvider);

                if (lastKnown != null) {
                    return lastKnown;
                }
            }
        } catch (SecurityException exception) {
            exception.printStackTrace();
        }
        // Always possible there's no means to determine location
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_search, menu);

        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();

        String[] columns = {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1
        };

        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0
        );

        final List<Prediction> predictionList = new ArrayList<>();
        final String[] currentSearch = {""};
        searchView.setSuggestionsAdapter(suggestionAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // Clear Focus
                searchView.clearFocus();

                SearchValidationDataView searchValidationDataView = new SearchValidationDataView();
                searchValidationDataView.prediction = predictionList.get(position);
                searchValidationDataView.viewType = currentView;

                mSearchViewModel.setSearchValidationDataViewMutable(searchValidationDataView);

                // Clear Suggestion List
                predictionList.clear();

                // Reset List
                MatrixCursor cursor = new MatrixCursor(columns);
                suggestionAdapter.swapCursor(cursor);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Clear Focus
                searchView.clearFocus();

                SearchValidationDataView searchValidationDataView = new SearchValidationDataView();
                searchValidationDataView.prediction = null;
                searchValidationDataView.viewType = currentView;
                searchValidationDataView.searchMethod = SearchValidationDataView.SearchMethod.SEARCH_STRING;
                searchValidationDataView.searchString = currentSearch[0];

                mSearchViewModel.setSearchValidationDataViewMutable(searchValidationDataView);

                // Clear Suggestion List
                predictionList.clear();
                // Reset List
                MatrixCursor cursor = new MatrixCursor(columns);
                suggestionAdapter.swapCursor(cursor);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearch[0] = newText;

                if (newText.length() > 3) {
                    Repository repository = Repository.getRepository();
                    Location loc = getLastKnownCoarseLocation();
                    @SuppressLint("DefaultLocale") Call<AutocompleteResponse> call =
                            repository.getService().getAutocomplete(newText, 5000, String.format("%f,%f", loc.getLatitude(), loc.getLongitude()), "establishment");
                    call.enqueue(new Callback<AutocompleteResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<AutocompleteResponse> call, @NonNull Response<AutocompleteResponse> response) {
                            if (response.body() != null) {
                                predictionList.clear();

                                MatrixCursor cursor = new MatrixCursor(columns);

                                List<Prediction> predictionsList = response.body().getPredictions();

                                for (int i = 0; i < predictionsList.size(); i++) {
                                    predictionList.add(predictionsList.get(i));
                                    String[] tmp = {Integer.toString(i), predictionsList.get(i).getDescription()};
                                    cursor.addRow(tmp);
                                }

                                suggestionAdapter.swapCursor(cursor);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AutocompleteResponse> call, @NonNull Throwable t) {
                            Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                return false;
            }
        });

        return true;
    }

    private void SuggestionSelectedMap(SearchValidationDataView prediction) {

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
                Picasso.Builder builder = new Picasso.Builder(this);
                builder.downloader(new OkHttp3Downloader(this));
                builder.build().load(user.getPhotoUrl())
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_foreground)
                        .into(mHeaderNavViewBinding.headerUserAvatar);
            }
        }
    }

    private void SignIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

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
                R.id.nav_map, R.id.nav_list, R.id.nav_workmates)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            assert destination.getLabel() != null;
            if (destination.getLabel().equals("Map View")) {
                currentView = ViewTypes.MAP;
            } else if (destination.getLabel().equals("List View")) {
                currentView = ViewTypes.LIST;
            } else if (destination.getLabel().equals("Workmates")) {
                currentView = ViewTypes.WORKMATES;
            }
        });
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
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
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
        LocationManager locationManager;

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {

    }
}
