package utot.utot.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;

import org.json.JSONException;
import org.json.JSONObject;

import utot.utot.asynctasks.LoginTask;
import utot.utot.asynctasks.RegisterTask;
import utot.utot.login.LoginSplashScreen;
import utot.utot.register.RegisterActivity;

/**
 * Created by elysi on 12/23/2016.
 */

public class LoginCommon {

    public static FacebookCallback<LoginResult> facebookCallback(final Activity act, final boolean isLoggingIn){
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                final String accessToken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        if(isLoggingIn){
                            new LoginTask(act.getApplicationContext(), act, bFacebookData.getString(FinalVariables.EMAIL),
                                    bFacebookData.getString(FinalVariables.FB_ID));
                        } else{
                            new RegisterTask(act.getApplicationContext(), act,
                                    bFacebookData.getString(FinalVariables.EMAIL), bFacebookData.getString(FinalVariables.FB_ID),
                                    bFacebookData.getString(FinalVariables.FB_ID),
                                    bFacebookData.getString(FinalVariables.FIRST_NAME),
                                    bFacebookData.getString(FinalVariables.LAST_NAME)).execute();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
//                act.startActivity(new Intent(act, LoginSplashScreen.class));
//                act.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                act.finish();
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

    private static Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            bundle.putString(FinalVariables.FB_ID, id);
            if (object.has("first_name"))
                bundle.putString(FinalVariables.FIRST_NAME, object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString(FinalVariables.LAST_NAME, object.getString("last_name"));
            if (object.has("email"))
                bundle.putString(FinalVariables.EMAIL, object.getString("email"));

            return bundle;
        }
        catch(JSONException e) {
            Log.d("ErrorParsing","Error parsing JSON");
        }
        return null;
    }
}
