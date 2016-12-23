package utot.utot.helpers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;

import utot.utot.login.LoginSplashScreen;
import utot.utot.register.RegisterActivity;

/**
 * Created by elysi on 12/23/2016.
 */

public class LoginCommon {

    public static FacebookCallback<LoginResult> facebookCallback(final Activity act){
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //              "User ID: "
//                        + loginResult.getAccessToken().getUserId()
//                        + "\n" +
//                        "Auth Token: "
//                        + loginResult.getAccessToken().getToken()
                act.startActivity(new Intent(act, LoginSplashScreen.class));
                act.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                act.finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(act, "Login attempt cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(act, "An error has occurred during login", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
