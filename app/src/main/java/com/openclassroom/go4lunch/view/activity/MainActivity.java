package com.openclassroom.go4lunch.view.activity;

import androidx.activity.result.ActivityResultLauncher;

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
import android.app.SearchManager;
import android.content.Intent;
import android.database.MatrixCursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.api.autocomplete.Prediction;
import com.openclassroom.go4lunch.message.SearchValidateMessage;
import com.openclassroom.go4lunch.type.SearchType;
import com.openclassroom.go4lunch.utils.transform.CircleCropTransform;
import com.openclassroom.go4lunch.utils.ex.ActivityEX;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.type.FragmentViewType;
import com.openclassroom.go4lunch.viewmodel.SearchViewModel;
import com.openclassroom.go4lunch.databinding.ActivityMainBinding;
import com.openclassroom.go4lunch.databinding.HeaderNavViewBinding;
import com.openclassroom.go4lunch.viewmodel.UserInfoViewModel;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends ActivityEX {

    // --------------------
    // Static Properties
    // --------------------
    private static final String TAG = MainActivity.class.getName();
    private static final int RC_FINE_LOCATION = 950001;

    // --------------------
    // Instance Properties
    // --------------------
    private ActivityMainBinding mActivityMainBinding;
    private HeaderNavViewBinding mHeaderNavViewBinding;
    private SearchViewModel mSearchViewModel;
    private UserInfoViewModel mUserInfoViewModel;
    private ActivityResultLauncher<Intent> mSignInLauncher;
    private User mCurrentUser;
    private FragmentViewType mCurrentView;

    // --------------------
    // Activity Override
    // --------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mUserInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        mUserInfoViewModel.getCurrentUser().observe(this, user -> {
            mCurrentUser = user;
        });

        configureToolBar();
        configureBottomNavBar();
        configureLeftDrawerLayout();
        configureAuth();
        configureLocationPermission();
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
        if (mUserInfoViewModel.isCurrentUserLogged()) {
            updateLeftDrawerLayoutProfile();
        } else {
            SignIn();
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
                    Snackbar.make(mActivityMainBinding.getRoot(), R.string.signed_out, Snackbar.LENGTH_SHORT).show();
                    updateLeftDrawerLayoutProfile();
                });
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (response == null) {
            // Canceled Signed In
            SignIn();
            Snackbar.make(mActivityMainBinding.getRoot(), R.string.canceled_sign_in, Snackbar.LENGTH_SHORT).show();
        } else if (result.getResultCode() == RESULT_OK) {
            // Sign in Success
            mUserInfoViewModel.updateUserList();
            Snackbar.make(mActivityMainBinding.getRoot(), getString(R.string.signed_in_as_user, Objects.requireNonNull(mUserInfoViewModel.getCurrentFirebaseUser()).getDisplayName()), Snackbar.LENGTH_SHORT).show();
        } else {
            // Sign in failed
            Snackbar.make(mActivityMainBinding.getRoot(), R.string.sign_in_failed, Snackbar.LENGTH_SHORT).show();
        }

        updateLeftDrawerLayoutProfile();
    }


    // ---------------------
    // ToolBar
    // ---------------------
    private void configureToolBar() {
        setSupportActionBar(mActivityMainBinding.toolbar);
    }

    // Top Bar Menu and SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create Top Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_search, menu);

        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();

        // Define CursorMatrix columns
        String[] columns = {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1
        };

        // Suggestion Adapter
        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0
        );

        // Prediction List
        final List<Prediction> predictionList = new ArrayList<>();

        // Current entered text buffer
        final String[] currentSearch = {""};

        // Set the Adapter we created
        searchView.setSuggestionsAdapter(suggestionAdapter);

        // To Handle Click in a Suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // Clear Focus
                searchView.clearFocus();

                // Create the Data containing the search to send to needed Fragment.
                SearchValidateMessage searchValidationDataView = new SearchValidateMessage()
                        .prediction(predictionList.get(position))
                        .viewtype(mCurrentView);

                // Send in the ViewModel the current Search Data
                mSearchViewModel.setSearchValidationDataViewMutable(searchValidationDataView);

                // Clear Suggestion List
                predictionList.clear();

                // Reset List with a new Cursor
                MatrixCursor cursor = new MatrixCursor(columns);
                suggestionAdapter.swapCursor(cursor);
                return true;
            }
        });

        // To handle Text Change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override // When pressing Return
            public boolean onQueryTextSubmit(String query) {
                // Clear Focus
                searchView.clearFocus();

                SearchValidateMessage searchValidationDataView = new SearchValidateMessage()
                        .prediction(null)
                        .viewtype(mCurrentView)
                        .searchmethod(SearchType.SEARCH_STRING)
                        .searchstring(currentSearch[0]);

                mSearchViewModel.setSearchValidationDataViewMutable(searchValidationDataView);

                // Clear Suggestion List
                predictionList.clear();

                // Reset List
                MatrixCursor cursor = new MatrixCursor(columns);
                suggestionAdapter.swapCursor(cursor);
                return false;
            }

            @Override // When text change, query new autocomplete
            public boolean onQueryTextChange(String newText) {
                // Update text
                currentSearch[0] = newText;

                // Do search only when reached at least 3 characters
                if (newText.length() > 3) {
                    Location loc = getLastKnowLocation();

                    // Get string of the current Location, using the Locale.CANADA to be sure to get the 0.50,-10.5 format and not the , instead of . like in French
                    String locString = String.format(Locale.CANADA, getString(R.string.location_formating), loc.getLatitude(), loc.getLongitude());

                    mSearchViewModel.callAutocomplete(newText, locString, "establishment", autocompleteResponse -> {
                        if (autocompleteResponse != null) {
                            predictionList.clear();

                            // Make new Cursor
                            MatrixCursor cursor = new MatrixCursor(columns);
                            List<Prediction> predictionsList = autocompleteResponse.getPredictions();

                            for (int i = 0; i < predictionsList.size(); i++) {
                                predictionList.add(predictionsList.get(i));
                                String[] tmp = {Integer.toString(i), predictionsList.get(i).getDescription()};
                                cursor.addRow(tmp);
                            }

                            suggestionAdapter.swapCursor(cursor);
                        }
                    });
                }
                return false;
            }
        });
        return true;
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
            if (destination.getLabel().equals(getString(R.string.menu_map_view))) {
                mCurrentView = FragmentViewType.MAP;
            } else if (destination.getLabel().equals(getString(R.string.menu_list_view))) {
                mCurrentView = FragmentViewType.LIST;
            } else if (destination.getLabel().equals(getString(R.string.menu_workmates))) {
                mCurrentView = FragmentViewType.WORKMATES;
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mActivityMainBinding.bottomNavigation, navController);
    }

    // --------------------
    // Left Drawer Layout
    // --------------------
    private void configureLeftDrawerLayout() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mActivityMainBinding.getRoot(), mActivityMainBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActivityMainBinding.getRoot().addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mHeaderNavViewBinding = HeaderNavViewBinding.bind(mActivityMainBinding.leftMenuNav.getHeaderView(0));
        mActivityMainBinding.leftMenuNav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_nav_your_lunch) {
                String eatingAt = mCurrentUser.getEatingAt();
                if (eatingAt == null || eatingAt.equals("")) {
                    Snackbar.make(mActivityMainBinding.getRoot(), "You haven't chose a restaurant yet", Snackbar.LENGTH_SHORT).show();
                } else {
                    openDetailRestaurant(eatingAt);
                }
            } else if (id == R.id.menu_nav_settings) {
                Intent settingIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingIntent);
            } else if (id == R.id.menu_nav_sign_out) {
                SignOut();
                SignIn();
            }

            mActivityMainBinding.getRoot().closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void updateLeftDrawerLayoutProfile() {
        updateUserOnFirebase();

        FirebaseUser user = mUserInfoViewModel.getCurrentFirebaseUser();
        if (user != null) {
            mHeaderNavViewBinding.headerUserName.setText(user.getDisplayName());
            mHeaderNavViewBinding.headerUserEmail.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {
                Picasso.Builder builder = new Picasso.Builder(this);
                builder.downloader(new OkHttp3Downloader(this));
                builder.build().load(user.getPhotoUrl())
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_foreground)
                        .transform(new CircleCropTransform())
                        .into(mHeaderNavViewBinding.headerUserAvatar);
            }
        }

        mUserInfoViewModel.updateUserList();
    }

    // -----------------------------
    // Permission
    // -----------------------------
    private void configureLocationPermission() {
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

    // --------------------------
    // Firebase
    // --------------------------
    private void updateUserOnFirebase() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        FirebaseUser user = mUserInfoViewModel.getCurrentFirebaseUser();
        if (user != null) {
            DocumentReference docRef = rootRef.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> userToUpdate = new HashMap<>();
                    userToUpdate.put("name", user.getDisplayName());
                    userToUpdate.put("email", user.getEmail());
                    Uri photoUrl = user.getPhotoUrl();
                    String photoUrlString;
                    if (photoUrl == null)
                        photoUrlString = "";
                    else
                        photoUrlString = photoUrl.toString();
                    userToUpdate.put("avatarUrl", photoUrlString);

                    if (!Objects.requireNonNull(document).exists()) {
                        userToUpdate.put("likes", new ArrayList<String>());
                        userToUpdate.put("eatingAt", "");
                        userToUpdate.put("eatingAtName", "");
                    } else {
                        userToUpdate.put("likes", document.get("likes"));
                        userToUpdate.put("eatingAt", document.get("eatingAt"));
                        userToUpdate.put("eatingAtName", document.get("eatingAtName"));
                    }

                    rootRef.collection("users").document(user.getUid())
                            .set(userToUpdate)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "User DocumentSnapshot successfully written!");
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error writing document", e);
                            });
                } else {
                    Log.d(TAG, "Error getting Document with ", task.getException());
                }
            });
        }
    }
}
