package utot.utot.login;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.customobjects.CustomFrameLayout;
import utot.utot.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
     // Picasso.with(this).load(R.drawable.aa_2).into((CustomFrameLayout) findViewById(R.id.mainwindow));
        Picasso.with(this).load(R.drawable.utotlogo1).into((ImageView)findViewById(R.id.utotLogo));

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_fb);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
  //              "User ID: "
//                        + loginResult.getAccessToken().getUserId()
//                        + "\n" +
//                        "Auth Token: "
//                        + loginResult.getAccessToken().getToken()
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, LoginSplashScreen.class));
                LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            @Override
            public void onCancel() {
               Toast.makeText(LoginActivity.this, "Login attempt cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "An error has occurred during login", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.fb_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                LoginActivity.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();

            }
        });

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // authenticate user
                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                boolean authenticated = true;
                if(authenticated){
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, LoginSplashScreen.class));
                    LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                } else{
                    Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
