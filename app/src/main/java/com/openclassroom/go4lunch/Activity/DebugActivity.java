package com.openclassroom.go4lunch.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.Activity.Abstract.AuthBaseActivity;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.databinding.ActivityDebugBinding;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DebugActivity extends AuthBaseActivity {

    private static final String TAG = DebugActivity.class.getName();

    private ActivityDebugBinding mBinding;
    private ActivityResultLauncher<Intent> mSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDebugBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mSignInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                this::onSignInResult
        );

        setButtonsCallback();

        if (isCurrentUserLogged()) {
            updateProfile();
        } else {
            SignIn();
        }
    }

    private void setButtonsCallback() {
        mBinding.signInButton.setOnClickListener(v -> SignIn());
        mBinding.signOutButton.setOnClickListener(v -> SignOut());
        mBinding.deleteButton.setOnClickListener(v -> DeleteAccount());
    }

    private void updateProfile() {
        FirebaseUser user = getCurrentUser();

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
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mBinding.userImageView);
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

    private void DeleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(task -> {
                    Snackbar.make(mBinding.getRoot(), R.string.account_have_been_deleted, Snackbar.LENGTH_SHORT).show();
                    updateProfile();
                });
    }

    private void SignOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    Snackbar.make(mBinding.getRoot(), R.string.signed_out, Snackbar.LENGTH_SHORT).show();
                    updateProfile();
                });
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

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (response == null) {
            // Canceled Signed In
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
}