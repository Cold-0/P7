package com.openclassroom.go4lunch.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.utils.transform.CircleCropTransform;
import com.openclassroom.go4lunch.utils.ex.ActivityEX;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.databinding.ActivitySettingsBinding;
import com.openclassroom.go4lunch.viewmodel.UserInfoViewModel;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SettingsActivity extends ActivityEX {

    // --------------------
    // Static Properties
    // --------------------
    private static final String TAG = SettingsActivity.class.getName();

    // --------------------
    // Properties
    // --------------------
    private ActivitySettingsBinding mBinding;
    private UserInfoViewModel mUserInfoViewModel;
    private ActivityResultLauncher<Intent> mSignInLauncher;

    // --------------------
    // Override
    // --------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mUserInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        mSignInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                this::onSignInResult
        );

        configureButtons();

        if (mUserInfoViewModel.isCurrentUserLogged()) {
            updateSettingsViews();
        } else {
            SignIn();
        }
    }

    // --------------------
    // Configure
    // --------------------
    private void configureButtons() {
        mBinding.signInButton.setOnClickListener(v -> SignIn());
        mBinding.signOutButton.setOnClickListener(v -> SignOut());
        mBinding.deleteButton.setOnClickListener(v -> DeleteAccount());
    }

    // --------------------
    // Update View
    // --------------------
    private void updateSettingsViews() {
        FirebaseUser user = mUserInfoViewModel.getCurrentFirebaseUser();

        // Update Profile
        if (user == null) {
            // Update User Picture
            mBinding.userImageView.setImageDrawable(null);

            // Update User Name
            mBinding.userName.setText(R.string.not_connected);

            // Update User Email
            mBinding.userEmail.setText("");

        } else {
            // Update User Picture
            if (user.getPhotoUrl() != null) {
                Picasso.Builder builder = new Picasso.Builder(this);
                builder.downloader(new OkHttp3Downloader(this));
                builder.build().load(user.getPhotoUrl())
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_foreground)
                        .transform(new CircleCropTransform())
                        .into(mBinding.userImageView);
            } else {
                mBinding.userImageView.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }

            // Update User Name
            mBinding.userName.setText(user.getDisplayName());

            // Update User Email
            mBinding.userEmail.setText(user.getEmail());
        }

        // Update SignIn/SignOut button
        if (user == null) {
            mBinding.signInButton.setEnabled(true);
            mBinding.signOutButton.setEnabled(false);
            mBinding.deleteButton.setEnabled(false);
        } else {
            mBinding.signInButton.setEnabled(false);
            mBinding.signOutButton.setEnabled(true);
            mBinding.deleteButton.setEnabled(true);
        }
    }

    // --------------------
    // Action Callback
    // --------------------
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
                    updateSettingsViews();
                });
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (response == null) {
            // Canceled Signed In
            Snackbar.make(mBinding.getRoot(), R.string.canceled_sign_in, Snackbar.LENGTH_SHORT).show();
        } else if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            Snackbar.make(mBinding.getRoot(), getString(R.string.signed_in_as_user, Objects.requireNonNull(mUserInfoViewModel.getCurrentFirebaseUser()).getDisplayName()), Snackbar.LENGTH_SHORT).show();
        } else {
            // Sign in failed
            Snackbar.make(mBinding.getRoot(), R.string.sign_in_failed, Snackbar.LENGTH_SHORT).show();
        }

        updateSettingsViews();
    }

    private void DeleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(task -> {
                    Snackbar.make(mBinding.getRoot(), R.string.account_have_been_deleted, Snackbar.LENGTH_SHORT).show();
                    updateSettingsViews();
                });
    }
}