package com.tasty.wines.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;

import java.util.Arrays;

public class LoginHelper {


    public final static int RC_SIGN_IN = 1;

    public static void openLoginDialog(Activity activity) {
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                LoginHelper.RC_SIGN_IN);
    }

    public static boolean verifyLogin(int requestCode, int resultCode, Intent data, Activity activity) {
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {

                return true;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(activity, "Sign-in cancelled", Toast.LENGTH_LONG).show();

                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(activity, "No network", Toast.LENGTH_LONG).show();
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(activity, "Unknown Error", Toast.LENGTH_LONG).show();
                }
            }

            Toast.makeText(activity, "Unknown response", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
